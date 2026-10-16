package smartcity.algorithms.dynamicprogramming;

import smartcity.structures.CustomGraph;
import smartcity.structures.IntArrayList;

/**
 * Tree Rerooting Dynamic Programming.
 * Computes the sum of distances from EVERY vertex to all other vertices in a tree in O(N) total time.
 * Pass 1: Bottom-up DP computing subtree size and subtree distance sum.
 * Pass 2: Top-down rerooting transfer:
 *         ans[child] = ans[parent] - count[child] + (N - count[child]).
 */
public class TreeRerooting {

    public static class RerootResult {
        public final long[] sumDistances;
        public final int bestCentroidNode;
        public final long minDistanceSum;

        public RerootResult(long[] sumDistances, int bestCentroidNode, long minDistanceSum) {
            this.sumDistances = sumDistances;
            this.bestCentroidNode = bestCentroidNode;
            this.minDistanceSum = minDistanceSum;
        }

        @Override
        public String toString() {
            return String.format("Tree Centroid (Optimal Root): Node %d with minimal total distance sum %,d",
                    bestCentroidNode, minDistanceSum);
        }
    }

    public static RerootResult solveAllPairsDistances(CustomGraph tree) {
        int n = tree.V();
        if (n == 0) return new RerootResult(new long[0], -1, 0);
        if (n == 1) return new RerootResult(new long[]{0}, 0, 0);

        int[] count = new int[n];
        long[] inDist = new long[n];
        long[] ans = new long[n];

        // Pass 1: Bottom-up DFS rooted at node 0
        dfsPostOrder(tree, 0, -1, count, inDist);

        // ans for node 0 is exactly inDist[0]
        ans[0] = inDist[0];

        // Pass 2: Top-down Rerooting DFS
        dfsReroot(tree, 0, -1, count, ans, n);

        int bestNode = 0;
        long minSum = ans[0];
        for (int i = 1; i < n; i++) {
            if (ans[i] < minSum) {
                minSum = ans[i];
                bestNode = i;
            }
        }

        return new RerootResult(ans, bestNode, minSum);
    }

    private static void dfsPostOrder(CustomGraph tree, int u, int p, int[] count, long[] inDist) {
        count[u] = 1;
        inDist[u] = 0;

        IntArrayList adj = tree.adj(u);
        for (int i = 0; i < adj.size(); i++) {
            int v = adj.get(i);
            if (v != p) {
                dfsPostOrder(tree, v, u, count, inDist);
                count[u] += count[v];
                inDist[u] += inDist[v] + count[v];
            }
        }
    }

    private static void dfsReroot(CustomGraph tree, int u, int p, int[] count, long[] ans, int n) {
        IntArrayList adj = tree.adj(u);
        for (int i = 0; i < adj.size(); i++) {
            int v = adj.get(i);
            if (v != p) {
                // Reroot transfer equation:
                ans[v] = ans[u] - count[v] + (n - count[v]);
                dfsReroot(tree, v, u, count, ans, n);
            }
        }
    }
}
