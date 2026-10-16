package smartcity.algorithms.graph;

import smartcity.structures.CircularQueue;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;

import java.util.Arrays;

/**
 * Minimum-Cost Maximum-Flow via Successive Shortest Augmenting Paths with SPFA.
 * Handles negative residual costs induced by flow cancellations on reverse edges.
 * Terminates when no augmenting path exists from source to sink.
 */
public class MinCostMaxFlow {

    public static class FlowCostResult {
        public final double flow;
        public final double cost;

        public FlowCostResult(double flow, double cost) {
            this.flow = flow;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("Max Flow: %.2f | Min Total Cost: %.2f", flow, cost);
        }
    }

    public static FlowCostResult computeMinCostMaxFlow(CustomFlowNetwork G, int s, int t) {
        int n = G.V();
        double totalFlow = 0;
        double totalCost = 0;

        double[] dist = new double[n];
        CustomFlowEdge[] edgeTo = new CustomFlowEdge[n];
        boolean[] inQueue = new boolean[n];

        while (true) {
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(inQueue, false);
            Arrays.fill(edgeTo, null);

            CircularQueue<Integer> q = new CircularQueue<>();
            dist[s] = 0;
            q.enqueue(s);
            inQueue[s] = true;

            // SPFA (Shortest Path Faster Algorithm) to handle negative residual costs
            while (!q.isEmpty()) {
                int u = q.dequeue();
                inQueue[u] = false;

                DynamicArray<CustomFlowEdge> edges = G.adj(u);
                for (int i = 0; i < edges.size(); i++) {
                    CustomFlowEdge e = edges.get(i);
                    int v = e.other(u);
                    double cap = e.residualCapacityTo(v);
                    double edgeCost = e.costTo(v);

                    if (cap > 1e-9 && dist[u] + edgeCost < dist[v] - 1e-9) {
                        dist[v] = dist[u] + edgeCost;
                        edgeTo[v] = e;
                        if (!inQueue[v]) {
                            q.enqueue(v);
                            inQueue[v] = true;
                        }
                    }
                }
            }

            if (dist[t] == Double.POSITIVE_INFINITY) {
                break; // No more augmenting paths
            }

            // Find bottleneck capacity along shortest path
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }

            // Augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle);
            }

            totalFlow += bottle;
            totalCost += bottle * dist[t];
        }

        return new FlowCostResult(totalFlow, totalCost);
    }
}
