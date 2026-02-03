package echo.mixin.client;

import echo.ui.web.components.WebMultiplayerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget; // Ważny import
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {

    // Shadowujemy pole, które jest nullem i powoduje crasha
    @Shadow protected MultiplayerServerListWidget serverListWidget;

    @Shadow @Final private Screen parent;

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Zabezpieczenie przed pętlą: Sprawdzamy, czy już nie próbujemy otworzyć naszego ekranu
        if (mc.currentScreen instanceof WebMultiplayerScreen) {
            return;
        }

        // Ustawiamy nasz ekran
        // Używamy opóźnienia (schedule), aby uniknąć problemów z cyklem życia ekranu w tym samym ticku
        mc.execute(() -> mc.setScreen(new WebMultiplayerScreen(this.parent)));

        ci.cancel(); // Anulujemy tworzenie vanilla GUI
    }

    // --- TO JEST FIX NA CRASHA ---
    // Metoda removed() wywoływana jest przy zamykaniu ekranu.
    // Ponieważ anulowaliśmy init(), widgety są nullem. Musimy zablokować wykonanie removed().
    @Inject(method = "removed", at = @At("HEAD"), cancellable = true)
    private void onRemoved(CallbackInfo ci) {
        if (this.serverListWidget == null) {
            // Jeśli widget nie istnieje (bo anulowaliśmy init), nie pozwalamy wykonać reszty metody
            ci.cancel();
        }
    }
}