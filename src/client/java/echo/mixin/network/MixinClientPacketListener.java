package echo.mixin.network;

import echo.impl.commands.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPacketListener {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChat(String content, CallbackInfo ci) {
        CommandManager cm = new CommandManager();
        if (cm.handleChat(content)) {
            ci.cancel();
        }
    }
}