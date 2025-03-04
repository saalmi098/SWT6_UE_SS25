package swt6.nonmodular.clients;

import swt6.nonmodular.beans.SimpleTimer;

public class TimerClient {
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SimpleTimer timer = new SimpleTimer(100, 10);
        timer.addTimerListener(te -> {
            System.out.printf("timer expired: %d/%d%n", te.getTickCount(), te.getNumTicks());
        });

        timer.start();
        sleep(500);
        timer.stop();
        sleep(500);
        timer.start();
    }
}
