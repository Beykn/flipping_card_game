package com.example.flipping_card_game;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomImageManager {
    private static File tempFolder;


    public static File getTempFolder() {
        return tempFolder;
    }


    //cihazdan görsel seçmek için kullanılan fonksiyon
    //tekrarlayan durumları göz önüne alarak oluşturuldu
    public static boolean selectAndPrepareCustomImages(Window ownerWindow, int requiredImageCount) {
        // BİLGİLENDİRME: Seçim açılmadan önce kullanıcıya yapması gereken seçimi ekranda gösteriyoruz
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.initOwner(ownerWindow);
        infoAlert.setTitle("Image Selection");
        infoAlert.setHeaderText("Please Select Images");
        infoAlert.setContentText("Please select at least " + requiredImageCount + " images for the custom deck.");
        infoAlert.showAndWait(); // Kullanıcı OK diyene kadar bekler

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + requiredImageCount + " images for cards!");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );

        List<File> selectedFiles = null;
        boolean selectionSuccessful = false;

        // --- DÖNGÜ BAŞLANGICI: Geçerli seçim yapılana veya çıkış onaylanana kadar devam eder ---
        while (!selectionSuccessful) {
            selectedFiles = fileChooser.showOpenMultipleDialog(ownerWindow);

            // A) CANCEL / KAPATMA DURUMU
            if (selectedFiles == null) {
                Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
                logoutAlert.initOwner(ownerWindow);
                logoutAlert.setTitle("Logout");
                logoutAlert.setHeaderText("You are about to exit!");
                logoutAlert.setContentText("Do you want to exit the application?");

                Optional<ButtonType> result = logoutAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Uygulamayı tamamen kapatır
                    Platform.exit();
                    System.exit(0);
                } else {
                    // Cancel/İptal seçildiyse while döngüsü başa döner ve tekrar FileChooser açılır
                    continue;
                }
            }

            // B) EKSİK RESİM SEÇİMİ DURUMU
            if (selectedFiles.size() < requiredImageCount) {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.initOwner(ownerWindow);
                warningAlert.setTitle("Incomplete Selection");
                warningAlert.setHeaderText("Not Enough Images Selected!");
                warningAlert.setContentText("You must select at least " + requiredImageCount + " images to proceed.");
                warningAlert.showAndWait();

                // Eksik seçim uyarı kapatıldıktan sonra while döngüsü başa döner ve tekrar FileChooser açılır
                continue;
            }

            // C) SEÇİM BAŞARILI
            selectionSuccessful = true;
        }

        // --- GEÇİCİ KLASÖR KOPYALAMA VE İŞLEME ---
        try {
            if (tempFolder == null || !tempFolder.exists()) {
                tempFolder = Files.createTempDirectory("flip_game_custom_").toFile();
                tempFolder.deleteOnExit();
            }

            List<Path> copiedTempPath = new ArrayList<>();
            for (int i = 0; i < requiredImageCount; i++) {
                File src = selectedFiles.get(i);
                File dest = new File(tempFolder, src.getName());

                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                copiedTempPath.add(dest.toPath());
            }

            ImageRenamer.renameAndNormalizeImages(copiedTempPath, 30);
            return true;

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.initOwner(ownerWindow);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to Prepare Images");
            errorAlert.setContentText("An error occurred: " + e.getMessage());
            errorAlert.showAndWait();
            return false;
        }
    }
}
