package echo.api.setting.impl;

import echo.api.setting.Setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {

    private final List<String> modes;
    private int index;

    public ModeSetting(String name, String current, String... modes) {
        super(name, current);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(current);
    }

    public void cycle() {
        if (index < modes.size() - 1) {
            index++;
        } else {
            index = 0;
        }
        setValue(modes.get(index));
    }

    public List<String> getModes() {
        return modes;
    }

    @Override
    public void setValue(String value) {
        if (modes.contains(value)) {
            super.setValue(value);
            this.index = modes.indexOf(value);
        }
    }

    public boolean is(String mode) {
        return getValue().equalsIgnoreCase(mode);
    }
}