package swt6.spring.basics.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class TraceAspect {

    // Hinweis Kurztest: Spring Doku anschauen zu Aspect Oriented Programming with Spring --> "Declaring a pointcut"
    // https://docs.spring.io/spring-framework/docs/4.3.15.RELEASE/spring-framework-reference/html/aop.html

    // -- Version 1: define new pointcut expression
    // @Before bekommt eine Pointcut-Expression
    // Der Teil innerhalb er Pointcut-Expression ergibt sich aus den Methoden findAllEmployees und findEmployeeById in WorkLogServiceImpl
    // "*" bedeutet beliebige Anzahl von Zeichen (ausser ".")
    // ".." bedeutet "beliebige Anzahl von Zeichen zwischen zwei Punkten
//    @Before("execution(public * swt6.spring.basics.aop.logic..find*(..))")

    // -- Version 2: reference existing pointcut
    @Before("findMethods()")
    public void traceBefore(JoinPoint jp) { // optionaler Parameter fuer Kontextinformationen (z.B. Zielobjekt, Parameter, ...)
        String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
        System.out.println("--> " + methodName);
    }

//    @After("findMethods()") // @After wird immer ausgefuehrt, egal ob eine Exception geworfen wird oder nicht
    @AfterReturning("findMethods()") // @AfterReturning wird nur ausgefuehrt, wenn Methode erfolgreich beendet wurde
    public void traceAfter(JoinPoint jp) {
        String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
        System.out.println("<-- " + methodName);
    }

    @AfterThrowing(pointcut = "findMethods()", throwing = "ex") // "ex" ist der Name des Methodenparameters fuer die Exception
    public void traceException(JoinPoint jp, Throwable ex) {
        String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
        System.out.printf("##> %s%n    threw exception<%s>%n ", methodName, ex);
    }

    // nur fuer "find*ById" Methoden
    @Around("execution(public * swt6.spring.basics.aop.logic..find*ById*(..))")
    public Object traceAround(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName();

        System.out.println("==> " + methodName);

        Object returnVal = pjp.proceed(); // Aufruf der Methode, um die es geht (Zielmethode)
        System.out.println("<== " + methodName);

        return returnVal;
    }

    // eigene Methode, damit Pointcut Expression nicht mehrfach geschrieben werden muss
    @Pointcut("execution(public * swt6.spring.basics.aop.logic..find*(..))")
    private void findMethods() {}
}
