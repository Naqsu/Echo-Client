package echo.api.setting;

import java.util.function.Supplier;

/**
 * Generyczna klasa bazowa dla wszystkich ustawień w kliencie.
 * Obsługuje nazwę, wartość oraz dynamiczną widoczność w GUI.
 *
 * @param <T> Typ przechowywanej wartości (np. Boolean, Double, Color, String).
 */
public class Setting<T> {

    private final String name;
    private T value;
    private final T defaultValue;

    // Funkcja określająca, czy ustawienie ma być widoczne w GUI.
    // Np. pokaż "Speed Mode" tylko gdy "Speed" jest włączone.
    private final Supplier<Boolean> visibility;

    public Setting(String name, T value) {
        this(name, value, () -> true);
    }

    public Setting(String name, T value, Supplier<Boolean> visibility) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sprawdza, czy ustawienie powinno być renderowane w GUI.
     */
    public boolean isVisible() {
        return visibility.get();
    }
}