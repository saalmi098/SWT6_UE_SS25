package swt6.orm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

// V1: Mapping wird in hibernate.cfg.xml definiert
// V2: Mapping wird ueber Annotationen definiert

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public Employee() {} // Default Konstruktor ist Voraussetzung fuer OR-Mapper!!

    public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    // es ist ueblich, Getter und Setter zu implementieren in Domaenen-Objekten, aber nicht zwingend notwendig fuer Hiberante (Reflection)


    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "%d: %s, %s (%s)".formatted(id, lastName, firstName, dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
