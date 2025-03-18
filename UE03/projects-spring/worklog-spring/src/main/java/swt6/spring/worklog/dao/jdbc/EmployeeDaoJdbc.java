package swt6.spring.worklog.dao.jdbc;

import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoJdbc implements EmployeeDao {

    @Setter
//    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    // Version 1: Data access code without Spring
    public void insert1(final Employee e) throws DataAccessException {
        final String sql = "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) values (?, ?, ?)";
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getFirstName());
            stmt.setString(2, e.getLastName());
            stmt.setDate(3, Date.valueOf(e.getDateOfBirth()));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    // Version 2: Data access using Spring's JdbcTemplate
    public void insert2(final Employee e) throws DataAccessException {
        final String sql = "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) values (?, ?, ?)";
//        jdbcTemplate.update(sql, ps -> {
//            ps.setString(1, e.getFirstName());
//            ps.setString(2, e.getLastName());
//            ps.setDate(3, Date.valueOf(e.getDateOfBirth()));
//        });

        jdbcTemplate.update(sql,
            e.getFirstName(),
            e.getLastName(),
            Date.valueOf(e.getDateOfBirth()));
    }

    // Version 3: Data access using Spring's JdbcTemplate and KeyHolder
    public void insert(final Employee e) throws DataAccessException {
        final String sql = "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) values (?, ?, ?)";

        // Mitgeben, welche die Auto-generierten Spalten sind (KeyHolder = Container, der die generierten Schluessel haelt)
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setDate(3, Date.valueOf(e.getDateOfBirth()));
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue(); // Auto-generierter Schluessel ermitteln
        e.setId(id);

        // --> Employee bekommt ID zugewiesen
    }

    @Override
    public Optional<Employee> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public Employee merge(Employee entity) {
        return null;
    }
}
