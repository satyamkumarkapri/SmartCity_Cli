package smartcityx.util;

public class ConsoleUtil {
    
    public static void printHeader(String title) {
        System.out.println("\n==================================================");
        int spaces = (50 - title.length()) / 2;
        for (int i = 0; i < spaces; i++) System.out.print(" ");
        System.out.println(title);
        System.out.println("==================================================");
    }
    
    public static void printSection(String title) {
        System.out.println("\n--------------------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------------------");
    }
    
    public static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }
    
    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }
    
    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }
    
    public static void printTableLine() {
        System.out.println("+------------------------------------------------+");
    }
}
