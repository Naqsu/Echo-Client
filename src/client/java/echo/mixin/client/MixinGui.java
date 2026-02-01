package echo.mixin.client;

import echo.Echo;
import echo.api.event.impl.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinGui {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (Echo.getInstance().getEventBus() != null) {
            Echo.getInstance().getEventBus().post(new Render2DEvent(context, tickDelta));
        }
    }
}