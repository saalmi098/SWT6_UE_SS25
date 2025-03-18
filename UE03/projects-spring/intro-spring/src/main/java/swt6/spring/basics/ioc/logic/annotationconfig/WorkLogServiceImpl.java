package swt6.spring.basics.ioc.logic.annotationconfig;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.logic.xmlconfig.WorkLogService;
import swt6.spring.basics.ioc.util.Log;
import swt6.spring.basics.ioc.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {

  private Map<Long, Employee> employees = new HashMap<>();

  // Unterschied zwischen @Autowired und @Inject: (
  // @Autowired ... Spring-Annotation
  // @Inject ... offizieller Dependency-Injection-Standard von Java (CDI - Context and Dependency Injection, JSR330)

  // @Autowired / @Inject sucht nach Implementierungen des Interface Logger

  // -- Variant 1: Field injection using @Autowired or @Inject
  @Inject // oder @Autowired
  @Setter
//  @Named("consoleLogger") // V1: notwendig, wenn mehrere Implementierungen des Interfaces Logger existieren -> per Bean-Namen identifizieren
  @Log(Log.Type.STANDARD) //   V2: eigener Qualifier "Log.java"

  // Vor- und Nachteile von @Named und @Log:
  //  -> @Named einfacher (kein eigener Qualifier noetig), aber auch fehleranfaellig
  //  -> @Log aufwaendiger, aber typsicher

  // -- Variant 2: Setter injection when using Lombok
//  @Setter(onMethod_ = {@Autowired}) // onMethod ... Annotationen angeben, die ueber den Setter geschrieben werden, wenn dieser generiert wird
  private Logger logger;

  // -- Variant 3: Constructor injection using @Autowired or @Inject
  public WorkLogServiceImpl(/*@Autowired*/ Logger logger) {
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
