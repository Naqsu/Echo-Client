package echo.ui.components.impl;

import echo.api.setting.impl.NumberSetting;
import echo.ui.clickgui.ModuleComponent;
import echo.ui.components.Component;
import echo.util.math.MathUtil;
import echo.util.render.ColorUtil;
import echo.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class Slider extends Component {

    private final NumberSetting numSet;
    private boolean dragging;

    public Slider(NumberSetting setting, ModuleComponent parent) {
        super(setting, parent);
        this.numSet = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) { // <-- ZMIANA
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        float w = parent.getParent().getWidth();
        float h = getHeight();

        RenderUtil.drawRoundedRect(context, x + 2, y, w - 4, h, 2, 0x60000000);

        // Obsługa przeciągania
        if (dragging) {
            double diff = Math.min(w, Math.max(0, mouseX - x));
            double min = numSet.getMin();
            double max = numSet.getMax();
            double val = (diff / w) * (max - min) + min;
            numSet.setValue(MathUtil.round(val, 2));
        }

        // Pasek postępu
        float sliderW = (float) ((numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()) * (w - 4));
        int color = ColorUtil.getRainbow(0, 0.7f, 1.0f);

        RenderUtil.drawRoundedRect(context, x + 2, y + h - 3, sliderW, 2, 1, color);
        RenderUtil.drawGlow(context, x + 2, y + h - 3, sliderW, 2, 1, 4, color);

        String display = numSet.getName() + " " + numSet.getValue();
        RenderUtil.drawText(context, display, x + 6, y + 2, 0xFFCCCCCC, true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        if (isHovered(mouseX, mouseY, x, y, parent.getParent().getWidth(), getHeight()) && button == 0) {
            dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }
}