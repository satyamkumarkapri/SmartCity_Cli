package smartcity.algorithms.graph;

import smartcity.structures.CircularQueue;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;

/**
 * Edmonds-Karp Maximum Flow Algorithm using BFS for shortest augmenting paths.
 * Time Complexity: O(V * E^2).
 * Space Complexity: O(V).
 */
public class EdmondsKarp {
    private boolean[] marked;
    private CustomFlowEdge[] edgeTo;
    private double value;
    private int augmentations;

    public EdmondsKarp(CustomFlowNetwork G, int s, int t) {
        G.validateVertex(s);
        G.validateVertex(t);
        if (s == t) throw new IllegalArgumentException("Source and sink must be distinct");

        value = 0.0;
        augmentations = 0;

        while (hasAugmentingPath(G, s, t)) {
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }

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

        CircularQueue<Integer> queue = new CircularQueue<>();
        queue.enqueue(s);
        marked[s] = true;

        while (!queue.isEmpty() && !marked[t]) {
            int v = queue.dequeue();
            for (CustomFlowEdge e : G.adj(v)) {
                int w = e.other(v);
                if (e.residualCapacityTo(w) > 0 && !marked[w]) {
                    edgeTo[w] = e;
                    marked[w] = true;
                    queue.enqueue(w);
                }
            }
        }
        return marked[t];
    }

    public double value() { return value; }
    public int getAugmentations() { return augmentations; }
    public boolean inCut(int v) { return marked[v]; }
}
