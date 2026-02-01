package echo.ui.components.impl;

import echo.api.setting.impl.ModeSetting;
import echo.ui.clickgui.ModuleComponent;
import echo.ui.components.Component;
import echo.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class ModeBox extends Component {

    private final ModeSetting modeSet;

    public ModeBox(ModeSetting setting, ModuleComponent parent) {
        super(setting, parent);
        this.modeSet = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) { // <-- ZMIANA
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        float w = parent.getParent().getWidth();
        float h = getHeight();

        RenderUtil.drawRoundedRect(context, x + 2, y, w - 4, h, 2, 0x60000000);

        String text = modeSet.getName() + ": " + modeSet.getValue();
        RenderUtil.drawText(context, text, x + 6, y + 3, 0xFFCCCCCC, true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = parent.getParent().getX();
        float y = parent.getParent().getY() + offset;
        if (isHovered(mouseX, mouseY, x, y, parent.getParent().getWidth(), getHeight()) && button == 0) {
            modeSet.cycle();
            return true;
        }
        return false;
    }
}