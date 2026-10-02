package smartcityx.algorithms.flow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Dinic {
    private int[] level;
    private int[] ptr;
    private FlowNetwork G;

    public Dinic(FlowNetwork G) {
        this.G = G;
    }

    public double maxFlow(int s, int t) {
        double flow = 0;
        level = new int[G.V()];
        ptr = new int[G.V()];

        while (bfs(s, t)) {
            Arrays.fill(ptr, 0);
            double pushed;
            while ((pushed = dfs(s, t, Double.POSITIVE_INFINITY)) != 0) {
                flow += pushed;
            }
        }
        return flow;
    }

    private boolean bfs(int s, int t) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);
                if (e.residualCapacityTo(w) > 0 && level[w] == -1) {
                    level[w] = level[v] + 1;
                    q.add(w);
                }
            }
        }
        return level[t] != -1;
    }

    private double dfs(int v, int t, double flow) {
        if (flow == 0 || v == t) return flow;
        int i = 0;
        for (FlowEdge e : G.adj(v)) {
            if (i < ptr[v]) { i++; continue; }
            int w = e.other(v);
            if (level[w] == level[v] + 1 && e.residualCapacityTo(w) > 0) {
                double pushed = dfs(w, t, Math.min(flow, e.residualCapacityTo(w)));
                if (pushed != 0) {
                    e.addResidualFlowTo(w, pushed);
                    return pushed;
                }
            }
            ptr[v]++;
            i++;
        }
        return 0;
    }
}
