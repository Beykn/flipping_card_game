module com.example.flipping_card_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.flipping_card_game to javafx.fxml;
    exports com.example.flipping_card_game;
}