package com.example.takehomeassignment1;

import java.util.List;

public class WordData {
    private String word;
    private String definition;
    private List<String> imagePaths;


    public WordData() {}


    public WordData(String word, String definition, List<String> imagePaths) {
        this.word = word;
        this.definition = definition;
        this.imagePaths = imagePaths;
    }


    public String getWord() { return word; }
    public String getDefinition() { return definition; }
    public List<String> getImagePaths() { return imagePaths; }


    public void setWord(String word) { this.word = word; }
    public void setDefinition(String definition) { this.definition = definition; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
}