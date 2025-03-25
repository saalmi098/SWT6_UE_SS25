package swt6.spring.worklog.dao;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.config.JpaConfig1;
import swt6.spring.worklog.dao.jpa.EmployeeDaoJpa;
import swt6.spring.worklog.domain.Employee;
import swt6.util.JpaUtil;
import swt6.util.PrintUtil;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaConfig1.class})
public class JpaDaoTest {

    @Autowired
    private EntityManagerFactory emf;

    @Autowired // Instanz von EmployeeDaoJpa wird automatisch injiziert
    private EmployeeDao employeeDao;

    @Test
    @Transactional
    public void testInsertUpdate() {
        PrintUtil.printTitle("insert/update employee", 60, '-');

        // V1: Explizite Transaktion -> Problem: Vermischt Geschaeftslogik-Code mit Technologie-Code
//        JpaUtil.executeInTransaction(emf, () -> {
//            Employee empl1 = new Employee("Josefine", "Feichtlbauer", LocalDate.of(1970, 10, 26));
//            employeeDao.insert(empl1);
//        });

        // V2: mit @Transactional -> Erfordert @EnableTransactionManagement und ein TransactionManager-Bean (JpaConfig1)
        Employee empl1 = new Employee("Josefine", "Feichtlbauer", LocalDate.of(1970, 10, 26));
        employeeDao.insert(empl1);
        empl1.setFirstName("Johanna");
        empl1 = employeeDao.merge(empl1);
    }

    @Test
    @Transactional
    public void testFindById() {
        testInsertUpdate(); // RIESEN PFUSCH!!! Aber wir inserten einen Employee damit wir einen im First-Level-Cache haben

        PrintUtil.printTitle("find employee", 60, '-');
        Optional<Employee> empl = employeeDao.findById(1L);
        System.out.printf("empl = " + empl.map(Employee::toString).orElse("<not found>"));

        empl = employeeDao.findById(100L);
        System.out.printf("empl = " + empl.map(Employee::toString).orElse("<not found>"));
    }

    @Test
    @Transactional
    public void testFindAll() {
        testInsertUpdate(); // RIESEN PFUSCH!!! Aber wir inserten einen Employee damit wir einen im First-Level-Cache haben

        PrintUtil.printTitle("find all employees", 60, '-');
        employeeDao.findAll().forEach(System.out::println);
    }
}