package swt6.spring.basics.ioc;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import swt6.spring.basics.ioc.logic.factorybased.WorkLogServiceImpl;
import swt6.spring.basics.ioc.logic.xmlconfig.WorkLogService;

public class IocTest {

    // Hinweis: wir bauen in der Uebung keine "richtigen" Unit-Tests

    @Test
    public void testFactoryApproach() {
        WorkLogServiceImpl workLog = new WorkLogServiceImpl();
        workLog.findAllEmployees();
        workLog.findEmployeeById(3L);
    }

    @Test
    public void testXmlConfig() {
        try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext("swt6/spring/basics/ioc/applicationContext-config.xml")) {
//            WorkLogService workLog = factory.getBean("workLogService-setter-injected", WorkLogService.class); // Logs erscheinen in der Konsole
//            WorkLogService workLog = factory.getBean("workLogService-constructor-injected", WorkLogService.class); // Logs erscheinen in File log.txt
            WorkLogService workLog = factory.getBean("workLogService", WorkLogService.class); // Logs erscheinen in File log.txt
            workLog.findAllEmployees();
            workLog.findEmployeeById(3L);
        }
    }

    @Test
    public void testJavaConfig() {
    }
}
