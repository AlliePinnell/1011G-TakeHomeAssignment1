package com.example.takehomeassignment1;


import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ImageService {

    private static final String IMAGE_DIR = "images";
    private static final int MAX_IMAGES = 5;
    public ImageService() {
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public List<String> getImages(String keyword) throws IOException {
        List<String> cached = getCachedImages(keyword, MAX_IMAGES);
        if (!cached.isEmpty()) return cached;

        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiUrl = "https://boringapi.com/api/v1/photos?search=" + encoded + "&limit=" + MAX_IMAGES;

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        int code = conn.getResponseCode();
        if (code != 200) {
            System.err.println("Image fetch failed: " + code);
            return cached; // return empty
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            JsonArray photos = json.getAsJsonArray("photos");

            List<String> resultPaths = new ArrayList<>();
            for (int i = 0; i < Math.min(MAX_IMAGES, photos.size()); i++) {
                JsonObject photo = photos.get(i).getAsJsonObject();
                String imgUrl = photo.get("url").getAsString();

                Path imgPath = Path.of(IMAGE_DIR, keyword + "_" + (i + 1) + ".jpg");
                downloadImage(imgUrl, imgPath);
                resultPaths.add(imgPath.toString());
            }

            return resultPaths;
        }
    }

    private List<String> getCachedImages(String keyword, int count) {
        List<String> files = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Path path = Path.of(IMAGE_DIR, keyword + "_" + i + ".jpg");
            if (Files.exists(path)) files.add(path.toString());
        }
        return files;
    }

    private void downloadImage(String imgUrl, Path dest) {
        try (InputStream in = new URL(imgUrl).openStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to download image: " + e.getMessage());
        }
    }

    public List<String> fetchAndCacheImages(String keyword, int count) throws IOException {
        List<String> cached = getCachedImages(keyword, count);
        if (!cached.isEmpty()) return cached;

        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiUrl = "https://boringapi.com/api/v1/photos?search=" + encoded + "&limit=" + count;

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        int code = conn.getResponseCode();
        if (code != 200) {
            System.err.println("Image fetch failed with code: " + code);
            return cached; // fallback to cache
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            JsonArray photos = json.getAsJsonArray("photos");

            List<String> resultPaths = new ArrayList<>();
            for (int i = 0; i < Math.min(count, photos.size()); i++) {
                JsonObject photo = photos.get(i).getAsJsonObject();
                String imgUrl = photo.get("url").getAsString();

                Path imgPath = Path.of(IMAGE_DIR, keyword + "_" + (i + 1) + ".jpg");
                downloadImage(imgUrl, imgPath);
                resultPaths.add(imgPath.toString());
            }
            return resultPaths;
        }
    }

}