package com.yourname.voiceassistant.commands;

import com.yourname.voiceassistant.Main;
import com.yourname.voiceassistant.services.WeatherService;
import com.yourname.voiceassistant.services.NLPService;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WeatherCommand implements CommandHandler {
    @Override
    public boolean canHandle(String command) {
        return command.toLowerCase().contains("weather");
    }

    @Override
    public void handle(String command) {
        String city = NLPService.extractCity(command);
        if (city != null) {
            String weatherInfo = WeatherService.getWeather(city);
            Main.speak(weatherInfo);
        } else {
            Main.speak("Please specify a city for weather information.");
        }
    }
}