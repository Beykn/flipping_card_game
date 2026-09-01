/*
package com.example.flipping_card_game;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChangeFileName {

    public static void main(String[] args) {
        Path targetFile = Paths.get("{YOUR_DIRECTORY_NAME}");

        try {
            List<Path> pngFiles = new ArrayList<>();

            try (DirectoryStream<Path> flow = Files.newDirectoryStream(targetFile, "*.png")) {
                for (Path file : flow) {
                    pngFiles.add(file);
                }
            }

            List<Path> tempFiles = new ArrayList<>();
            for (int i = 0; i < pngFiles.size(); i++) {
                Path file = pngFiles.get(i);
                Path tempPath = file.resolveSibling("temp_" + i + ".tmp");
                Files.move(file, tempPath);
                tempFiles.add(tempPath);
            }

            int count = 1;
            for (Path tempFile : tempFiles) {
                if (count > 30) {
                    System.out.println("We cannot continue. Our boundary is 30.");
                    break;
                }
                String newName = count + ".png";
                Path targetPath = tempFile.resolveSibling(newName);

                Files.move(tempFile, targetPath);
                System.out.println("Image renamed: " + newName);

                count++;
            }

        } catch (IOException e) {
            System.err.println("Some errors caused!: " + e.getMessage());
        }
    }
}

 */