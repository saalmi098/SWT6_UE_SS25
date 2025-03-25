package swt6.spring.worklog.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.TestAnnotationUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.config.JpaConfig1;
import swt6.spring.worklog.domain.Employee;
import swt6.util.PrintUtil;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaConfig1.class})
public class SpringDataRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Transactional
    public void testSave() {
        Employee empl1 = new Employee("Josefine", "Feichtlbauer", LocalDate.of(1970, 10, 26));
        Employee empl2 = new Employee("Josef", "Himmelbauer", LocalDate.of(1990, 1, 15));

        PrintUtil.printTitle("insert employee", 60, '-');
        empl1 = employeeRepository.save(empl1);
        empl2 = employeeRepository.save(empl2);
        employeeRepository.flush(); // Aenderungen sofort in die Datenbank schreiben

        PrintUtil.printTitle("update employee", 60, '-');
        empl1.setLastName("Himmelbauer-Huber");
        empl1 = employeeRepository.save(empl1);
        employeeRepository.flush();
    }

    @Test
    @Transactional
    public void testFind() {
        testSave(); // (hier auch Pfusch)

        PrintUtil.printTitle("findById", 60, '-');
        Optional<Employee> empl = employeeRepository.findById(1L);
        System.out.printf("empl = " + empl.map(Employee::toString).orElse("<not found>"));

        PrintUtil.printTitle("findAll", 60, '-');
        employeeRepository.findAll().forEach(System.out::println);
    }

    @Test
    @Transactional
    public void testQueries() {
        testSave();

        PrintUtil.printTitle("findByLastName", 60, '-');
        Optional<Employee> empl = employeeRepository.findByLastName("Himmelbauer-Huber");

        PrintUtil.printTitle("findByLastNameContaining", 60, '-');
        employeeRepository.findByLastNameContaining("mm").forEach(System.out::println);

        PrintUtil.printTitle("findOlderThan", 60, '-');
        employeeRepository.findOlderThan(LocalDate.of(1980, 1, 1)).forEach(System.out::println);
    }
}