package echo.mixin.network;

import echo.Echo;
import echo.api.event.impl.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinConnection {

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (Echo.getInstance().getEventBus() != null) {
            PacketEvent event = new PacketEvent(packet, PacketEvent.Type.INCOMING);
            Echo.getInstance().getEventBus().post(event);
            if (event.isCancelled()) ci.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if (Echo.getInstance().getEventBus() != null) {
            PacketEvent event = new PacketEvent(packet, PacketEvent.Type.OUTGOING);
            Echo.getInstance().getEventBus().post(event);
            if (event.isCancelled()) ci.cancel();
        }
    }
}