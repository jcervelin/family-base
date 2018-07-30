package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.config.UnitTestingSupport;
import io.jcervelin.familybase.domains.Child;
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
import java.util.function.Predicate;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.templates.ChildDocumentTemplates.VALID_CHILD_DOCUMENT;
import static io.jcervelin.familybase.templates.ChildTemplates.CHILD_WITHOUT_DATE_OF_BIRTH;
import static io.jcervelin.familybase.templates.ChildTemplates.VALID_CHILD;
import static io.jcervelin.familybase.templates.ParentDocumentTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ManageChildrenUseCaseTest extends UnitTestingSupport {

    @InjectMocks
    private ManageChildrenUseCase target;

    @Mock
    private ParentsRepository repository;

    @Spy
    private FamilyConverter familyConverter;

    @Captor
    private ArgumentCaptor<ParentDocument> childArgumentCaptor;

    @Test
    public void updateShouldSendAValidChild() {

        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        final ChildDocument validChildDocument = from(ChildDocument.class).gimme(VALID_CHILD_DOCUMENT);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);


        //WHEN
        when(repository.findByChildrenId(validChildDocument.getId())).thenReturn(Optional.of(validParentDocument));
        when(repository.save(any(ParentDocument.class))).thenReturn(validParentDocument);
        target.updateChild(validChild);

        //THEN
        verify(repository, times(1)).save(childArgumentCaptor.capture());
        final ParentDocument result = childArgumentCaptor.getValue();
        final ChildDocument resultChildDocument = result.getChildren().stream()
                .filter(Predicate.isEqual(validChildDocument))
                .findFirst()
                .orElse(null);

        final Child resultChild = familyConverter.fromChildrenDocumentToChildren().convert(resultChildDocument);

        assertThat(resultChild).isEqualToComparingFieldByField(validChild);
    }

    @Test(expected = Exception.class)
    public void saveShouldReturnException() {

        //GIVEN
        final Child validChild = from(ChildDocument.class).gimme(VALID_CHILD);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        //WHEN
        when(repository.save(validParentDocument)).thenThrow(new Exception());
        target.updateChild(validChild);

        //THEN
        //Exception is thrown

    }

    @Test
    public void findAllShouldFind6Children() {
        //GIVEN
        final List<ParentDocument> parentDocuments = from(ParentDocument.class)
                .gimme(3, VALID_PARENT_DOCUMENT, PARENT_DOCUMENT_WITHOUT_ID, PARENT_DOCUMENT_WITHOUT_DATE_OF_BIRTH);
        final List<Child> children = from(Child.class)
                .gimme(6, VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH,
                        VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH,
                        VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH);

        //WHEN
        when(repository.findAll()).thenReturn(parentDocuments);
        final Collection<Child> result = target.findAll();

        //THEN
        assertThat(result.size()).isEqualTo(6);
        assertThat(result).containsAll(children);

    }

    @Test
    public void findAllShouldReturn0ChildrenAndThenThrowException() {
        //GIVEN
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage("No child found");

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
        thrown.expectMessage("No child found");

        //WHEN
        when(repository.findAll()).thenReturn(null);
        target.findAll();

        //THEN
        //Exception is thrown
    }

    @Test
    public void findByIdShouldFindValidChild() {
        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);
        final ChildDocument validChildDocument = from(ChildDocument.class).gimme(VALID_CHILD_DOCUMENT);
        final ParentDocument validParentDocument = from(ParentDocument.class).gimme(VALID_PARENT_DOCUMENT);

        //WHEN
        when(repository.findByChildrenId(any(ObjectId.class))).thenReturn(Optional.of(validParentDocument));

        final Child result = target.findById(validChildDocument.getId().toString());

        //THEN
        assertThat(result).isEqualToComparingFieldByField(validChild);

    }

    @Test
    public void findByIdShouldReturnException() {
        //GIVEN
        final ObjectId id = new ObjectId();
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage(String.format("No child found for id %s", id));

        //WHEN
        when(repository.findByChildrenId(any(ObjectId.class))).thenReturn(Optional.empty());
        target.findById(id.toString());

        //THEN
        //Exception is thrown
    }
}