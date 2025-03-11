package swt6.orm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // default
@DiscriminatorColumn(name="employee_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("E")

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

    // CascadeType.ALL ... loesche ich einen Employee, dann werden auch alle LogbookEntries geloescht; fuege ich einen neuen Employee hinzu, dann werden auch alle LogbookEntries hinzugefuegt etc.
    // auf dieser Seite macht ALL Sinn, auf der Seite des LogbookEntries aber nicht (loescht man einen LogbookEntry, dann wird auch der Employee geloescht)
    // mappedBy... LogbookEntry verwaltet Fremdschluessel = Owning-Side (die Seite, die den Fremdschluessel hat) -> auf der anderen Seite steht mappedBy
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    private Set<LogbookEntry> logbookEntries = new HashSet<>();
    // (V1) Unidirektionale Beziehung: Employee kennt LogbookEntry, aber LogbookEntry kennt Employee nicht
    // -> hat zur Folge, dass LogbookEntry nichts von Employees weiss, daher wird eine Zwischentabelle angelegt

    // V2: Address wird in einer eigenen Tabelle abgelegt (OneToOne)
    // Default-Verhalten (ohne @OneToOne): Address wird in Employee-Tabelle eingebettet (Address wird serialisiert)
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "ADDRESS_ID") // optional (ADDRESS_ID ist bereits default)

    // V3: Embedded (Address wird in Employee-Tabelle eingebettet)
    @Embedded
    @AttributeOverride(name = "zipCode", column = @Column(name = "address_zipCode"))
    @AttributeOverride(name = "city", column = @Column(name = "address_city"))
    @AttributeOverride(name = "street", column = @Column(name = "address_street"))
    @ToString.Exclude
    private Address address;

    @ElementCollection // default: Tabelle EMPLOYEE_PHONES (EMPLOYEE_ID (FK), PHONES)
    @CollectionTable(name = "EMPL_PHONES", joinColumns = @JoinColumn(name = "EMPL_ID")) // optional (Tabelle EMPL_PHONES (EMPL_ID (FK), PHONES))
    @Column(name = "PHONE_NR")
    @ToString.Exclude
    private Set<String> phones = new HashSet<>();

    public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public void addLogbookEntry(LogbookEntry entry) {
        if (entry.getEmployee() != null) {
            entry.getEmployee().logbookEntries.remove(entry);
        }

        logbookEntries.add(entry);
        entry.setEmployee(this);
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }
}
