package smartcity.services;

import smartcity.algorithms.graph.CriticalPathMethod;
import smartcity.algorithms.graph.Dinic;
import smartcity.algorithms.graph.EdmondsKarp;
import smartcity.algorithms.graph.MinCut;
import smartcity.model.TrafficEdge;
import smartcity.model.TrafficNode;
import smartcity.repository.ProjectRepository;
import smartcity.repository.TrafficRepository;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Transportation Analytics and Project Scheduling
 * using M4 (Network Flow, Dinic, Min-Cut) and CPM.
 */
public class TrafficAnalyticsService {
    private final TrafficRepository trafficRepo;
    private final ProjectRepository projectRepo;

    public static class MaxFlowResult {
        public final int maxFlow;
        public final DynamicArray<TrafficEdge> bottleneckEdges;

        public MaxFlowResult(int maxFlow, DynamicArray<TrafficEdge> bottleneckEdges) {
            this.maxFlow = maxFlow;
            this.bottleneckEdges = bottleneckEdges != null ? bottleneckEdges : new DynamicArray<>();
        }
    }

    public static class ScheduleResult {
        public final int makespan;
        public final DynamicArray<String> criticalPath;

        public ScheduleResult(int makespan, DynamicArray<String> criticalPath) {
            this.makespan = makespan;
            this.criticalPath = criticalPath != null ? criticalPath : new DynamicArray<>();
        }
    }

    public TrafficAnalyticsService(TrafficRepository trafficRepo, ProjectRepository projectRepo) {
        this.trafficRepo = trafficRepo;
        this.projectRepo = projectRepo;
    }

    public void displayNetworkOverview() {
        ConsoleUtil.printSection("Metropolitan Traffic Grid Overview");
        System.out.printf("Junctions (Vertices) : %d loaded%n", trafficRepo.getNodeCount());
        System.out.printf("Arterial Roads (Edges): %d loaded%n%n", trafficRepo.getEdgeCount());

        int nShow = Math.min(6, trafficRepo.getNodeCount());
        System.out.println("Sample Intersections:");
        for (int i = 0; i < nShow; i++) {
            TrafficNode node = trafficRepo.getNode(i);
            System.out.println("  " + node.toString());
        }

        int eShow = Math.min(8, trafficRepo.getEdgeCount());
        System.out.println("\nSample Road Links:");
        for (int i = 0; i < eShow; i++) {
            TrafficEdge edge = trafficRepo.getEdge(i);
            System.out.println("  " + edge.toString());
        }
    }

    /**
     * Calculates maximum vehicle throughput between two junctions using Dinic's Algorithm.
     */
    public void computeMaxFlow(int source, int sink) {
        int n = trafficRepo.getNodeCount();
        if (source < 0 || source >= n || sink < 0 || sink >= n || source == sink) {
            ConsoleUtil.printError(String.format("Invalid source (%d) or sink (%d). Must be distinct between 0 and %d.", source, sink, n - 1));
            return;
        }

        ConsoleUtil.printSection("Traffic Maximum Flow Analysis: Junction [" + source + "] -> Junction [" + sink + "]");

        CustomFlowNetwork netDinic = trafficRepo.buildFlowNetwork();
        long start = System.nanoTime();
        Dinic dinic = new Dinic(netDinic, source, sink);
        int maxFlowDinic = (int) dinic.value();
        long elapsedDinic = System.nanoTime() - start;

        // Verify with Edmonds-Karp
        CustomFlowNetwork netEK = trafficRepo.buildFlowNetwork();
        long startEK = System.nanoTime();
        EdmondsKarp ek = new EdmondsKarp(netEK, source, sink);
        int maxFlowEK = (int) ek.value();
        long elapsedEK = System.nanoTime() - startEK;

        System.out.printf("  Maximum Arterial Throughput : %s%,d vehicles / hour%s%n",
                ConsoleUtil.BRIGHT_GREEN, maxFlowDinic, ConsoleUtil.RESET);
        System.out.printf("  Dinic Execution Time        : %,d ns (%.4f ms)%n",
                elapsedDinic, elapsedDinic / 1_000_000.0);
        System.out.printf("  Edmonds-Karp Time           : %,d ns (%.4f ms)%n",
                elapsedEK, elapsedEK / 1_000_000.0);
        System.out.printf("  Dual Verification           : %s%n",
                (maxFlowDinic == maxFlowEK ? ConsoleUtil.BRIGHT_GREEN + "100% MATCH" + ConsoleUtil.RESET : ConsoleUtil.RED + "DISCREPANCY" + ConsoleUtil.RESET));

        ConsoleUtil.printSuccess("Maximum network flow computed successfully.");
    }

