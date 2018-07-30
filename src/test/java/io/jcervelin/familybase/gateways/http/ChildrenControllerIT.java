package io.jcervelin.familybase.gateways.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.familybase.config.IntegrationTestingSupport;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import io.jcervelin.familybase.gateways.mongo.ParentsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.domains.Endpoints.CHILDREN;
import static io.jcervelin.familybase.templates.ChildTemplates.CHILD_WITHOUT_DATE_OF_BIRTH;
import static io.jcervelin.familybase.templates.ChildTemplates.VALID_CHILD;
import static io.jcervelin.familybase.templates.ParentDocumentTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class ChildrenControllerIT extends IntegrationTestingSupport {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Autowired
    private ParentsRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mongoTemplate
                .getCollectionNames()
                .forEach(mongoTemplate::dropCollection);

        mockMvc = webAppContextSetup(webAppContext).build();
    }

    @Test
    public void findAll() throws Exception {
        //GIVEN
        final List<ParentDocument> parentDocuments = from(ParentDocument.class)
                .gimme(3, VALID_PARENT_DOCUMENT, PARENT_DOCUMENT_WITHOUT_ID, PARENT_DOCUMENT_WITHOUT_DATE_OF_BIRTH);
        final List<Child> children = from(Child.class)
                .gimme(6, VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH,
                        VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH,
                        VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH);
        parentDocuments.forEach(repository::save);

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(get(CHILDREN))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        final String content = mvcResult.getResponse().getContentAsString();

        final Collection<Child> results = mapper.readValue(content, new TypeReference<Collection<Child>>() {
        });

        assertThat(content).isNotEmpty();
        assertThat(results.size()).isEqualTo(6);

        assertThat(results).containsExactlyElementsOf(children);
    }

    @Test
    public void findById() throws Exception {
        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        repository.save(validParentDocument);
        // WHEN
        final MvcResult mvcResult = mockMvc.perform(get(CHILDREN + "/" + validChild.getId()))
                .andExpect(status().isOk())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        final Child result = mapper.readValue(content, Child.class);

        assertThat(result).isEqualToComparingFieldByField(validChild);
    }

    @Test
    public void update() throws Exception {
        //GIVEN
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT_WITH_ONE_CHILD);
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        repository.save(validParentDocument);

        // WHEN
        mockMvc.perform(put(CHILDREN, validChild)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(validChild)))
                .andExpect(status().isCreated());
        final MvcResult mvcResult = mockMvc.perform(get(CHILDREN))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        final String content = mvcResult.getResponse().getContentAsString();

        final Collection<Child> results = mapper.readValue(content, new TypeReference<Collection<Child>>() {
        });

        assertThat(results.size()).isEqualTo(1);
        final Child result = results.stream().findFirst().orElse(null);
        assertThat(result).isEqualToIgnoringGivenFields(validChild, "id");

        // GIVEN ANOTHER CHILD USING THE SAME ID
        final Child validChildToBeUpdated = from(Child.class).gimme(VALID_CHILD);
        validChildToBeUpdated.setId(result.getId());

        // WHEN
        validChildToBeUpdated.setEmailAddress("modified@gmail.com");

        mockMvc.perform(put(CHILDREN, validChild)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(validChildToBeUpdated)))
                .andExpect(status().isCreated());
        final MvcResult mvcResultUpdate = mockMvc.perform(get(CHILDREN))
                .andExpect(status().isOk())
                .andReturn();

        // THEN WILL BE UPDATED
        final String contentUpdate = mvcResultUpdate.getResponse().getContentAsString();

        final Collection<Child> resultsUpdated = mapper.readValue(contentUpdate, new TypeReference<Collection<Child>>() {
        });

        assertThat(resultsUpdated.size()).isEqualTo(1);
        assertThat(resultsUpdated.stream()
                .findFirst()
                .orElse(Child.builder().build())
                .getEmailAddress())
                .isEqualTo("modified@gmail.com");
    }
}