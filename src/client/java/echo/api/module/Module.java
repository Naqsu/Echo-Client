package echo.api.module;

import echo.Echo;
import echo.Wrapper;
import echo.api.setting.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    protected final MinecraftClient mc = Wrapper.getMinecraft(); // Tu była zmiana

    private final String name;
    private final String description;
    private final Category category;

    private int key;
    private boolean enabled;

    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = 0;
        this.enabled = false;
    }

    public void onEnable() {
        if (Echo.getInstance().getEventBus() != null)
            Echo.getInstance().getEventBus().register(this);
    }

    public void onDisable() {
        if (Echo.getInstance().getEventBus() != null)
            Echo.getInstance().getEventBus().unregister(this);
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    protected void addSetting(Setting<?> setting) {
        this.settings.add(setting);
    }

    protected void addSettings(Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public List<Setting<?>> getSettings() { return settings; }

    // Gettery
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public boolean isEnabled() { return enabled; }
}