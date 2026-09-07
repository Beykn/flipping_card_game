package com.example.flipping_card_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
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

            GameGridController gameController = loader.getController();
            String selectedDifficulty = difficultyChoiceBox.getValue();
            String selectedTheme = cardChoiceBox.getValue();

            // 1. Önce sahneyi (Scene) oluşturun ve Stage'e yerleştirin
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.setResizable(true);

            // 2. Sahne ekrana yüklendikten SONRA setupGame metodunu çağırın
            // Böylece cardGrid.getScene().getWindow() ifadesi null dönmez!
            gameController.setupGame(selectedDifficulty, selectedTheme);

            stage.centerOnScreen();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading game grid scene!");
        }
    }
}