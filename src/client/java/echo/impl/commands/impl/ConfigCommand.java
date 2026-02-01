package echo.impl.commands.impl;

import echo.Echo;
import echo.impl.commands.Command;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "Manages configs", "c", "cfg");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length < 2) {
            error("Usage: .config <load/save> <name>");
            return;
        }

        String action = args[0].toLowerCase();
        String name = args[1];

        switch (action) {
            case "load":
                Echo.getInstance().getConfigManager().loadConfig(name);
                print("Loaded config: " + name);
                break;
            case "save":
                Echo.getInstance().getConfigManager().saveConfig(name);
                print("Saved config: " + name);
                break;
            default:
                error("Unknown action: " + action);
                break;
        }
    }
}