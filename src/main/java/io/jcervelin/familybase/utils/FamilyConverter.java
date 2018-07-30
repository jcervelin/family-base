package io.jcervelin.familybase.utils;

import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.domains.mongo.ChildDocument;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FamilyConverter {

    public Converter<ParentDocument, Parent> fromParentToParentDocument() {
        return parent -> ParentDocument
                .builder()
                .dateOfBirth(parent.getDateOfBirth())
                .emailAddress(parent.getEmailAddress())
                .firstName(parent.getFirstName())
                .gender(parent.getGender())
                .lastName(parent.getLastName())
                .title(parent.getTitle())
                .secondName(parent.getSecondName())
                .id(Optional.ofNullable(parent.getId())
                        .filter(StringUtils::isNotEmpty)
                        .map(ObjectId::new)
                        .orElse(null))
                .children(parent.getChildren().stream()
                        .map(child -> fromChildrenToChildrenDocument().convert(child))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public Converter<Parent, ParentDocument> fromParentDocumentToParent() {
        return parentDocument -> Parent
                .builder()
                .dateOfBirth(parentDocument.getDateOfBirth())
                .emailAddress(parentDocument.getEmailAddress())
                .firstName(parentDocument.getFirstName())
                .gender(parentDocument.getGender())
                .lastName(parentDocument.getLastName())
                .title(parentDocument.getTitle())
                .secondName(parentDocument.getSecondName())
                .id(Optional.ofNullable(parentDocument.getId())
                        .filter(objectId -> StringUtils.isNotEmpty(objectId.toString()))
                        .map(ObjectId::toString)
                        .orElse(null))
                .children(parentDocument.getChildren().stream()
                        .map(child -> fromChildrenDocumentToChildren().convert(child))
                        .collect(Collectors.toList())
                )
                .build();
    }

    public Converter<Child, ChildDocument> fromChildrenDocumentToChildren() {
        return childDocument -> {
            Child child = Child.builder().build();
            BeanUtils.copyProperties(childDocument, child, "id");
            child.setId(childDocument.getId().toString());
            return child;
        };
    }

    public Converter<ChildDocument, Child> fromChildrenToChildrenDocument() {
        return child -> {
            ChildDocument childDocument = ChildDocument.builder().build();
            BeanUtils.copyProperties(child, childDocument, "id");
            try {
                childDocument.setId(Optional.ofNullable(child.getId())
                        .map(ObjectId::new).orElse(new ObjectId()));
            } catch (Exception ex) {
                throw new UnsupportedOperationException(String.format("Invalid child id: %s", child.getId()));
            }
            return childDocument;
        };
    }

    public void updateChildDocument(ChildDocument newChild, ChildDocument oldChild) {
        BeanUtils.copyProperties(newChild, oldChild, "id");
    }
}
