package com.example.takehomeassignment1;


import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DictionaryApiService {

    public String getTopDefinition(String word) throws java.io.IOException {
        String encoded = URLEncoder.encode(word, StandardCharsets.UTF_8);
        String urlStr = "https://api.dictionaryapi.dev/api/v2/entries/en/" + encoded;

        String json = fetch(urlStr);
        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

        if (arr.size() == 0) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();
        JsonArray meanings = obj.getAsJsonArray("meanings");
        if (meanings == null || meanings.size() == 0) return null;

        JsonObject firstMeaning = meanings.get(0).getAsJsonObject();
        JsonArray definitions = firstMeaning.getAsJsonArray("definitions");
        if (definitions == null || definitions.size() == 0) return null;

        JsonObject firstDef = definitions.get(0).getAsJsonObject();
        if (firstDef.has("definition")) {
            return firstDef.get("definition").getAsString();
        }
        return null;
    }

    private String fetch(String urlStr) throws java.io.IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int code = conn.getResponseCode();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 400 ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
