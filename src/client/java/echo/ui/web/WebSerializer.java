package echo.ui.web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import echo.Echo;
import echo.api.module.Category;
import echo.api.module.Module;
import echo.api.setting.Setting;
import echo.api.setting.impl.*;

public class WebSerializer {

    private static final Gson gson = new Gson();

    public static String getModulesJson() {
        JsonArray categoriesArray = new JsonArray();

        // Pozycje startowe okienek (opcjonalne, frontend może to nadpisywać)
        int x = 20;
        int y = 20;

        for (Category category : Category.values()) {
            JsonObject catObj = new JsonObject();
            catObj.addProperty("name", category.getName());
            catObj.addProperty("id", category.name()); // ID dla frontendu
            catObj.addProperty("x", x);
            catObj.addProperty("y", y);

            JsonArray modulesArray = new JsonArray();
            for (Module module : Echo.getInstance().getModuleManager().getModulesByCategory(category)) {
                JsonObject modObj = new JsonObject();
                modObj.addProperty("name", module.getName());
                modObj.addProperty("description", module.getDescription());
                modObj.addProperty("enabled", module.isEnabled());
                modObj.addProperty("key", module.getKey());

                JsonArray settingsArray = new JsonArray();
                for (Setting<?> setting : module.getSettings()) {
                    if (!setting.isVisible()) continue;

                    JsonObject setObj = new JsonObject();
                    setObj.addProperty("name", setting.getName());

                    if (setting instanceof BooleanSetting bs) {
                        setObj.addProperty("type", "boolean");
                        setObj.addProperty("value", bs.getValue());
                    }
                    else if (setting instanceof NumberSetting ns) {
                        setObj.addProperty("type", "number");
                        setObj.addProperty("value", ns.getValue());
                        setObj.addProperty("min", ns.getMin());
                        setObj.addProperty("max", ns.getMax());
                        setObj.addProperty("step", ns.getStep());
                    }
                    else if (setting instanceof ModeSetting ms) {
                        setObj.addProperty("type", "mode");
                        setObj.addProperty("value", ms.getValue());
                        JsonArray modes = new JsonArray();
                        ms.getModes().forEach(modes::add);
                        setObj.add("modes", modes);
                    }
                    else if (setting instanceof StringSetting ss) {
                        setObj.addProperty("type", "string");
                        setObj.addProperty("value", ss.getValue());
                    }
                    else if (setting instanceof ColorSetting cs) {
                        setObj.addProperty("type", "color");
                        setObj.addProperty("value", cs.getRGB());
                        setObj.addProperty("rainbow", cs.isRainbow());
                    }
                    else if (setting instanceof SectionSetting ss) {
                        setObj.addProperty("type", "section");
                        setObj.addProperty("value", ss.isExpanded());
                    }

                    settingsArray.add(setObj);
                }
                modObj.add("settings", settingsArray);
                modulesArray.add(modObj);
            }

            catObj.add("modules", modulesArray);
            categoriesArray.add(catObj);

            x += 120; // Przesunięcie dla kolejnej kategorii
        }
        return gson.toJson(categoriesArray);
    }
}