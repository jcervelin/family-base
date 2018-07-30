package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.domains.GenderType;
import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import io.jcervelin.familybase.gateways.mongo.ParentsRepository;
import io.jcervelin.familybase.utils.FamilyConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ManageParentsUseCase {

    private final ParentsRepository repository;
    private final FamilyConverter parentConverter;

    public void save(final Parent parent) {
        parent.setGender(GenderType.valueOf(parent.getGender().toUpperCase()).getType());
        parent.getChildren()
                .forEach(child ->
                        child.setGender(GenderType.valueOf(parent.getGender().toUpperCase()).getType()));
        log.info("Saving parent: {}", parent);

        repository.save(parentConverter.fromParentToParentDocument().convert(parent));
    }

    public Collection<Parent> findAll() {
        final Collection<ParentDocument> allNotNull = Optional.ofNullable(repository.findAll())
                .filter(parents -> !parents.isEmpty())
                .orElseThrow(() -> new UnprocessableEntityException("No parent found"));

        final List<Parent> parents = allNotNull.stream()
                .map(parentDocument ->
                        parentConverter.fromParentDocumentToParent().convert(parentDocument))
                .collect(Collectors.toList());
        log.info("Found parents: {}", parents);
        return parents;
    }

    public Parent findById(String id) {
        log.info("Finding parent: {}", id);
        final ParentDocument parentDocumentNotNull = repository.findById(new ObjectId(id))
                .orElseThrow(() ->
                        new UnprocessableEntityException(String.format("No parent found for id %s", id)));
        final Parent parent = parentConverter.fromParentDocumentToParent().convert(parentDocumentNotNull);
        log.info("Parent found: {}", parent);
        return parent;
    }

}
