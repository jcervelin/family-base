package io.jcervelin.familybase.domains;

import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import javax.persistence.*;
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
@Entity(name = "PARENTS")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String emailAddress;
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dateOfBirth;
    private String gender;
    private String secondName;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Child> children;
}
