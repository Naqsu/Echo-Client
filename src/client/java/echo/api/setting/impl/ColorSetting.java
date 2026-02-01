package echo.api.setting.impl;

import echo.api.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting<Color> {

    private boolean rainbow;

    public ColorSetting(String name, Color value) {
        super(name, value);
        this.rainbow = false;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    // Konwersja do inta dla renderera
    public int getRGB() {
        return getValue().getRGB();
    }
}