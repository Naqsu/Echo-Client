package echo.mixin.client;

import echo.Echo;
import echo.api.event.impl.Render3DEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinLevelRenderer {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
                          Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
                          Matrix4f projectionMatrix, CallbackInfo ci) {

        if (Echo.getInstance().getEventBus() != null) {
            Echo.getInstance().getEventBus().post(new Render3DEvent(matrices, tickDelta));
        }
    }
}