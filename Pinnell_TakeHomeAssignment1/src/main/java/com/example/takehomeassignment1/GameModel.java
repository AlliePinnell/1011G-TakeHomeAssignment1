package com.example.takehomeassignment1;

public class GameModel {
    private String word;
    private String imagePath;
    private int guessesLeft;

    public GameModel(String word, String imagePath) {
        reset(word, imagePath);
    }


    public String getBlanks() {
        return "_ ".repeat(word.length()).trim();
    }

    public boolean isGameOver() {
        return guessesLeft <= 0;
    }

    public void reset(String newWord, String newImagePath) {
        this.word = newWord;
        this.imagePath = newImagePath;
        this.guessesLeft = 3;
    }


    public boolean checkGuess(String guess) {
        return word.equalsIgnoreCase(guess.trim());
    }

    public void decrementGuesses() {
        guessesLeft--;
    }

    public int getGuessesLeft() {
        return guessesLeft;
    }

    public String getWord() {
        return word;
    }

    public String getImagePath() {
        return imagePath;
    }


}