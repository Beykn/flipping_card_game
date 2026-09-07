package com.example.flipping_card_game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ImageRenamer {

    public static void renameAndNormalizeImages(List<Path> targetFiles, int maxLimit) throws IOException {
        // 1. ADIM: İsim çakışmalarını önlemek için önce geçici (.tmp) isimler veriyoruz
        for (int i = 0; i < targetFiles.size(); i++) {
            Path file = targetFiles.get(i);
            Path tempPath = file.resolveSibling("temp_" + i + ".tmp");

            // Dosyayı temp_i.tmp olarak yeniden adlandır/taşı
            Files.move(file, tempPath, StandardCopyOption.REPLACE_EXISTING);
            tempPath.toFile().deleteOnExit(); // Kapanışta silinmesini sağla

            // Listeyi geçici dosya yoluyla güncelle
            targetFiles.set(i, tempPath);
        }

        // 2. ADIM: Geçici isimdeki dosyaları 1.png, 2.png, ... şeklinde düzenliyoruz
        int count = 1;
        for (Path tempFile : targetFiles) {
            if (count > maxLimit) {
                System.out.println("We cannot continue. Our boundary is " + maxLimit);
                break;
            }

            String newName = count + ".png";
            Path targetPath = tempFile.resolveSibling(newName);

            // temp_i.tmp -> count.png
            Files.move(tempFile, targetPath, StandardCopyOption.REPLACE_EXISTING);
            targetPath.toFile().deleteOnExit();

            System.out.println("Image renamed: " + newName);
            count++;
        }
    }
}