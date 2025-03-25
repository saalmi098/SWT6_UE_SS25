package swt6.spring.worklog.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import swt6.spring.worklog.config.JdbcConfig;
import swt6.spring.worklog.domain.Employee;
import swt6.util.DbScriptRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static swt6.util.PrintUtil.printTitle;

public class JdbcDaoTest {

    private void createSchema(DataSource ds, String ddlScript) {
        try {
            DbScriptRunner scriptRunner = new DbScriptRunner(ds.getConnection());
            InputStream is = JdbcDaoTest.class.getClassLoader().getResourceAsStream(ddlScript);
            if (is == null) throw new IllegalArgumentException(
                    String.format("File %s not found in classpath.", ddlScript));
            scriptRunner.runScript(new InputStreamReader(is));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJdbc() {

        try (AbstractApplicationContext factory =
                     new AnnotationConfigApplicationContext(JdbcConfig.class)) {

            printTitle("create schema", 60, '-');
            createSchema(factory.getBean("dataSource", DataSource.class), "worklog-db-schema.sql");

            // get reference to implementation of EmployeeDao
            EmployeeDao emplDao = factory.getBean("employeeDaoJdbc", EmployeeDao.class); // Name wird durch Factory-Methode vorgegeben (Methodenname employeeDaoJdbc)

            printTitle("insert employee", 60, '-');
            Employee empl1 = new Employee("Josefine", "Feichtlbauer", LocalDate.of(1970, 10, 26));
            emplDao.insert(empl1);
            System.out.println("empl1 = " + (empl1 == null ? (null) : empl1.toString()));

            printTitle("update employee", 60, '-');
            empl1.setFirstName("Jaquira");
            empl1 = emplDao.merge(empl1);
            System.out.println("empl1 = " + (empl1 == null ? (null) : empl1.toString()));

            printTitle("find employee", 60, '-');
            Optional<Employee> empl = emplDao.findById(1L);
            empl.ifPresentOrElse(e -> System.out.println("empl =" + e),
                    () -> System.out.println("empl not found"));
            empl = emplDao.findById(100L);
            empl.ifPresentOrElse(e -> System.out.println("empl =" + e),
                    () -> System.out.println("empl not found"));
            printTitle("find all employees", 60, '-');
            emplDao.findAll().forEach(System.out::println);
        }
    }
}
