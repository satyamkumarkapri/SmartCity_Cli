package smartcity.util;

/**
 * Console output and UI layout formatting utility.
 * Provides curated ANSI palettes, box-drawing characters, and structured layout helpers.
 */
public class ConsoleUtil {
    // Basic ANSI Styles
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";

    // Foreground Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // High Intensity Colors & Dark Styles
    public static final String BOLD_BLACK = "\u001B[1;30m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    public static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ███████╗███╗   ███╗ █████╗ ██████╗ ████████╗ ██████╗██╗████████╗██╗   ██╗██╗  ██╗");
        System.out.println("  ██╔════╝████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝██╔════╝██║╚══██╔══╝╚██╗ ██╔╝╚██╗██╔╝");
        System.out.println("  ███████╗██╔████╔██║███████║██████╔╝   ██║   ██║     ██║   ██║    ╚████╔╝  ╚███╔╝ ");
        System.out.println("  ╚════██║██║╚██╔╝██║██╔══██║██╔══██╗   ██║   ██║     ██║   ██║     ╚██╔╝   ██╔██╗ ");
        System.out.println("  ███████║██║ ╚═╝ ██║██║  ██║██║  ██║   ██║   ╚██████╗██║   ██║      ██║   ██╔╝ ██╗");
        System.out.println("  ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝" + RESET);
        System.out.println(BOLD_BLACK + "        SmartCityX — Integrated Smart-City Analytics Platform (DSA-3)" + RESET);
        System.out.println(BOLD_BLACK + "    Heterogeneous Retrieval, Resource Allocation, and Discrete Optimization\n" + RESET);
    }

    public static void printHeader(String title) {
        System.out.println("\n" + CYAN + "╔══════════════════════════════════════════════════════════════════════════════╗" + RESET);
        int totalWidth = 76;
        int padding = Math.max(0, (totalWidth - title.length()) / 2);
        int rightPad = Math.max(0, totalWidth - padding - title.length());
        System.out.print(CYAN + "║ " + RESET + BOLD_BLACK);
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.print(title.toUpperCase());
        for (int i = 0; i < rightPad; i++) System.out.print(" ");
        System.out.println(CYAN + " ║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════════════════════════╝" + RESET);
    }

    public static void printSection(String title) {
        System.out.println("\n" + BRIGHT_BLUE + BOLD + "───◆ " + title.toUpperCase() + " ◆" + RESET + "─".repeat(Math.max(5, 74 - title.length())) + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(BRIGHT_GREEN + BOLD + "  ✔ [SUCCESS] " + RESET + BOLD_BLACK + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + "  ✖ [ERROR] " + RESET + RED + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(BRIGHT_YELLOW + BOLD + "  ▲ [WARNING] " + RESET + YELLOW + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(BRIGHT_CYAN + BOLD + "  ℹ [INFO] " + RESET + BOLD_BLACK + message + RESET);
    }
}
