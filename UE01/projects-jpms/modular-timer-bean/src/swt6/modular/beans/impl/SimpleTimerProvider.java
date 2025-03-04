package swt6.modular.beans.impl;

import swt6.modular.beans.TimerProvider;

public class SimpleTimerProvider implements TimerProvider {
    @Override
    public double timerResolution() {
        return 1/1000.0;
    }

    @Override
    public SimpleTimer createTimer(int interval, int numTicks) {
        return new SimpleTimer(interval, numTicks);
    }
}
