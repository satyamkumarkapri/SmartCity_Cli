package smartcity.model;

import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Result container for head-to-head algorithm benchmarks.
 */
public class ComparisonResult extends QueryResult {
    public static class Entry {
        public final String name;
        public final int matchesCount;
        public final long executionTimeNs;
        public final long comparisonsCount;
        public final String timeComplexity;
        public final String spaceComplexity;

        public Entry(String name, int matchesCount, long timeNs, long comparisons, String timeComp, String spaceComp) {
            this.name = name;
            this.matchesCount = matchesCount;
            this.executionTimeNs = timeNs;
            this.comparisonsCount = comparisons;
            this.timeComplexity = timeComp;
            this.spaceComplexity = spaceComp;
        }
    }

    private final String pattern;
    private final int textLength;
    private final DynamicArray<Entry> entries;
    private final boolean allAgreed;

    public ComparisonResult(boolean success, AlgorithmMetrics metrics, String explanation,
                            String pattern, int textLength, DynamicArray<Entry> entries, boolean allAgreed) {
        super(success, "Comparison Engine", metrics, explanation);
        this.pattern = pattern;
        this.textLength = textLength;
        this.entries = entries != null ? entries : new DynamicArray<>();
        this.allAgreed = allAgreed;
    }

    public DynamicArray<Entry> getEntries() { return entries; }
    public boolean isAllAgreed() { return allAgreed; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("ALGORITHM HEAD-TO-HEAD BENCHMARK"));
        sb.append(ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Benchmark Parameters & Status ◆───────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : \"%s\" (length: %d)\n", "Search Pattern", pattern, pattern.length()));
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %,d characters\n", "Text Corpus Length", textLength));
        String agreeStr = allAgreed ? (ConsoleUtil.BRIGHT_GREEN + "ALL IMPLEMENTATIONS AGREE (100%)" + ConsoleUtil.RESET)
                : (ConsoleUtil.RED + "DISCREPANCY DETECTED" + ConsoleUtil.RESET);
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Agreement Status", agreeStr));

        sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Benchmark Performance Table ◆─────────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format(" " + ConsoleUtil.BOLD_BLACK + "%-20s │ %-7s │ %-13s │ %-13s │ %-10s │ %s" + ConsoleUtil.RESET + "\n",
                "Algorithm", "Matches", "Time (ns)", "Comparisons", "Time Comp.", "Space"));
        sb.append("─────────────────────┼─────────┼───────────────┼───────────────┼────────────┼──────────\n");
        for (Entry e : entries) {
            sb.append(String.format(" " + ConsoleUtil.CYAN + "%-20s" + ConsoleUtil.RESET + " │ %-7d │ %,13d │ %,13d │ " + ConsoleUtil.BRIGHT_YELLOW + "%-10s" + ConsoleUtil.RESET + " │ %s\n",
                    e.name, e.matchesCount, e.executionTimeNs, e.comparisonsCount, e.timeComplexity, e.spaceComplexity));
        }
        sb.append("─────────────────────┴─────────┴───────────────┴───────────────┴────────────┴──────────\n");
        return sb.toString();
    }
}
