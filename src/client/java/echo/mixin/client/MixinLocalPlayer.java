package echo.mixin.client;

import echo.Echo;
import echo.api.event.impl.MotionEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinLocalPlayer {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        // Prosta implementacja eventu ruchu
        if (Echo.getInstance().getEventBus() != null) {
            Echo.getInstance().getEventBus().post(new MotionEvent(
                    player.getX(), player.getY(), player.getZ(),
                    player.getYaw(), player.getPitch(), player.isOnGround(),
                    MotionEvent.Phase.PRE
            ));
        }
    }
}