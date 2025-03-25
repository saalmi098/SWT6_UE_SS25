package swt6.spring.worklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBootApplication steht fuer @Configuration, @ComponentScan und @EnableAutoConfiguration
@SpringBootApplication
public class SpringBootMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args); // erstellt Bean-Factory, statet Tomcat etc.
    }
}
