package swt6.spring.worklog.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.jdbc.EmployeeDaoJdbc;

import javax.sql.DataSource;

@Configuration
@PropertySource("jdbc.properties")
public class JdbcConfig {

    // Zugriff auf Werte in Properties-Datei mit @Value
    @Value("${database.driverClassName}")
    private String driverClassName;

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // ========================= DAO DEFINITIONS ================================

    // Factory-Methode zur Erzeugung eines EmployeeDao-Objekts
    @Bean
    public EmployeeDao employeeDaoJdbc(DataSource dataSource) {
        EmployeeDaoJdbc employeeDaoJdbc = new EmployeeDaoJdbc();
        employeeDaoJdbc.setDataSource(dataSource);
        return employeeDaoJdbc;
    }

    // ====================== BUSINESS OBJECT DEFINIONS =========================
}