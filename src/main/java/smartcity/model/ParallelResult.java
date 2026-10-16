package smartcity.model;

import smartcity.util.ConsoleUtil;

/**
 * Result container for parallel vs sequential execution analysis.
 */
public class ParallelResult extends QueryResult {
    private final String operation;
    private final int inputSize;
    private final double resultValue;
    private final long sequentialTimeNs;
    private final long parallelTimeNs;
    private final double measuredSpeedup;
    private final int workerThreads;
    private final String theoreticalWork;
    private final String theoreticalSpan;
    private final boolean resultsMatch;

    public ParallelResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                          String explanation, String operation, int inputSize, double resultValue,
                          long sequentialTimeNs, long parallelTimeNs, double measuredSpeedup,
                          int workerThreads, String theoreticalWork, String theoreticalSpan,
                          boolean resultsMatch) {
        super(success, algorithmName, metrics, explanation);
        this.operation = operation;
        this.inputSize = inputSize;
        this.resultValue = resultValue;
        this.sequentialTimeNs = sequentialTimeNs;
        this.parallelTimeNs = parallelTimeNs;
        this.measuredSpeedup = measuredSpeedup;
        this.workerThreads = workerThreads;
        this.theoreticalWork = theoreticalWork;
        this.theoreticalSpan = theoreticalSpan;
        this.resultsMatch = resultsMatch;
    }

    public String getOperation() { return operation; }
    public int getInputSize() { return inputSize; }
    public double getResultValue() { return resultValue; }
    public long getSequentialTimeNs() { return sequentialTimeNs; }
    public long getParallelTimeNs() { return parallelTimeNs; }
    public double getMeasuredSpeedup() { return measuredSpeedup; }
    public int getWorkerThreads() { return workerThreads; }
    public String getTheoreticalWork() { return theoreticalWork; }
    public String getTheoreticalSpan() { return theoreticalSpan; }
    public boolean isResultsMatch() { return resultsMatch; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("PARALLEL ALGORITHM ANALYSIS"));
        sb.append(ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Work-Span & Speedup Metrics ◆─────────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Operation", operation));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %,d elements\n", "Input Size", inputSize));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %.4f\n", "Computed Result", resultValue));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %d worker threads\n", "Worker Threads (P)", workerThreads));
        String seqStr = String.format("%,d ns (%.3f ms)", sequentialTimeNs, sequentialTimeNs / 1e6);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Sequential (T1)", seqStr));
        String parStr = String.format("%,d ns (%.3f ms)", parallelTimeNs, parallelTimeNs / 1e6);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Parallel (Tp)", parStr));
        String speedupStr = String.format("%.2fx  (Efficiency: %.1f%%)", measuredSpeedup, (measuredSpeedup / workerThreads) * 100.0);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_GREEN + "%s\n" + ConsoleUtil.RESET, "Measured Speedup", speedupStr));
        String compStr = String.format("Work T1 = %s │ Span Tinf = %s", theoreticalWork, theoreticalSpan);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : " + ConsoleUtil.BRIGHT_YELLOW + "%s\n" + ConsoleUtil.RESET, "Complexity", compStr));
        String matchStr = resultsMatch ? (ConsoleUtil.BRIGHT_GREEN + "PASS (Identical results)" + ConsoleUtil.RESET)
                : (ConsoleUtil.RED + "MISMATCH DETECTED" + ConsoleUtil.RESET);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Correctness", matchStr));
        return sb.toString();
    }
}
