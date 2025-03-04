import swt6.modular.beans.TimerProvider;

// per Konvention wuerde man hier den Package-Namen (swt6.modular.clients) nehmen
// wir haben es abweichend definiert zur Demonstration
module swt.modular.clients {

    // Grundlegender Unterschied (Kurztest!):
    // requires: man gibt den Modulnamen an
    // exports: man gibt den Packagenamen an

    requires swt.modular.beans;

    uses TimerProvider;
}