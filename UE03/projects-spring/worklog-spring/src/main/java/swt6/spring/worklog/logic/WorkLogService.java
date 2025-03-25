package swt6.spring.worklog.logic;

import java.util.List;
import java.util.Optional;

import swt6.spring.worklog.domain.Employee;

public interface WorkLogService {
    Employee syncEmployee(Employee employee);
    Optional<Employee> findEmployeeById(Long id);
    List<Employee> findAllEmployees();
}
