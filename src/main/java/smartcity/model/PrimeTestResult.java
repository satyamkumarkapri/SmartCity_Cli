package smartcity.model;

import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Result container for Miller-Rabin primality testing.
 */
public class PrimeTestResult extends QueryResult {
    private final long candidate;
    private final boolean isPrime;
    private final boolean isDeterministic;
    private final int rounds;
    private final DynamicArray<Long> witnesses;
    private final double errorProbabilityBound;

    public PrimeTestResult(boolean success, String algorithmName, AlgorithmMetrics metrics,
                           String explanation, long candidate, boolean isPrime,
                           boolean isDeterministic, int rounds, DynamicArray<Long> witnesses,
                           double errorProbabilityBound) {
        super(success, algorithmName, metrics, explanation);
        this.candidate = candidate;
        this.isPrime = isPrime;
        this.isDeterministic = isDeterministic;
        this.rounds = rounds;
        this.witnesses = witnesses != null ? witnesses : new DynamicArray<>();
        this.errorProbabilityBound = errorProbabilityBound;
    }

    public long getCandidate() { return candidate; }
    public boolean isPrime() { return isPrime; }
    public boolean isDeterministic() { return isDeterministic; }
    public int getRounds() { return rounds; }
    public DynamicArray<Long> getWitnesses() { return witnesses; }
    public double getErrorProbabilityBound() { return errorProbabilityBound; }

    @Override
    public String formatOutput() {
        StringBuilder sb = new StringBuilder(formatCommonHeader("MILLER-RABIN PRIMALITY TEST RESULT"));
        sb.append(ConsoleUtil.BRIGHT_BLUE + ConsoleUtil.BOLD + "───◆ Primality Verification Metrics ◆──────────────────────────────────────────" + ConsoleUtil.RESET + "\n");
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %,d\n", "Candidate Integer", candidate));
        String classStr = isPrime ? (ConsoleUtil.BRIGHT_GREEN + "PRIME" + ConsoleUtil.RESET)
                : (ConsoleUtil.RED + "COMPOSITE" + ConsoleUtil.RESET);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Classification", classStr));
        String modeStr = isDeterministic ? (ConsoleUtil.BRIGHT_CYAN + "DETERMINISTIC (100% Guaranteed)" + ConsoleUtil.RESET)
                : (ConsoleUtil.YELLOW + "PROBABILISTIC (Monte Carlo)" + ConsoleUtil.RESET);
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Guarantee Mode", modeStr));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %d rounds\n", "Witness Rounds", rounds));
        sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Tested Bases", witnesses.toString()));
        if (!isDeterministic && isPrime) {
            String errStr = String.format("<= %.2e (4^-%d)", errorProbabilityBound, rounds);
            sb.append(String.format("  " + ConsoleUtil.BOLD + "%-18s" + ConsoleUtil.RESET + " : %s\n", "Error Probability", errStr));
        }
        return sb.toString();
    }
}
