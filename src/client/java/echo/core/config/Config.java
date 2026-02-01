package echo.core.config;

import com.google.gson.JsonObject;
import echo.Echo;
import echo.api.module.Module;
import echo.api.setting.Setting;
import echo.api.setting.impl.*;

import java.io.File;

/**
 * Reprezentuje pojedynczy profil konfiguracyjny.
 * Odpowiada za przygotowanie danych do zapisu i odczytanie ich z JSONa.
 */
public class Config {

    private final String name;
    private final File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(Echo.CLIENT_DIR, "configs/" + name + ".json");
    }

    /**
     * Zwraca nazwę konfigu.
     */
    public String getName() {
        return name;
    }

    /**
     * Zwraca plik fizyczny.
     */
    public File getFile() {
        return file;
    }

    /**
     * Tworzy obiekt JSON reprezentujący aktualny stan całego klienta dla tego configu.
     */
    public JsonObject toJsonObject() {
        JsonObject root = new JsonObject();
        JsonObject modulesObj = new JsonObject();

        for (Module module : Echo.getInstance().getModuleManager().getModules()) {
            JsonObject moduleJson = new JsonObject();

            // Stan podstawowy
            moduleJson.addProperty("enabled", module.isEnabled());
            moduleJson.addProperty("key", module.getKey());

            // Ustawienia
            if (!module.getSettings().isEmpty()) {
                JsonObject settingsJson = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    if (setting instanceof BooleanSetting bs) {
                        settingsJson.addProperty(bs.getName(), bs.getValue());
                    } else if (setting instanceof NumberSetting ns) {
                        settingsJson.addProperty(ns.getName(), ns.getValue());
                    } else if (setting instanceof ModeSetting ms) {
                        settingsJson.addProperty(ms.getName(), ms.getValue());
                    } else if (setting instanceof ColorSetting cs) {
                        settingsJson.addProperty(cs.getName(), cs.getRGB());
                        settingsJson.addProperty(cs.getName() + "_Rainbow", cs.isRainbow());
                    } else if (setting instanceof StringSetting ss) {
                        settingsJson.addProperty(ss.getName(), ss.getValue());
                    }
                }
                moduleJson.add("settings", settingsJson);
            }

            modulesObj.add(module.getName(), moduleJson);
        }

        root.add("modules", modulesObj);
        root.addProperty("clientVersion", Echo.VERSION);
        root.addProperty("timestamp", System.currentTimeMillis());

        return root;
    }
}