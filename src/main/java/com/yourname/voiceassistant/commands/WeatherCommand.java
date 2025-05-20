package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;
import com.yourname.voiceassistant.services.WeatherService;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WeatherCommand implements CommandHandler {
    private static final Map<String, String> CITY_ALIASES = Map.of(
            "bombay", "mumbai",
            "calcutta", "kolkata",
            "bengaluru", "bangalore",
            "madras", "chennai"
    );

    @Override
    public boolean canHandle(String command) {
        return command.matches(".*\\b(weather|temperature|rain|forecast|humid|hot|cold|wind)\\b.*");
    }

    @Override
    public void handle(String command) {
        String city = extractCity(command.toLowerCase());

        if (city == null) {
            Main.speak("Please specify an Indian city. Example: 'What's the weather in Mumbai?'");
            return;
        }

        // Normalize city name
        city = CITY_ALIASES.getOrDefault(city, city);
        String weather = WeatherService.getWeather(city);

        Main.speak(weather);
    }

    private String extractCity(String command) {
        // Match city names with word boundaries
        Pattern pattern = Pattern.compile(
                "\\b(?:weather|temperature|rain|forecast).+?\\b" +
                        "(mumbai|delhi|bangalore|hyderabad|chennai|kolkata|pune|ahmedabad|jaipur|lucknow|bhopal|patna|indore|coimbatore|kochi|bombay|calcutta|bengaluru|madras)\\b"
        );

        Matcher matcher = pattern.matcher(command);
        return matcher.find() ? matcher.group(1) : null;
    }
}