package swt6.spring.worklog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.domain.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
// JpaRepository aus Spring Data, stellt CRUD-Methoden fuer eine Entity bereit
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // V1: Generierte Abfrage: Ableitung aus Methodennamen
    Optional<Employee> findByLastName(String lastName);

    // V2: Generierte Abfrage: Deklarativ (wuerde auch schon durch Methodennamen funktionieren)
    // @Query nimmt eine JPQL-Query oder native SQL-Query entgegen
//    @Query("select e from Employee e where e.lastName like %?1%") // ?1 ist der erste Parameter
    @Query("select e from Employee e where e.lastName like %:substring%") // benannter Parameter
    List<Employee> findByLastNameContaining(@Param("substring") String lastName);

    @Query("select e from Employee e where e.dateOfBirth < :date")
    List<Employee> findOlderThan(@Param("date") LocalDate date); // best practice: @Param immer verwenden
}
