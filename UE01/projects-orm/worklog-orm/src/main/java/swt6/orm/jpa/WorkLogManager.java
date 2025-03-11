package swt6.orm.jpa;

import jakarta.persistence.*;
import swt6.orm.domain.*;
import swt6.util.JpaUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static swt6.util.JpaUtil.executeInTransaction;

public class WorkLogManager {
    // V1
    private static void insertEmployeeV1(Employee employee) {

        try (EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("WorkLogPU")) {
            EntityManager em = emFactory.createEntityManager();

            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(employee); // ueberfuehrt neues Objekt von Zustand "transient" in Zustand "persistent"
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }

    // V2
    private static void insertEmployeeV2(Employee employee) { // Employee hier ist transient
        try (var em = JpaUtil.getTransactionalEntityManager()) {
            try {
                em.persist(employee); // Employee ist jetzt persistent (wird vom Persistenz-Manager verwaltet, Objekt bekommt einen Schluessel)
                JpaUtil.commit(em); // Employee wird in die Datenbank geschrieben (zuvor lebt er im First Level Cache vom Persistenz-Manager)
            } catch (Exception e) {
                JpaUtil.rollback(em);
                throw e;
            }
            // sobald wir das Close vom EntityManager aufrufen (hier mit try-with-resources) ist Employee detached
        }

        // dieser Aufbau ist immer wieder der gleiche -> auslagern mit Template-Method-Pattern -> insertEmployeeV3 und JpaUtil.executeInTransaction
    }

    // V3
    private static void insertEmployeeV3(Employee employee) {
        executeInTransaction(em -> {
            em.persist(employee);
        });

        // in IntelliJ -> Settings -> Live Templates -> Java -> Snippet "emtx" erstellt
    }

    private static <T> void insertEntity(T entity) {
        executeInTransaction(em -> {
            em.persist(entity);
        });
    }

    private static <T> T saveEntity(T entity) {
        // merge:
        // Schritt 1: Prueft, ob es in der Datenbank schon ein Objekt mit dem gleichen Schluessel gibt
        // Schritt 2a: Wenn nein, wird das Objekt in der Datenbank eingefuegt
        // Schritt 2b: Wenn ja, wird das Objekt in der Datenbank mit den Werten des uebergebenen Objekts aktualisiert

        // Ganz wichtig: merge gibt eine Referenz auf das Objekt zurueck, das in der Datenbank verwaltet wird
        // -> das zurueckgegebene Objekt ist immer ein persistentes Objekt!!!!! (auf jeden Fall merken)

        return executeInTransaction(em -> (T)em.merge(entity));
    }

    private static Employee addLogbookEntries(Employee emp, LogbookEntry... entries) {
        // employee ist hier detached

        return executeInTransaction(em -> {
            // employee wird persistent mit merge
            var persEmp = em.merge(emp);

            // VOR for-schleife mergen: Automatic Dirty Checking (Hibernate) -> Hibernate ueberwacht die Objekte im First Level Cache und schreibt die Aenderungen in die Datenbank

            for (LogbookEntry entry : entries) {
                persEmp.addLogbookEntry(entry);
                // hier greift Automatic Dirty Checking Mechanismus -> schreibt die Aenderungen in die Datenbank
            }

            return persEmp;
        });
    }

    private static Employee addPhones(Employee emp, String... phones) {
        return executeInTransaction(em -> {
            var persEmp = em.merge(emp); // employee wird wieder persistent

            for (String phone : phones) {
                persEmp.addPhone(phone);
            }

            return persEmp;
        });
    }

    private static void listEmployees() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        executeInTransaction(em -> {
            // JPQL ... Java Persistence Query Language (in UE01 mit Hibernate war es HQL)
            // JPQL ist ein Subset von HQL (jedes gueltige JPQL-Statement ist auch ein gueltiges HQL-Statement, aber nicht umgekehrt)
            List<Employee> empList = em.createQuery("select e from Employee e", Employee.class).getResultList();
            empList.forEach(e -> {
                System.out.println(e);

                if (e.getAddress() != null) {
                    System.out.printf("  address: %s%n", e.getAddress());
                }

                if (!e.getPhones().isEmpty()) {
                    System.out.println("  phone numbers:");
                    e.getPhones().forEach(p -> System.out.printf("    %s%n", p));
                }

                if (!e.getLogbookEntries().isEmpty()) {
                    System.out.println("  logbookEntries:");
                    e.getLogbookEntries().forEach(
                            l -> System.out.printf("    %s: %s - %s%n", l.getActivity(),
                                    l.getStartTime().format(fmt), l.getEndTime().format(fmt))
                    );
                }
            });
        });
    }

    private static void listEntriesOfEmployee(Employee emp) {
        executeInTransaction(em -> {
            System.out.printf("logbook entries of employee: %s (%d)%n",
                    emp.getLastName(), emp.getId());

            TypedQuery<LogbookEntry> qry = em.createQuery("select le from LogbookEntry le where le.employee=:empl", LogbookEntry.class);
            qry.setParameter("empl", emp);

            qry.getResultList().forEach(e -> System.out.printf("    %s%n", e));
        });
    }

