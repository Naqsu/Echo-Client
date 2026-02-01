package echo.ui.components;

import echo.api.setting.Setting;
import echo.ui.clickgui.ModuleComponent;
import net.minecraft.client.gui.DrawContext;

public abstract class Component {

    protected final Setting<?> setting;
    protected final ModuleComponent parent;
    protected float offset;

    public Component(Setting<?> setting, ModuleComponent parent) {
        this.setting = setting;
        this.parent = parent;
    }

    // ZMIANA: DrawContext zamiast GuiGraphics
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);

    public void mouseReleased(double mouseX, double mouseY, int button) {}
    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    public void setOffset(float offset) { this.offset = offset; }
    public float getHeight() { return 14; }
    public boolean isVisible() { return setting.isVisible(); }

    protected boolean isHovered(double mouseX, double mouseY, float x, float y, float w, float h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}