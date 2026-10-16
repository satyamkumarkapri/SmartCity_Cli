package smartcity.util;

import java.util.Scanner;

/**
 * Terminal user input helper with validation and error recovery.
 */
public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static void setScanner(Scanner s) {
        scanner = s;
    }

    public static boolean hasNext() {
        return scanner != null && scanner.hasNextLine();
    }

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new java.util.NoSuchElementException("End of input stream");
            }
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Please enter a valid integer.");
            }
        }
    }

    public static int getIntWithDefault(String prompt, int defaultVal) {
        while (true) {
            System.out.print(prompt + " [default: " + defaultVal + "]: ");
            if (!scanner.hasNextLine()) {
                throw new java.util.NoSuchElementException("End of input stream");
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultVal;
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Please enter a valid integer.");
            }
        }
    }

    public static double getDoubleWithDefault(String prompt, double defaultVal) {
        while (true) {
            System.out.print(prompt + " [default: " + defaultVal + "]: ");
            if (!scanner.hasNextLine()) {
                throw new java.util.NoSuchElementException("End of input stream");
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultVal;
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Please enter a valid decimal number.");
            }
        }
    }

    public static long getLongWithDefault(String prompt, long defaultVal) {
        while (true) {
            System.out.print(prompt + " [default: " + defaultVal + "]: ");
            if (!scanner.hasNextLine()) {
                throw new java.util.NoSuchElementException("End of input stream");
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultVal;
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Please enter a valid integer.");
            }
        }
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new java.util.NoSuchElementException("End of input stream");
        }
        return scanner.nextLine().trim();
    }

    public static String getStringWithDefault(String prompt, String defaultVal) {
        System.out.print(prompt + " [default: " + defaultVal + "]: ");
        if (!scanner.hasNextLine()) {
            throw new java.util.NoSuchElementException("End of input stream");
        }
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? defaultVal : line;
    }

    public static boolean getYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            if (!scanner.hasNextLine()) {
                throw new java.util.NoSuchElementException("End of input stream");
            }
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) return true;
            if (input.equals("N") || input.equals("NO")) return false;
            ConsoleUtil.printError("Please enter Y or N.");
        }
    }
}
