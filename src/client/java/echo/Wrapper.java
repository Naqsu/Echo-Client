package echo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class Wrapper {

    public static MinecraftClient getMinecraft() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {
        return getMinecraft().player;
    }

    public static ClientWorld getWorld() {
        return getMinecraft().world;
    }

    public static boolean isIngame() {
        return getPlayer() != null && getWorld() != null;
    }
}