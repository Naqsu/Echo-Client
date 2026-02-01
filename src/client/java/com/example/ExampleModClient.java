package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick());
    }

    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    // H key to open a BasicBrowser screen
    public static final KeyBinding KEY_MAPPING = new KeyBinding(
            "Open Basic Browser", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H, "key.categories.misc"
    );

    public void onTick() {
        // Check if our key was pressed
        if (KEY_MAPPING.wasPressed() && !(minecraft.currentScreen instanceof BasicBrowser)) {
            //Display the web browser UI.
            minecraft.setScreen(new BasicBrowser(
                    Text.literal("Basic Browser")
            ));
        }
    }
}