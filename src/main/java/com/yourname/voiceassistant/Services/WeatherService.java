package com.yourname.voiceassistant.services;

import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {
    private static final String API_KEY = System.getenv("02fd6d3f526309cc3832b5fa887fedb5");
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static String getWeather(String city) {
        if (city == null || city.trim().isEmpty()) {
            return "Please specify a city name";
        }

        try {
            String urlString = String.format("%s?q=%s,IN&appid=%s&units=metric",
                    BASE_URL, city, API_KEY);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return handleApiError(responseCode, city);
            }

            String response = readResponse(conn);
            return parseWeatherData(response, city);

        } catch (java.net.SocketTimeoutException e) {
            return "Weather service timeout. Please try again.";
        } catch (IOException e) {
            return "Network error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    private static String handleApiError(int code, String city) {
        switch (code) {
            case 401: return "Error: Invalid API configuration";
            case 404: return "City '" + city + "' not found in India";
            case 429: return "Too many requests. Please wait";
            default: return "Weather service error (Code: " + code + ")";
        }
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            return response.toString();
        }
    }

    private static String parseWeatherData(String response, String city) {
        JSONObject json = new JSONObject(response);
        JSONObject main = json.getJSONObject("main");

        String actualCity = json.getString("name");
        String description = json.getJSONArray("weather")
                .getJSONObject(0)
                .getString("description");

        return String.format(
                "Weather in %s (%s): %s\n" +
                        "• Temperature: %.1f°C (Feels like %.1f°C)\n" +
                        "• Humidity: %d%%\n" +
                        "• Wind: %.1f km/h",
                city,
                actualCity,
                description,
                main.getDouble("temp"),
                main.getDouble("feels_like"),
                main.getInt("humidity"),
                json.getJSONObject("wind").getDouble("speed") * 3.6
        );
    }
}