package com.example;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;

public class BasicBrowser extends Screen {
    private static final int BROWSER_DRAW_OFFSET = 20;
    private static final double GUI_WIDTH_PCT = 0.6;
    private static final double GUI_HEIGHT_PCT = 0.7;

    private int getGuiLeft() { return (int)(width * (1.0 - GUI_WIDTH_PCT) / 2); }
    private int getGuiTop() { return (int)(height * (1.0 - GUI_HEIGHT_PCT) / 2); }
    private int getGuiWidth() { return (int)(width * GUI_WIDTH_PCT); }
    private int getGuiHeight() { return (int)(height * GUI_HEIGHT_PCT); }

    private MCEFBrowser browser;

    private final MinecraftClient minecraft = MinecraftClient.getInstance();

    public BasicBrowser(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        if (browser == null) {
            String url = "http://localhost:8000/clickgui.html";
            boolean transparent = true;
            browser = MCEF.createBrowser(url, transparent);
            resizeBrowser();
        }
    }

    private int mouseX(double x) {
        double scale = minecraft.getWindow().getScaleFactor();
        return (int) ((x - getGuiLeft()) * scale);
    }
    private int mouseY(double y) {
        double scale = minecraft.getWindow().getScaleFactor();
        return (int) ((y - getGuiTop()) * scale);
    }
    private int scaleX(double x) {
        // Zajmij 50% szerokości okna Minecrafta
        return (int) ((x * 0.5) * minecraft.getWindow().getScaleFactor());
    }

    private int scaleY(double y) {
        // Zajmij 50% wysokości okna Minecrafta
        return (int) ((y * 0.5) * minecraft.getWindow().getScaleFactor());
    }
    private void resizeBrowser() {
        if (width > 100 && height > 100) {
            double scale = minecraft.getWindow().getScaleFactor();
            browser.resize((int)(getGuiWidth() * scale), (int)(getGuiHeight() * scale));
        }
    }
    @Override
    public void resize(MinecraftClient minecraft, int i, int j) {
        super.resize(minecraft, i, j);
        resizeBrowser();
    }

    @Override
    public void close() {
        browser.close();
        super.close();
    }

    @Override
    public void render(DrawContext guiGraphics, int i, int j, float f) {
        // Opcjonalnie: przyciemnienie tła pod GUI
        guiGraphics.fill(0, 0, width, height, 0x88000000);

        super.render(guiGraphics, i, j, f);

        int x1 = getGuiLeft();
        int y1 = getGuiTop();
        int x2 = x1 + getGuiWidth();
        int y2 = y1 + getGuiHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, browser.getRenderer().getTextureID());

        Tessellator t = Tessellator.getInstance();
        BufferBuilder buffer = t.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        // Rysowanie quada o wymiarach GUI
        buffer.vertex(x1, y2, 0).texture(0.0f, 1.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x2, y2, 0).texture(1.0f, 1.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x2, y1, 0).texture(1.0f, 0.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x1, y1, 0).texture(0.0f, 0.0f).color(255, 255, 255, 255).next();

        t.draw();
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        browser.sendMousePress(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        browser.sendMouseRelease(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        browser.sendMouseMove(mouseX(mouseX), mouseY(mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        browser.sendMouseWheel(mouseX(mouseX), mouseY(mouseY), delta, 0);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyPress(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyRelease(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (codePoint == (char) 0) return false;
        browser.sendKeyTyped(codePoint, modifiers);
        browser.setFocus(true);
        return super.charTyped(codePoint, modifiers);
    }
}
