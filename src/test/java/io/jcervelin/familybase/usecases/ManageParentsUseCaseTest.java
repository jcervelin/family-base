package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.config.UnitTestingSupport;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.domains.mongo.ChildDocument;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import io.jcervelin.familybase.gateways.mongo.ParentsRepository;
import io.jcervelin.familybase.utils.FamilyConverter;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.templates.ParentDocumentTemplates.*;
import static io.jcervelin.familybase.templates.ParentTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ManageParentsUseCaseTest extends UnitTestingSupport {

    @InjectMocks
    private ManageParentsUseCase target;

    @Mock
    private ParentsRepository repository;

    @Spy
    private FamilyConverter familyConverter;

    @Captor
    private ArgumentCaptor<ParentDocument> parentArgumentCaptor;

    @Test
    public void saveShouldSendAValidParent() {

        //GIVEN
        final Parent validParent = from(Parent.class).gimme(VALID_PARENT);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        //WHEN
        when(repository.save(validParentDocument)).thenReturn(validParentDocument);
        target.save(validParent);

        //THEN
        verify(repository, only()).save(parentArgumentCaptor.capture());
        final ParentDocument result = parentArgumentCaptor.getValue();
        assertThat(result).isEqualToIgnoringGivenFields(validParentDocument, "gender");
        assertThat(result).isEqualToIgnoringGivenFields(validParentDocument, "gender");

        result.getChildren().forEach(child -> {
            final ChildDocument expectedChild = validParentDocument.getChildren().stream()
                    .filter(c -> c.equals(child))
                    .findFirst().orElse(null);
            assertThat(child).isEqualToComparingFieldByField(expectedChild);
        });

    }

    @Test(expected = Exception.class)
    public void saveShouldReturnException() {

        //GIVEN
        final Parent validParent = from(Parent.class).gimme(VALID_PARENT);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        //WHEN
        when(repository.save(validParentDocument)).thenThrow(new Exception());
        target.save(validParent);

        //THEN
        //Exception is thrown

    }

    @Test
    public void findAllShouldFind3Parents() {
        //GIVEN
        final List<Parent> parents = from(Parent.class)
                .gimme(3, VALID_PARENT, PARENT_WITHOUT_ID, PARENT_WITHOUT_DATE_OF_BIRTH);
        final List<ParentDocument> parentDocuments = from(ParentDocument.class)
                .gimme(3, VALID_PARENT_DOCUMENT, PARENT_DOCUMENT_WITHOUT_ID, PARENT_DOCUMENT_WITHOUT_DATE_OF_BIRTH);

        //WHEN
        when(repository.findAll()).thenReturn(parentDocuments);
        final Collection<Parent> result = target.findAll();

        //THEN
        assertThat(result.size()).isEqualTo(3);
        result.forEach(parent -> {
            final Parent parentExpected = parents.stream()
                    .filter(p -> p.equals(parent))
                    .findFirst().orElse(null);
            assertThat(parent).isEqualToIgnoringGivenFields(parentExpected, "children", "gender");
            assertThat(parent.getGender()).isEqualToIgnoringCase(parentExpected.getGender());

            parent.getChildren().forEach(child -> {
                final Child expectedChild = parentExpected.getChildren().stream()
                        .filter(c -> c.equals(child))
                        .findFirst().orElse(null);
                assertThat(child).isEqualToIgnoringGivenFields(expectedChild, "gender");
                assertThat(child.getGender()).isEqualToIgnoringCase(expectedChild.getGender());
            });
        });
    }

    @Test
    public void findAllShouldReturn0ParentsAndThenThrowException() {
        //GIVEN
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage("No parent found");

        //WHEN
        when(repository.findAll()).thenReturn(new ArrayList<>());
        target.findAll();

        //THEN
        //Exception is thrown
    }

    @Test
    public void findAllShouldReturnNullThenThrowException() {
        //GIVEN
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage("No parent found");

        //WHEN
        when(repository.findAll()).thenReturn(null);
        target.findAll();

        //THEN
        //Exception is thrown
    }

    @Test
    public void findByIdShouldFindValidParent() {
        //GIVEN
        final Parent validParent = from(Parent.class).gimme(VALID_PARENT);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        //WHEN
        when(repository.findById(validParentDocument.getId())).thenReturn(Optional.of(from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT)));

        final Parent result = target.findById(validParent.getId());

        //THEN
        assertThat(result).isEqualToIgnoringGivenFields(validParent, "gender");
        assertThat(result.getGender()).isEqualToIgnoringCase(validParent.getGender());
        result.getChildren().forEach(child -> {
            final Child expectedChild = validParent.getChildren().stream()
                    .filter(c -> c.equals(child))
                    .findFirst().orElse(null);
            assertThat(child).isEqualToIgnoringGivenFields(expectedChild, "gender");
            assertThat(child.getGender()).isEqualToIgnoringCase(expectedChild.getGender());
        });

    }

    @Test
    public void findByIdShouldReturnException() {
        //GIVEN
        final ObjectId objectId = new ObjectId("5b5e30a04caf45731516eb66");
        final String id = "5b5e30a04caf45731516eb66";

        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage(String.format("No parent found for id %s", id));

        //WHEN
        when(repository.findById(objectId)).thenReturn(Optional.empty());
        target.findById(id);

        //THEN
        //Exception is thrown
    }
}