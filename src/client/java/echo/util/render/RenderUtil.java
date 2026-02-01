package echo.util.render;

import echo.Wrapper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class RenderUtil {

    // Rysowanie prostokąta (fill)
    public static void drawRect(DrawContext context, float x, float y, float w, float h, int color) {
        context.fill((int)x, (int)y, (int)(x + w), (int)(y + h), color);
    }

    // Rysowanie zaokrąglonego prostokąta (Symulacja, bo shadery masz wyłączone)
    public static void drawRoundedRect(DrawContext context, float x, float y, float w, float h, float radius, int color) {
        // W 1.20.1 bez shaderów rysujemy zwykły prostokąt jako fallback
        drawRect(context, x, y, w, h, color);
    }

    // Rysowanie tekstu
    public static void drawText(DrawContext context, String text, float x, float y, int color, boolean shadow) {
        TextRenderer tr = Wrapper.getMinecraft().textRenderer;
        context.drawText(tr, text, (int)x, (int)y, color, shadow);
    }

    // Rysowanie tekstu "Neon" (Symulacja - rysuje cień + tekst)
    public static void drawNeonText(DrawContext context, String text, float x, float y, int color, int alpha) {
        TextRenderer tr = Wrapper.getMinecraft().textRenderer;
        // Prosty glow (rysowanie tekstu z offsetem i mniejszą alfą)
        context.drawText(tr, text, (int)x - 1, (int)y, ColorUtil.withAlpha(color, 50), false);
        context.drawText(tr, text, (int)x + 1, (int)y, ColorUtil.withAlpha(color, 50), false);
        context.drawText(tr, text, (int)x, (int)y - 1, ColorUtil.withAlpha(color, 50), false);
        context.drawText(tr, text, (int)x, (int)y + 1, ColorUtil.withAlpha(color, 50), false);

        // Główny tekst
        context.drawText(tr, text, (int)x, (int)y, color, true);
    }

    // Rysowanie Glow (Symulacja prostokątem z małą alfą)
    public static void drawGlow(DrawContext context, float x, float y, float w, float h, int radius, int blur, int color) {
        drawRect(context, x - 2, y - 2, w + 4, h + 4, ColorUtil.withAlpha(color, 50));
    }

    // Gradient
    public static void drawGradientRect(DrawContext context, float x, float y, float w, float h, float startAlpha, float endAlpha, int color1, int color2) {
        context.fillGradient((int)x, (int)y, (int)(x+w), (int)(y+h), color1, color2);
    }
}