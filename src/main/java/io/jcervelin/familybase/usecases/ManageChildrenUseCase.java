package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.GenderType;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.gateways.h2.ChildrenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ManageChildrenUseCase {

    private final ChildrenRepository repository;

    public void save(final Child child) {
        log.info("Saving child: {}", child);
        child.setGender(GenderType.valueOf(child.getGender().toUpperCase()).getType());
        repository.save(child);
    }

    public Collection<Child> findAll() {
        final Collection<Child> all = repository.findAll();
        log.info("Found children: {}", all);
        return Optional.ofNullable(all)
                .filter(children -> !children.isEmpty())
                .orElseThrow(() -> new UnprocessableEntityException("No child found"));
    }

    public Child findById(Long id) {
        log.info("Finding child: {}", id);
        final Child child = repository.findOne(id);
        log.info("Parent found: {}", child);
        return Optional.ofNullable(child)
                .orElseThrow(() -> new UnprocessableEntityException(String.format("No child found for id %s", id)));
    }
}
