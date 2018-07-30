package io.jcervelin.familybase.domains;

import lombok.*;

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
public class Parent {
    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
    private Collection<Child> children;
}
