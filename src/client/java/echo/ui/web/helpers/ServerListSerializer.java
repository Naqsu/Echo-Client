package echo.ui.web.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.network.ServerInfo;

import java.util.Base64; // <-- Ważny import
import java.util.List;

public class ServerListSerializer {

    private static final Gson gson = new Gson();

    public static String toJson(List<ServerInfo> servers) {
        JsonArray array = new JsonArray();

        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);
            JsonObject obj = new JsonObject();

            obj.addProperty("index", i);
            obj.addProperty("name", server.name);
            obj.addProperty("ip", server.address);
            obj.addProperty("ping", server.ping);

            // --- POPRAWKA ---
            // Pobieramy tablicę bajtów
            byte[] iconBytes = server.getFavicon();

            if (iconBytes != null) {
                // Konwertujemy bajty na String Base64
                String base64Encoded = Base64.getEncoder().encodeToString(iconBytes);
                // Dodajemy prefiks wymagany przez HTML
                obj.addProperty("icon", "data:image/png;base64," + base64Encoded);
            } else {
                obj.add("icon", null);
            }
            // ----------------

            array.add(obj);
        }

        return gson.toJson(array);
    }
}