package smartcity.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.algorithms.graph.*;
import smartcity.model.ScheduleResult;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module 4: Network Flow, Scheduling & Graph Algorithms Tests")
public class NetworkFlowAndGraphTest {

    @Test
    @DisplayName("Max Flow & Min-Cut: Ford-Fulkerson, Edmonds-Karp, Dinic equivalence and cut duality")
    public void testMaxFlowAlgorithmsAndMinCutDuality() {
        // Standard network from CLRS textbook
        CustomFlowNetwork net = new CustomFlowNetwork(6);
        net.addEdge(new CustomFlowEdge(0, 1, 16));
        net.addEdge(new CustomFlowEdge(0, 2, 13));
        net.addEdge(new CustomFlowEdge(1, 2, 10));
        net.addEdge(new CustomFlowEdge(1, 3, 12));
        net.addEdge(new CustomFlowEdge(2, 1, 4));
        net.addEdge(new CustomFlowEdge(2, 4, 14));
        net.addEdge(new CustomFlowEdge(3, 2, 9));
        net.addEdge(new CustomFlowEdge(3, 5, 20));
        net.addEdge(new CustomFlowEdge(4, 3, 7));
        net.addEdge(new CustomFlowEdge(4, 5, 4));

        FordFulkerson ff = new FordFulkerson(net, 0, 5);
        assertEquals(23.0, ff.value(), 1e-6);

        // Reset network for Edmonds-Karp
        CustomFlowNetwork netEK = buildSampleNetwork();
        EdmondsKarp ek = new EdmondsKarp(netEK, 0, 5);
        assertEquals(23.0, ek.value(), 1e-6);

        // Reset network for Dinic
        CustomFlowNetwork netDinic = buildSampleNetwork();
        Dinic dinic = new Dinic(netDinic, 0, 5);
        assertEquals(23.0, dinic.value(), 1e-6);

        // Max-Flow Min-Cut Duality
        CustomFlowNetwork netCut = buildSampleNetwork();
        MinCut.CutAnalysis cut = MinCut.computeMinCut(netCut, 0, 5);
        assertEquals(23.0, cut.maxFlowValue, 1e-6);
        assertEquals(23.0, cut.cutCapacity, 1e-6);
        assertTrue(cut.dualityVerified);
        assertTrue(cut.cutEdges.size() > 0);
    }

    private CustomFlowNetwork buildSampleNetwork() {
        CustomFlowNetwork net = new CustomFlowNetwork(6);
        net.addEdge(new CustomFlowEdge(0, 1, 16));
        net.addEdge(new CustomFlowEdge(0, 2, 13));
        net.addEdge(new CustomFlowEdge(1, 2, 10));
        net.addEdge(new CustomFlowEdge(1, 3, 12));
        net.addEdge(new CustomFlowEdge(2, 1, 4));
        net.addEdge(new CustomFlowEdge(2, 4, 14));
        net.addEdge(new CustomFlowEdge(3, 2, 9));
        net.addEdge(new CustomFlowEdge(3, 5, 20));
        net.addEdge(new CustomFlowEdge(4, 3, 7));
        net.addEdge(new CustomFlowEdge(4, 5, 4));
        return net;
    }

    @Test
    @DisplayName("Flow Network: Disconnected network and zero capacity edges")
    public void testDisconnectedFlowNetwork() {
        CustomFlowNetwork net = new CustomFlowNetwork(4);
        net.addEdge(new CustomFlowEdge(0, 1, 10));
        net.addEdge(new CustomFlowEdge(2, 3, 10)); // No path from 0 to 3

        Dinic dinic = new Dinic(net, 0, 3);
        assertEquals(0.0, dinic.value(), 1e-6);

        // Zero capacity edge
        CustomFlowNetwork zeroNet = new CustomFlowNetwork(2);
        zeroNet.addEdge(new CustomFlowEdge(0, 1, 0.0));
        Dinic zeroDinic = new Dinic(zeroNet, 0, 1);
        assertEquals(0.0, zeroDinic.value(), 1e-6);
    }

    @Test
    @DisplayName("Bipartite Matching: Augmenting Paths and Max-Flow Reduction")
    public void testBipartiteMatching() {
        boolean[][] bp = {
                {true, true, false},
                {false, true, true},
                {true, false, false}
        };

        BipartiteMatching.MatchingResult res = BipartiteMatching.findMatching(bp);
        assertEquals(3, res.matchCount);
        assertTrue(res.isPerfectMatching);
        assertEquals(3, res.pairs.size());

        // Max-Flow reduction equivalence
        int flowMatchCount = BipartiteMatching.maxMatchingViaMaxFlow(bp);
        assertEquals(3, flowMatchCount);
    }

    @Test
    @DisplayName("Min-Cost Max-Flow: SPFA augmenting paths")
    public void testMinCostMaxFlow() {
        CustomFlowNetwork net = new CustomFlowNetwork(4);
        // Path 1: 0 -> 1 -> 3 (cap 2, cost 1 + 2 = 3)
        net.addEdge(new CustomFlowEdge(0, 1, 2, 1.0));
        net.addEdge(new CustomFlowEdge(1, 3, 2, 2.0));
        // Path 2: 0 -> 2 -> 3 (cap 3, cost 2 + 3 = 5)
        net.addEdge(new CustomFlowEdge(0, 2, 3, 2.0));
        net.addEdge(new CustomFlowEdge(2, 3, 3, 3.0));

        MinCostMaxFlow.FlowCostResult res = MinCostMaxFlow.computeMinCostMaxFlow(net, 0, 3);
        assertEquals(5.0, res.flow, 1e-6);
        // Cost: 2 units * 3 + 3 units * 5 = 6 + 15 = 21
        assertEquals(21.0, res.cost, 1e-6);
    }

    @Test
    @DisplayName("Critical Path Method (CPM) Project Scheduling")
    public void testCriticalPathMethod() {
        CriticalPathMethod.Task[] tasks = {
                new CriticalPathMethod.Task(0, "T0", 3, new int[]{}),
                new CriticalPathMethod.Task(1, "T1", 4, new int[]{0}),
                new CriticalPathMethod.Task(2, "T2", 2, new int[]{0}),
                new CriticalPathMethod.Task(3, "T3", 5, new int[]{1, 2})
        };

        ScheduleResult sched = CriticalPathMethod.scheduleProject(tasks);
        assertEquals(12, sched.getMakespan()); // 3 + 4 + 5 = 12
        assertTrue(sched.getCriticalPath().contains("T0"));
        assertTrue(sched.getCriticalPath().contains("T1"));
        assertTrue(sched.getCriticalPath().contains("T3"));
        assertFalse(sched.getCriticalPath().contains("T2")); // T2 has slack
    }

    @Test
    @DisplayName("Project Selection via Min-Cut")
    public void testProjectSelection() {
        double[] profits = {100, 200, -150};
        int[][] deps = {{0, 2}, {1, 2}}; // Both projects 0 and 1 require task 2

        ProjectSelectionFlow.ProjectResult res = ProjectSelectionFlow.solveProjectSelection(profits, deps);
        assertEquals(150.0, res.maxNetProfit, 1e-6); // 100 + 200 - 150 = 150
        assertTrue(res.selectedProjects.contains(0));
        assertTrue(res.selectedProjects.contains(1));
        assertTrue(res.selectedProjects.contains(2));
    }
}
