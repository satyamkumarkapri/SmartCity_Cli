package smartcityx.services;

import smartcityx.algorithms.flow.*;
import smartcityx.util.ComplexityUtil;
import java.util.Scanner;

public class NetworkService {
    public void runNetworkFlow() {
        System.out.println("Simulating Water Distribution Network Flow\n");
        
        int s = 0, t = 5;

        ComplexityUtil.display("Ford-Fulkerson", "O(E * f)", "O(E * f)", "O(E * f)", "O(V)");
        FlowNetwork G = buildGraph();
        long start = System.nanoTime();
        FordFulkerson ff = new FordFulkerson(G, s, t);
        long end = System.nanoTime();
        System.out.println("Ford-Fulkerson Max Flow: " + ff.value() + " | Time: " + (end - start) + " ns\n");

        ComplexityUtil.display("Edmonds-Karp", "O(V * E^2)", "O(V * E^2)", "O(V * E^2)", "O(V)");
        G = buildGraph();
        start = System.nanoTime();
        EdmondsKarp ek = new EdmondsKarp(G, s, t);
        end = System.nanoTime();
        System.out.println("Edmonds-Karp Max Flow: " + ek.value() + " | Time: " + (end - start) + " ns\n");

        ComplexityUtil.display("Dinic's Algorithm", "O(V^2 * E)", "O(V^2 * E)", "O(V^2 * E)", "O(V)");
        G = buildGraph();
        start = System.nanoTime();
        Dinic dinic = new Dinic(G);
        double flow = dinic.maxFlow(s, t);
        end = System.nanoTime();
        System.out.println("Dinic Max Flow: " + flow + " | Time: " + (end - start) + " ns\n");
    }

    private FlowNetwork buildGraph() {
        FlowNetwork G = new FlowNetwork(6);
        G.addEdge(new FlowEdge(0, 1, 16));
        G.addEdge(new FlowEdge(0, 2, 13));
        G.addEdge(new FlowEdge(1, 2, 10));
        G.addEdge(new FlowEdge(1, 3, 12));
        G.addEdge(new FlowEdge(2, 1, 4));
        G.addEdge(new FlowEdge(2, 4, 14));
        G.addEdge(new FlowEdge(3, 2, 9));
        G.addEdge(new FlowEdge(3, 5, 20));
        G.addEdge(new FlowEdge(4, 3, 7));
        G.addEdge(new FlowEdge(4, 5, 4));
        return G;
    }

    public void runBipartiteMatching() {
        System.out.println("Bipartite Matching: Emergency Teams to Incidents\n");
        ComplexityUtil.display("Bipartite Matching (DFS based)", "O(V * E)", "O(V * E)", "O(V * E)", "O(V)");
        
        boolean[][] bpGraph = new boolean[][]{
            {true, true, false},
            {false, true, true},
            {true, false, false}
        };

        long start = System.nanoTime();
        int maxMatches = BipartiteMatching.maxMatching(bpGraph);
        long end = System.nanoTime();

        System.out.println("Maximum Assignments: " + maxMatches);
        System.out.println("Execution Time: " + (end - start) + " ns");
    }
}
