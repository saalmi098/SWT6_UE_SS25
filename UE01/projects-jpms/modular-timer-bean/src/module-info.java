import swt6.modular.beans.impl.SimpleTimerProvider;
import swt6.modular.beans.TimerProvider;

// per Konvention wuerde man hier den Package-Namen (swt6.modular.beans) nehmen
// wir haben es abweichend definiert zur Demonstration
module swt.modular.beans {

    // Grundlegender Unterschied (Kurztest!):
    // requires: man gibt den Modulnamen an
    // exports: man gibt den Packagenamen an

    exports swt6.modular.beans;

    provides TimerProvider with SimpleTimerProvider; // dadurch haben wir durch den ServiceLoader die Moeglichkeit, nach dem SimpleTimerProvider zu suchen

    opens swt6.modular.beans.impl; // fuer Reflection (Sicherheitsmechanismus aushebeln, der standardmaessig darueber liegt)
}