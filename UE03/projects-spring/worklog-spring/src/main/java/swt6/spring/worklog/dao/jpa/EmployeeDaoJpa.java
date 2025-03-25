package swt6.spring.worklog.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

// registriert den Bean im Spring-Context (Sonderfall von @Component) + Exception-Translation
// (Technologieabhängige Exceptions (ORM-Exceptions) werden in Datenbank-/Technologieunabhängige Exceptions umgewandelt)
// dafuer muss aber der Bean PersistenceExceptionTranslationPostProcessor registriert sein -> dann koennen wir unten ueberall throws DataAccessException schreiben
@Repository
public class EmployeeDaoJpa implements EmployeeDao {

    @PersistenceContext // Annotation aus JPA (nicht Spring!), die den Entity Manager injiziert
    private EntityManager em;

    @Override
    public Optional<Employee> findById(Long id) throws DataAccessException {
        return Optional.ofNullable(em.find(Employee.class, id));
    }

    @Override
    public List<Employee> findAll() throws DataAccessException {
        return em.createQuery("select e from Employee e", Employee.class).getResultList();
    }

    @Override
    public void insert(Employee entity) throws DataAccessException {
        em.persist(entity);
    }

    @Override
    public Employee merge(Employee entity) throws DataAccessException {
        return em.merge(entity);
    }
}
