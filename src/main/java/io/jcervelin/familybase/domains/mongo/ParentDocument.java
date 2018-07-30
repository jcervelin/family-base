package io.jcervelin.familybase.domains.mongo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Juliano Cervelin on 25/07/2018
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "Parents")
public class ParentDocument {
    @Id
    private ObjectId id;
    private String title;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
    private Collection<ChildDocument> children;
}
