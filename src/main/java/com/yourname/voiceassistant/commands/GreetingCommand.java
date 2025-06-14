package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;

public class GreetingCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("hello") ||
                command.toLowerCase().contains("hi");
    }

    @Override
    public void handle(String command) {
        Main.speak("Hello there! How can I help you?");
    }
}