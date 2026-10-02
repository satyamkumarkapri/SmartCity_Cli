package smartcityx.util;

public class ComplexityUtil {
    public static void display(String algo, String timeBest, String timeAvg, String timeWorst, String space) {
        ConsoleUtil.printSection("ALGORITHM COMPLEXITY: " + algo);
        System.out.println(String.format("%-15s: %s", "Time (Best)", timeBest));
        System.out.println(String.format("%-15s: %s", "Time (Average)", timeAvg));
        System.out.println(String.format("%-15s: %s", "Time (Worst)", timeWorst));
        System.out.println(String.format("%-15s: %s", "Space", space));
        System.out.println("--------------------------------------------------");
    }
}
