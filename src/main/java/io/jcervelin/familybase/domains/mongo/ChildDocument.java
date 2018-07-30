package io.jcervelin.familybase.domains.mongo;

import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChildDocument {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
}