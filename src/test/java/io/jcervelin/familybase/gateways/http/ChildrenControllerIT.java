package io.jcervelin.familybase.gateways.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.familybase.config.IntegrationTestingSupport;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.gateways.h2.ChildrenRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.domains.Endpoints.CHILDREN;
import static io.jcervelin.familybase.templates.ChildTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChildrenControllerIT extends IntegrationTestingSupport {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Autowired
    private ChildrenRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webAppContext).build();
    }

    @Test
    public void findAll() throws Exception {
        //GIVEN
        final Collection<Child> children = from(Child.class)
                .gimme(3, VALID_CHILD, MID_CHILD, YOUNG_CHILD);

        repository.save(children);

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(get(CHILDREN))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        final String content = mvcResult.getResponse().getContentAsString();

        final Collection<Child> results = mapper.readValue(content, new TypeReference<Collection<Child>>() {
        });

        assertThat(content).isNotEmpty();
        assertThat(results.size()).isEqualTo(3);

        assertThat(results).containsExactlyElementsOf(children);

        results.stream().forEach(child -> {
            final Child expectedChild = children.stream()
                    .filter(c -> c.getEmailAddress().equals(child.getEmailAddress()))
                    .findFirst()
                    .orElse(null);
            assertThat(child).isEqualToIgnoringGivenFields(expectedChild, "id");
        });
    }

    @Test
    public void findById() throws Exception {
        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        validChild.setId(null);
        repository.save(validChild);
        // WHEN
        final MvcResult mvcResult = mockMvc.perform(get(CHILDREN + "/1"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        final Child result = mapper.readValue(content, Child.class);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result).isEqualToIgnoringGivenFields(validChild, "id");
    }

    @Test
    public void save() throws Exception {
        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        validChild.setId(null);
        repository.save(validChild);

        // WHEN
        mockMvc.perform(post(CHILDREN, validChild)
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
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result).isEqualToIgnoringGivenFields(validChild, "id");


        final Child newValidChild = from(Child.class).gimme(VALID_CHILD);
        newValidChild.setId(null);

        // WHEN
        mockMvc.perform(post(CHILDREN, validChild)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newValidChild)))
                .andExpect(status().isCreated());
        final MvcResult mvcResultUpdate = mockMvc.perform(get(CHILDREN))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        final String contentUpdate = mvcResultUpdate.getResponse().getContentAsString();

        final Collection<Child> resultsUpdated = mapper.readValue(contentUpdate, new TypeReference<Collection<Child>>() {
        });

        assertThat(resultsUpdated.size()).isEqualTo(2);

    }

    @Test
    public void update() throws Exception {
        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        validChild.setId(null);
        repository.save(validChild);

        // WHEN
        mockMvc.perform(post(CHILDREN, validChild)
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
        validChildToBeUpdated.setId(1L);

        // WHEN
        validChildToBeUpdated.setEmailAddress("modified@gmail.com");

        mockMvc.perform(post(CHILDREN, validChild)
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