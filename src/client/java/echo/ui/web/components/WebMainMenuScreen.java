package echo.ui.web.components;

import echo.ui.web.WebComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;

public class WebMainMenuScreen extends Screen {

    private final WebComponent webComponent;
    private String lastUrl = "";

    public WebMainMenuScreen() {
        super(Text.literal("Main Menu"));
        // Ładujemy plik mainmenu.html (bez folderu, bo w HtmlLoader dałem go do głównego katalogu html,
        // ale jeśli w loaderze dałeś "menus/mainmenu.html", to tu też zmień).
        this.webComponent = new WebComponent("mainmenu.html", 0, 0, 100, 100);
    }

    @Override
    protected void init() {
        super.init();
        // Rozciągnij przeglądarkę na cały ekran
        webComponent.resize(width, height);

        // Opcjonalnie: Wstrzyknij nazwę gracza do HTML
        injectUserData();
    }

    private void injectUserData() {
        if (client.player != null || client.getSession() != null) {
            String username = client.getSession().getUsername();
            // Prosty JS do podmiany tekstu w elemencie <b id="username">
            String js = "document.getElementById('username').innerText = '" + username + "';";
            // Wykonujemy z lekkim opóźnieniem w tick(), bo browser może się jeszcze ładować
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Renderujemy tło (czarne, żeby nie było widać dirtu podczas ładowania)
        context.fill(0, 0, width, height, 0xFF000000);

        webComponent.render(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        super.tick();
        webComponent.tick();

        // Sprawdzamy, czy URL się zmienił (tak komunikuje się JS z Javą)
        if (webComponent.getBrowser() != null) {
            String currentUrl = webComponent.getBrowser().getURL();
            if (currentUrl != null && !currentUrl.equals(lastUrl)) {
                lastUrl = currentUrl;
                if (currentUrl.contains("#echo:")) {
                    handleAction(currentUrl.split("#echo:")[1]);
                }
            }
        }
    }

    private void handleAction(String actionParams) {
        // Format z HTML: action:timestamp
        String action = actionParams.split(":")[0];

        switch (action) {
            case "singleplayer":
                this.client.setScreen(new SelectWorldScreen(this));
                break;
            case "multiplayer":
                this.client.setScreen(new WebMultiplayerScreen(this));
                break;
            case "options":
                this.client.setScreen(new OptionsScreen(this, this.client.options));
                break;
            case "quit":
                this.client.scheduleStop();
                break;
            case "cosmetics":
                System.out.println("Otwieranie menu kosmetyków (TODO)");
                break;
        }
    }

    // --- Przekazywanie inputu do przeglądarki ---

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        webComponent.sendMousePress(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        webComponent.sendMouseRelease(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        webComponent.sendMouseMove(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        webComponent.sendKeyPress(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        webComponent.sendKeyRelease(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        webComponent.sendKeyTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }
}