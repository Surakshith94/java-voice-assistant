package com.yourname.voiceassistant.commands;

public interface CommandHandler {
    boolean canHandle(String command);
    void handle(String command);
}