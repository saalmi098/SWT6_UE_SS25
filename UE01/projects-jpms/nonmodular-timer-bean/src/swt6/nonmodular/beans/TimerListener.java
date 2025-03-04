package swt6.nonmodular.beans;

import java.util.EventListener;

@FunctionalInterface // nicht zwingend erforderlich, aber sinnvoll damit wir nur 1 Methode vorgeben koennen (fuer Lambdas, anonyme Klassen, ...)
public interface TimerListener extends EventListener {
    void expired(TimerEvent e);
}
