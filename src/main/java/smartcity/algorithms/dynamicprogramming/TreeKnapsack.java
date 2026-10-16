package smartcity.algorithms.dynamicprogramming;

import smartcity.structures.CustomGraph;
import smartcity.structures.IntArrayList;

/**
 * Tree Knapsack / Tree-Constrained Subset DP.
 * Selects an optimal subset of nodes in a rooted tree respecting tree precedence
 * (a node can be chosen only if its parent is chosen) maximizing total benefit within capacity.
 * Time Complexity: O(N * W^2).
 */
public class TreeKnapsack {

    public static class KnapsackResult {
        public final int maxBenefit;
        public final int[] subtreeSizes;

        public KnapsackResult(int maxBenefit, int[] subtreeSizes) {
            this.maxBenefit = maxBenefit;
            this.subtreeSizes = subtreeSizes;
        }

        @Override
        public String toString() {
            return String.format("Maximum Subtree Benefit: %,d", maxBenefit);
        }
    }

    /**
     * Solves Tree Knapsack.
     * @param tree Directed tree where edges go from parent to child
     * @param weights Weights/costs of each node
     * @param values Benefits/values of each node
     * @param capacity Maximum weight budget
     */
    public static KnapsackResult solveTreeKnapsack(CustomGraph tree, int[] weights, int[] values, int capacity) {
        int n = tree.V();
        if (n == 0 || capacity <= 0) return new KnapsackResult(0, new int[0]);

        // dp[u][w] = max value in subtree u with weight w
        int[][] dp = new int[n][capacity + 1];
        int[] sz = new int[n];

        dfs(tree, 0, -1, weights, values, capacity, dp, sz);

        int best = 0;
        for (int w = 0; w <= capacity; w++) {
            if (dp[0][w] > best) best = dp[0][w];
        }

        return new KnapsackResult(best, sz);
    }

    private static void dfs(CustomGraph tree, int u, int p, int[] weights, int[] values, int capacity,
                            int[][] dp, int[] sz) {
        sz[u] = weights[u];
        if (weights[u] <= capacity) {
            dp[u][weights[u]] = values[u];
        }

        IntArrayList adj = tree.adj(u);
        for (int i = 0; i < adj.size(); i++) {
            int v = adj.get(i);
            if (v != p) {
                dfs(tree, v, u, weights, values, capacity, dp, sz);
                sz[u] += sz[v];

                // Merge child v's knapsack into parent u (0/1 knapsack style backwards)
                for (int w = capacity; w >= weights[u]; w--) {
                    for (int subW = 0; subW <= w - weights[u]; subW++) {
                        if (dp[u][w - subW] > 0 && dp[v][subW] > 0) {
                            dp[u][w] = Math.max(dp[u][w], dp[u][w - subW] + dp[v][subW]);
                        }
                    }
                }
            }
        }
    }
}
