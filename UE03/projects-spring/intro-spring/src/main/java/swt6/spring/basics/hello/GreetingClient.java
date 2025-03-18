package swt6.spring.basics.hello;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GreetingClient {

    public static void main(String[] args) {

        // Konfiguration kommt aus dem Claasspath aus einer XML-Datei
        try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext("swt6/spring/basics/hello/greetingService.xml")) {

            GreetingService greetingService = factory.getBean("greetingService", GreetingService.class);
            greetingService.sayHello();
        } // factory.close()

    }
}
