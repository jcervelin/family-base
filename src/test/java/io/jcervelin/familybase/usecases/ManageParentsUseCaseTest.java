package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.config.UnitTestingSupport;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.gateways.h2.ParentsRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.HashSet;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.templates.ParentTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ManageParentsUseCaseTest extends UnitTestingSupport {

    @InjectMocks
    private ManageParentsUseCase target;

    @Mock
    private ParentsRepository repository;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @Test
    public void saveShouldSendAValidParent() {

        //GIVEN
        final Parent validParent = from(Parent.class).gimme(VALID_PARENT);

        //WHEN
        when(repository.save(validParent)).thenReturn(validParent);
        target.save(validParent);

        //THEN
        verify(repository, only()).save(parentArgumentCaptor.capture());
        final Parent result = parentArgumentCaptor.getValue();
        assertThat(result).isEqualToComparingFieldByField(validParent);

        result.getChildren().forEach(child -> {
            final Child expectedChild = validParent.getChildren().stream()
                    .filter(c -> c.equals(child))
                    .findFirst().orElse(null);
            assertThat(child).isEqualToComparingFieldByField(expectedChild);
        });

    }

    @Test(expected = Exception.class)
    public void saveShouldReturnException() {

        //GIVEN
        final Parent validParent = from(Parent.class).gimme(VALID_PARENT);

        //WHEN
        when(repository.save(validParent)).thenThrow(new Exception());
        target.save(validParent);

        //THEN
        //Exception is thrown

    }

    @Test
    public void findAllShouldFind3Parents() {
        //GIVEN
        final Collection<Parent> parents = from(Parent.class)
                .gimme(3, VALID_PARENT, PARENT_WITHOUT_ID, PARENT_WITHOUT_DATE_OF_BIRTH);

        //WHEN
        when(repository.findAll()).thenReturn(parents);
        final Collection<Parent> result = target.findAll();

        //THEN
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactlyElementsOf(parents);
        result.forEach(parent -> {
            final Parent parentExpected = parents.stream()
                    .filter(p -> p.equals(parent))
                    .findFirst().orElse(null);
            assertThat(parent).isEqualToComparingFieldByField(parentExpected);
            parent.getChildren().forEach(child -> {
                final Child expectedChild = parentExpected.getChildren().stream()
                        .filter(c -> c.equals(child))
                        .findFirst().orElse(null);
                assertThat(child).isEqualToComparingFieldByField(expectedChild);
            });
        });
    }

    @Test
    public void findAllShouldReturn0ParentsAndThenThrowException() {
        //GIVEN
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage("No parent found");

        //WHEN
        when(repository.findAll()).thenReturn(new HashSet<>());
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

        //WHEN
        when(repository.findOne(validParent.getId())).thenReturn(from(Parent.class).gimme(VALID_PARENT));

        final Parent result = target.findById(validParent.getId());

        //THEN
        assertThat(result).isEqualToComparingFieldByField(validParent);

        result.getChildren().forEach(child -> {
            final Child expectedChild = validParent.getChildren().stream()
                    .filter(c -> c.equals(child))
                    .findFirst().orElse(null);
            assertThat(child).isEqualToComparingFieldByField(expectedChild);
        });

    }

    @Test
    public void findByIdShouldReturnException() {
        //GIVEN
        final Long id = 10L;
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage(String.format("No parent found for id %s", id));

        //WHEN
        when(repository.findOne(id)).thenReturn(null);
        target.findById(id);

        //THEN
        //Exception is thrown
    }
}