package echo.ui.clickgui;

import echo.api.module.Category;
import echo.api.module.Module;
import echo.ui.web.components.WebClickGuiScreen;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Module {

    public ClickGuiScreen() {
        super("ClickGUI", "Web based GUI", Category.VISUAL);
        setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        if (mc.currentScreen == null) {
            mc.setScreen(WebClickGuiScreen.getInstance());
        }
        setEnabled(false);
    }
}