package echo.ui.clickgui;

import echo.api.module.Module;
import echo.api.setting.Setting;
import echo.api.setting.impl.*;
import echo.ui.components.Component;
import echo.ui.components.impl.*;
import echo.util.render.ColorUtil;
import echo.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent {

    private final Module module;
    private final Panel parent;
    private float offset;
    private boolean expanded;
    private final List<Component> settingsComponents = new ArrayList<>();

    public ModuleComponent(Module module, Panel parent, float offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;

        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting bs) settingsComponents.add(new Checkbox(bs, this));
            else if (setting instanceof NumberSetting ns) settingsComponents.add(new Slider(ns, this));
            else if (setting instanceof ModeSetting ms) settingsComponents.add(new ModeBox(ms, this));
        }
    }

    public void setOffset(float offset) { this.offset = offset; }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float x = parent.getX();
        float y = parent.getY() + offset;
        float w = parent.getWidth();
        float h = 16;
        boolean hovered = isHovered(mouseX, mouseY, x, y, w, h);

        int color = module.isEnabled() ? ColorUtil.getRainbow(0, 0.7f, 1.0f) : 0x00000000;
        int textColor = module.isEnabled() ? -1 : 0xFFAAAAAA;

        if (module.isEnabled()) {
            RenderUtil.drawRoundedRect(context, x + 2, y, w - 4, h, 3, ColorUtil.withAlpha(color, 150));
            if (hovered) {
                RenderUtil.drawGlow(context, x + 2, y, w - 4, h, 3, 5, ColorUtil.withAlpha(color, 100));
            }
        } else if (hovered) {
            RenderUtil.drawRoundedRect(context, x + 2, y, w - 4, h, 3, 0x40FFFFFF);
        }

        RenderUtil.drawText(context, module.getName(), x + 6, y + 4, textColor, true);

        if (!settingsComponents.isEmpty()) {
            String arrow = expanded ? "-" : "+";
            RenderUtil.drawText(context, arrow, x + w - 12, y + 4, 0xFF888888, true);
        }

        if (expanded) {
            float setY = y + h;
            for (Component comp : settingsComponents) {
                if (comp.isVisible()) {
                    comp.setOffset(setY - parent.getY());
                    comp.render(context, mouseX, mouseY, delta);
                    setY += comp.getHeight();
                }
            }
        }
    }

    // Pozostałe metody bez zmian (kopiuj z oryginału)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = parent.getX();
        float y = parent.getY() + offset;
        if (isHovered(mouseX, mouseY, x, y, parent.getWidth(), 16)) {
            if (button == 0) { module.toggle(); return true; }
            else if (button == 1) { expanded = !expanded; return true; }
        }
        if (expanded) {
            for (Component comp : settingsComponents) if (comp.isVisible() && comp.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (expanded) for (Component comp : settingsComponents) if (comp.isVisible()) comp.mouseReleased(mouseX, mouseY, button);
    }
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) for (Component comp : settingsComponents) if (comp.isVisible()) comp.keyPressed(keyCode, scanCode, modifiers);
    }
    public float getHeight() {
        float h = 16;
        if (expanded) for (Component comp : settingsComponents) if (comp.isVisible()) h += comp.getHeight();
        return h;
    }
    public Panel getParent() { return parent; }
    private boolean isHovered(double mouseX, double mouseY, float x, float y, float w, float h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}