package io.jcervelin.familybase.domains;

import lombok.*;

import java.time.LocalDate;

/**
 * @author Juliano Cervelin on 25/07/2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Child {
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
}