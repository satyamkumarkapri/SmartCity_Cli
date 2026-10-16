package smartcity.algorithms.dynamicprogramming;

import smartcity.exceptions.InvalidInputException;
import smartcity.structures.IntArrayList;

/**
 * Exact Hamiltonian Path detection and reconstruction via Bitmask Dynamic Programming.
 * Time Complexity: O(n^2 * 2^n).
 * Space Complexity: O(n * 2^n).
 */
public class HamiltonianPath {
    public static final int MAX_N = 20;

    public static class PathResult {
        public final boolean exists;
        public final IntArrayList path;

        public PathResult(boolean exists, IntArrayList path) {
            this.exists = exists;
            this.path = path != null ? path : new IntArrayList();
        }

        @Override
        public String toString() {
            if (!exists) return "No Hamiltonian Path exists.";
            StringBuilder sb = new StringBuilder("Hamiltonian Path: ");
            for (int i = 0; i < path.size(); i++) {
                sb.append(path.get(i));
                if (i < path.size() - 1) sb.append(" -> ");
            }
            return sb.toString();
        }
    }

    public static PathResult findPath(boolean[][] adj) {
        return findHamiltonianPath(adj);
    }

    public static PathResult findHamiltonianPath(boolean[][] adj) {
        if (adj == null || adj.length == 0) return new PathResult(false, new IntArrayList());
        int n = adj.length;
        if (n == 1) {
            IntArrayList p = new IntArrayList();
            p.add(0);
            return new PathResult(true, p);
        }
        if (n > MAX_N) {
            throw new InvalidInputException("Input size n = " + n + " exceeds safety threshold " + MAX_N);
        }

        int numStates = 1 << n;
        boolean[][] dp = new boolean[numStates][n];
        int[][] parent = new int[numStates][n];

        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = true;
            parent[1 << i][i] = -1;
        }

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if (!dp[mask][u]) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && adj[u][v]) {
                        int nextMask = mask | (1 << v);
                        dp[nextMask][v] = true;
                        parent[nextMask][v] = u;
                    }
                }
            }
        }

        int fullMask = numStates - 1;
        int endNode = -1;
        for (int u = 0; u < n; u++) {
            if (dp[fullMask][u]) {
                endNode = u;
                break;
            }
        }

        if (endNode == -1) return new PathResult(false, new IntArrayList());

        IntArrayList path = new IntArrayList();
        int currMask = fullMask;
        int curr = endNode;
        while (curr != -1) {
            path.add(0, curr);
            int prev = parent[currMask][curr];
            currMask ^= (1 << curr);
            curr = prev;
        }

        return new PathResult(true, path);
    }
}
