package com.example.flipping_card_game;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.net.URL;
import javafx.scene.media.AudioClip;

import java.time.Instant;
import java.util.Optional;
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
    private boolean isCustomTheme = false;
    private boolean isCancelled = false;

    //I will use these to the reset button
    private String lastDifficulty = "Easy";
    private String lastCardTheme = "Animal";

    public void setupGame( String difficulty, String card) {

        Stage stage = (Stage) cardGrid.getScene().getWindow();

        this.lastDifficulty = difficulty;
        this.lastCardTheme = card;

        int input = 0;
        cardGrid.getChildren().clear();
        firstSelectedButton = null;
        matchedPairsCount = 0;
        noMatching = 0;
        isProcessing = false;
        secondsElapsed = 0;
        totalPairs = input; // Oyunun biteceği toplam çift sayısı

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


        // Seçilen temaya göre yapılacak işlem
        // Gelen temaya göre klasör yolunu ayarlıyoruz
        if ("Pokemon".equalsIgnoreCase(card)) {
            this.cardPath = "/com/example/flipping_card_game/img/pokemon/";
            this.isCustomTheme = false;
        } else if ("Animal".equalsIgnoreCase(card)) {
            this.cardPath = "/com/example/flipping_card_game/img/animal/";
            this.isCustomTheme = false;
        } else {
            //center the grid
            stage.centerOnScreen();
            // Custom seçildiyse kullanıcının galerisinden resim seçtirip hazırlıyoruz
            this.isCustomTheme = true;
            //stop timer until the choose images
            timer.stop();
            boolean success = CustomImageManager.selectAndPrepareCustomImages(null, input);
            if (!success) {
                if (statusLabel != null) {
                    statusLabel.setText("Image selected cancelled!");
                    isCancelled = true;
                }
                if(isCancelled){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Logout");
                    alert.setHeaderText("You are about the logout !");
                    alert.setContentText("Do you want to save before exiting ");

                    if(alert.showAndWait().get() == ButtonType.OK){
                        Platform.exit();
                        System.exit(0);
                    }
                }
                return;
            }
            //start timer again
            startTimer();
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
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / cols);
            cardGrid.getColumnConstraints().add(cc);
        }
        for (int r = 0; r < rows; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setPercentHeight(100.0 / rows);
            cardGrid.getRowConstraints().add(rc);
        }

        statusLabel.setText("Game started! Select a card.");


        // --- PENCEREYİ MASAÜSTÜNÜN TAM ORTASINA ALAN KESİN KOD ---
        // Bütün nesneler oluşturulup Scene yüklendikten SONRA çalışır
        Platform.runLater(() -> {
            if (cardGrid != null && cardGrid.getScene() != null) {

                if (stage != null) {
                    stage.sizeToScene();   // İçerik boyutunu yeniden hesapla
                    stage.centerOnScreen(); // Ekranın tam ortasına yerleştir
                }
            }
        });

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
        Image image;

        if(isCustomTheme){
            //geçici klasördeki dosyalar
            File imageFile = new File(CustomImageManager.getTempFolder(),cardValue + ".png");
            if(!imageFile.exists()){
                System.err.println("Image not found: " +imageFile.getAbsolutePath());
                return new ImageView();
            }
            image = new Image(imageFile.toURI().toString());
        }else{

            String imagePath = cardPath + cardValue + ".png";
            var stream = getClass().getResourceAsStream(imagePath);

            if (stream == null) {
                System.err.println("Image not found : " + imagePath);
                return new ImageView(); // Çökmeyi önlemek için boş dön
            }
            image = new Image(stream);
        }


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
                //firstSelectedButton.setDisable(true);
                clickedButton.setDisable(true);
                //move this line here because after matched user could be chosen directly new card
                firstSelectedButton.setDisable(true);
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

    //RESET
    @FXML
    private void handleResetButton(){
        if(timer != null){
            timer.stop();
        }

        //pause transitions
        firstSelectedButton = null;
        isProcessing = false;
        matchedPairsCount = 0;
        noMatching = 0;
        secondsElapsed = 0;

        setupGame(lastDifficulty, lastCardTheme);

        statusLabel.setText("Game Reset ! ");
    }

    //Main Menu
    @FXML
    private void handleMainMenuButton(javafx.event.ActionEvent event){
        try{
            if(timer != null){
                timer.stop();
            }
            // Ana menü FXML dosyası
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start_view.fxml"));
            Parent root = loader.load();

            //  Mevcut Stage'i alıp Ana Menü sahnesine geçiş yaptı
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.centerOnScreen();

            stage.show();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Ana menüye dönerken hata oluştu!");
            }
        }
    }





}