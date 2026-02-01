package echo.mixin.client;

import echo.Echo;
import echo.api.event.impl.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {

    @Inject(method = "run", at = @At("HEAD"))
    private void onRun(CallbackInfo ci) {
        Echo.getInstance().init();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        Echo.getInstance().shutdown();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (Echo.getInstance().getEventBus() != null) {
            Echo.getInstance().getEventBus().post(new TickEvent(TickEvent.Phase.START));
        }
    }
}