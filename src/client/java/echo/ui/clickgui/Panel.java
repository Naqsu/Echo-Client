package echo.ui.clickgui;

import echo.Echo;
import echo.api.module.Category;
import echo.api.module.Module;
import echo.ui.components.Draggable;
import echo.util.render.ColorUtil;
import echo.util.render.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class Panel extends Draggable {

    private final Category category;
    private boolean expanded = true;
    private final List<ModuleComponent> modules = new ArrayList<>();

    public Panel(Category category, float x, float y) {
        super(x, y, 110, 20);
        this.category = category;
        float moduleY = height;
        for (Module module : Echo.getInstance().getModuleManager().getModulesByCategory(category)) {
            modules.add(new ModuleComponent(module, this, moduleY));
            moduleY += 16;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.updatePosition(mouseX, mouseY);

        float totalH = height + (expanded ? getComponentsHeight() : 0);
        int glowColor = ColorUtil.getRainbow(0, 0.7f, 1.0f);

        RenderUtil.drawGlow(context, x, y, width, totalH, 6, 15, ColorUtil.withAlpha(glowColor, 80));
        RenderUtil.drawRoundedRect(context, x, y, width, height, 4, 0xFF151515);

        String title = category.getName();
        // font -> textRenderer
        float titleW = MinecraftClient.getInstance().textRenderer.getWidth(title);
        RenderUtil.drawNeonText(context, title, x + (width - titleW) / 2, y + 6, -1, 100);

        if (expanded) {
            RenderUtil.drawRoundedRect(context, x, y + height - 2, width, getComponentsHeight() + 2, 4, 0x95080808);
            float currentY = y + height;
            for (ModuleComponent modComp : modules) {
                modComp.setOffset(currentY - y);
                modComp.render(context, mouseX, mouseY, delta);
                currentY += modComp.getHeight();
            }
        }
    }

    // Reszta metod bez zmian
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.isHovered(mouseX, mouseY)) {
            if (button == 0) return super.startDragging(mouseX, mouseY);
            else if (button == 1) {
                expanded = !expanded;
                return true;
            }
        }
        if (expanded) {
            for (ModuleComponent modComp : modules) {
                if (modComp.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.stopDragging();
        if (expanded) {
            for (ModuleComponent modComp : modules) modComp.mouseReleased(mouseX, mouseY, button);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) {
            for (ModuleComponent modComp : modules) modComp.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public float getComponentsHeight() {
        float h = 0;
        for (ModuleComponent modComp : modules) h += modComp.getHeight();
        return h;
    }
}