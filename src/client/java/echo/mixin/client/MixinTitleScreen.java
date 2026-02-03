package echo.mixin.client;

import echo.ui.web.components.WebMainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Zabezpieczenie przed pętlą (żeby nie otwierał WebMenu w kółko)
        if (!(mc.currentScreen instanceof WebMainMenuScreen)) {
            mc.setScreen(new WebMainMenuScreen());
            ci.cancel(); // Anulujemy inicjalizację domyślnego TitleScreen
        }
    }
}