package echo.impl.modules.visual;

import echo.api.module.Category;
import echo.api.module.Module;
import echo.ui.web.components.WebClickGuiScreen;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {

    public ClickGui() {
        super("ClickGUI", "Displays the HTML interface.", Category.VISUAL);
        setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        if (mc.currentScreen == null) {
            // ZMIANA: Używamy WebClickGuiScreen.getInstance()
            mc.setScreen(WebClickGuiScreen.getInstance());
        }
        setEnabled(false);
    }
}