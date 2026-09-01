package com.example.flipping_card_game;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;


import java.net.URL;
import javafx.scene.media.AudioClip;

import java.time.Instant;
import java.util.Random;

public class GameGridController {

    @FXML private Label statusLabel;
    @FXML private Label timerLabel;
    @FXML private Label wrongAttemptsLabel;
    @FXML private GridPane cardGrid;

    private int[][] matrix;
    private Button[][] buttons;
    private int rows, cols;
    private int matchedPairsCount;
    private int totalPairs;
    private int noMatching = 0;
    private  double cardFlipDelay = 1.0;

    private Button firstSelectedButton = null;
    private int firstRow = -1, firstCol = -1;

    private Instant startTime;
    private Timeline timer;                // zamanlayıcı
    private long secondsElapsed = 0;
    private boolean isProcessing = false;
    private String cardPath;


    public void setupGame( String difficulty, String card) {

        // Gelen temaya göre klasör yolunu ayarlıyoruz
        if ("Pokemon".equalsIgnoreCase(card)) {
            this.cardPath = "/com/example/flipping_card_game/img/pokemon/";
        } else {
            // Varsayılan: Animal
            this.cardPath = "/com/example/flipping_card_game/img/animal/";
        }

        int input = 0;
        cardGrid.getChildren().clear();
        firstSelectedButton = null;
        matchedPairsCount = 0;
        noMatching = 0;
        isProcessing = false;
        secondsElapsed = 0;


        updateScoreboard();
        startTimer();

        int totalCards = 0;

        if(difficulty.equalsIgnoreCase("Easy")){
            input = 4;
        }
        else if(difficulty.equalsIgnoreCase("Easy")){
            input = 12;
        }else{
            input = 24;
        }

        totalCards = input * 2;
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

        gameChallenge(totalCards ,difficulty);

        matrix = new int[rows][cols];
        buttons = new Button[rows][cols];
        int arrayIndex = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = array3[arrayIndex++];

                Button btn = new Button(" ");
                //change the stable size
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                final int rPos = r;
                final int cPos = c;
                btn.setOnAction(e -> handleCardClick(rPos, cPos, btn));

                buttons[r][c] = btn;
                cardGrid.add(btn, c, r);
            }
        }
        //flexible row and column size
        cardGrid.getColumnConstraints().clear();
        cardGrid.getRowConstraints().clear();

        for (int c = 0; c < cols; c++){
            javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / cols);
            cardGrid.getColumnConstraints().add(cc);
        }
        for (int r = 0; r < rows; r++) {
            javafx.scene.layout.RowConstraints rc = new javafx.scene.layout.RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setPercentHeight(100.0 / rows);
            cardGrid.getRowConstraints().add(rc);
        }

        statusLabel.setText("Game started! Select a card.");
    }



    private void playSoundEffect(String soundFileName){
        try {
            String path = "/com/example/flipping_card_game/sound_effect/" + soundFileName;
            URL resource = getClass().getResource(path);

            if(resource != null){
                AudioClip clip = new AudioClip(resource.toExternalForm());
                clip.play();

            }else{
                System.out.println("File not found : " + path);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ImageView getCradImageView(int cardValue, Button button) {
        String imagePath = cardPath + cardValue + ".png";
        var stream = getClass().getResourceAsStream(imagePath);

        if (stream == null) {
            System.err.println("Image not found : " + imagePath);
            return new ImageView(); // Çökmeyi önlemek için boş dön
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        imageView.fitWidthProperty().bind(button.widthProperty().multiply(0.6));
        imageView.fitHeightProperty().bind(button.heightProperty().multiply(0.6));


        return imageView;
    }


    private void gameChallenge(int totalCards, String challenge) {

        if ("Easy".equalsIgnoreCase(challenge) ) {
            cardFlipDelay = 2.0;
            rows = 2;
            cols = 4;
        } else if ("Medium".equalsIgnoreCase(challenge) ) {
            cardFlipDelay = 1.5;
            rows = 4;
            cols = 6;

        } else if ("Hard".equalsIgnoreCase(challenge)){
            cardFlipDelay = 0.5;
            rows = 6;
            cols = 8;
        }

    }


    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        startTime = Instant.now();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            if (timerLabel != null) {
                timerLabel.setText("Time: " + secondsElapsed + "s");
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateScoreboard() {
        if (wrongAttemptsLabel != null) {
            wrongAttemptsLabel.setText("Wrong Attempts: " + noMatching);
        }
    }

    private void handleCardClick(int r, int c, Button clickedButton) {
        playSoundEffect("click.wav");

        if (isProcessing || clickedButton.getGraphic() != null || clickedButton.isDisabled()) {
            PauseTransition soundDelay = new PauseTransition(Duration.millis(250));
            playSoundEffect("select_already_open.wav");
            return;
        }

        int cardValue = matrix[r][c];
        clickedButton.setGraphic(getCradImageView(cardValue, clickedButton));

        if (firstSelectedButton == null) {
            firstSelectedButton = clickedButton;

            firstRow = r;
            firstCol = c;
        } else {

            if (firstRow == r && firstCol == c) return;

            if (matrix[firstRow][firstCol] == matrix[r][c]) {
                PauseTransition soundDelay = new PauseTransition(Duration.millis(250));
                playSoundEffect("matched.wav");
                statusLabel.setText("Match found!");
                firstSelectedButton.setDisable(true);
                clickedButton.setDisable(true);
                firstSelectedButton = null;
                matchedPairsCount++;

                if (matchedPairsCount == totalPairs) {
                    if (timer != null) timer.stop();
                }
            } else {
                PauseTransition soundDelay = new PauseTransition(Duration.millis(250));
                playSoundEffect("not_matched.wav");
                statusLabel.setText("Not a match!");
                isProcessing = true;
                noMatching++;
                updateScoreboard();

                Button b1 = firstSelectedButton;
                Button b2 = clickedButton;

                PauseTransition pause = new PauseTransition(Duration.seconds(cardFlipDelay));
                pause.setOnFinished(e -> {
                    b1.setGraphic(null);
                    b2.setGraphic(null);

                    firstSelectedButton = null;
                    isProcessing = false;
                    statusLabel.setText("Select a card.");
                });
                pause.play();
            }
        }
    }

}