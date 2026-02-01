package echo.api.module;

import echo.impl.modules.hud.ArrayListModule;
import echo.impl.modules.hud.Watermark;
import echo.impl.modules.movement.Flight;
import echo.impl.modules.visual.ClickGui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    // ZMIANA: Używamy echo.api.module.Module, a nie java.lang.Module
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        register(new Flight());
        register(new Watermark());
        register(new ArrayListModule());
        register(new ClickGui());

        modules.sort(Comparator.comparing(Module::getName));
    }

    private void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() { return modules; }

    public Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return modules.stream().filter(m -> m.getClass() == clazz).map(clazz::cast).findFirst().orElse(null);
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isEnabled).collect(Collectors.toList());
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }

    public void onKey(int key) {
        for (Module module : modules) {
            if (module.getKey() == key) {
                module.toggle();
            }
        }
    }
}