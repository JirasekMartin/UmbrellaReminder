package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UmbrellaReminder {

    private static final String API_KEY = "c6b0acc331f573f51609527d8d15bf4f"; // Vložte svůj OpenWeatherMap API klíč
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static void main(String[] args) {
        String location = "Jihlava"; // Zadejte své město nebo lokalitu

        try {
            JsonObject weatherData = getWeatherData(location);
            if (weatherData != null) {
                double rainVolume = getRainVolume(weatherData);
                if (rainVolume > 0) {
                    sendUmbrellaReminderEmail(); // Pošlete upozornění na deštník
                    System.out.println("Vem si s sebou deštník!");
                } else {
                    System.out.println("Deštník nebude potřeba.");
                }
            } else {
                System.out.println("Nelze získat data o počasí pro zadanou lokalitu.");
            }
        } catch (IOException e) {
            System.out.println("Chyba při získávání dat o počasí: " + e.getMessage());
        }
    }

    private static JsonObject getWeatherData(String location) throws IOException {
        String apiUrl = String.format(API_URL, location, API_KEY);
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), JsonObject.class);
    }

    private static double getRainVolume(JsonObject weatherData) {
        if (!weatherData.has("rain")) {
            return 0;
        }

        JsonObject rain = weatherData.getAsJsonObject("rain");
        if (rain.has("1h")) {
            return rain.get("1h").getAsDouble();
        } else if (rain.has("3h")) {
            return rain.get("3h").getAsDouble();
        }

        return 0;
    }

    private static void sendUmbrellaReminderEmail() {
        // Implementujte kód pro odeslání e-mailu s upozorněním na deštník
        // Zde by mohl být váš kód pro odeslání e-mailu, můžete použít knihovny jako JavaMail API nebo Apache Commons Email.
    }
}