package echo;

import net.fabricmc.api.ClientModInitializer;

public class EchoInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ta metoda wykonuje się, gdy Fabric ładuje moda.
        // Główne init() i tak mamy w MixinMinecraft, ale to jest dobre miejsce na logi startowe.
        System.out.println("[Echo] Fabric detected Echo Client!");
    }
}