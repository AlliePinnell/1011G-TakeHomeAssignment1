package com.example.takehomeassignment1;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<String> historyList;
    @FXML
    private Button playGameButton;


    private WordCache cache;
    private DictionaryApiService dictionaryapi;
    private ImageService imageService;


    public void initialize() {
        cache = WordCache.load();
        historyList.getItems().addAll(cache.getAllWords());


        dictionaryapi = new DictionaryApiService();
        imageService = new ImageService();


        searchButton.setOnAction(e -> onSearch());


        historyList.setOnMouseClicked(e -> {
            String selected = historyList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                WordData data = cache.getWordData(selected);
                if (data != null) showResultWindow(data);
            }
        });

        playGameButton.setOnAction(e -> openGameWindow());
    }


    private void onSearch() {
        String word = searchField.getText().trim();
        if (word.isEmpty()) return;


// If cached, show immediately
        WordData cached = cache.getWordData(word);
        if (cached != null) {
            showResultWindow(cached);
            return;
        }


        try {
            String definition = dictionaryapi.getTopDefinition(word);
            if (definition == null) {
                DefinitionAlert.show("No definition found for '" + word + "'");
                return;
            }


// Fetch and cache images (synchronous)
            List<String> imagePaths = imageService.fetchAndCacheImages(word, 5);


            WordData data = new WordData(word, definition, imagePaths);
            cache.put(word, data);
            cache.save();


// update history list view
            historyList.getItems().setAll(cache.getAllWords());


            showResultWindow(data);
        } catch (IOException ex) {
            DefinitionAlert.show("Error: " + ex.getMessage());
        }
    }


    private void showResultWindow(WordData data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result-view.fxml"));
            Parent root = loader.load();
            ResultController ctrl = loader.getController();
            ctrl.setData(data);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Result: " + data.getWord());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openGameWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("JAVPARDY!");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
