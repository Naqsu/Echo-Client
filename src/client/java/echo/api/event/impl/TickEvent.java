package echo.api.event.impl;

/**
 * Zdarzenie wywoływane w każdym ticku gry (zazwyczaj 20 razy na sekundę).
 * Używane do logiki movementu, combatu itp.
 */
public class TickEvent {

    private final Phase phase;

    public TickEvent(Phase phase) {
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }

    public boolean isPre() {
        return phase == Phase.START;
    }

    public boolean isPost() {
        return phase == Phase.END;
    }

    public enum Phase {
        START, // Przed przetworzeniem logiki ticku
        END    // Po przetworzeniu logiki ticku
    }
}