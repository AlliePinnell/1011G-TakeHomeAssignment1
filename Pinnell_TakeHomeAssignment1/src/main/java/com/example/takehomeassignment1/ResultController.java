package com.example.takehomeassignment1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import java.io.File;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResultController {

    @FXML
    private Label wordLabel;
    @FXML private Label definitionLabel;
    @FXML private FlowPane imagesPane;


    public void setData(WordData data) {
        wordLabel.setText(data.getWord());
        definitionLabel.setText(data.getDefinition());


        imagesPane.getChildren().clear();
        List<String> paths = data.getImagePaths();
        for (String p : paths) {
            File f = new File(p);
            if (f.exists()) {
                ImageView iv = new ImageView(new Image(f.toURI().toString()));
                iv.setFitWidth(160);
                iv.setFitHeight(120);
                iv.setPreserveRatio(true);
                imagesPane.getChildren().add(iv);
            }
        }
    }
}
