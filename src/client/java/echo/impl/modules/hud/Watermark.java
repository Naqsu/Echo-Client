package echo.impl.modules.hud;

import echo.Echo;
import echo.api.event.Listener;
import echo.api.event.impl.Render2DEvent;
import echo.api.setting.impl.ColorSetting;
import echo.api.setting.impl.ModeSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Watermark extends HudModule {

    private final ModeSetting colorMode = new ModeSetting("Color Mode", "Solid", "Solid", "Rainbow", "Gradient");
    private final ColorSetting color1 = new ColorSetting("Color 1", new Color(0, 0, 139));
    private final ColorSetting color2 = new ColorSetting("Color 2", new Color(0, 191, 255));

    public Watermark() {
        super("Watermark", "Displays client info.");
        this.posX.setValue(10.0);
        this.posY.setValue(10.0);
        addSettings(colorMode, color1, color2);
    }

    @Listener
    public void onRender(Render2DEvent event) {
        DrawContext context = event.getContext();
        String text = Echo.NAME + " | " + Echo.VERSION + " | " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        // W DrawContext rysujemy tak:
        context.drawText(mc.textRenderer, text, getX(), getY(), -1, true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {}
}