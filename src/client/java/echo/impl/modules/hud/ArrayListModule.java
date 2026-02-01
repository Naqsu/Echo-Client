package echo.impl.modules.hud;

import echo.Echo;
import echo.api.event.Listener;
import echo.api.event.impl.Render2DEvent;
import echo.api.module.Module;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListModule extends HudModule {

    public ArrayListModule() {
        super("ArrayList", "Displays enabled modules");
        posX.setValue(2.0);
        posY.setValue(4.0);
    }

    @Listener
    public void onRender(Render2DEvent event) {
        DrawContext context = event.getContext();

        List<Module> modules = Echo.getInstance().getModuleManager().getEnabledModules()
                .stream()
                .filter(m -> !(m instanceof HudModule))
                .sorted(Comparator.comparingInt(m -> -mc.textRenderer.getWidth(((Module)m).getName())))
                .collect(Collectors.toList());

        int yOffset = getY();
        int screenWidth = mc.getWindow().getScaledWidth();

        for (Module module : modules) {
            String name = module.getName();
            int width = mc.textRenderer.getWidth(name);
            int xPos = screenWidth - width - getX();

            context.fill(xPos - 2, yOffset - 1, screenWidth, yOffset + mc.textRenderer.fontHeight + 1, 0x80000000);
            context.drawText(mc.textRenderer, name, xPos, yOffset, -1, true);

            yOffset += mc.textRenderer.fontHeight + 2;
        }
    }

    @Override
    public void render(DrawContext context, float tickDelta) {}
}