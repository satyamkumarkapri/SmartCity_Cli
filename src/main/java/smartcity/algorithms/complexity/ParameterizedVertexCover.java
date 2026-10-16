package smartcity.algorithms.complexity;

import smartcity.structures.IntArrayList;

/**
 * Fixed-Parameter Tractable (FPT) Bounded Branching Solver for Parameterized Vertex Cover.
 * Determines if a graph G = (V, E) possesses a vertex cover of size at most k.
 * Time Complexity: O(2^k * E).
 * Terminates immediately whenever the parameter budget k is exhausted.
 */
public class ParameterizedVertexCover {

    public static class FPTResult {
        public final boolean found;
        public final int targetK;
        public final IntArrayList vertexCover;
        public final long branchesExplored;

        public FPTResult(boolean found, int targetK, IntArrayList cover, long branches) {
            this.found = found;
            this.targetK = targetK;
            this.vertexCover = cover != null ? cover : new IntArrayList();
            this.branchesExplored = branches;
        }

        @Override
        public String toString() {
            if (!found) {
                return String.format("No vertex cover of size <= %d exists (Budget exhausted after %d branch steps).",
                        targetK, branchesExplored);
            }
            return String.format("Found valid Vertex Cover of size %d (Target k=%d) in %d branch steps:\nCover: %s",
                    vertexCover.size(), targetK, branchesExplored, vertexCover.toString());
        }
    }

    public static FPTResult solve(boolean[][] graph, int k) {
        if (graph == null || graph.length == 0) {
            return new FPTResult(true, k, new IntArrayList(), 1);
        }
        int n = graph.length;
        boolean[][] edges = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, edges[i], 0, n);
        }

        long[] branches = new long[]{0};
        IntArrayList currentCover = new IntArrayList();
        boolean success = branch(edges, n, k, currentCover, branches);

        return new FPTResult(success, k, currentCover, branches[0]);
    }

    private static boolean branch(boolean[][] edges, int n, int k, IntArrayList cover, long[] branches) {
        branches[0]++;

        // Find first uncovered edge (u, v)
        int uEdge = -1, vEdge = -1;
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (edges[u][v]) {
                    uEdge = u;
                    vEdge = v;
                    break;
                }
            }
            if (uEdge != -1) break;
        }

        // Base case 1: All edges are covered
        if (uEdge == -1) {
            return true;
        }

        // Base case 2: Uncovered edges remain but budget k is exhausted
        if (k <= 0) {
            return false;
        }

        // Branch 1: Include u in cover
        cover.add(uEdge);
        boolean[] removedEdgesU = removeIncidentEdges(edges, n, uEdge);
        if (branch(edges, n, k - 1, cover, branches)) {
            return true;
        }
        // Backtrack u
        restoreIncidentEdges(edges, n, uEdge, removedEdgesU);
        cover.remove(cover.size() - 1);

        // Branch 2: Include v in cover
        cover.add(vEdge);
        boolean[] removedEdgesV = removeIncidentEdges(edges, n, vEdge);
        if (branch(edges, n, k - 1, cover, branches)) {
            return true;
        }
        // Backtrack v
        restoreIncidentEdges(edges, n, vEdge, removedEdgesV);
        cover.remove(cover.size() - 1);

        return false;
    }

    private static boolean[] removeIncidentEdges(boolean[][] edges, int n, int vertex) {
        boolean[] removed = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (edges[vertex][i]) {
                removed[i] = true;
                edges[vertex][i] = false;
                edges[i][vertex] = false;
            }
        }
        return removed;
    }

    private static void restoreIncidentEdges(boolean[][] edges, int n, int vertex, boolean[] removed) {
        for (int i = 0; i < n; i++) {
            if (removed[i]) {
                edges[vertex][i] = true;
                edges[i][vertex] = true;
            }
        }
    }
}
