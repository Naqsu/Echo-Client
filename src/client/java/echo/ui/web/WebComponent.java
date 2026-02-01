package echo.ui.web;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import java.lang.reflect.Method;

public class WebComponent {

    private MCEFBrowser browser;
    private final String url;
    private int x, y, width, height;

    // Cache ostatniego URL
    private String lastUrl = "";

    public WebComponent(String fileName, int x, int y, int width, int height) {
        this.url = HtmlLoader.getUrl(fileName);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        initBrowser();
    }

    private void initBrowser() {
        if (browser == null) {
            // Tworzymy przeglądarkę
            browser = MCEF.createBrowser(url, true);

            // --- FIX NA 240 FPS ---
            // Twoja biblioteka nie ma metody setWindowlessFrameRate wprost.
            // Próbujemy użyć Refleksji, aby znaleźć ją w klasie nadrzędnej (jeśli istnieje).
            // Jeśli nie, nic się nie stanie (nie będzie crasha).
            if (browser != null) {
                try {
                    Method method = browser.getClass().getMethod("setWindowlessFrameRate", int.class);
                    method.setAccessible(true);
                    method.invoke(browser, 240);
                    System.out.println("[Echo] Succesfully forced 240 FPS via Reflection!");
                } catch (Exception e) {
                    // Metoda nie istnieje wcale - to normalne w tej wersji biblioteki.
                    // FPSy musimy wymusić argumentami JVM (patrz instrukcja niżej).
                    System.out.println("[Echo] Native FPS method not found. Use JVM arguments.");
                }
            }

            resize(width, height);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        if (browser != null) {
            double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
            browser.resize((int) (width * scale), (int) (height * scale));
        }
    }

    public void render(DrawContext context) {
        if (browser == null) return;

        // Renderowanie tekstury z MCEFRenderer
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);

        // Pobieramy ID tekstury z renderera MCEF
        int textureId = browser.getRenderer().getTextureID();
        if (textureId <= 0) return; // Zabezpieczenie przed błędem OpenGL

        RenderSystem.setShaderTexture(0, textureId);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tessellator t = Tessellator.getInstance();
        BufferBuilder buffer = t.getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(x, y + height, 0).texture(0.0f, 1.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x + width, y + height, 0).texture(1.0f, 1.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x + width, y, 0).texture(1.0f, 0.0f).color(255, 255, 255, 255).next();
        buffer.vertex(x, y, 0).texture(0.0f, 0.0f).color(255, 255, 255, 255).next();

        t.draw();

        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
    }

    public void tick() {
        if (browser != null) {
            checkUrlEvents();
        }
    }

    private void checkUrlEvents() {
        String currentUrl = browser.getURL();
        if (currentUrl != null && !currentUrl.equals(lastUrl)) {
            lastUrl = currentUrl;
            if (currentUrl.contains("#echo:")) {
                String[] parts = currentUrl.split("#echo:");
                if (parts.length > 1) {
                    handleCommand(parts[1]);
                }
            }
        }
    }

    private void handleCommand(String command) {
        try {
            String[] args = command.split(":");
            if (args.length < 2) return;
            String action = args[0];

            if (action.equals("toggle")) {
                String moduleName = args[1];
                echo.api.module.Module m = echo.Echo.getInstance().getModuleManager().getModule(moduleName);
                if (m != null) m.toggle();
            }
            // ... reszta logiki set ...
            else if (action.equals("set")) {
                if (args.length < 4) return;
                // (Skopiuj logikę parsowania z poprzedniej odpowiedzi, jest poprawna)
                String moduleName = args[1];
                String settingName = java.net.URLDecoder.decode(args[2], "UTF-8");
                String valueStr = java.net.URLDecoder.decode(args[3], "UTF-8");

                echo.api.module.Module mod = echo.Echo.getInstance().getModuleManager().getModule(moduleName);
                if (mod != null) {
                    echo.api.setting.Setting<?> s = mod.getSettings().stream()
                            .filter(set -> set.getName().equals(settingName))
                            .findFirst().orElse(null);

                    if (s instanceof echo.api.setting.impl.BooleanSetting bs) bs.setValue(Boolean.parseBoolean(valueStr));
                    else if (s instanceof echo.api.setting.impl.NumberSetting ns) ns.setValue(Double.parseDouble(valueStr));
                    else if (s instanceof echo.api.setting.impl.ModeSetting ms) ms.setValue(valueStr);
                    // itd...
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Input Forwarding ---
    public void sendMousePress(double mouseX, double mouseY, int button) {
        if (browser != null) browser.sendMousePress(scale(mouseX), scale(mouseY), button);
    }
    public void sendMouseRelease(double mouseX, double mouseY, int button) {
        if (browser != null) browser.sendMouseRelease(scale(mouseX), scale(mouseY), button);
    }
    public void sendMouseMove(double mouseX, double mouseY) {
        if (browser != null) browser.sendMouseMove(scale(mouseX), scale(mouseY));
    }
    public void sendMouseWheel(double mouseX, double mouseY, double delta) {
        if (browser != null) browser.sendMouseWheel(scale(mouseX), scale(mouseY), delta * 20, 0);
    }
    public void sendKeyPress(int keyCode, int scanCode, int modifiers) {
        if (browser != null) browser.sendKeyPress(keyCode, scanCode, modifiers);
    }
    public void sendKeyRelease(int keyCode, int scanCode, int modifiers) {
        if (browser != null) browser.sendKeyRelease(keyCode, scanCode, modifiers);
    }
    public void sendKeyTyped(char codePoint, int modifiers) {
        if (browser != null) browser.sendKeyTyped(codePoint, modifiers);
    }
    private int scale(double value) {
        return (int) (value * MinecraftClient.getInstance().getWindow().getScaleFactor());
    }
    public MCEFBrowser getBrowser() { return browser; }
    public void reload() { if (browser != null) browser.reload(); }
    public boolean isLoading() { return browser != null && browser.isLoading(); }
    public void cleanup() { if (browser != null) { browser.close(); browser = null; } }
}