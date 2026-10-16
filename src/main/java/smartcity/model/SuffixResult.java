package smartcity.model;

import smartcity.util.ConsoleUtil;

/**
 * Result container for Suffix Array, LCP, and Suffix Automaton analyses.
 */
public class SuffixResult extends QueryResult {
    private final String textSample;
    private final int[] suffixArray;
    private final int[] lcpArray;
    private final long distinctSubstrings;
    private final boolean substringFound;

    public SuffixResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                        String explanation, String textSample, int[] suffixArray,
                        int[] lcpArray, long distinctSubstrings, boolean substringFound) {
        super(success, algorithmName, metrics, explanation);
        this.textSample = textSample;
        this.suffixArray = suffixArray;
        this.lcpArray = lcpArray;
        this.distinctSubstrings = distinctSubstrings;
        this.substringFound = substringFound;
    }

    public int[] getSuffixArray() { return suffixArray; }
    public int[] getLcpArray() { return lcpArray; }
    public long getDistinctSubstrings() { return distinctSubstrings; }
    public boolean isSubstringFound() { return substringFound; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("SUFFIX STRUCTURE ANALYSIS"));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %,d characters%s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                "Input Length", textSample.length(),
                " ".repeat(Math.max(0, 40 - String.format("%,d characters", textSample.length()).length()))));
        if (distinctSubstrings >= 0) {
            sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%,d unique substrings%s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET,
                    "Distinct Substrings", distinctSubstrings,
                    " ".repeat(Math.max(0, 34 - String.format("%,d unique substrings", distinctSubstrings).length()))));
        }
        String matchStr = substringFound ? (ConsoleUtil.BRIGHT_GREEN + "FOUND IN TEXT (VERIFIED)" + ConsoleUtil.RESET)
                : (ConsoleUtil.YELLOW + "NOT FOUND / N/A" + ConsoleUtil.RESET);
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %-63s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Query Match", matchStr));
        sb.append(ConsoleUtil.CYAN).append("╚══════════════════════════════════════════════════════════════════════════════╝\n").append(ConsoleUtil.RESET);

        if (suffixArray != null && suffixArray.length > 0) {
            int displayCount = Math.min(10, suffixArray.length);
            sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ First " + displayCount + " Suffix Array & LCP Array Entries ◆──────────────────────────────────" + ConsoleUtil.RESET + "\n");
            sb.append(String.format(" " + ConsoleUtil.BOLD_BLACK + "%-6s │ %-6s │ %-6s │ %s" + ConsoleUtil.RESET + "\n", "Rank", "SA[i]", "LCP[i]", "Suffix String S[SA[i]..]"));
            sb.append("───────┼────────┼────────┼─────────────────────────────────────────────\n");
            for (int i = 0; i < displayCount; i++) {
                int saIdx = suffixArray[i];
                int lcpVal = (lcpArray != null && i < lcpArray.length) ? lcpArray[i] : 0;
                String suff = textSample.substring(saIdx);
                if (suff.length() > 40) suff = suff.substring(0, 40) + "...";
                sb.append(String.format(" " + ConsoleUtil.CYAN + "%-6d" + ConsoleUtil.RESET + " │ %-6d │ " + ConsoleUtil.YELLOW + "%-6d" + ConsoleUtil.RESET + " │ %s\n",
                        i, saIdx, lcpVal, suff.replace("\n", " ")));
            }
            sb.append("───────┴────────┴────────┴─────────────────────────────────────────────\n");
        }
        return sb.toString();
    }
}
