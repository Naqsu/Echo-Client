package echo.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

/**
 * Klasa inicjalizująca Mixiny (Entry Point dla systemu Mixin).
 * W zależności od środowiska (Fabric/Forge/LaunchWrapper), może być wywoływana automatycznie.
 */
public class MixinLoader implements IMixinConnector {

    @Override
    public void connect() {
        System.out.println("[Echo] Injecting Mixins...");

        // Dodajemy konfigurację mixinów (plik mixins.echo.json w resources)
        Mixins.addConfiguration("echo.mixins.json");
    }
}