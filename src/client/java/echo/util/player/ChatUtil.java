package echo.util.player;

import echo.Wrapper;
import net.minecraft.text.Text;

public class ChatUtil {

    private static final String PREFIX = "§7[§9Echo§7] §r";

    public static void print(String message) {
        if (Wrapper.getPlayer() != null) {
            Wrapper.getPlayer().sendMessage(Text.literal(PREFIX + message));
        }
    }

    public static void printRaw(String message) {
        if (Wrapper.getPlayer() != null) {
            Wrapper.getPlayer().sendMessage(Text.literal(message));
        }
    }
}