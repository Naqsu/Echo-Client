package echo.api.setting.impl;

import echo.api.setting.Setting;

import java.util.function.Supplier;

public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public NumberSetting(String name, double value, double min, double max, double step, Supplier<Boolean> visibility) {
        super(name, value, visibility);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setValue(Double value) {
        // Clamp value
        double val = Math.max(min, Math.min(max, value));
        super.setValue(val);
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }
}