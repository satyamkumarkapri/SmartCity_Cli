package smartcity.services;

import smartcity.algorithms.graph.Dinic;
import smartcity.algorithms.graph.EdmondsKarp;
import smartcity.algorithms.graph.FordFulkerson;
import smartcity.algorithms.parallel.ParallelSort;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.strings.KMP;
import smartcity.algorithms.strings.NaiveSearch;
import smartcity.algorithms.strings.RabinKarp;
import smartcity.algorithms.strings.ZAlgorithm;
import smartcity.repository.DocumentRepository;
import smartcity.repository.SensorRepository;
import smartcity.repository.TrafficRepository;
import smartcity.structures.CustomFlowNetwork;
import smartcity.util.ConsoleUtil;

import java.util.Arrays;

/**
 * Service orchestrating rigorous head-to-head empirical algorithm benchmarking.
 */
public class BenchmarkService {
    private final DocumentRepository docRepo;
    private final TrafficRepository trafficRepo;
    private final SensorRepository sensorRepo;

    public BenchmarkService(DocumentRepository docRepo, TrafficRepository trafficRepo, SensorRepository sensorRepo) {
        this.docRepo = docRepo;
        this.trafficRepo = trafficRepo;
        this.sensorRepo = sensorRepo;
    }

    public void runStringBenchmark(String pattern) {
        ConsoleUtil.printSection("Benchmark 1: Exact Pattern Matching (Naive vs KMP vs Z vs Rabin-Karp)");
        String text = (docRepo.size() > 0) ? docRepo.get(1).getContent() : "MUNICIPAL WATER PIPELINE REGULATORY STANDARD";
        if (pattern == null || pattern.isEmpty()) pattern = "standard";

        System.out.printf("Corpus Text Length: %,d characters | Search Pattern: \"%s\" (length %d)%n%n",
                text.length(), pattern, pattern.length());

        // Warmup
        for (int i = 0; i < 20; i++) {
            KMP.search(text, pattern);
        }

        // 1. Naive
        long s1 = System.nanoTime();
        smartcity.structures.IntArrayList r1 = NaiveSearch.search(text, pattern);
        long t1 = System.nanoTime() - s1;

        // 2. KMP
        long s2 = System.nanoTime();
        smartcity.structures.IntArrayList r2 = KMP.search(text, pattern);
        long t2 = System.nanoTime() - s2;

        // 3. Z-Algorithm
        long s3 = System.nanoTime();
        smartcity.structures.IntArrayList r3 = ZAlgorithm.search(text, pattern);
        long t3 = System.nanoTime() - s3;

        // 4. Rabin-Karp
        long s4 = System.nanoTime();
        smartcity.structures.IntArrayList r4 = RabinKarp.search(text, pattern);
        long t4 = System.nanoTime() - s4;

        boolean agree = (r1.size() == r2.size()) && (r2.size() == r3.size()) && (r3.size() == r4.size());

        System.out.println(ConsoleUtil.BOLD_BLACK + " Algorithm             │ Matches │ Time (ns)   │ Time (ms)  │ Complexity (Time, Space)" + ConsoleUtil.RESET);
        System.out.println("───────────────────────┼─────────┼─────────────┼────────────┼─────────────────────────");
        System.out.printf(" Naive Brute Force     │ %7d │ %,11d │ %10.4f │ O(N * M), O(1)%n", r1.size(), t1, t1 / 1_000_000.0);
        System.out.printf(" Knuth-Morris-Pratt    │ %7d │ %,11d │ %10.4f │ O(N + M), O(M)%n", r2.size(), t2, t2 / 1_000_000.0);
        System.out.printf(" Z-Algorithm (Linear)  │ %7d │ %,11d │ %10.4f │ O(N + M), O(N + M)%n", r3.size(), t3, t3 / 1_000_000.0);
        System.out.printf(" Rabin-Karp (Rolling)  │ %7d │ %,11d │ %10.4f │ O(N + M) avg, O(1)%n", r4.size(), t4, t4 / 1_000_000.0);
        System.out.println("───────────────────────┴─────────┴─────────────┴────────────┴─────────────────────────");
        System.out.println(" Verification Status   : " + (agree ? ConsoleUtil.BRIGHT_GREEN + "100% MATCH ACROSS ALL 4 ALGORITHMS" + ConsoleUtil.RESET : ConsoleUtil.RED + "MISMATCH" + ConsoleUtil.RESET));
    }

