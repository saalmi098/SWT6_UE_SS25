package swt6.orm.domain;

import jakarta.persistence.*;
import lombok.*;

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

    // (V2) Bidirektionale Beziehung: Employee kennt LogbookEntry und LogbookEntry kennt Employee
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Employee employee;

    public LogbookEntry(String activity, LocalDateTime startTime, LocalDateTime endTime) {
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
