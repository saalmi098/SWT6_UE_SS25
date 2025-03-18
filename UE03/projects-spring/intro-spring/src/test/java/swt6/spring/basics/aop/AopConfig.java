package swt6.spring.basics.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import swt6.spring.basics.aop.advice.TraceAspect;
import swt6.spring.basics.aop.logic.WorkLogServiceImpl;

@Configuration
@EnableAspectJAutoProxy // ermoeglicht das Einweben von Annotationen, die mit AspectJ definiert sind
// -- V2: Register beans with component scan
// default (if no basePackageClass is provided): base package for component scan is the package of the config class
//@ComponentScan(basePackageClasses = { TraceAspect.class, WorkLogServiceImpl.class}) // ob WorklogServiceImpl oder WorkLogService, ist egal (beide sind in der gleichen Package)
@ComponentScan // kann man vereinfachen weil TraceAspect und WorklogServiceImpl bereits durch default basePackageClass gefunden werden
public class AopConfig {

    // -- V1: explicit bean registration
//    @Bean
//    public WorkLogService workLogService() {
//        return new WorkLogServiceImpl();
//    }
//
//    @Bean
//    public TraceAspect traceAspect() {
//        return new TraceAspect();
//    }
}