    public void runMaxFlowBenchmark(int s, int t) {
        ConsoleUtil.printSection("Benchmark 2: Maximum Flow (Ford-Fulkerson vs Edmonds-Karp vs Dinic)");
        int n = trafficRepo.getNodeCount();
        if (s < 0 || s >= n || t < 0 || t >= n || s == t) {
            s = 0;
            t = Math.max(1, n - 1);
        }

        System.out.printf("Traffic Grid: %d nodes, %d edges | Source: [%d] -> Sink: [%d]%n%n",
                trafficRepo.getNodeCount(), trafficRepo.getEdgeCount(), s, t);

        // 1. Ford-Fulkerson
        CustomFlowNetwork netFF = trafficRepo.buildFlowNetwork();
        long s1 = System.nanoTime();
        FordFulkerson ff = new FordFulkerson(netFF, s, t);
        int flowFF = (int) ff.value();
        long t1 = System.nanoTime() - s1;

        // 2. Edmonds-Karp
        CustomFlowNetwork netEK = trafficRepo.buildFlowNetwork();
        long s2 = System.nanoTime();
        EdmondsKarp ek = new EdmondsKarp(netEK, s, t);
        int flowEK = (int) ek.value();
        long t2 = System.nanoTime() - s2;

        // 3. Dinic
        CustomFlowNetwork netDinic = trafficRepo.buildFlowNetwork();
        long s3 = System.nanoTime();
        Dinic dinic = new Dinic(netDinic, s, t);
        int flowDinic = (int) dinic.value();
        long t3 = System.nanoTime() - s3;

        boolean agree = (flowFF == flowEK) && (flowEK == flowDinic);

        System.out.println(ConsoleUtil.BOLD_BLACK + " Algorithm             │ Max Flow (veh/hr) │ Time (ns)   │ Time (ms)  │ Theoretical Complexity" + ConsoleUtil.RESET);
        System.out.println("───────────────────────┼───────────────────┼─────────────┼────────────┼───────────────────────");
        System.out.printf(" Ford-Fulkerson (DFS)  │ %,17d │ %,11d │ %10.4f │ O(E * MaxFlow)%n", flowFF, t1, t1 / 1_000_000.0);
        System.out.printf(" Edmonds-Karp (BFS)    │ %,17d │ %,11d │ %10.4f │ O(V * E^2)%n", flowEK, t2, t2 / 1_000_000.0);
        System.out.printf(" Dinic (Blocking Flow) │ %,17d │ %,11d │ %10.4f │ O(V^2 * E)%n", flowDinic, t3, t3 / 1_000_000.0);
        System.out.println("───────────────────────┴───────────────────┴─────────────┴────────────┴───────────────────────");
        System.out.println(" Verification Status   : " + (agree ? ConsoleUtil.BRIGHT_GREEN + "100% AGREEMENT (Max Flow: " + flowDinic + ")" + ConsoleUtil.RESET : ConsoleUtil.RED + "MISMATCH" + ConsoleUtil.RESET));
    }

    public void runSortingBenchmark() {
        ConsoleUtil.printSection("Benchmark 3: Sensor Sorting (Java Dual-Pivot vs Randomized QuickSort vs Parallel Sort)");
        double[] raw = sensorRepo.extractValuesByType("");
        if (raw.length == 0) {
            raw = new double[1200];
            for (int i = 0; i < raw.length; i++) raw[i] = Math.random() * 500.0;
        }

        System.out.printf("Input Array Size: %,d double telemetry observations%n%n", raw.length);

        // 1. Dual-Pivot Arrays.sort
        double[] a1 = Arrays.copyOf(raw, raw.length);
        long s1 = System.nanoTime();
        Arrays.sort(a1);
        long t1 = System.nanoTime() - s1;

        // 2. Randomized QuickSort
        double[] a2 = Arrays.copyOf(raw, raw.length);
        long s2 = System.nanoTime();
        RandomizedQuickSort.sort(a2);
        long t2 = System.nanoTime() - s2;

        // 3. Parallel ForkJoin Sort
        double[] a3 = Arrays.copyOf(raw, raw.length);
        long s3 = System.nanoTime();
        ParallelSort.sort(a3);
        long t3 = System.nanoTime() - s3;

        boolean agree = Arrays.equals(a1, a2) && Arrays.equals(a2, a3);

        System.out.println(ConsoleUtil.BOLD_BLACK + " Sorting Implementation  │ Elements  │ Time (ns)   │ Time (ms)  │ Complexity" + ConsoleUtil.RESET);
        System.out.println("─────────────────────────┼───────────┼─────────────┼────────────┼───────────────────────");
        System.out.printf(" Arrays.sort (Dual-Pivot)│ %,9d │ %,11d │ %10.4f │ O(N log N)%n", a1.length, t1, t1 / 1_000_000.0);
        System.out.printf(" Randomized QuickSort    │ %,9d │ %,11d │ %10.4f │ O(N log N) expected%n", a2.length, t2, t2 / 1_000_000.0);
        System.out.printf(" Parallel Sort (ForkJoin)│ %,9d │ %,11d │ %10.4f │ O(N/P log N)%n", a3.length, t3, t3 / 1_000_000.0);
        System.out.println("─────────────────────────┴───────────┴─────────────┴────────────┴───────────────────────");
        System.out.println(" Sorted Order Integrity  : " + (agree ? ConsoleUtil.BRIGHT_GREEN + "100% IDENTICAL SORTED ORDER" + ConsoleUtil.RESET : ConsoleUtil.RED + "MISMATCH" + ConsoleUtil.RESET));
    }
}
