package swt6.orm.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import swt6.orm.domain.Employee;
import swt6.util.HibernateUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeManager {

  static String promptFor(BufferedReader in, String prompt) {
    System.out.print(prompt + "> ");
    System.out.flush();
    try {
      return in.readLine();
    }
    catch (Exception e) {
      return promptFor(in, prompt);
    }
  }

  // Version 1
  static void saveEmployee1(Employee e) {
    // sessionFactory = schwergewichtig, dauert lange zu instanziieren (moeglichst nur 1x machen in der Anwendung)
    try (var sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
      var session = sessionFactory.openSession(); // session = Konkrete Verbindung zur Datenbank (leichtgewichtig; nicht thread-safe)
      var tx = session.beginTransaction();
      session.persist(e);
      tx.commit();
    } // session.close() und sessionFactory.close() werden automatisch aufgerufen
  }

  // Version 2
  static void saveEmployee2(Employee e) {
    try (var sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
      var session = sessionFactory.getCurrentSession(); // wir bekommen eine Session die von Hibernate verwaltet wird, wird automatisch geschlossen beim Commit
      var tx = session.beginTransaction();
      session.persist(e);
      tx.commit();
    }
  }

  // Version 3
  static void saveEmployee3(Employee e) {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction();
      session.persist(e);
      tx.commit();
    }
  }

  public static List<Employee> getAllEmployees() {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction(); // Transaktion (fuer Lesesperren)

      // Hibernate Query Language (HQL) (SQL-aehnlich)
      List<Employee> empls = session.createQuery("select e from Employee e", Employee.class).getResultList();

      tx.commit();
      return empls;
    }
  }

  private static boolean updateEmployee(long employeeId, String firstName, String lastName, LocalDate dob) {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction();

      Employee emp = session.find(Employee.class, employeeId);
      if (emp != null) {
        // "automatic dirty checking" Mechanismus (Aenderungen an Objekten werden automatisch in die Datenbank geschrieben)
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setDateOfBirth(dob);
      }

      tx.commit();
      return emp != null;
    }
  }

  private static Employee findEmployeeById(long employeeId) {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction();
      Employee emp = session.find(Employee.class, employeeId); // find = Suche nach Primaerschluessel
      tx.commit();
      return emp;
    }
  }

  private static List<Employee> findEmployeesByLastName(String lastName) {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction();

      Query<Employee> query = session.createQuery("select e from Employee e where e.lastName like :lastName", Employee.class);
      query.setParameter("lastName", "%" + lastName + "%");
      List<Employee> emps = query.getResultList();
      tx.commit();

      return emps;
    }
  }

  public static boolean deleteEmployee(long employeeId) {
    try (var session = HibernateUtil.getCurrentSession()) {
      var tx = session.beginTransaction();

      // Variante 1
//      Employee emp = session.find(Employee.class, employeeId);
//      if (emp != null) {
//        session.remove(emp);
//      }
//      boolean deleted = emp != null;
//      return deleted;

      // Variante 2
      Query<?> deleteQuery = session.createQuery("delete from Employee e where e.id = :id");
      deleteQuery.setParameter("id", employeeId);
      boolean deleted = deleteQuery.executeUpdate() == 1; // Gibt Anzahl der geloeschten Datensaetze zurueck

      tx.commit();
      return deleted;
    }
  }

  public static void main(String[] args) {
    var    formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    var    in        = new BufferedReader(new InputStreamReader(System.in));
    String availCmds = "commands: quit, insert, list, update, delete, findById, findByLastName";

    HibernateUtil.getSessionFactory(); // SessionFactory instanziieren

    System.out.println("Hibernate Employee Admin");
    System.out.println(availCmds);
    String userCmd = promptFor(in, "");

    try {
      while (!userCmd.equals("quit")) {
        try {
          switch (userCmd) {
            case "insert" -> {
              saveEmployee3(new Employee(
                    promptFor(in, "firstName"),
                    promptFor(in, "lastName"),
                    LocalDate.parse(promptFor(in, "dateOfBirth (dd.mm.yyyy)"), formatter)
              ));
            }

            case "update" -> {
              boolean success = updateEmployee(
                    Long.parseLong(promptFor(in, "id")),
                    promptFor(in, "lastName"),
                    promptFor(in, "firstName"),
                    LocalDate.parse(promptFor(in, "dateOfBirth (dd.mm.yyyy)"), formatter)
              );
              System.out.println(success ? "Employee updated" : "Employee not found");
            }

            case "delete" -> {
              boolean success = deleteEmployee(Long.parseLong(promptFor(in, "id")));
              System.out.println(success ? "Employee deleted" : "Employee not found");
            }

            case "list" -> {
              for (var emp : getAllEmployees()) {
                System.out.println(emp);
              }
            }

            case "findById" -> {
                Employee emp = findEmployeeById(Long.parseLong(promptFor(in, "id")));
                System.out.println(emp != null ? emp : "Employee not found");
            }

            case "findByLastName" -> {
              for (var emp : findEmployeesByLastName(promptFor(in, "lastName"))) {
                System.out.println(emp);
              }
            }

            default -> {
              System.out.println("ERROR: invalid command");
              break;
            }
          } // switch
        } // try
        catch (NumberFormatException ignored) {
          System.out.println("ERROR: Cannot parse integer");
        }
          catch (DateTimeParseException ignored) {
          System.out.println("ERROR: Cannot parse date");
        }

        System.out.println(availCmds);
        userCmd = promptFor(in, "");
      } // while
    } // try
    catch (Exception ex) {
      ex.printStackTrace();
    } // catch
    finally {
        HibernateUtil.closeSessionFactory(); // SessionFactory schliessen
    }
  }
}
