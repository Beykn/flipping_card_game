import java.time.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        System.out.println("Enter a number, max pairs will be 30:");

        int input = 0; 

        try {
            input = scanner.nextInt();
            
            if (input < 1 || input > 30) {
                throw new IllegalArgumentException("Please enter a number between 1 and 30.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid integer.");
            return;
        } catch (IllegalArgumentException e) { 
            System.out.println(e.getMessage());
            return;
        }

        Random random = new Random();

        int[] array1 = new int[input];
        int[] array2 = new int[input];
        int[] array3 = new int[input * 2];

        for (int i = 0; i < input; i++) {
            array1[i] = i + 1;
            array2[i] = i + 1;
        }

        
        for (int i = array1.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            int temp = array1[i];
            array1[i] = array1[randomIndex];
            array1[randomIndex] = temp;
        }

        for (int i = array2.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            int temp = array2[i];
            array2[i] = array2[randomIndex];
            array2[randomIndex] = temp;
        }

       
        for (int i = 0; i < array1.length; i++) {
            array3[i] = array1[i];
        }
        for (int i = 0; i < array2.length; i++) {
            array3[array1.length + i] = array2[i];
        }


        int totalCards = array3.length;
        int row = 1;
        int col = totalCards;

        for (int i = (int) Math.sqrt(totalCards); i >= 1; i--) {
            if (totalCards % i == 0) {
                row = i;
                col = totalCards / i;
                break;
            }
        }

        int[][] matrix = new int[row][col];
        int arrayIndex = 0;

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                matrix[r][c] = array3[arrayIndex++];
            }
        }

        
        Instant startTime = Instant.now();

        while (true) {
            
            System.out.println("\n--- BOARD ---");
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    System.out.print(matrix[r][c] + " ");
                }
                System.out.println();
            }
            System.out.println("-------------");

            
            boolean allMatched = true;
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (matrix[r][c] != 0) {
                        allMatched = false;
                        break;
                    }
                }
                if (!allMatched) break;
            }

            if (allMatched) {
                System.out.println("\n Congratulations! You've matched all numbers!");
                break; 
            }

            
            System.out.println("--------- FIRST CHOICE ---------");
           
            System.out.print("Please choose column (0 to " + (col - 1) + "): ");
            int col1 = scanner.nextInt();
            System.out.print("Please choose row (0 to " + (row - 1) + "): ");
            int row1 = scanner.nextInt();

            System.out.println("--------- SECOND CHOICE ---------");
            
            System.out.print("Please choose column (0 to " + (col - 1) + "): ");
            int col2 = scanner.nextInt();
            System.out.print("Please choose row (0 to " + (row - 1) + "): ");
            int row2 = scanner.nextInt();

            
            boolean isSameCard = (row1 == row2 && col1 == col2);

            if (!isSameCard && matrix[row1][col1] != 0 && matrix[row2][col2] != 0 && matrix[row1][col1] == matrix[row2][col2]) {
                matrix[row1][col1] = 0;
                matrix[row2][col2] = 0;
                System.out.println("\n-> You found a match!");
            } else {
                System.out.println("\n-> Not a match (or invalid card selection), try again.");
            }
        }

        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Time taken: " + duration.toSeconds() + " seconds.");
    }
}