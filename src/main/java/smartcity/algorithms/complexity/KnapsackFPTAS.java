package smartcity.algorithms.complexity;

import smartcity.exceptions.InvalidInputException;
import smartcity.structures.IntArrayList;

/**
 * Fully Polynomial-Time Approximation Scheme (FPTAS) for 0/1 Knapsack.
 * Guarantees a solution with value >= (1 - epsilon) * OPT in polynomial time O(n^3 / epsilon).
 * Scales item values by factor K = (epsilon * maxVal) / n, then solves value-based dynamic programming.
 */
public class KnapsackFPTAS {

    public static class FPTASResult {
        public final int totalWeight;
        public final int totalValue;
        public final IntArrayList selectedItems;
        public final double epsilon;
        public final double scalingFactorK;
        public final double theoreticalLowerBoundOpt;

        public FPTASResult(int totalWeight, int totalValue, IntArrayList items,
                           double epsilon, double K, double bound) {
            this.totalWeight = totalWeight;
            this.totalValue = totalValue;
            this.selectedItems = items != null ? items : new IntArrayList();
            this.epsilon = epsilon;
            this.scalingFactorK = K;
            this.theoreticalLowerBoundOpt = bound;
        }

        @Override
        public String toString() {
            return String.format("""
            Knapsack FPTAS Result (epsilon = %.2f):
               Total Weight     : %,d
               Total Real Value : %,d
               Selected Items   : %s
               Scaling Factor K : %.4f
               Approximation    : Guaranteed value >= (1 - %.2f) * OPT (>= %.1f%% of OPT)
            """, epsilon, totalWeight, totalValue, selectedItems.toString(), scalingFactorK, epsilon, (1.0 - epsilon) * 100.0);
        }
    }

    public static FPTASResult solve(int[] weights, int[] values, int capacity, double epsilon) {
        if (weights == null || values == null || weights.length == 0) {
            return new FPTASResult(0, 0, new IntArrayList(), epsilon, 1.0, 0);
        }
        if (epsilon <= 0 || epsilon > 1.0) {
            throw new InvalidInputException("Epsilon must satisfy 0 < epsilon <= 1.0 (received " + epsilon + ").");
        }

        int n = weights.length;
        int maxVal = 0;
        for (int v : values) {
            if (v > maxVal) maxVal = v;
        }

        if (maxVal == 0 || capacity <= 0) {
            return new FPTASResult(0, 0, new IntArrayList(), epsilon, 1.0, 0);
        }

        // K = (epsilon * maxVal) / n
        double K = Math.max(1.0, (epsilon * maxVal) / (double) n);
        int[] scaledVals = new int[n];
        int maxScaledVal = 0;
        for (int i = 0; i < n; i++) {
            scaledVals[i] = (int) Math.floor(values[i] / K);
            if (scaledVals[i] > maxScaledVal) maxScaledVal = scaledVals[i];
        }

        int maxPossibleScaledSum = n * maxScaledVal;
        int INF = 1_000_000_000;

        // dp[i][v] = minimum weight to achieve scaled value v using subset of items 0..i-1
        int[] dp = new int[maxPossibleScaledSum + 1];
        int[][] parent = new int[n + 1][maxPossibleScaledSum + 1];

        for (int v = 1; v <= maxPossibleScaledSum; v++) {
            dp[v] = INF;
        }
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            int w = weights[i];
            int sv = scaledVals[i];

            for (int v = maxPossibleScaledSum; v >= sv; v--) {
                if (dp[v - sv] != INF && dp[v - sv] + w < dp[v]) {
                    dp[v] = dp[v - sv] + w;
                    parent[i + 1][v] = 1; // item i included
                }
            }
        }

        // Find highest scaled value within capacity
        int bestScaledVal = 0;
        for (int v = maxPossibleScaledSum; v >= 0; v--) {
            if (dp[v] <= capacity) {
                bestScaledVal = v;
                break;
            }
        }

        // Traceback selected items
        IntArrayList chosen = new IntArrayList();
        int curV = bestScaledVal;
        int realWeight = 0;
        int realVal = 0;

        for (int i = n; i > 0; i--) {
            if (parent[i][curV] == 1) {
                chosen.add(0, i - 1);
                realWeight += weights[i - 1];
                realVal += values[i - 1];
                curV -= scaledVals[i - 1];
            }
        }

        double bound = (1.0 - epsilon) * realVal;
        return new FPTASResult(realWeight, realVal, chosen, epsilon, K, bound);
    }
}
