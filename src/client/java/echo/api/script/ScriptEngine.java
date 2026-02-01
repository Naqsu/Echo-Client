package echo.api.script;

import java.io.File;

/**
 * Zarządza silnikiem skryptowym (np. GraalJS/Lua).
 * Tutaj odbywa się inicjalizacja kontekstu i ładowanie bindingów.
 */
public class ScriptEngine {

    public void init() {
        // Inicjalizacja silnika JS/Lua
    }

    public void loadScripts(File directory) {
        // Skanowanie folderu i ładowanie plików .js/.lua
    }

    public void reload() {
        // Przeładowanie
    }
}