package smartcity.model;

import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Result container for string search queries.
 */
public class StringSearchResult extends QueryResult {
    private final String pattern;
    private final int corpusSize;
    private final DynamicArray<MatchResult> matches;

    public StringSearchResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                              String explanation, String pattern, int corpusSize, DynamicArray<MatchResult> matches) {
        super(success, algorithmName, metrics, explanation);
        this.pattern = pattern;
        this.corpusSize = corpusSize;
        this.matches = matches != null ? matches : new DynamicArray<>();
    }

    public String getPattern() { return pattern; }
    public int getCorpusSize() { return corpusSize; }
    public DynamicArray<MatchResult> getMatches() { return matches; }
    public int getMatchCount() { return matches.size(); }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("EXACT PATTERN SEARCH RESULT"));
        sb.append(ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Query Parameters & Statistics ◆───────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : \"%s\"\n", "Query Pattern", pattern));
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : %,d documents\n", "Corpus Size", corpusSize));
        sb.append(String.format("  " + ConsoleUtil.BOLD_BLACK + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%,d occurrences found\n" + ConsoleUtil.RESET, "Matches Found", matches.size()));

        if (matches.isEmpty()) {
            sb.append("\n  " + ConsoleUtil.YELLOW + "ℹ No occurrences found for the query in active corpus." + ConsoleUtil.RESET + "\n");
        } else {
            int displayLimit = Math.min(10, matches.size());
            sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Top " + displayLimit + " Occurrences (of " + String.format("%,d", matches.size()) + " total) ◆" + ConsoleUtil.RESET + "───────────────────────────────────────\n");
            for (int i = 0; i < displayLimit; i++) {
                MatchResult m = matches.get(i);
                sb.append(String.format("  " + ConsoleUtil.CYAN + "[%2d]" + ConsoleUtil.RESET + " Doc #" + ConsoleUtil.BOLD_BLACK + "%d" + ConsoleUtil.RESET + " (" + ConsoleUtil.BOLD_BLACK + "%s" + ConsoleUtil.RESET + ") @ offset %d\n",
                        i + 1, m.getDocumentId(), m.getDocumentTitle(), m.getPosition()));
                sb.append("       Context: \"..." + m.getSnippet() + "...\"\n");
            }
            if (matches.size() > displayLimit) {
                sb.append("  ... and " + String.format("%,d", matches.size() - displayLimit) + " more matches across the corpus.\n");
            }
        }
        return sb.toString();
    }
}
