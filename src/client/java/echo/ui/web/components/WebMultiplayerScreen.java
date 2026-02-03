package echo.ui.web.components;

import echo.ui.web.WebComponent;
import echo.ui.web.helpers.ServerListSerializer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WebMultiplayerScreen extends Screen {

    private final Screen parent;
    private final WebComponent webComponent;
    private ServerList serverList;
    private String lastUrl = "";
    private boolean dataInjected = false;

    // Konstruktor
    public WebMultiplayerScreen(Screen parent) {
        super(Text.literal("Web Multiplayer"));
        this.parent = parent;
        this.webComponent = new WebComponent("multiplayer.html", 0, 0, 100, 100);
    }

    @Override
    protected void init() {
        super.init();
        webComponent.resize(width, height);

        // Ładujemy listę serwerów
        this.serverList = new ServerList(this.client);
        this.serverList.loadFile();

        this.dataInjected = false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Renderujemy tylko WebComponent - tło jest w CSS (rgba)
        webComponent.render(context);

        if (!dataInjected && webComponent.getBrowser() != null && !webComponent.isLoading()) {
            updateHtmlList();
            dataInjected = true;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void updateHtmlList() {
        List<ServerInfo> servers = new ArrayList<>();
        for (int i = 0; i < serverList.size(); i++) {
            servers.add(serverList.get(i));
        }
        String json = ServerListSerializer.toJson(servers);
        webComponent.getBrowser().executeJavaScript("loadServers(" + json + ")", webComponent.getBrowser().getURL(), 0);
    }

    @Override
    public void tick() {
        super.tick();
        webComponent.tick();

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
        String[] parts = actionParams.split(":");
        String action = parts[0];

        try {
            switch (action) {
                case "back":
                    client.setScreen(parent);
                    break;

                case "connect":
                    if (parts.length > 1) {
                        int index = Integer.parseInt(parts[1]);
                        if (index >= 0 && index < serverList.size()) {
                            connectToServer(serverList.get(index));
                        }
                    }
                    break;

                case "delete":
                    if (parts.length > 1) {
                        int index = Integer.parseInt(parts[1]);
                        if (index >= 0 && index < serverList.size()) {
                            serverList.remove(serverList.get(index));
                            serverList.saveFile();
                            updateHtmlList();
                        }
                    }
                    break;

                case "add_server":
                    // Format: add_server:NameEncoded|IPEncoded
                    if (parts.length > 1) {
                        String data = parts[1]; // Name|IP
                        String[] dataParts = data.split("\\|");
                        if (dataParts.length == 2) {
                            String name = URLDecoder.decode(dataParts[0], StandardCharsets.UTF_8);
                            String ip = URLDecoder.decode(dataParts[1], StandardCharsets.UTF_8);

                            ServerInfo newServer = new ServerInfo(name, ip, false);
                            this.serverList.add(newServer, false);
                            this.serverList.saveFile();
                            updateHtmlList();
                        }
                    }
                    break;

                case "direct_connect":
                    // Format: direct_connect:IPEncoded
                    if (parts.length > 1) {
                        String ip = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                        ServerInfo directInfo = new ServerInfo("Direct Connect", ip, false);
                        connectToServer(directInfo);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToServer(ServerInfo info) {
        ServerAddress address = ServerAddress.parse(info.address);
        ConnectScreen.connect(this, this.client, address, info, false);
    }

    // --- Input Forwarding ---
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) { webComponent.sendMousePress(mouseX, mouseY, button); return super.mouseClicked(mouseX, mouseY, button); }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) { webComponent.sendMouseRelease(mouseX, mouseY, button); return super.mouseReleased(mouseX, mouseY, button); }
    @Override public void mouseMoved(double mouseX, double mouseY) { webComponent.sendMouseMove(mouseX, mouseY); super.mouseMoved(mouseX, mouseY); }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) { webComponent.sendMouseWheel(mouseX, mouseY, amount); return super.mouseScrolled(mouseX, mouseY, amount); }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) { webComponent.sendKeyPress(keyCode, scanCode, modifiers); return super.keyPressed(keyCode, scanCode, modifiers); }
    @Override public boolean keyReleased(int keyCode, int scanCode, int modifiers) { webComponent.sendKeyRelease(keyCode, scanCode, modifiers); return super.keyReleased(keyCode, scanCode, modifiers); }
    @Override public boolean charTyped(char chr, int modifiers) { webComponent.sendKeyTyped(chr, modifiers); return super.charTyped(chr, modifiers); }
}