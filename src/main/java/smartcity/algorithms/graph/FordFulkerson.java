package smartcity.algorithms.graph;

import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;

/**
 * Ford-Fulkerson Maximum Flow Algorithm using DFS for augmenting paths.
 * Time Complexity: O(E * f) where f is maximum flow value.
 * Space Complexity: O(V).
 */
public class FordFulkerson {
    private boolean[] marked;
    private CustomFlowEdge[] edgeTo;
    private double value;
    private int augmentations;

    public FordFulkerson(CustomFlowNetwork G, int s, int t) {
        G.validateVertex(s);
        G.validateVertex(t);
        if (s == t) throw new IllegalArgumentException("Source and sink must be distinct");

        value = 0.0;
        augmentations = 0;

        while (hasAugmentingPath(G, s, t)) {
            // Find bottleneck capacity
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }

            // Augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle);
            }

            value += bottle;
            augmentations++;
        }
    }

    private boolean hasAugmentingPath(CustomFlowNetwork G, int s, int t) {
        edgeTo = new CustomFlowEdge[G.V()];
        marked = new boolean[G.V()];
        return dfs(G, s, t);
    }

    private boolean dfs(CustomFlowNetwork G, int v, int t) {
        marked[v] = true;
        if (v == t) return true;

        for (CustomFlowEdge e : G.adj(v)) {
            int w = e.other(v);
            if (!marked[w] && e.residualCapacityTo(w) > 0) {
                edgeTo[w] = e;
                if (dfs(G, w, t)) return true;
            }
        }
        return false;
    }

    public double value() { return value; }
    public int getAugmentations() { return augmentations; }
    public boolean inCut(int v) { return marked[v]; }
}
