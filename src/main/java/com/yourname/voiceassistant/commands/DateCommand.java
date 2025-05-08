package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("date") ||
                command.toLowerCase().contains("today's date");
    }

    @Override
    public void handle(String command) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        Main.speak("Today's date is " + date.format(formatter));
    }
}