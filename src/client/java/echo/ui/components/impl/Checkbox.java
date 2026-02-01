package echo.ui.components.impl;

import echo.api.setting.impl.BooleanSetting;
import echo.ui.clickgui.ModuleComponent;
import echo.ui.components.Component;
import echo.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class Checkbox extends Component {

    private final BooleanSetting boolSet;

    public Checkbox(BooleanSetting setting, ModuleComponent parent) {
        super(setting, parent);
        this.boolSet = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) { // <-- ZMIANA
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        float w = parent.getParent().getWidth();
        float h = getHeight();

        // Tło paska ustawień
        RenderUtil.drawRoundedRect(context, x + 2, y, w - 4, h, 2, 0x60000000);

        RenderUtil.drawText(context, boolSet.getName(), x + 6, y + 3, 0xFFCCCCCC, true);

        // Kwadracik checkboxa
        float boxSize = 8;
        float boxX = x + w - 14;
        float boxY = y + 3;

        int color = boolSet.getValue() ? 0xFF00FF00 : 0xFF555555;

        RenderUtil.drawRoundedRect(context, boxX, boxY, boxSize, boxSize, 2, color);
        if (boolSet.getValue()) {
            RenderUtil.drawGlow(context, boxX, boxY, boxSize, boxSize, 2, 4, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        if (isHovered(mouseX, mouseY, x, y, parent.getParent().getWidth(), getHeight()) && button == 0) {
            boolSet.toggle();
            return true;
        }
        return false;
    }
}