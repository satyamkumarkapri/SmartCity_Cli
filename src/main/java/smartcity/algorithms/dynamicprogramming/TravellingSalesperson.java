package smartcity.algorithms.dynamicprogramming;

import smartcity.exceptions.InvalidInputException;
import smartcity.structures.IntArrayList;

/**
 * Travelling Salesperson Problem (TSP) via Held-Karp Bitmask Dynamic Programming.
 * Time Complexity: O(n^2 * 2^n).
 * Space Complexity: O(n * 2^n).
 * Enforces strict safety boundary check n <= 20 to prevent heap exhaustion.
 */
public class TravellingSalesperson {
    public static final int MAX_N = 20;
    public static final double INF = 1e9;

    public static class TSPResult {
        public final double minCost;
        public final IntArrayList tour;
        public final boolean tourExists;

        public TSPResult(double minCost, IntArrayList tour, boolean exists) {
            this.minCost = minCost;
            this.tour = tour != null ? tour : new IntArrayList();
            this.tourExists = exists;
        }

        @Override
        public String toString() {
            if (!tourExists) return "No valid TSP tour exists (graph is disconnected).";
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Minimum Tour Cost: %.2f\nTour Sequence: ", minCost));
            for (int i = 0; i < tour.size(); i++) {
                sb.append(tour.get(i));
                if (i < tour.size() - 1) sb.append(" -> ");
            }
            return sb.toString();
        }
    }

    public static TSPResult solve(double[][] dist) {
        return solveTSP(dist);
    }

    public static TSPResult solve(int[][] dist) {
        if (dist == null) return new TSPResult(0, new IntArrayList(), false);
        double[][] d = new double[dist.length][dist.length];
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) d[i][j] = dist[i][j];
        }
        return solveTSP(d);
    }

    /**
     * Finds the minimum cost Hamiltonian Cycle starting and ending at vertex 0.
     * @param dist n x n cost matrix. dist[i][j] >= INF means no edge.
     */
    public static TSPResult solveTSP(double[][] dist) {
        if (dist == null || dist.length == 0) {
            return new TSPResult(0, new IntArrayList(), false);
        }
        int n = dist.length;
        if (n == 1) {
            IntArrayList single = new IntArrayList();
            single.add(0);
            single.add(0);
            return new TSPResult(0, single, true);
        }
        if (n > MAX_N) {
            throw new InvalidInputException(
                    "TSP input size n = " + n + " exceeds safety threshold MAX_N = " + MAX_N +
                    " (Requires " + (n * (1 << n)) + " states).");
        }

        int numStates = 1 << n;
        double[][] dp = new double[numStates][n];
        int[][] parent = new int[numStates][n];

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = INF;
                parent[i][j] = -1;
            }
        }

        // Base case: starting at vertex 0, only vertex 0 visited
        dp[1][0] = 0.0;

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] >= INF) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && dist[u][v] < INF) {
                        int nextMask = mask | (1 << v);
                        double newCost = dp[mask][u] + dist[u][v];
                        if (newCost < dp[nextMask][v]) {
                            dp[nextMask][v] = newCost;
                            parent[nextMask][v] = u;
                        }
                    }
                }
            }
        }

        // Return to start vertex 0
        int fullMask = numStates - 1;
        double minTotalCost = INF;
        int lastNode = -1;

        for (int u = 1; u < n; u++) {
            if (dp[fullMask][u] < INF && dist[u][0] < INF) {
                double total = dp[fullMask][u] + dist[u][0];
                if (total < minTotalCost) {
                    minTotalCost = total;
                    lastNode = u;
                }
            }
        }

        if (lastNode == -1 || minTotalCost >= INF) {
            return new TSPResult(INF, new IntArrayList(), false);
        }

        // Tour reconstruction
        IntArrayList tour = new IntArrayList();
        tour.add(0); // return to start
        int currMask = fullMask;
        int currNode = lastNode;

        while (currNode != -1) {
            tour.add(0, currNode);
            int prev = parent[currMask][currNode];
            currMask ^= (1 << currNode);
            currNode = prev;
        }

        return new TSPResult(minTotalCost, tour, true);
    }
}