    private static void loadEmployeesWithEntries() {
        executeInTransaction(em -> {
            // Erweiterung "join fetch": Default-Verhalten ueberschreiben und die assoziierten LogbookEntries sofort mitladen (selbst wenn Lazy-Loading in Mapping definiert ist)
            TypedQuery<Employee> qry = em.createQuery("select e from Employee e join fetch e.logbookEntries", Employee.class);
            qry.getResultList().forEach(e -> {
                System.out.println(e);
                e.getLogbookEntries().forEach(le -> System.out.println("  " + le));
            });
        });
    }

    private static <T> Optional<T> findAny(Class<T> entityClass) {
        return executeInTransaction(em -> {
            Optional<T> entity =
                    em.createQuery("select le from %s le".formatted(entityClass.getSimpleName()), entityClass).setMaxResults(1)
                            .getResultList().stream().findAny();
            return entity;
        });
    }

    private static void testFetchingStrategies() {
        // prepare: fetch valid ids for employee and logbookEntry
        var anyEmpl  = findAny(Employee.class);
        var anyEntry = findAny(LogbookEntry.class);
        if (anyEmpl.isEmpty() || anyEntry.isEmpty()) return;

        System.out.println("############################################");

        executeInTransaction(em -> {
            Long entryId = anyEntry.get().getId();
            System.out.println("###> Fetching LogbookEntry ...");
            LogbookEntry entry = em.find(LogbookEntry.class, entryId);
            System.out.println("###> Fetched LogbookEntry");
            Employee empl1 = entry.getEmployee();
            System.out.println("###> Fetched associated Employee");
            System.out.println(empl1);
            System.out.println("###> Accessed associated Employee");
        });

        System.out.println("############################################");

        executeInTransaction(em -> {
            Long emplId = anyEmpl.get().getId();
            System.out.println("###> Fetching Employee ...");
            Employee empl2 = em.find(Employee.class, emplId);
            System.out.println("###> Fetched Employee");
            Set<LogbookEntry> entries = empl2.getLogbookEntries();
            System.out.println("###> Fetched associated entries");
            for (LogbookEntry e : entries)
                System.out.println("  " + e);
            System.out.println("###> Accessed associated entries");
        });

        System.out.println("############################################");
    }

    public static void main(String[] args) {
        // Datenbank starten: Maven View -> worklog-orm -> Run Configurations -> Start DB

        // Persistenz-Manager: In Hibernate "Session", in JPA "EntityManager"

        try {
            System.out.println("--------- create schema ---------");
            JpaUtil.getEntityManagerFactory();

            var pe = new PermanentEmployee("Franz", "Mayr", LocalDate.of(1990, 01, 01));
            pe.setSalary(5000.0);
            pe.setAddress(new Address("4232", "Hagenberg", "Softwarepark 11"));
            Employee emp1 = pe;

            var te = new TemporaryEmployee("Susi", "Sorglos", LocalDate.of(2000, 03, 06));
            te.setHourlyRate(50.0);
            te.setRenter("Microsoft");
            te.setStartDate(LocalDate.of(2025, 01, 01));
            te.setEndDate(LocalDate.of(2025, 12, 31));
            Employee emp2 = te;

            // Problem V1: Es wird nur 1 Employee in die Datenbank eingefuegt, da Factory doppelt erzeugt wird und bei jedem Erzeugen die Datenbank neu erstellt wird
            // (siehe auch Kommentar in persistence.xml)
            //        insertEmployeeV1(emp1);
            //        insertEmployeeV1(emp2);

            LogbookEntry entry1 = new LogbookEntry("Analyse", LocalDateTime.of(2025, 3, 10, 8, 15), LocalDateTime.of(2025, 3, 10, 10, 15));
            LogbookEntry entry2 = new LogbookEntry("Implementierung", LocalDateTime.of(2025, 3, 10, 10, 30), LocalDateTime.of(2025, 3, 10, 15, 15));
            LogbookEntry entry3 = new LogbookEntry("Testen", LocalDateTime.of(2025, 3, 10, 11, 15), LocalDateTime.of(2025, 3, 10, 18, 30));

            System.out.println("--------- insertEmployee ---------");
            insertEmployeeV3(emp1);
            insertEmployeeV3(emp2);

            System.out.println("--------- saveEntity ---------");
            emp2.setLastName("Huber-Mayr");
            emp2 = saveEntity(emp2);

            System.out.println("--------- listEmployees (1) ---------");
            listEmployees();

            System.out.println("--------- addLogbookEntries ---------");
            emp1 = addLogbookEntries(emp1, entry1, entry2);
            emp2 = addLogbookEntries(emp2, entry3);

            System.out.println("--------- addPhones ---------");
            emp1 = addPhones(emp1, "(07543) 123 4567)", "(0664) 123 4567");

            System.out.println("--------- listEmployees (2) ---------");
            listEmployees();

            System.out.println("--------- listEntriesOfEmployee ---------");
            listEntriesOfEmployee(emp1);

            System.out.println("--------- loadEmployeesWithEntries ---------");
            loadEmployeesWithEntries();

//            System.out.println("--------- testFetchingStrategies ---------");
//            testFetchingStrategies();
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
