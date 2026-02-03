package echo.ui.web;

import echo.Echo;
import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class HtmlLoader {

    private static final String RESOURCE_PATH = "/assets/echo/html/";
    public static final File HTML_DIR = new File(Echo.CLIENT_DIR, "html");

    public static void init() {
        if (!HTML_DIR.exists()) {
            HTML_DIR.mkdirs();
        }

        System.out.println("[HtmlLoader] Folder HTML: " + HTML_DIR.getAbsolutePath());

        copyResource("clickgui/clickgui.html");
        copyResource("clickgui/script.js");
        copyResource("clickgui/styles.css");

        copyResource("mainmenu.html");
        copyResource("multiplayer.html");
    }

    private static void copyResource(String fileName) {
        try {
            String fullResourcePath = RESOURCE_PATH + fileName;
            URL resource = HtmlLoader.class.getResource(fullResourcePath);

            if (resource == null) {
                System.err.println("[Echo] BŁĄD: Nie znaleziono w JAR: " + fullResourcePath);
                return;
            }

            File targetFile = new File(HTML_DIR, fileName);
            File parentDir = targetFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (InputStream in = resource.openStream()) {
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // System.out.println("[Echo] Skopiowano: " + fileName); // Zakomentowane żeby nie spamować
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUrl(String fileName) {
        File file = new File(HTML_DIR, fileName);

        // Diagnostyka
        if (!file.exists()) {
            System.err.println("[WebGUI] BŁĄD KRYTYCZNY: Plik HTML nie istnieje na dysku!");
            System.err.println(" -> Szukano: " + file.getAbsolutePath());
        } else {
            System.out.println("[WebGUI] Ładowanie: " + file.toURI().toString());
        }

        // toURI() automatycznie obsługuje "file:///" i slesze na Windowsie
        return file.toURI().toString();
    }
}