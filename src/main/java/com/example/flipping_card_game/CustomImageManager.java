package com.example.flipping_card_game;

import javafx.stage.FileChooser;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class CustomImageManager {
    private static File tempFolder;

    public static File getTempFolder() {
        return tempFolder;
    }


    //cihazdan görsel seçmek için kullanılan fonksiyon
    public static boolean selectAndPrepareCustomImages (Window ownerWindow, int requiredImageCount){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Kartlar için " + requiredImageCount + " adet görüntü seçin!");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(ownerWindow);

        if (selectedFiles == null || selectedFiles.size() < requiredImageCount) {
            System.err.println("Yetersiz resim seçildi! En az " + requiredImageCount + " adet resim seçmelisiniz.");
            return false;
        }

        try {
            //geçici klasör oluşturdum
            if(tempFolder == null || !tempFolder.exists()){
                tempFolder = Files.createTempDirectory("flip_game_custom_").toFile();
                tempFolder.deleteOnExit();//kapanınca silecek
            }

            //kullanıcının seçtiği dosyalar temp klasörüne kopyalandı isimleri değiştrilecek
            List<Path> copiedTempPath = new ArrayList<>();
            for (int i = 0; i < requiredImageCount ; i++) {
                File src = selectedFiles.get(i);
                File dest = new File(tempFolder,src.getName());

                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                copiedTempPath.add(dest.toPath());
            }

            //dosyalar yeniden isimlendirildi
            ImageRenamer.renameAndNormalizeImages(copiedTempPath,30);
            return true;

        } catch (IOException e) {
            System.err.println("Görseller hazırlanırken hata oluştu: " + e.getMessage());
            return false;
        }

    }

}
