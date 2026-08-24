package com.example.flipping_card_game;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.time.Instant;
import java.util.Random;

public class GameGridController {

    @FXML private Label statusLabel;
    @FXML private GridPane cardGrid;

    private int[][] matrix;
    private Button[][] buttons;
    private int rows, cols;
    private int matchedPairsCount;
    private int totalPairs;
    private int noMatching = 0;

    private Button firstSelectedButton = null;
    private int firstRow = -1, firstCol = -1;

    private Instant startTime;
    private boolean isProcessing = false;

    public void setupGame(int input) {
        this.totalPairs = input;
        cardGrid.getChildren().clear();
        firstSelectedButton = null;
        matchedPairsCount = 0;
        noMatching = 0;
        isProcessing = false;

        int totalCards = input * 2;
        int[] array1 = new int[input];
        int[] array2 = new int[input];
        int[] array3 = new int[totalCards];

        for (int i = 0; i < input; i++) {
            array1[i] = i + 1;
            array2[i] = i + 1;
        }

        Random random = new Random();
        for (int i = array1.length - 1; i > 0; i--) {
            int r1 = random.nextInt(i + 1);
            int temp1 = array1[i];
            array1[i] = array1[r1];
            array1[r1] = temp1;

            int r2 = random.nextInt(i + 1);
            int temp2 = array2[i];
            array2[i] = array2[r2];
            array2[r2] = temp2;
        }

        for (int i = 0; i < input; i++) {
            array3[i] = array1[i];
            array3[input + i] = array2[i];
        }

        rows = 1;
        cols = totalCards;
        for (int i = (int) Math.sqrt(totalCards); i >= 1; i--) {
            if (totalCards % i == 0) {
                rows = i;
                cols = totalCards / i;
                break;
            }
        }

        matrix = new int[rows][cols];
        buttons = new Button[rows][cols];
        int arrayIndex = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = array3[arrayIndex++];

                Button btn = new Button(" ");
                btn.setPrefSize(90, 90);
                btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                final int rPos = r;
                final int cPos = c;
                btn.setOnAction(e -> handleCardClick(rPos, cPos, btn));

                buttons[r][c] = btn;
                cardGrid.add(btn, c, r);
            }
        }

        startTime = Instant.now();
        statusLabel.setText("Game started! Select a card.");
    }

    private void handleCardClick(int r, int c, Button clickedButton) {
        if (isProcessing || !clickedButton.getText().equals(" ") || clickedButton.isDisabled()) {
            return;
        }

        clickedButton.setText(String.valueOf(matrix[r][c]));

        if (firstSelectedButton == null) {
            firstSelectedButton = clickedButton;
            firstRow = r;
            firstCol = c;
        } else {
            if (firstRow == r && firstCol == c) return;

            if (matrix[firstRow][firstCol] == matrix[r][c]) {
                statusLabel.setText("Match found!");
                firstSelectedButton.setDisable(true);
                clickedButton.setDisable(true);
                firstSelectedButton = null;
                matchedPairsCount++;

                if (matchedPairsCount == totalPairs) {
                    Instant endTime = Instant.now();
                    long seconds = java.time.Duration.between(startTime, endTime).toSeconds();
                    statusLabel.setText("Completed in " + seconds + " seconds!");
                }
            } else {
                statusLabel.setText("Not a match!");
                isProcessing = true;
                noMatching++;

                statusLabel.setText("You choose " + noMatching + " times wrong card !");
                Button b1 = firstSelectedButton;
                Button b2 = clickedButton;

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    b1.setText(" ");
                    b2.setText(" ");
                    firstSelectedButton = null;
                    isProcessing = false;
                    statusLabel.setText("Select a card.");
                });
                pause.play();
            }
        }
    }
}