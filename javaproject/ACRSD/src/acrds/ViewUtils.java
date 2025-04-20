package acrds;



import java.util.Scanner;

public class ViewUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printMenu(String title, String[] options) {
        System.out.println("=== " + title + " ===");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s\n", i + 1, options[i]);
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printTable(String[] headers, String[][] data) {
        for (String h : headers) System.out.printf("%-20s", h);
        System.out.println();
        for (String[] row : data) {
            for (String cell : row) System.out.printf("%-20s", cell);
            System.out.println();
        }
    }
}
