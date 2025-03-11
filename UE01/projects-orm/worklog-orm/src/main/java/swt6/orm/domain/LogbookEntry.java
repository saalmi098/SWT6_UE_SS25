package swt6.orm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDateTime;

// Kurztest: Gibt mit Sicherheit Punkteabzuege wenn @Entity @NoArgsConstructor und @Id nicht angegeben werden (absolut notwendig)
// (getter/setter nicht zwingend notwendig (wir koennen ueber Field-Access arbeiten) und @ToString auch nicht)
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LogbookEntry implements Serializable {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String activity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // @ManyToOne fetching strategies:
    //  [FetchMode.JOIN]    [FetchType.EAGER]   1 join, eager fetch
    //  FetchMode.SELECT    [FetchType.EAGER]   2 selects, eager fetch
    //  [FetchMode.SELECT]  FetchType.LAZY      2 selects, lazy fetch
    //  FetchMode.JOIN      FetchType.LAZY      contradictory
    // Default fetch strategy: EAGER JOIN

    @org.hibernate.annotations.Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    // (V2) Bidirektionale Beziehung: Employee kennt LogbookEntry und LogbookEntry kennt Employee
    private Employee employee;

    public LogbookEntry(String activity, LocalDateTime startTime, LocalDateTime endTime) {
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
