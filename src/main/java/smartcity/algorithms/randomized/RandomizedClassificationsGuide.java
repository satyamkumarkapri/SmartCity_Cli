package smartcity.algorithms.randomized;

/**
 * Educational guide explaining the fundamental difference between
 * Las Vegas and Monte Carlo randomized algorithms.
 */
public class RandomizedClassificationsGuide {

    public static String getClassificationOverview() {
        return """
        ================================================================================
         LAS VEGAS vs. MONTE CARLO RANDOMIZED ALGORITHMS
        ================================================================================
        1. Las Vegas Algorithms:
           - Correctness : Deterministically 100% correct answer on every run.
           - Runtime     : Running time is a random variable dependent on coin flips.
           - Example     : Randomized QuickSort.
             Always produces a correctly sorted array; expected time is O(n log n),
             worst-case time is O(n^2) with negligible probability (<= 1/2^n).

        2. Monte Carlo Algorithms:
           - Runtime     : Deterministic, strictly bounded time complexity.
           - Correctness : Small, bounded probability of returning an incorrect answer.
           - Amplification: Running k independent trials reduces error probability
             exponentially (e.g., error <= (1/4)^k).
           - Example     : Miller-Rabin Primality Test.
             If it reports 'Composite', the number is guaranteed composite (witness found).
             If it reports 'Prime', the probability of error is at most (1/4)^k.
        ================================================================================
        """;
    }
}
