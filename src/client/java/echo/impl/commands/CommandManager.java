package echo.impl.commands;

import echo.impl.commands.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();
    private final String prefix = ".";

    public CommandManager() {
        register(new BindCommand());
        register(new ConfigCommand());
        register(new HelpCommand());
    }

    private void register(Command command) {
        commands.add(command);
    }

    /**
     * Główna metoda przetwarzająca wiadomość.
     * Wywoływana np. z MixinLocalPlayer (metoda chat).
     */
    public boolean handleChat(String message) {
        if (!message.startsWith(prefix)) return false;

        String content = message.substring(prefix.length());
        String[] args = content.split(" ");
        String commandName = args[0];

        Command command = getCommand(commandName);
        if (command != null) {
            // Usuń nazwę komendy z argumentów
            String[] cleanArgs = Arrays.copyOfRange(args, 1, args.length);
            try {
                command.onCommand(cleanArgs);
            } catch (Exception e) {
                command.error("Failed to execute command: " + e.getMessage());
                e.printStackTrace();
            }
            return true; // Anuluj wysłanie wiadomości do serwera
        }

        return false;
    }

    public Command getCommand(String name) {
        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)) return cmd;
            for (String alias : cmd.getAliases()) {
                if (alias.equalsIgnoreCase(name)) return cmd;
            }
        }
        return null;
    }

    public List<Command> getCommands() {
        return commands;
    }
}