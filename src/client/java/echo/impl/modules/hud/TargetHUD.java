package echo.impl.modules.hud;

import echo.api.event.Listener;
import echo.api.event.impl.Render2DEvent;
import echo.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class TargetHUD extends HudModule {

    public TargetHUD() {
        super("TargetHUD", "Displays info about your target.");
        this.posX.setValue(300.0);
        this.posY.setValue(300.0);
    }

    @Listener
    public void onRender(Render2DEvent event) {
        // Placeholder celu
        LivingEntity target = null;

        if (target instanceof PlayerEntity) {
            renderTarget(event.getContext(), (PlayerEntity) target);
        }
    }

    private void renderTarget(DrawContext context, PlayerEntity target) {
        float x = getX();
        float y = getY();
        float width = 120;
        float height = 40;

        RenderUtil.drawRoundedRect(context, x, y, width, height, 5, 0x90000000);
        context.drawText(mc.textRenderer, target.getName().getString(), (int)(x + 5), (int)(y + 5), -1, true);
        RenderUtil.drawRoundedRect(context, x + 5, y + 25, width - 10, 10, 2, 0xFF00FF00);
    }

    @Override
    public void render(DrawContext context, float partialTicks) {}
}