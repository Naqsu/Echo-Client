package echo.impl.commands.impl;

import echo.Echo;
import echo.api.module.Module;
import echo.impl.commands.Command;
import net.minecraft.client.util.InputUtil;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Binds a module to a key", "b");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length < 2) {
            error("Usage: .bind <module> <key>");
            return;
        }

        String moduleName = args[0];
        String keyName = args[1].toUpperCase();

        Module module = Echo.getInstance().getModuleManager().getModule(moduleName);

        if (module == null) {
            error("Module '" + moduleName + "' not found.");
            return;
        }

        if (keyName.equalsIgnoreCase("NONE")) {
            module.setKey(0);
            print("Unbound " + module.getName());
            return;
        }

        int keyCode;
        try {
            keyCode = InputUtil.fromTranslationKey("key.keyboard." + keyName.toLowerCase()).getCode();
        } catch (Exception e) {
            try {
                // Fallback do pojedynczych liter
                keyCode = InputUtil.fromTranslationKey("key.keyboard." + keyName.toLowerCase().charAt(0)).getCode();
            } catch (Exception ex) {
                keyCode = -1;
            }
        }

        if (keyCode <= 0) {
            try {
                keyCode = Integer.parseInt(keyName);
            } catch (NumberFormatException e) {
                error("Invalid key: " + keyName);
                return;
            }
        }

        module.setKey(keyCode);
        print("Bound " + module.getName() + " to " + keyName + " (ID: " + keyCode + ")");
    }
}