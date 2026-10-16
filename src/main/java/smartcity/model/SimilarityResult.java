package smartcity.model;

import smartcity.util.ConsoleUtil;

/**
 * Result container for document similarity analyses.
 */
public class SimilarityResult extends QueryResult {
    private final String docTitle1;
    private final String docTitle2;
    private final String metricType;
    private final double similarityScore;
    private final double distance;
    private final String details;

    public SimilarityResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                            String explanation, String docTitle1, String docTitle2,
                            String metricType, double similarityScore, double distance, String details) {
        super(success, algorithmName, metrics, explanation);
        this.docTitle1 = docTitle1;
        this.docTitle2 = docTitle2;
        this.metricType = metricType;
        this.similarityScore = similarityScore;
        this.distance = distance;
        this.details = details;
    }

    public String getDocTitle1() { return docTitle1; }
    public String getDocTitle2() { return docTitle2; }
    public String getMetricType() { return metricType; }
    public double getSimilarityScore() { return similarityScore; }
    public double getDistance() { return distance; }
    public String getDetails() { return details; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("DOCUMENT SIMILARITY RESULT"));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Document A", docTitle1));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Document B", docTitle2));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Similarity Metric", metricType));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Calculated Dist.", String.format("%.4f", distance)));
        sb.append(String.format("║  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%-54s" + ConsoleUtil.CYAN + " ║\n" + ConsoleUtil.RESET, "Similarity Score", String.format("%.2f%%", similarityScore * 100.0)));
        sb.append(ConsoleUtil.CYAN).append("╚══════════════════════════════════════════════════════════════════════════════╝\n").append(ConsoleUtil.RESET);
        if (details != null && !details.isEmpty()) {
            sb.append("\n" + ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Sequence Alignment & Comparison Details ◆─────────────────────────────────" + ConsoleUtil.RESET + "\n");
            sb.append(details).append("\n");
        }
        return sb.toString();
    }
}
