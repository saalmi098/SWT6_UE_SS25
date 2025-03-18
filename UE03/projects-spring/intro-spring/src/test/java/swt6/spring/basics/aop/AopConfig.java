package swt6.spring.basics.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import swt6.spring.basics.aop.advice.TraceAspect;
import swt6.spring.basics.aop.logic.WorkLogService;
import swt6.spring.basics.aop.logic.WorkLogServiceImpl;

@Configuration
@EnableAspectJAutoProxy // ermoeglicht das Einweben von Annotationen, die mit AspectJ definiert sind
public class AopConfig {

    @Bean
    public WorkLogService workLogService() {
        return new WorkLogServiceImpl();
    }

    @Bean
    public TraceAspect traceAspect() {
        return new TraceAspect();
    }
}
