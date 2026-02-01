package echo.ui.web;

import echo.Echo;
import echo.ui.web.components.WebClickGuiScreen;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class HotSwapService extends Thread {

    private final Path htmlDir;
    private boolean running = true;

    public HotSwapService() {
        super("Echo-HotSwap-Thread");
        this.htmlDir = HtmlLoader.HTML_DIR.toPath();
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            // Rejestrujemy folder do obserwacji zmian (ENTRY_MODIFY)
            htmlDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("[HotSwap] Watching for changes in: " + htmlDir);

            while (running) {
                WatchKey key;
                try {
                    // Czekamy na zdarzenie (z timeoutem, żeby móc zatrzymać wątek)
                    key = watchService.poll(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    return;
                }

                if (key == null) continue;

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // Ignorujemy zdarzenia systemowe (overflow)
                    if (kind == StandardWatchEventKinds.OVERFLOW) continue;

                    // Pobieramy nazwę zmienionego pliku
                    Path filename = (Path) event.context();
                    String fileNameStr = filename.toString();

                    // Reagujemy tylko na pliki webowe
                    if (fileNameStr.endsWith(".html") || fileNameStr.endsWith(".css") || fileNameStr.endsWith(".js")) {
                        System.out.println("[HotSwap] File changed: " + fileNameStr);
                        triggerReload();
                    }
                }

                // Resetujemy klucz, aby odbierać kolejne zdarzenia
                boolean valid = key.reset();
                if (!valid) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void triggerReload() {
        // Zmiana pliku następuje w osobnym wątku, ale MCEF/Minecraft musi być wywołany w głównym wątku.
        MinecraftClient.getInstance().execute(() -> {
            if (WebClickGuiScreen.getInstance() != null) {
                WebClickGuiScreen.getInstance().reloadPage();
            }
        });
    }

    public void shutdown() {
        this.running = false;
    }
}