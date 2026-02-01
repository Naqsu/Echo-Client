package echo.impl.commands;

import echo.Wrapper;
import net.minecraft.text.Text;

public abstract class Command {

    private final String name;
    private final String description;
    private final String[] aliases;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public abstract void onCommand(String[] args);

    protected void print(String message) {
        if (Wrapper.getPlayer() != null) {
            Wrapper.getPlayer().sendMessage(Text.literal("§7[§9Echo§7] " + message));
        }
    }

    protected void error(String message) {
        print("§cError: " + message);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String[] getAliases() { return aliases; }
}