import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        System.out.println("Enter a number:");
        int input = scanner.nextInt();
        Random random = new Random();

        int[] array1 = new int[input];
        int[] array2 = new int[input];

        
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

        
        while (true) {

            boolean allMatched = true;
            for (int val : array1) {
                if (val != 0) {
                    allMatched = false; 
                    break; 
                }
            }


            if (allMatched) {
                System.out.println(" Congratulations! You've matched all numbers!");
                break; 
            }
            
            System.out.print("Array 1: ");
            for (int val : array1) System.out.print(val + " ");
            System.out.println();

            System.out.print("Array 2: ");
            for (int val : array2) System.out.print(val + " ");
            System.out.println("\n--------------------");

            System.out.print("Please choose index for array1 (0 to " + (input - 1) + "): ");
            int index1 = scanner.nextInt();

            System.out.print("Please choose index for array2 (0 to " + (input - 1) + "): ");
            int index2 = scanner.nextInt();

            if (array1[index1] != 0 && array1[index1] == array2[index2]) {
                array1[index1] = 0;
                array2[index2] = 0;
                System.out.println("-> You found a match!\n");
            } else {
                System.out.println("-> Not a match, try again.\n");
            }

            
        }
    }
}