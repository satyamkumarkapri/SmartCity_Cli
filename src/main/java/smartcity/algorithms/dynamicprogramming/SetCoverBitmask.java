package smartcity.algorithms.dynamicprogramming;

import smartcity.exceptions.InvalidInputException;
import smartcity.structures.IntArrayList;

/**
 * Exact Set Cover via Bitmask Dynamic Programming.
 * Time Complexity: O(k * 2^m) where m = universe size, k = number of subsets.
 * Strictly checks universe size m <= 20.
 */
public class SetCoverBitmask {
    public static final int MAX_UNIVERSE = 20;
    public static final int INF = 1_000_000_000;

    public static class SetCoverResult {
        public final boolean possible;
        public final int minCost;
        public final IntArrayList selectedSubsets;

        public SetCoverResult(boolean possible, int minCost, IntArrayList selectedSubsets) {
            this.possible = possible;
            this.minCost = minCost;
            this.selectedSubsets = selectedSubsets != null ? selectedSubsets : new IntArrayList();
        }

        @Override
        public String toString() {
            if (!possible) return "No valid set cover covers the entire universe.";
            return String.format("Set Cover Cost: %d | Selected Subsets: %s", minCost, selectedSubsets.toString());
        }
    }

    /**
     * Solves Exact Minimum-Weight Set Cover.
     * @param universeSize Number of elements in universe (0..universeSize-1)
     * @param subsetMasks Bitmasks of elements covered by each subset
     * @param subsetCosts Costs associated with choosing each subset (or null for unweighted min-subset cover)
     */
    public static SetCoverResult solveSetCover(int universeSize, int[] subsetMasks, int[] subsetCosts) {
        if (universeSize < 0) throw new IllegalArgumentException("Universe size cannot be negative");
        if (universeSize == 0) return new SetCoverResult(true, 0, new IntArrayList());
        if (universeSize > MAX_UNIVERSE) {
            throw new InvalidInputException("Universe size " + universeSize + " exceeds safety limit " + MAX_UNIVERSE);
        }

        int k = subsetMasks.length;
        int numStates = 1 << universeSize;
        int[] dp = new int[numStates];
        int[] parentMask = new int[numStates];
        int[] parentSubset = new int[numStates];

        for (int i = 0; i < numStates; i++) {
            dp[i] = INF;
            parentMask[i] = -1;
            parentSubset[i] = -1;
        }

        dp[0] = 0;

        for (int mask = 0; mask < numStates; mask++) {
            if (dp[mask] == INF) continue;

            for (int s = 0; s < k; s++) {
                int nextMask = mask | subsetMasks[s];
                int cost = (subsetCosts != null && s < subsetCosts.length) ? subsetCosts[s] : 1;
                if (dp[mask] + cost < dp[nextMask]) {
                    dp[nextMask] = dp[mask] + cost;
                    parentMask[nextMask] = mask;
                    parentSubset[nextMask] = s;
                }
            }
        }

        int targetMask = numStates - 1;
        if (dp[targetMask] >= INF) {
            return new SetCoverResult(false, INF, new IntArrayList());
        }

        IntArrayList chosen = new IntArrayList();
        int cur = targetMask;
        while (cur > 0) {
            int subIdx = parentSubset[cur];
            chosen.add(0, subIdx);
            cur = parentMask[cur];
        }

        return new SetCoverResult(true, dp[targetMask], chosen);
    }
}
