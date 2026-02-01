package echo.api.event;

/**
 * Określa priorytet wykonywania listenerów.
 * Im wyższy priorytet, tym szybciej metoda zostanie wywołana.
 */
public enum EventPriority {
    LOWEST(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    MONITOR(4); // Najwyższy priorytet (wywoływany pierwszy)

    private final int value;

    EventPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}