    public MaxFlowResult computeMaxThroughput(String sourceId, String targetId) {
        int s = trafficRepo.findNodeIndexById(sourceId);
        int t = trafficRepo.findNodeIndexById(targetId);
        if (s == -1) s = 0;
        if (t == -1) t = Math.min(9, trafficRepo.getNodeCount() - 1);
        if (s == t) t = (s + 1) % trafficRepo.getNodeCount();

        CustomFlowNetwork net = trafficRepo.buildFlowNetwork();
        Dinic dinic = new Dinic(net, s, t);
        int flow = (int) dinic.value();

        MinCut.CutAnalysis cut = MinCut.computeMinCut(net, s, t);
        DynamicArray<TrafficEdge> bottleneck = new DynamicArray<>();
        for (int i = 0; i < trafficRepo.getEdgeCount(); i++) {
            TrafficEdge e = trafficRepo.getEdge(i);
            if (e.getSource() < cut.sourcePartition.length && e.getTarget() < cut.sourcePartition.length) {
                if (cut.sourcePartition[e.getSource()] && !cut.sourcePartition[e.getTarget()]) {
                    bottleneck.add(e);
                }
            }
        }
        return new MaxFlowResult(flow, bottleneck);
    }

    /**
     * Identifies bottleneck roads using Max-Flow Min-Cut theorem.
     */
    public void identifyBottleneckMinCut(int source, int sink) {
        int n = trafficRepo.getNodeCount();
        if (source < 0 || source >= n || sink < 0 || sink >= n || source == sink) {
            ConsoleUtil.printError("Invalid source or sink.");
            return;
        }

        ConsoleUtil.printSection("Critical Infrastructure Bottleneck Min-Cut (Junction " + source + " -> " + sink + ")");
        CustomFlowNetwork net = trafficRepo.buildFlowNetwork();
        MinCut.CutAnalysis cut = MinCut.computeMinCut(net, source, sink);

        System.out.printf("  Total Bottleneck Cut Capacity : %s%,.0f vehicles/hr%s%n",
                ConsoleUtil.BRIGHT_YELLOW, cut.cutCapacity, ConsoleUtil.RESET);
        System.out.printf("  Max-Flow / Min-Cut Duality    : %s%n",
                cut.dualityVerified ? ConsoleUtil.BRIGHT_GREEN + "VERIFIED EQUALITY" + ConsoleUtil.RESET : "MISMATCH");

        System.out.println("\nCritical Arterial Roads Crossing the Min-Cut (Vulnerable Links):");
        int count = 0;
        for (int i = 0; i < cut.cutEdges.size(); i++) {
            CustomFlowEdge e = cut.cutEdges.get(i);
            count++;
            System.out.printf("  ★ Critical Road #%d: Junction [%d] -> Junction [%d] (Capacity: %,.0f veh/hr)%n",
                    count, e.from(), e.to(), e.capacity());
        }
        ConsoleUtil.printSuccess(String.format("Identified %d critical arterial bottleneck road(s).", count));
    }

    /**
     * Schedules municipal roadwork engineering project tasks using Critical Path Method (CPM).
     */
    public void scheduleProjectTasks() {
        ConsoleUtil.printSection("Municipal Roadwork Project Scheduling (CPM)");
        if (projectRepo.getTaskCount() == 0) {
            ConsoleUtil.printWarning("No project tasks loaded.");
            return;
        }

        CriticalPathMethod.Task[] tasks = projectRepo.buildCPMTasks();
        smartcity.model.ScheduleResult result = CriticalPathMethod.scheduleProject(tasks);

        System.out.println(result.formatOutput());
        ConsoleUtil.printSuccess(String.format("Critical Path Method completed for %d municipal tasks. Total makespan: %d days.",
                tasks.length, result.getMakespan()));
    }

    public ScheduleResult computeCriticalPathSchedule() {
        if (projectRepo.getTaskCount() == 0) {
            return new ScheduleResult(0, new DynamicArray<>());
        }
        CriticalPathMethod.Task[] tasks = projectRepo.buildCPMTasks();
        smartcity.model.ScheduleResult result = CriticalPathMethod.scheduleProject(tasks);
        return new ScheduleResult(result.getMakespan(), result.getCriticalPath());
    }
}
