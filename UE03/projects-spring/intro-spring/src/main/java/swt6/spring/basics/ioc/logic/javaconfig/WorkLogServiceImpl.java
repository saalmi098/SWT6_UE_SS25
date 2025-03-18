package swt6.spring.basics.ioc.logic.javaconfig;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.logic.xmlconfig.WorkLogService;
import swt6.spring.basics.ioc.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {

  private Map<Long, Employee> employees = new HashMap<>();


  @Setter
  private Logger logger;

  public WorkLogServiceImpl(Logger logger) {
    this.logger = logger;
  }

  @PostConstruct // wird automatisch im Konstruktor aufgerufen (nach dem Konstruktor)
  private void init() {
    employees.put(1L, new Employee(1L, "Bill", "Gates"));
    employees.put(2L, new Employee(2L, "James", "Goslin"));
    employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
  }

  @Override
  public Employee findEmployeeById(Long id) {
    Employee empl =  employees.get(id);
    logger.log("findEmployeeById(%d) --> %s".formatted(id, (empl != null) ? empl.toString() : "<null>"));
    return empl;
  }

  @Override
  public List<Employee> findAllEmployees() {
    logger.log("findAllEmployees()");
    return new ArrayList<Employee>(employees.values());
  }
}
