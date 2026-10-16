package smartcity.algorithms.graph;

import smartcity.structures.CircularQueue;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;

import java.util.Arrays;

/**
 * Dinic's Algorithm for Maximum Network Flow using Level Graphs and Blocking Flows.
 * Features current-edge work pointer optimization (ptr array).
 * Time Complexity: O(V^2 * E) general networks, O(E * sqrt(V)) unit networks.
 * Space Complexity: O(V).
 */
public class Dinic {
    private final CustomFlowNetwork G;
    private final int s;
    private final int t;
    private int[] level;
    private int[] ptr;
    private double maxFlow;
    private int augmentations;

    public Dinic(CustomFlowNetwork G, int s, int t) {
        G.validateVertex(s);
        G.validateVertex(t);
        if (s == t) throw new IllegalArgumentException("Source and sink must be distinct");

        this.G = G;
        this.s = s;
        this.t = t;
        this.level = new int[G.V()];
        this.ptr = new int[G.V()];
        this.maxFlow = 0;
        this.augmentations = 0;

        compute();
    }

    private void compute() {
        while (bfs()) {
            Arrays.fill(ptr, 0); // Reset current-edge pointers for the new phase
            double pushed;
            while ((pushed = dfs(s, t, Double.POSITIVE_INFINITY)) > 0) {
                maxFlow += pushed;
                augmentations++;
            }
        }
    }

    private boolean bfs() {
        Arrays.fill(level, -1);
        level[s] = 0;

        CircularQueue<Integer> q = new CircularQueue<>();
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            DynamicArray<CustomFlowEdge> edges = G.adj(v);
            for (int i = 0; i < edges.size(); i++) {
                CustomFlowEdge e = edges.get(i);
                int w = e.other(v);
                if (e.residualCapacityTo(w) > 0 && level[w] == -1) {
                    level[w] = level[v] + 1;
                    q.enqueue(w);
                }
            }
        }
        return level[t] != -1;
    }

    private double dfs(int v, int t, double pushed) {
        if (pushed == 0 || v == t) return pushed;

        DynamicArray<CustomFlowEdge> edges = G.adj(v);
        for (; ptr[v] < edges.size(); ptr[v]++) {
            CustomFlowEdge e = edges.get(ptr[v]);
            int w = e.other(v);
            double cap = e.residualCapacityTo(w);

            if (level[w] == level[v] + 1 && cap > 0) {
                double tr = dfs(w, t, Math.min(pushed, cap));
                if (tr > 0) {
                    e.addResidualFlowTo(w, tr);
                    return tr;
                }
            }
        }
        return 0;
    }

    public double value() {
        return maxFlow;
    }

    public int getAugmentations() {
        return augmentations;
    }

    public boolean inCut(int v) {
        return level[v] != -1;
    }
}
