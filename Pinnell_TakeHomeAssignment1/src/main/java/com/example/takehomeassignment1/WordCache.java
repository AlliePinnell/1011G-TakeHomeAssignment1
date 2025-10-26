package com.example.takehomeassignment1;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class WordCache {

    private static final String CACHE_FILE = "word_cache.json";
    private Map<String, WordData> map = new LinkedHashMap<>();


    public static WordCache load() {
        WordCache wc = new WordCache();
        try {
            if (!Files.exists(Paths.get(CACHE_FILE))) return wc;
            try (Reader r = new FileReader(CACHE_FILE)) {
                Gson g = new Gson();
                Type type = new TypeToken<Map<String, WordData>>(){}.getType();
                wc.map = g.fromJson(r, type);
                if (wc.map == null) wc.map = new LinkedHashMap<>();
            }
        } catch (Exception e) {
            wc.map = new LinkedHashMap<>();
        }
        return wc;
    }


    public void save() throws IOException {
        try (Writer w = new FileWriter(CACHE_FILE)) {
            Gson g = new Gson();
            g.toJson(map, w);
        }
    }


    public WordData getWordData(String word) {
        return map.get(word);
    }


    public void put(String word, WordData data) {
        map.put(word, data);
    }


    public void saveQuiet() { try { save(); } catch (IOException ignored) {} }


    public Set<String> getAllWords() { return map.keySet(); }


    public void saveIfNeeded() { saveQuiet(); }
}