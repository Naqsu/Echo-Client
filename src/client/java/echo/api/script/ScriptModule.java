package echo.api.script;

import echo.api.module.Category;
import echo.api.module.Module;

/**
 * Wrapper, który sprawia, że skrypt zachowuje się jak zwykły moduł Javy.
 */
public class ScriptModule extends Module {

    public ScriptModule(String name, String description) {
        super(name, description, Category.SCRIPTS);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Wywołanie funkcji onEnable() w skrypcie
    }

    @Override
    public void onDisable() {
        super.onDisable();
        // Wywołanie funkcji onDisable() w skrypcie
    }
}