package smartcity.algorithms.graph;

import smartcity.structures.CircularQueue;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;

/**
 * Minimum Cut Computation & Max-Flow Min-Cut Duality Verification.
 * Traverses final residual graph to partition vertices into (S, T),
 * extracts all boundary cut edges, computes cut capacity, and confirms equality with max flow.
 */
public class MinCut {

    public static class CutAnalysis {
        public final boolean[] sourcePartition;
        public final DynamicArray<CustomFlowEdge> cutEdges;
        public final double cutCapacity;
        public final double maxFlowValue;
        public final boolean dualityVerified;

        public CutAnalysis(boolean[] sourcePartition, DynamicArray<CustomFlowEdge> cutEdges,
                           double cutCapacity, double maxFlowValue, boolean dualityVerified) {
            this.sourcePartition = sourcePartition;
            this.cutEdges = cutEdges != null ? cutEdges : new DynamicArray<>();
            this.cutCapacity = cutCapacity;
            this.maxFlowValue = maxFlowValue;
            this.dualityVerified = dualityVerified;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Max-Flow: %.2f | Min-Cut Capacity: %.2f (Equality: %b)\n",
                    maxFlowValue, cutCapacity, dualityVerified));
            sb.append("Min-Cut Edges (Edges from Source Partition S to Sink Partition T):\n");
            for (CustomFlowEdge e : cutEdges) {
                sb.append(String.format("   %d -> %d  (Capacity: %.2f, Flow: %.2f)\n",
                        e.from(), e.to(), e.capacity(), e.flow()));
            }
            return sb.toString();
        }
    }

    public static CutAnalysis computeMinCut(CustomFlowNetwork G, int s, int t) {
        // Check if flow was already computed on G
        double currentNetFlow = 0.0;
        for (CustomFlowEdge e : G.adj(s)) {
            currentNetFlow += (e.from() == s ? e.flow() : -e.flow());
        }

        double maxFlow;
        if (currentNetFlow > 1e-9) {
            maxFlow = currentNetFlow;
        } else {
            Dinic dinic = new Dinic(G, s, t);
            maxFlow = dinic.value();
        }

        // Find all vertices reachable from source s in the residual graph
        boolean[] inS = new boolean[G.V()];
        CircularQueue<Integer> q = new CircularQueue<>();
        q.enqueue(s);
        inS[s] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();
            DynamicArray<CustomFlowEdge> edges = G.adj(v);
            for (int i = 0; i < edges.size(); i++) {
                CustomFlowEdge e = edges.get(i);
                int w = e.other(v);
                if (!inS[w] && e.residualCapacityTo(w) > 1e-9) {
                    inS[w] = true;
                    q.enqueue(w);
                }
            }
        }

        // Identify cut edges: edges from u in S to v in T
        DynamicArray<CustomFlowEdge> cutEdges = new DynamicArray<>();
        double cutCap = 0;

        for (CustomFlowEdge e : G.edges()) {
            if (inS[e.from()] && !inS[e.to()]) {
                cutEdges.add(e);
                cutCap += e.capacity();
            }
        }

        boolean dualityHolds = Math.abs(cutCap - maxFlow) < 1e-5;

        return new CutAnalysis(inS, cutEdges, cutCap, maxFlow, dualityHolds);
    }
}
