package echo.core.config;

import echo.Echo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Zarządza kolekcją konfiguracji (Presetów).
 * Skanuje folder /configs/ i udostępnia listę dostępnych profili.
 */
public class PresetManager {

    private final List<Config> presets = new ArrayList<>();
    private final File configDir;
    private Config activeConfig;

    public PresetManager() {
        this.configDir = new File(Echo.CLIENT_DIR, "configs");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        refresh();
    }

    /**
     * Skanuje katalog w poszukiwaniu plików .json i aktualizuje listę.
     */
    public void refresh() {
        presets.clear();

        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            Arrays.stream(files)
                    .map(file -> file.getName().replace(".json", ""))
                    .map(Config::new)
                    .forEach(presets::add);
        }
    }

    /**
     * Zwraca listę wszystkich dostępnych configów.
     */
    public List<Config> getPresets() {
        return presets;
    }

    /**
     * Ładuje config o podanej nazwie.
     */
    public void loadPreset(String name) {
        // Używamy ConfigManagera do fizycznego odczytu
        Echo.getInstance().getConfigManager().loadConfig(name);

        // Aktualizujemy wskaźnik na aktywny config
        this.activeConfig = getPreset(name);
        if (this.activeConfig == null) {
            // Jeśli config nie istniał w pamięci, ale loadConfig go obsłużył (np. utworzył default), dodaj go
            this.activeConfig = new Config(name);
            presets.add(activeConfig);
        }
    }

    /**
     * Zapisuje obecny stan klienta jako preset.
     */
    public void savePreset(String name) {
        Echo.getInstance().getConfigManager().saveConfig(name);
        refresh(); // Odśwież listę, bo mógł powstać nowy plik
        this.activeConfig = getPreset(name);
    }

    /**
     * Usuwa preset.
     */
    public void deletePreset(String name) {
        Config config = getPreset(name);
        if (config != null) {
            if (config.getFile().exists()) {
                config.getFile().delete();
            }
            presets.remove(config);
        }
    }

    public Config getPreset(String name) {
        return presets.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Config getActiveConfig() {
        return activeConfig;
    }
}