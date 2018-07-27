package io.jcervelin.familybase.domains;

import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Juliano Cervelin on 25/07/2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "CHILDREN")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
}