package swt6.spring.basics.aop;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import swt6.spring.basics.aop.logic.EmployeeIdNotFoundException;
import swt6.spring.basics.aop.logic.WorkLogService;

public class AopTest {

    @Test
    public void testAopWithXmlConfig() {

        // TODO haben wir uebersprungen...

//        try (AbstractApplicationContext factory =
//                     new ClassPathXmlApplicationContext("<< insert spring config file here >>")) {
//
//        }
    }

    @Test
    public void testAOPWithJavaConfig() {
        try (AbstractApplicationContext factory = new AnnotationConfigApplicationContext(AopConfig.class)) {
            WorkLogService workLogService = factory.getBean("workLogService", WorkLogService.class); // name muss mit @Service in WorkLogServiceImpl uebereinstimmen

            try {
                workLogService.findAllEmployees();
                for (int i = 1; i <= 4; i++) {
                    workLogService.findEmployeeById((long)i);
                }
            } catch (EmployeeIdNotFoundException e) {
                // ignored
            }
        }
    }
}
