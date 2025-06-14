package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;
import com.yourname.voiceassistant.services.DatabaseService;
import com.yourname.voiceassistant.services.NLPService;

public class PreferenceCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("remember") ||
                command.toLowerCase().contains("what is my");
    }

    @Override
    public void handle(String command) {
        if (command.toLowerCase().contains("remember")) {
            handleRemember(command);
        } else if (command.toLowerCase().contains("what is my")) {
            handleRecall(command);
        }
    }

    private void handleRemember(String command) {
        String key = extractKey(command, "remember that");
        String value = extractValue(command);

        if (key != null && value != null) {
            DatabaseService.setPreference(key, value);
            Main.speak("I'll remember that your " + key + " is " + value);
        } else {
            Main.speak("I couldn't understand what to remember. Try saying 'remember that my favorite color is blue'");
        }
    }

    private void handleRecall(String command) {
        String key = extractKey(command, "what is my");
        if (key != null) {
            String value = DatabaseService.getPreference(key);
            if (value != null) {
                Main.speak("Your " + key + " is " + value);
            } else {
                Main.speak("I don't know your " + key);
            }
        }
    }

    private String extractKey(String command, String prefix) {
        String lowerCommand = command.toLowerCase();
        int prefixIndex = lowerCommand.indexOf(prefix);
        if (prefixIndex >= 0) {
            String remaining = command.substring(prefixIndex + prefix.length()).trim();
            if (remaining.contains("is")) {
                return remaining.substring(0, remaining.indexOf("is")).trim();
            }
        }
        return null;
    }

    private String extractValue(String command) {
        if (command.contains("is")) {
            return command.substring(command.indexOf("is") + 2).trim();
        }
        return null;
    }
}