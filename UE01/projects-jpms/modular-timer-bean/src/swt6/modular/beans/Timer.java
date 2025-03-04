package swt6.modular.beans;

public interface Timer {
    void start();

    void stop();

    boolean isRunning();

    // add/remove-Namen leiten sich aus dem Event-Interface ab
    void addTimerListener(TimerListener l);

    void removeTimerListener(TimerListener l);
}
