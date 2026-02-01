package echo.ui.framework.render;

import net.minecraft.client.gui.DrawContext;

public class Renderer2D {

    public static void drawRect(DrawContext context, float x, float y, float w, float h, int color) {
        context.fill((int)x, (int)y, (int)(x + w), (int)(y + h), color);
    }

    public static void drawRoundedRect(DrawContext context, float x, float y, float w, float h, float radius, int color) {
        // Placeholder, w RenderUtil masz lepszą implementację
        drawRect(context, x, y, w, h, color);
    }

    public static void drawGradientRect(DrawContext context, float x, float y, float w, float h, int startColor, int endColor) {
        context.fillGradient((int)x, (int)y, (int)(x + w), (int)(y + h), startColor, endColor);
    }
}