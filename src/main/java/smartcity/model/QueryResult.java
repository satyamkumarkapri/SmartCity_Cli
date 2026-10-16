package smartcity.model;

import smartcity.util.ConsoleUtil;

/**
 * Base contract for all query execution results in SmartCityX.
 * Produces structured, styled panel outputs for the CLI.
 */
public abstract class QueryResult {
    private final boolean success;
    private final String algorithmName;
    private final AlgorithmMetrics metrics;
    private final String explanation;

    public QueryResult(boolean success, String algorithmName, AlgorithmMetrics metrics, String explanation) {
        this.success = success;
        this.algorithmName = algorithmName;
        this.metrics = metrics;
        this.explanation = explanation;
    }

    public boolean isSuccess() { return success; }
    public String getAlgorithmName() { return algorithmName; }
    public AlgorithmMetrics getMetrics() { return metrics; }
    public String getExplanation() { return explanation; }

    /**
     * Formats this result into human-readable tabular text for CLI display.
     */
    public abstract String formatOutput();

    protected String formatCommonHeader(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(ConsoleUtil.CYAN).append("╔══════════════════════════════════════════════════════════════════════════════╗\n").append(ConsoleUtil.RESET);
        int totalWidth = 76;
        int padding = Math.max(0, (totalWidth - title.length()) / 2);
        int rightPad = Math.max(0, totalWidth - padding - title.length());
        sb.append(ConsoleUtil.CYAN).append("║ ").append(ConsoleUtil.RESET).append(ConsoleUtil.BOLD_BLACK);
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(title.toUpperCase());
        for (int i = 0; i < rightPad; i++) sb.append(" ");
        sb.append(ConsoleUtil.CYAN).append(" ║\n").append(ConsoleUtil.RESET);
        sb.append(ConsoleUtil.CYAN).append("╠══════════════════════════════════════════════════════════════════════════════╣\n").append(ConsoleUtil.RESET);
        
        sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_CYAN + "%-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Algorithm", algorithmName));
        String statusLabel = success ? (ConsoleUtil.BRIGHT_GREEN + "✔ SUCCESS" + ConsoleUtil.RESET) : (ConsoleUtil.RED + "✖ FAILED" + ConsoleUtil.RESET);
        sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %-63s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Execution Status", statusLabel));
        
        if (metrics != null) {
            String timeStr = String.format("%,d ns (%.4f ms)", metrics.getExecutionTimeNs(), metrics.getExecutionTimeMs());
            sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Execution Time", timeStr));
            if (metrics.getComparisonCount() >= 0) {
                String compStr = String.format("%,d operations", metrics.getComparisonCount());
                sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Comparisons", compStr));
            }
            if (metrics.getCollisionCount() >= 0) {
                String collStr = String.format("%,d collisions", metrics.getCollisionCount());
                sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Hash Collisions", collStr));
            }
            sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_YELLOW + "%-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                    "Time Complexity", metrics.getTimeComplexity()));
            sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_YELLOW + "%-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                    "Space Complexity", metrics.getSpaceComplexity()));
        }
        if (explanation != null && !explanation.isEmpty()) {
            sb.append(ConsoleUtil.CYAN).append("╟──────────────────────────────────────────────────────────────────────────────╢\n").append(ConsoleUtil.RESET);
            sb.append(String.format("║  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Theoretical Note", ""));
            String[] words = explanation.split(" ");
            StringBuilder line = new StringBuilder();
            for (String w : words) {
                if (line.length() + w.length() + 1 > 72) {
                    sb.append(String.format(ConsoleUtil.CYAN + "║  " + ConsoleUtil.BOLD_BLACK + "%-72s" + ConsoleUtil.CYAN + "  ║\n" + ConsoleUtil.RESET, line.toString()));
                    line = new StringBuilder();
                }
                if (line.length() > 0) line.append(" ");
                line.append(w);
            }
            if (line.length() > 0) {
                sb.append(String.format(ConsoleUtil.CYAN + "║  " + ConsoleUtil.BOLD_BLACK + "%-72s" + ConsoleUtil.CYAN + "  ║\n" + ConsoleUtil.RESET, line.toString()));
            }
        }
        sb.append(ConsoleUtil.CYAN).append("╚══════════════════════════════════════════════════════════════════════════════╝\n").append(ConsoleUtil.RESET);
        return sb.toString();
    }
}
