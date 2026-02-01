package echo.core.auth.alt;

import echo.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

public class MicrosoftAuth {

    public void loginOffline(String username) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());

        // Konstruktor Session: username, uuid, accessToken, xuid, clientId, type
        Session session = new Session(
                username,
                uuid.toString(),
                "",
                Optional.empty(),
                Optional.empty(),
                Session.AccountType.LEGACY
        );

        setSession(session);
    }

    private void setSession(Session session) {
        try {
            MinecraftClient mc = Wrapper.getMinecraft();

            // Szukamy pola typu Session
            Field sessionField = null;
            for (Field f : MinecraftClient.class.getDeclaredFields()) {
                if (f.getType() == Session.class) {
                    sessionField = f;
                    break;
                }
            }

            if (sessionField != null) {
                sessionField.setAccessible(true);
                sessionField.set(mc, session);
                System.out.println("Zalogowano jako: " + session.getUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}