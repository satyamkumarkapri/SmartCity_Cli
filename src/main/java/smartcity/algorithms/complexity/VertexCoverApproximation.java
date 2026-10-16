package smartcity.algorithms.complexity;

import smartcity.structures.IntArrayList;

/**
 * Maximal-Matching 2-Approximation for Minimum Vertex Cover.
 * Time Complexity: O(V + E).
 * Guarantee: Size of returned cover |C| <= 2 * OPT.
 * Proof: Edges selected form a maximal matching M. Any valid vertex cover must include
 * at least one vertex from each disjoint edge in M (so OPT >= |M|).
 * The algorithm selects both endpoints of each edge in M, so |C| = 2|M| <= 2 * OPT.
 */
public class VertexCoverApproximation {

    public static class ApproxVCResult {
        public final IntArrayList vertexCover;
        public final int matchingSize;
        public final boolean allEdgesCovered;
        public final double approximationFactorUpper;

        public ApproxVCResult(IntArrayList cover, int matchingSize, boolean allCovered) {
            this.vertexCover = cover != null ? cover : new IntArrayList();
            this.matchingSize = matchingSize;
            this.allEdgesCovered = allCovered;
            this.approximationFactorUpper = 2.0;
        }

        @Override
        public String toString() {
            return String.format("Approximate Vertex Cover: %s (Size: %d)\n" +
                            "Maximal Matching Size: %d (Lower bound on OPT: >= %d)\n" +
                            "Every Edge Covered: %b | Approximation Factor: <= %.1fx OPT",
                    vertexCover.toString(), vertexCover.size(), matchingSize, matchingSize, allEdgesCovered, approximationFactorUpper);
        }
    }

    public static ApproxVCResult findApproximateVertexCover(boolean[][] graph) {
        if (graph == null || graph.length == 0) {
            return new ApproxVCResult(new IntArrayList(), 0, true);
        }
        int n = graph.length;
        boolean[] inCover = new boolean[n];
        boolean[][] edges = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, edges[i], 0, n);
        }

        IntArrayList cover = new IntArrayList();
        int matchingSize = 0;

        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (edges[u][v]) {
                    // Uncovered edge found: add BOTH endpoints to cover
                    cover.add(u);
                    cover.add(v);
                    inCover[u] = true;
                    inCover[v] = true;
                    matchingSize++;

                    // Mark all incident edges as covered
                    for (int i = 0; i < n; i++) {
                        edges[u][i] = false;
                        edges[i][u] = false;
                        edges[v][i] = false;
                        edges[i][v] = false;
                    }
                }
            }
        }

        // Verification: ensure every original edge has at least one endpoint in cover
        boolean allCovered = true;
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (graph[u][v] && !inCover[u] && !inCover[v]) {
                    allCovered = false;
                    break;
                }
            }
        }

        return new ApproxVCResult(cover, matchingSize, allCovered);
    }
}
