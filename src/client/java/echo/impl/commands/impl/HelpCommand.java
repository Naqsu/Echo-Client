package echo.impl.commands.impl;

import echo.impl.commands.Command;

// Uwaga: Tutaj musisz pobrać instancję CommandManagera w lepszy sposób
// W tej chwili w Echo.java nie ma getCommandManager(), więc dodam to jako założenie,
// że dodasz getter w Echo.java
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Displays this list", "h", "?");
    }

    @Override
    public void onCommand(String[] args) {
        print("§lAvailable Commands:");
        // W prawdziwym kodzie: Echo.getInstance().getCommandManager().getCommands()...
        // Tymczasowo statyczna lista dla przykładu:
        print(" - bind <module> <key>");
        print(" - config <save/load> <name>");
        print(" - help");
    }
}