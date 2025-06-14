package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;
import com.yourname.voiceassistant.services.DatabaseService;
import com.yourname.voiceassistant.services.NLPService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReminderCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("remind me") ||
                command.toLowerCase().contains("set a reminder");
    }

    @Override
    public void handle(String command) {
        if (command.toLowerCase().contains("do i have any reminders") ||
                command.toLowerCase().contains("any reminders")) {
            checkReminders();
        } else {
            setReminder(command);
        }
    }

    private void setReminder(String command) {
        String description = extractDescription(command);
        String time = extractTime(command);

        if (description != null && time != null) {
            if (DatabaseService.addReminder(description, time)) {
                Main.speak("I've set a reminder for " + description + " at " + formatTimeForSpeech(time));
            } else {
                Main.speak("Sorry, I couldn't set that reminder.");
            }
        } else {
            Main.speak("Please specify both what to remind you about and when.");
        }
    }

    private void checkReminders() {
        List<String> reminders = DatabaseService.getDueReminders();
        if (reminders.isEmpty()) {
            Main.speak("You don't have any reminders due now.");
        } else {
            Main.speak("You have " + reminders.size() + " reminders:");
            for (String reminder : reminders) {
                Main.speak(reminder);
            }
        }
    }

    private String extractDescription(String command) {
        // Simple extraction - would be enhanced with NLP in a real application
        if (command.contains("to")) {
            return command.substring(command.indexOf("to") + 2, command.indexOf("at")).trim();
        }
        return null;
    }

    private String extractTime(String command) {
        // Simple time extraction - would be enhanced in a real application
        try {
            if (command.contains("at")) {
                String timePart = command.substring(command.indexOf("at") + 2).trim();
                // Try to parse as proper time format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime.parse("2023-" + timePart, formatter);
                return "2023-" + timePart;
            }
        } catch (DateTimeParseException e) {
            return null;
        }
        return null;
    }

    private String formatTimeForSpeech(String time) {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a on MMMM d");
            LocalDateTime dateTime = LocalDateTime.parse(time, parser);
            return dateTime.format(formatter);
        } catch (Exception e) {
            return time;
        }
    }
}