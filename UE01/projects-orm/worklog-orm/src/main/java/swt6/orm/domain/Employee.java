package swt6.orm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
// @EqualsAndHashCode darf man in Kombination mit Hibernate nicht verwenden, da es zu Problemen fuehren kann (s. Vorlesung)
// (siehe https://thorben-janssen.com/lombok-hibernate-how-to-avoid-common-pitfalls/)
public class Employee implements Serializable { // Serializable keine Voraussetzung fuer Hibernte, aber in Zukunft bei Spring benoetigt

    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE) // Access-Modifier von ID-Setter aendern
    private Long id;

    // In der IDE sieht man links von den Feldern ein "a" Icon, das bedeutet dass die Felder automatisch gemapped werden (??)
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.firstName = firstName;
    }


}
