package echo.impl.modules.hud;

import echo.api.module.Category;
import echo.api.module.Module;
import echo.api.setting.impl.NumberSetting;
import net.minecraft.client.gui.DrawContext;

public abstract class HudModule extends Module {

    protected final NumberSetting posX = new NumberSetting("X", 2.0, 0.0, 2000.0, 1.0);
    protected final NumberSetting posY = new NumberSetting("Y", 2.0, 0.0, 2000.0, 1.0);

    public HudModule(String name, String description) {
        super(name, description, Category.HUD);
        addSettings(posX, posY);
        setEnabled(true);
    }

    public abstract void render(DrawContext context, float tickDelta); // <-- ZMIANA

    public int getX() { return posX.getValue().intValue(); }
    public int getY() { return posY.getValue().intValue(); }
}