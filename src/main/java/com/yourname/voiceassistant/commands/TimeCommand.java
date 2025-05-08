package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("time") &&
                !command.toLowerCase().contains("timer");
    }

    @Override
    public void handle(String command) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        Main.speak("The current time is " + time.format(formatter));
    }
}