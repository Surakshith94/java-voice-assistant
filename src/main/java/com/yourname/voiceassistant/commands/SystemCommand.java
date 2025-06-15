package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SystemCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.contains("open") || command.contains("close") ||
                command.contains("search") || command.contains("launch");
    }

    @Override
    public void handle(String command) {
        try {
            if (command.contains("chrome")) {
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome"});
                } else {
                    Runtime.getRuntime().exec(new String[]{"google-chrome"});
                }
                Main.speak("Opening Google Chrome");
            }
            // ... other apps
        } catch (IOException e) {
            Main.speak("Sorry, I couldn't open that application.");
        }
    }

    private void handleOpenCommand(String command) throws IOException {
        if (command.contains("chrome")) {
            Runtime.getRuntime().exec(new String[]{"google-chrome"});
            Main.speak("Opening Google Chrome");
        } else if (command.contains("notepad")) {
            Runtime.getRuntime().exec(new String[]{"notepad.exe"});
            Main.speak("Opening Notepad");
        } else if (command.contains("calculator")) {
            Runtime.getRuntime().exec(new String[]{"calc.exe"});
            Main.speak("Opening Calculator");
        }
        // Add more applications as needed
    }

    private void handleCloseCommand(String command) throws IOException {
        if (command.contains("chrome")) {
            Runtime.getRuntime().exec(new String[]{"taskkill", "/IM", "chrome.exe", "/F"});
            Main.speak("Closing Google Chrome");
        } else if (command.contains("notepad")) {
            Runtime.getRuntime().exec(new String[]{"taskkill", "/IM", "notepad.exe", "/F"});
            Main.speak("Closing Notepad");
        }
        // Add more applications as needed
    }

    private void handleSearchCommand(String command) throws IOException, URISyntaxException {
        if (command.contains("search") && Desktop.isDesktopSupported()) {
            String query = command.substring(command.indexOf("search") + 6).trim();
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + encodedQuery));
            Main.speak("Searching Google for " + query);
        }
    }
}