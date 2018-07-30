package io.jcervelin.familybase.usecases;

import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.GenderType;
import io.jcervelin.familybase.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.familybase.domains.mongo.ChildDocument;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import io.jcervelin.familybase.gateways.mongo.ParentsRepository;
import io.jcervelin.familybase.utils.FamilyConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ManageChildrenUseCase {

    private final ParentsRepository repository;
    private final FamilyConverter familyConverter;

    public void updateChild(final Child child) {
        log.info("Saving child: {}", child);
        child.setGender(GenderType.valueOf(child.getGender().toUpperCase()).getType());
        final ChildDocument childDocument = familyConverter.fromChildrenToChildrenDocument().convert(child);
        final ParentDocument parentDocumentFound = repository.findByChildrenId(childDocument.getId())
                .orElseThrow(() -> new UnprocessableEntityException(String.format("No child found for id %s", child.getId())));

        ChildDocument childFound = parentDocumentFound.getChildren()
                .stream()
                .filter(Predicate.isEqual(childDocument))
                .findFirst()
                .orElseThrow(() -> new UnprocessableEntityException(String.format("No child found for id %s", child.getId())));

        familyConverter.updateChildDocument(childDocument, childFound);

        repository.save(parentDocumentFound);
    }

    public Collection<Child> findAll() {
        final List<ChildDocument> allChildren = CollectionUtils.emptyIfNull(repository.findAll())
                .stream()
                .map(ParentDocument::getChildren)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final List<ChildDocument> childrenDocumentFound = Optional.of(allChildren)
                .filter(children -> !children.isEmpty())
                .orElseThrow(() -> new UnprocessableEntityException("No child found"));

        final List<Child> childrenFound = childrenDocumentFound.stream().map(childDocument ->
                familyConverter.fromChildrenDocumentToChildren().convert(childDocument))
                .collect(Collectors.toList());
        log.info("Found children: {}", childrenFound);
        return childrenFound;
    }

    public Child findById(String childId) {
        log.info("Finding child: {}", childId);
        final ParentDocument parent = repository.findByChildrenId(new ObjectId(childId))
                .orElse(ParentDocument.builder().build());
        log.info("Parent found: {}", parent);

        final Optional<ChildDocument> childDocumentFoundOpt = CollectionUtils.emptyIfNull(parent.getChildren())
                .stream()
                .filter(childDocument -> childDocument.getId().toString().equals(childId))
                .findFirst();

        final ChildDocument childDocumentFound = childDocumentFoundOpt
                .orElseThrow(() -> new UnprocessableEntityException(String.format("No child found for id %s", childId)));

        final Child child = familyConverter.fromChildrenDocumentToChildren().convert(childDocumentFound);

        log.info("Found child: {}", child);
        return child;
    }
}
