package echo.api.event.impl;

import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent {

    private final MatrixStack matrixStack;
    private final float tickDelta;

    public Render3DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}