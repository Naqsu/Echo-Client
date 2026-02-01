package echo.core.config;

import com.google.gson.*;
import echo.Echo;
import echo.api.module.Module;
import echo.api.setting.Setting;
import echo.api.setting.impl.BooleanSetting;
import echo.api.setting.impl.ModeSetting;
import echo.api.setting.impl.NumberSetting;

import java.io.*;

/**
 * System zarządzania konfiguracją JSON.
 * Zapisuje stan modułów i ich ustawienia.
 */
public class ConfigManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configDir;

    public ConfigManager() {
        this.configDir = new File(Echo.CLIENT_DIR, "configs");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public void saveConfig(String name) {
        File file = new File(configDir, name + ".json");
        JsonObject root = new JsonObject();
        JsonObject modulesObj = new JsonObject();

        for (Module module : Echo.getInstance().getModuleManager().getModules()) {
            JsonObject moduleJson = new JsonObject();

            // Podstawowe dane modułu
            moduleJson.addProperty("enabled", module.isEnabled());
            moduleJson.addProperty("key", module.getKey());

            // Ustawienia (Settings)
            if (!module.getSettings().isEmpty()) {
                JsonObject settingsJson = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    if (setting instanceof BooleanSetting bs) {
                        settingsJson.addProperty(bs.getName(), bs.getValue());
                    } else if (setting instanceof NumberSetting ns) {
                        settingsJson.addProperty(ns.getName(), ns.getValue());
                    } else if (setting instanceof ModeSetting ms) {
                        settingsJson.addProperty(ms.getName(), ms.getValue());
                    }
                    // Tutaj można dodać ColorSetting, StringSetting itp.
                }
                moduleJson.add("settings", settingsJson);
            }

            modulesObj.add(module.getName(), moduleJson);
        }

        root.add("modules", modulesObj);

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(String name) {
        File file = new File(configDir, name + ".json");
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (!root.has("modules")) return;
            JsonObject modulesObj = root.getAsJsonObject("modules");

            for (Module module : Echo.getInstance().getModuleManager().getModules()) {
                if (modulesObj.has(module.getName())) {
                    JsonObject moduleJson = modulesObj.getAsJsonObject(module.getName());

                    // Wczytaj stan
                    if (moduleJson.has("enabled")) {
                        module.setEnabled(moduleJson.get("enabled").getAsBoolean());
                    }
                    if (moduleJson.has("key")) {
                        module.setKey(moduleJson.get("key").getAsInt());
                    }

                    // Wczytaj ustawienia
                    if (moduleJson.has("settings")) {
                        JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                        for (Setting<?> setting : module.getSettings()) {
                            if (!settingsJson.has(setting.getName())) continue;

                            JsonElement element = settingsJson.get(setting.getName());

                            try {
                                if (setting instanceof BooleanSetting bs) {
                                    bs.setValue(element.getAsBoolean());
                                } else if (setting instanceof NumberSetting ns) {
                                    ns.setValue(element.getAsDouble());
                                } else if (setting instanceof ModeSetting ms) {
                                    ms.setValue(element.getAsString());
                                }
                            } catch (Exception e) {
                                System.err.println("Error loading setting " + setting.getName() + " for module " + module.getName());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}