package echo.core.auth.alt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import echo.Echo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Zarządza listą kont, ich zapisem i odczytem (JSON).
 */
public class AltManager {

    private final List<Alt> alts = new ArrayList<>();
    private final File altsFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AltManager() {
        this.altsFile = new File(Echo.CLIENT_DIR, "alts.json");
        loadAlts();
    }

    public List<Alt> getAlts() {
        return alts;
    }

    public void addAlt(Alt alt) {
        alts.add(alt);
        saveAlts();
    }

    public void removeAlt(Alt alt) {
        alts.remove(alt);
        saveAlts();
    }

    /**
     * Zapisuje listę kont do pliku alts.json.
     */
    public void saveAlts() {
        try (Writer writer = new FileWriter(altsFile)) {
            gson.toJson(alts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje listę kont z pliku.
     */
    public void loadAlts() {
        if (!altsFile.exists()) return;

        try (Reader reader = new FileReader(altsFile)) {
            List<Alt> loaded = gson.fromJson(reader, new TypeToken<List<Alt>>(){}.getType());
            if (loaded != null) {
                alts.clear();
                alts.addAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loguje się na konto w nowym wątku (aby nie zamrozić gry).
     */
    public void login(Alt alt) {
        new Thread(() -> {
            MicrosoftAuth auth = new MicrosoftAuth();

            if (alt.getType() == Alt.AccountType.CRACKED) {
                auth.loginOffline(alt.getEmail());
                alt.setUsername(alt.getEmail());
                alt.setStatus(Alt.Status.WORKING);
            } else {
                // Tu nastąpiłoby pełne logowanie Microsoft
                // Ze względu na skomplikowanie OAuth2, w tej wersji
                // szkieletowej zalecam użycie biblioteki zewnętrznej lub placeholder
                System.out.println("Microsoft login pending implementation...");
                // auth.loginMicrosoft(alt.getEmail(), alt.getPassword());
            }
        }, "Alt Login Thread").start();
    }
}