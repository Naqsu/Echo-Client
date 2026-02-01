package echo.api.event.impl;

import net.minecraft.network.packet.Packet;

public class PacketEvent {

    private Packet<?> packet;
    private final Type type;
    private boolean cancelled;

    public PacketEvent(Packet<?> packet, Type type) {
        this.packet = packet;
        this.type = type;
        this.cancelled = false;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Type getType() {
        return type;
    }

    public boolean isIncoming() {
        return type == Type.INCOMING;
    }

    public boolean isOutgoing() {
        return type == Type.OUTGOING;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public enum Type {
        INCOMING,
        OUTGOING
    }
}