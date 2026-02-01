package echo.api.setting.impl;

import echo.api.setting.Setting;

import java.util.function.Supplier;

/**
 * Specjalny typ ustawienia, który nie zmienia logiki modułu,
 * a jedynie służy do organizowania GUI (zwijana kategoria).
 *
 * Wartość (Boolean) określa, czy sekcja jest ROZWINIĘTA (true) czy ZWINIĘTA (false).
 */
public class SectionSetting extends Setting<Boolean> {

    public SectionSetting(String name, boolean expanded) {
        super(name, expanded);
    }

    public SectionSetting(String name, boolean expanded, Supplier<Boolean> visibility) {
        super(name, expanded, visibility);
    }

    public boolean isExpanded() {
        return getValue();
    }

    public void setExpanded(boolean expanded) {
        setValue(expanded);
    }

    /**
     * Przełącza stan sekcji (zwiń/rozwiń).
     */
    public void toggle() {
        setValue(!getValue());
    }
}