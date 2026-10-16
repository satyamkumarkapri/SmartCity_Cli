package smartcity.model;

/**
 * Encapsulates performance, comparison, and theoretical complexity metrics
 * measured during algorithm execution.
 */
public class AlgorithmMetrics {
    private final long executionTimeNs;
    private final long comparisonCount;
    private final long collisionCount;
    private final long memoryEstimateBytes;
    private final String timeComplexity;
    private final String spaceComplexity;

    public AlgorithmMetrics(long executionTimeNs, long comparisonCount, long collisionCount, 
                            long memoryEstimateBytes, String timeComplexity, String spaceComplexity) {
        this.executionTimeNs = executionTimeNs;
        this.comparisonCount = comparisonCount;
        this.collisionCount = collisionCount;
        this.memoryEstimateBytes = memoryEstimateBytes;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }

    public AlgorithmMetrics(long executionTimeNs, String timeComplexity, String spaceComplexity) {
        this(executionTimeNs, -1, -1, -1, timeComplexity, spaceComplexity);
    }

    public long getExecutionTimeNs() { return executionTimeNs; }
    public double getExecutionTimeMs() { return executionTimeNs / 1_000_000.0; }
    public long getComparisonCount() { return comparisonCount; }
    public long getCollisionCount() { return collisionCount; }
    public long getMemoryEstimateBytes() { return memoryEstimateBytes; }
    public String getTimeComplexity() { return timeComplexity; }
    public String getSpaceComplexity() { return spaceComplexity; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Execution Time : %,d ns (%.4f ms)\n", executionTimeNs, getExecutionTimeMs()));
        if (comparisonCount >= 0) {
            sb.append(String.format("Comparisons    : %,d\n", comparisonCount));
        }
        if (collisionCount >= 0) {
            sb.append(String.format("Hash Collisions: %,d\n", collisionCount));
        }
        sb.append(String.format("Time Complexity: %s\n", timeComplexity));
        sb.append(String.format("Space Complex. : %s", spaceComplexity));
        return sb.toString();
    }
}
