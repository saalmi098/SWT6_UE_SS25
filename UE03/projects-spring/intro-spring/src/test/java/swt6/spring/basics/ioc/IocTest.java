package swt6.spring.basics.ioc;

import org.junit.Test;
import swt6.spring.basics.ioc.logic.factorybased.WorkLogServiceImpl;

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
  }

  @Test
  public void testJavaConfig() {
  }
}
