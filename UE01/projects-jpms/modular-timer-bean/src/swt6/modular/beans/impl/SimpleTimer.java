package swt6.modular.beans.impl;

import swt6.modular.beans.Timer;
import swt6.modular.beans.TimerEvent;
import swt6.modular.beans.TimerListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleTimer implements Timer {
    // Hinweise Kurztest:
    // Namenskonventionen anschauen (z.B. Frage: ist dies konform mit JavaBeans Namenskonventionen?)
    // 3 Bestandteile von Events (EventObject, Event-Interface, Methoden zum Registrieren und Deregistrieren von Listenern)

    private int interval = 1000;
    private int numTicks = 1;
    private boolean stopTimer = false;
    private Thread tickerThread = null;
    private List<TimerListener> listeners = new ArrayList<>();

    public SimpleTimer() {}

    public SimpleTimer(int interval, int numTicks) {
        this.interval = interval;
        this.numTicks = numTicks;
    }

    @Override
    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("Timer is already running");
        }

        int interval = getInterval();
        int numTicks = getNumTicks();

        tickerThread = new Thread(() -> {
            int tickCount = 0;
            while (!stopTimer && tickCount < numTicks) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!stopTimer) {
                    tickCount++;
                    fireEvent(new TimerEvent(SimpleTimer.this, tickCount, numTicks));
                }
            }

            stopTimer = false;
            tickerThread = null;
        });

        tickerThread.start();
    }

    private void fireEvent(TimerEvent e) {
        listeners.forEach(l -> l.expired(e));
    }

    @Override
    public void stop() {
        stopTimer = true;
    }

    @Override
    public boolean isRunning() {
        return tickerThread != null && tickerThread.isAlive();
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getNumTicks() {
        return numTicks;
    }

    public void setNumTicks(int numTicks) {
        this.numTicks = numTicks;
    }

    // add/remove-Namen leiten sich aus dem Event-Interface ab
    @Override
    public void addTimerListener(TimerListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTimerListener(TimerListener l) {
        listeners.remove(l);
    }
}
