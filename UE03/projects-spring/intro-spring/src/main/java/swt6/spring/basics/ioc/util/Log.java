package swt6.spring.basics.ioc.util;

import jakarta.inject.Qualifier;
import java.lang.annotation.*;

// Annotation fuer Qualifier
@Qualifier

// CONSTRUCTOR fuer Konstruktor-Injection (bzw. PARAMETER fuer Konstruktor Parameter), METHOD fuer Setter-Injection
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})

// Annotation bleibt zur Laufzeit erhalten (wird nicht vom Compiler entfernt)
@Retention(RetentionPolicy.RUNTIME)

// z.B. fuer JavaDoc (Annotation wird in der JavaDoc-Dokumentation angezeigt)
@Documented
public @interface Log {
    Type value() default Type.STANDARD;

    enum Type {
        STANDARD,
        FILE
    }
}
