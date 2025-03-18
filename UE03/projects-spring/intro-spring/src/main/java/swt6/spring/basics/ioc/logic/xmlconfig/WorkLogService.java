package swt6.spring.basics.ioc.logic.xmlconfig;

import swt6.spring.basics.ioc.domain.Employee;

import java.util.List;

public interface WorkLogService {
    Employee findEmployeeById(Long id);
    List<Employee> findAllEmployees();
}
