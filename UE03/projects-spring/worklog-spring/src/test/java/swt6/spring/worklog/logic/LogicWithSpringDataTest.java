package swt6.spring.worklog.logic;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import swt6.spring.worklog.config.JpaConfig2;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;
import swt6.util.JpaUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static swt6.util.PrintUtil.printTitle;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaConfig2.class})
public class LogicWithSpringDataTest { // Kopie von LogicWithJpaTest, aber verwendet JpaConfig2 statt JpaConfig1

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private EmployeeDao employeeDaoJpa;

    @Autowired
    private EntityManagerFactory emFactory;

    @Test
    public void testBusinessLogicWithJpaDaos() {
        printTitle("createEmployeesWithLogbookEntries", 60, '-');
        createEmployeesWithLogbookEntries(workLogService);

        printTitle("printEmployeesWithLogbookEntries", 60, '-');

        JpaUtil.executeInOpenEntityManager(emFactory, () -> {
            printEmployeesWithLogbookEntries(workLogService);
        });

    }

    private void createEmployeesWithLogbookEntries(WorkLogService workLog) {
        var empl1 = new Employee("Sepp", "Forcher", LocalDate.of(1935, 12, 12));
        var empl2 = new Employee("Alfred", "Kunz", LocalDate.of(1944, 8, 10));
        var empl3 = new Employee("Sigfried", "Hinz", LocalDate.of(1954, 5, 3));

        LogbookEntry entry1 = new LogbookEntry("Analyse",
                LocalDateTime.of(2018, 3, 1, 10, 0), LocalDateTime.of(2018, 3, 1, 11, 30));
        LogbookEntry entry2 = new LogbookEntry("Implementierung",
                LocalDateTime.of(2018, 3, 1, 11, 30), LocalDateTime.of(2018, 3, 1, 16, 30));
        LogbookEntry entry3 = new LogbookEntry("Testen",
                LocalDateTime.of(2018, 3, 1, 10, 15), LocalDateTime.of(2018, 3, 1, 14, 30));

        empl1.addLogbookEntry(entry1);
        empl1.addLogbookEntry(entry2);
        empl2.addLogbookEntry(entry3);

        empl1 = workLog.syncEmployee(empl1);
        empl2 = workLog.syncEmployee(empl2);
        empl3 = workLog.syncEmployee(empl3);
    }

    private void printEmployeesWithLogbookEntries(WorkLogService workLog) {
        for (Employee e : workLog.findAllEmployees()) {
            System.out.println(e);
            for (LogbookEntry le : e.getLogbookEntries()) {
                System.out.printf("   %d: %s%n", le.getId(), le);
            }
        }
    }
}