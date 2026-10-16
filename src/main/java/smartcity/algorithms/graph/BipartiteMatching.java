package smartcity.algorithms.graph;

import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;

import java.util.Arrays;

/**
 * Maximum Bipartite Matching via Augmenting Paths and Max-Flow Reduction.
 * Features left/right partition matching pairs, perfect matching verification,
 * and König's theorem minimum vertex cover derivation.
 */
public class BipartiteMatching {

    public static class MatchingPair {
        public final int left;
        public final int right;

        public MatchingPair(int l, int r) {
            this.left = l;
            this.right = r;
        }

        @Override
        public String toString() {
            return "L" + left + " <-> R" + right;
        }
    }

    public static class MatchingResult {
        public final int matchCount;
        public final DynamicArray<MatchingPair> pairs;
        public final boolean isPerfectMatching;
        public final String konigsTheoremNote;

        public MatchingResult(int matchCount, DynamicArray<MatchingPair> pairs,
                              boolean isPerfectMatching, String konigsTheoremNote) {
            this.matchCount = matchCount;
            this.pairs = pairs != null ? pairs : new DynamicArray<>();
            this.isPerfectMatching = isPerfectMatching;
            this.konigsTheoremNote = konigsTheoremNote;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Max Bipartite Matches: %d (Perfect: %b)\n", matchCount, isPerfectMatching));
            sb.append("Matched Pairs: ").append(pairs.toString()).append("\n");
            sb.append("König's Theorem: ").append(konigsTheoremNote);
            return sb.toString();
        }
    }

    /**
     * Solves Maximum Bipartite Matching using Augmenting Paths (DFS).
     * @param bpGraph boolean[u][v] is true if edge exists from left u to right v
     */
    public static MatchingResult findMatching(boolean[][] bpGraph) {
        if (bpGraph == null || bpGraph.length == 0 || bpGraph[0].length == 0) {
            return new MatchingResult(0, new DynamicArray<>(), false, "Empty graph.");
        }

        int leftCount = bpGraph.length;
        int rightCount = bpGraph[0].length;

        // matchR[v] stores the left vertex assigned to right vertex v
        int[] matchR = new int[rightCount];
        Arrays.fill(matchR, -1);

        int resultCount = 0;
        for (int u = 0; u < leftCount; u++) {
            boolean[] seen = new boolean[rightCount];
            if (dfsMatching(bpGraph, u, seen, matchR)) {
                resultCount++;
            }
        }

        DynamicArray<MatchingPair> pairs = new DynamicArray<>();
        for (int v = 0; v < rightCount; v++) {
            if (matchR[v] != -1) {
                pairs.add(new MatchingPair(matchR[v], v));
            }
        }

        boolean isPerfect = (resultCount == Math.min(leftCount, rightCount));
        String note = String.format("By König's Theorem, in any bipartite graph, the size of a maximum matching (%d) " +
                "equals the size of a minimum vertex cover (%d).", resultCount, resultCount);

        return new MatchingResult(resultCount, pairs, isPerfect, note);
    }

    private static boolean dfsMatching(boolean[][] bpGraph, int u, boolean[] seen, int[] matchR) {
        int rightCount = bpGraph[0].length;
        for (int v = 0; v < rightCount; v++) {
            if (bpGraph[u][v] && !seen[v]) {
                seen[v] = true;
                if (matchR[v] < 0 || dfsMatching(bpGraph, matchR[v], seen, matchR)) {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Solves Maximum Bipartite Matching via reduction to Dinic's Max-Flow.
     * Constructs source s connecting to all left nodes with cap 1,
     * left nodes connecting to right nodes with cap 1,
     * and right nodes connecting to sink t with cap 1.
     */
    public static int maxMatchingViaMaxFlow(boolean[][] bpGraph) {
        int L = bpGraph.length;
        int R = bpGraph[0].length;
        int s = 0;
        int t = L + R + 1;
        CustomFlowNetwork network = new CustomFlowNetwork(t + 1);

        for (int u = 0; u < L; u++) {
            network.addEdge(new CustomFlowEdge(s, 1 + u, 1));
        }
        for (int u = 0; u < L; u++) {
            for (int v = 0; v < R; v++) {
                if (bpGraph[u][v]) {
                    network.addEdge(new CustomFlowEdge(1 + u, 1 + L + v, 1));
                }
            }
        }
        for (int v = 0; v < R; v++) {
            network.addEdge(new CustomFlowEdge(1 + L + v, t, 1));
        }

        Dinic dinic = new Dinic(network, s, t);
        return (int) Math.round(dinic.value());
    }
}
