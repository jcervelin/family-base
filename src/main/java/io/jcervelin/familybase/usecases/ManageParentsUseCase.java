package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.domains.GenderType;
import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.gateways.h2.ParentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ManageParentsUseCase {

    private final ParentsRepository repository;

    public void save(final Parent parent) {
        parent.setGender(GenderType.valueOf(parent.getGender().toUpperCase()).getType());
        parent.getChildren()
                .stream()
                .forEach(child -> child.setGender(GenderType.valueOf(parent.getGender().toUpperCase()).getType()));
        log.info("Saving parent: {}", parent);
        repository.save(parent);
    }

    public Collection<Parent> findAll() {
        final Collection<Parent> all = repository.findAll();
        log.info("Found parents: {}", all);
        return Optional.ofNullable(all)
                .filter(parents -> !parents.isEmpty())
                .orElseThrow(() -> new UnprocessableEntityException("No parent found"));
    }

    public Parent findById(Long id) {
        log.info("Finding parent: {}", id);
        final Parent parent = repository.findOne(id);
        log.info("Parent found: {}", parent);
        return Optional.ofNullable(parent)
                .orElseThrow(() -> new UnprocessableEntityException(String.format("No parent found for id %s", id)));
    }

}
