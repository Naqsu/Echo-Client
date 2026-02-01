package echo.ui.web.components;

import echo.ui.web.WebComponent;
import echo.ui.web.WebSerializer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class WebClickGuiScreen extends Screen {

    private static WebClickGuiScreen INSTANCE;
    private final WebComponent webComponent;
    private boolean isDataInjected = false;

    private WebClickGuiScreen() {
        super(Text.literal("Echo WebGUI"));

        // --- POPRAWKA ---
        // Zmieniono "clickgui.html" na "clickgui/clickgui.html",
        // ponieważ taką strukturę stworzył Twój HtmlLoader.
        this.webComponent = new WebComponent("clickgui/clickgui.html", 0, 0, 100, 100);
    }

    public static WebClickGuiScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebClickGuiScreen();
        }
        return INSTANCE;
    }

    @Override
    protected void init() {
        super.init();
        webComponent.resize(width, height);
        isDataInjected = false;
    }

    private void tryInjectData() {
        if (webComponent.getBrowser() != null && !webComponent.isLoading()) {
            String json = WebSerializer.getModulesJson();
            String jsCode = "if(typeof ClientAPI !== 'undefined' && typeof ClientAPI.loadCategories === 'function') { ClientAPI.loadCategories(" + json + "); }";
            webComponent.getBrowser().executeJavaScript(jsCode, webComponent.getBrowser().getURL(), 0);
            isDataInjected = true;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isDataInjected) {
            tryInjectData();
        }
        webComponent.render(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        super.tick();
        webComponent.tick();
    }

    public void reloadPage() {
        if (webComponent != null) {
            System.out.println("[WebGUI] Reloading HTML...");
            webComponent.reload();
            this.isDataInjected = false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        webComponent.sendMousePress(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        webComponent.sendMouseRelease(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        webComponent.sendMouseMove(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        webComponent.sendMouseWheel(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_F5) {
            reloadPage();
            return true;
        }
        webComponent.sendKeyPress(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        webComponent.sendKeyRelease(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        webComponent.sendKeyTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }
}