package com.example.flipping_card_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {


    @FXML private Label statusLabel;
    @FXML private ChoiceBox<String> difficultyChoiceBox;
    @FXML private ChoiceBox<String> cardChoiceBox;

    @FXML
    public void initialize(){
        difficultyChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyChoiceBox.setValue("Medium");

        cardChoiceBox.getItems().addAll("Animal","Pokemon","Custom");
        cardChoiceBox.setValue("Animal");
    }

    @FXML
    protected void onStartGameClick(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game_grid.fxml"));
            Parent root = loader.load();

            // Yeni Controller'a veri aktarımı
            GameGridController gameController = loader.getController();
            String selectedDifficulty = difficultyChoiceBox.getValue();
            String selectedTheme = cardChoiceBox.getValue();
            gameController.setupGame( selectedDifficulty,selectedTheme);

            // Sayfa değiştirme
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();

            // PENCEREYİ İÇERİĞE GÖRE OTOMATİK BÜYÜTÜR:
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading game grid scene!");
        }
    }
}