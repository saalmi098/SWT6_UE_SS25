package swt6.spring.worklog.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.TransactionManager;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.jpa.EmployeeDaoJpa;

@Configuration
@Import(JpaDataSourceConfig.class)
public class JpaConfig1 {

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public TransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf.getObject());
        return transactionManager;
    }

    // ============================== DATA ACCESS LAYER =============================

    @Bean
    public EmployeeDao employeeDaoJpa() {
        return new EmployeeDaoJpa();
    }

    //============================= BUSINESS LOGIC LAYER ============================

    // ============================== PRESENTATION LAYER  ===========================
}