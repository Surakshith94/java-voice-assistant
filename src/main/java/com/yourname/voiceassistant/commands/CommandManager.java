package com.yourname.voiceassistant;

import com.yourname.voiceassistant.commands.CommandHandler;
import com.yourname.voiceassistant.commands.GreetingCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<CommandHandler> commandHandlers;

    public CommandManager() {
        commandHandlers = new ArrayList<>();
        // Register command handlers
        commandHandlers.add(new GreetingCommand());
    }

    public void processCommand(String command) {
        for (CommandHandler handler : commandHandlers) {
            if (handler.canHandle(command)) {
                handler.handle(command);
                return;
            }
        }
        Main.speak("I didn't understand that command.");
    }
}