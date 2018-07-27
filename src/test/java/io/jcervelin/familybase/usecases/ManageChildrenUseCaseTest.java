package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.config.UnitTestingSupport;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.gateways.h2.ChildrenRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.HashSet;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.familybase.templates.ChildTemplates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ManageChildrenUseCaseTest extends UnitTestingSupport {

    @InjectMocks
    private ManageChildrenUseCase target;

    @Mock
    private ChildrenRepository repository;

    @Captor
    private ArgumentCaptor<Child> childArgumentCaptor;

    @Test
    public void saveShouldSendAValidChild() {

        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);

        //WHEN
        when(repository.save(validChild)).thenReturn(validChild);
        target.save(validChild);

        //THEN
        verify(repository, only()).save(childArgumentCaptor.capture());
        final Child result = childArgumentCaptor.getValue();
        assertThat(result).isEqualToComparingFieldByField(validChild);
    }

    @Test(expected = Exception.class)
    public void saveShouldReturnException() {

        //GIVEN
        final Child validChild = from(Child.class).gimme(VALID_CHILD);

        //WHEN
        when(repository.save(validChild)).thenThrow(new Exception());
        target.save(validChild);

        //THEN
        //Exception is thrown

    }

    @Test
    public void findAllShouldFind3Children() {
        //GIVEN
        final Collection<Child> children = from(Child.class)
                .gimme(3, VALID_CHILD, CHILD_WITHOUT_ID, CHILD_WITHOUT_DATE_OF_BIRTH);

        //WHEN
        when(repository.findAll()).thenReturn(children);
        final Collection<Child> result = target.findAll();

        //THEN
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactlyElementsOf(children);

    }

    @Test
    public void findAllShouldReturn0ChildrenAndThenThrowException() {
        //GIVEN
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage("No child found");

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

        //WHEN
        when(repository.findOne(validChild.getId())).thenReturn(validChild);

        final Child result = target.findById(validChild.getId());

        //THEN
        assertThat(result).isEqualToComparingFieldByField(validChild);

    }

    @Test
    public void findByIdShouldReturnException() {
        //GIVEN
        final Long id = 10L;
        thrown.expect(UnprocessableEntityException.class);
        thrown.expectMessage(String.format("No child found for id %s", id));

        //WHEN
        when(repository.findOne(id)).thenReturn(null);
        target.findById(id);

        //THEN
        //Exception is thrown
    }
}