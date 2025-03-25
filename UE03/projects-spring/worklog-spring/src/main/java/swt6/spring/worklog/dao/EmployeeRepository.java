package swt6.spring.worklog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.domain.Employee;

@Repository
// JpaRepository aus Spring Data, stellt CRUD-Methoden fuer eine Entity bereit
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
