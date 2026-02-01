package echo.util.math;

/**
 * Proste narzędzie do mierzenia czasu (cooldowny).
 */
public class TimerUtil {

    private long lastMS = 0L;

    public boolean hasReached(long milliseconds) {
        return (System.currentTimeMillis() - lastMS) >= milliseconds;
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public long getTimeElapsed() {
        return System.currentTimeMillis() - lastMS;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }
}