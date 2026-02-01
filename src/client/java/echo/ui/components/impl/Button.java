package echo.ui.components.impl;

import echo.util.render.ColorUtil;
import echo.util.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class Button {

    private float x, y, width, height;
    private final String text;
    private final Consumer<Button> action;

    public Button(String text, float x, float y, float width, float height, Consumer<Button> action) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.action = action;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) { // <-- DrawContext
        boolean hovered = isHovered(mouseX, mouseY);

        int color = hovered ? ColorUtil.getRainbow(0, 0.7f, 1.0f) : 0xFF202020;
        int textColor = hovered ? -1 : 0xFFAAAAAA;

        RenderUtil.drawRoundedRect(context, x, y, width, height, 4, color);

        if (hovered) {
            RenderUtil.drawGlow(context, x, y, width, height, 4, 8, ColorUtil.withAlpha(color, 100));
        }

        // W Yarn 1.20.1: textRenderer zamiast font, getWidth zamiast width
        float textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
        float textX = x + (width - textWidth) / 2;
        float textY = y + (height - 8) / 2;

        if (hovered) {
            RenderUtil.drawNeonText(context, text, textX, textY, -1, 100);
        } else {
            RenderUtil.drawText(context, text, textX, textY, textColor, true);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            if (action != null) {
                action.accept(this);
            }
            return true;
        }
        return false;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}