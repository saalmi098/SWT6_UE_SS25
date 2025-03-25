package swt6.spring.worklog.dao.jdbc;

import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoJdbc implements EmployeeDao {

    @Setter
//    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate; // = DAO-Template (s. Vorlesungsfolien)

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
    @Override
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
    public Optional<Employee> findById(Long id) {
        final String sql = "select ID, FIRSTNAME, LASTNAME, DATEOFBIRTH from EMPLOYEE where ID = ?";
        List<Employee> empList = jdbcTemplate.query(sql, new EmployeeMapper(), id);
        if (empList.isEmpty()) {
            return Optional.empty();
        } if (empList.size() == 1) {
            return Optional.of(empList.getFirst());
        }
        else {
            throw new IncorrectResultSizeDataAccessException(1, empList.size());
        }
    }

    @Override
    public List<Employee> findAll() {
        final String sql = "select ID, FIRSTNAME, LASTNAME, DATEOFBIRTH from EMPLOYEE";
        return jdbcTemplate.query(sql, new EmployeeMapper());
    }

    @Override
    public Employee merge(Employee empl) {
        if (empl.getId() == null) {
            insert(empl);
        } else {
            update(empl);
        }

        return empl;
    }

    private void update(final Employee e) throws DataAccessException {
        // Kopiert von insert2

        final String sql = "update EMPLOYEE set FIRSTNAME=?, LASTNAME=?, DATEOFBIRTH=? where ID=? ";

        jdbcTemplate.update(
                sql,
                e.getFirstName(),
                e.getLastName(),
                Date.valueOf(e.getDateOfBirth()),
                e.getId());
    }

    protected static class EmployeeMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee e = new Employee();
            e.setId(rs.getLong("ID")); // rs.getLong(1)
            e.setFirstName(rs.getString("FIRSTNAME")); // rs.getString(2)
            e.setLastName(rs.getString("LASTNAME")); // rs.getString(3)
            e.setDateOfBirth(rs.getDate("DATEOFBIRTH").toLocalDate()); // rs.getDate(4).toLocalDate()
            return e;
        }
    }
}
