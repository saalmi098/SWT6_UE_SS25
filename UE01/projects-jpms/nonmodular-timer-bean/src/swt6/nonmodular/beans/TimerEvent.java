package swt6.nonmodular.beans;

import java.util.EventObject;

// EventObject beerben ist eher ein Marker-Interface (nicht zwingend erforderlich, aber sinnvoll) - gilt auch fuer EventListener
// Events sollten nach Moeglichkeit immutable implementiert werden
public class TimerEvent extends EventObject {
    private int numTicks = 0;
    private int tickCount = 0;

    public TimerEvent(Object source, int tickCount, int numTicks) {
        super(source);
        this.tickCount = tickCount;
        this.numTicks = numTicks;
    }

    public int getTickCount() {
        return tickCount;
    }

    public int getNumTicks() {
        return numTicks;
    }
}
