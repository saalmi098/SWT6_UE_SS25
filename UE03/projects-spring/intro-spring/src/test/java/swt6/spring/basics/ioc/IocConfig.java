package swt6.spring.basics.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import swt6.spring.basics.ioc.logic.javaconfig.WorkLogServiceImpl;
import swt6.spring.basics.ioc.logic.xmlconfig.WorkLogService;
import swt6.spring.basics.ioc.util.Logger;

@Configuration
@ComponentScan(basePackageClasses = {Logger.class, WorkLogServiceImpl.class}) // durchsucht werden alle Klassen, die in demselben Package wie Logger liegen (+ Subpackages (?))
public class IocConfig { // = Pendant zu XML-File

//    // Registriert ein Bean unter dem Namen "consoleLogger" (Methodenname)
//    @Bean
//    @Log
//    public Logger consoleLogger() {
//        return new ConsoleLogger();
//    }
//
//    @Bean
//    @Log(Log.Type.FILE)
//    public Logger fileLogger() {
//        return new FileLogger("log.txt");
//    }
//
//    @Bean
//    public WorkLogService workLog(@Log Logger logger) {
//        return new WorkLogServiceImpl(consoleLogger());
//    }

    @Bean
    public WorkLogService workLog() {
        return new WorkLogServiceImpl();
    }
}
