package swt6.orm.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import swt6.orm.domain.Employee;
import swt6.util.JpaUtil;

import java.time.LocalDate;
import java.util.List;

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

    private static void listEmployees() {
        executeInTransaction(em -> {
            // JPQL ... Java Persistence Query Language (in UE01 mit Hibernate war es HQL)
            // JPQL ist ein Subset von HQL (jedes gueltige JPQL-Statement ist auch ein gueltiges HQL-Statement, aber nicht umgekehrt)
            List<Employee> empList = em.createQuery("select e from Employee e", Employee.class).getResultList();
            empList.forEach(System.out::println);
        });
    }

    public static void main(String[] args) {
        // Datenbank starten: Maven View -> worklog-orm -> Run Configurations -> Start DB

        // Persistenz-Manager: In Hibernate "Session", in JPA "EntityManager"

        try {
            System.out.println("--------- create schema ---------");
            JpaUtil.getEntityManagerFactory();

            Employee emp1 = new Employee("Franz", "Mayr", LocalDate.of(1990, 01, 01));
            Employee emp2 = new Employee("Susi", "Sorglos", LocalDate.of(2000, 03, 06));

            // Problem V1: Es wird nur 1 Employee in die Datenbank eingefuegt, da Factory doppelt erzeugt wird und bei jedem Erzeugen die Datenbank neu erstellt wird
            // (siehe auch Kommentar in persistence.xml)
            //        insertEmployeeV1(emp1);
            //        insertEmployeeV1(emp2);

            System.out.println("--------- insertEmployee ---------");
            insertEmployeeV3(emp1);
            insertEmployeeV3(emp2);

            System.out.println("--------- saveEntity ---------");
            emp2.setLastName("Huber-Mayr");
            emp2 = saveEntity(emp2);

            System.out.println("--------- listEmployees ---------");
            listEmployees();
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
