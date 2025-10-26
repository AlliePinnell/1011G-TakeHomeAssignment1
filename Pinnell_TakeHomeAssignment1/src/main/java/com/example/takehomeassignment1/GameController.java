package com.example.takehomeassignment1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.*;


public class GameController {

    @FXML
    private TextField guessField;

    @FXML
    private Button submitButton;

    @FXML
    private Button restartButton;

    @FXML
    private Label blanksLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label messageLabel;

    private GameModel model;
    private WordCache cache;
    private Random random = new Random();

    public void initialize() {
        cache = WordCache.load();
        startNewGame();

        submitButton.setOnAction(e -> handleGuess());
        restartButton.setOnAction(e -> startNewGame());
    }


    private void startNewGame() {
        List<String> words = new ArrayList<>(cache.getAllWords());

        if (words.isEmpty()) {
            messageLabel.setText("No words in cache.");
            imageView.setImage(null);
            return;
        }

        String word = words.get(random.nextInt(words.size()));
        WordData data = cache.getWordData(word);
        String imagePath = null;
        if (!data.getImagePaths().isEmpty()) {
            imagePath = data.getImagePaths().get(0);
        }

        model = new GameModel(word, imagePath);
        updateView();

        guessField.setDisable(false);
        submitButton.setDisable(false);
        messageLabel.setText("");
    }

    private void handleGuess() {
        String guess = guessField.getText().trim();
        if (guess.isEmpty()) return;

        if (model.checkGuess(guess)) {
            messageLabel.setText("That is correct.");
            endGame();
        } else {
            model.decrementGuesses();
            if (model.isGameOver()) {
                messageLabel.setText("I'm sorry, but that's incorrect. Correct answer is " + model.getWord());
                endGame();
            } else {
                messageLabel.setText("I'm sorry, but that's incorrect. " + model.getGuessesLeft() + " guesses left.");
            }
        }
        guessField.clear();
    }

    private void updateView() {
        blanksLabel.setText(model.getBlanks());

        String path = model.getImagePath();
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    private void endGame() {
        guessField.setDisable(true);
        submitButton.setDisable(true);
        blanksLabel.setText(model.getWord());
    }
}