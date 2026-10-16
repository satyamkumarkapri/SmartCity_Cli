package smartcity.cli;

import smartcity.algorithms.complexity.KnapsackFPTAS;
import smartcity.algorithms.complexity.ParameterizedVertexCover;
import smartcity.algorithms.complexity.VertexCoverApproximation;
import smartcity.algorithms.dynamicprogramming.*;
import smartcity.algorithms.graph.*;
import smartcity.algorithms.parallel.BlellochScan;
import smartcity.algorithms.parallel.ParallelReduce;
import smartcity.algorithms.randomized.MillerRabin;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.randomized.ReservoirSampling;
import smartcity.algorithms.strings.*;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.algorithms.suffix.SuffixAutomaton;
import smartcity.structures.*;
import smartcity.util.ConsoleUtil;

import java.util.Random;

/**
 * Built-in self-test suite runnable directly from the CLI or automated tests.
 * Validates data structures and algorithms across all 6 modules.
 */
public class SelfTestRunner {
    private int passed = 0;
    private int failed = 0;

    public void runAllTests() {
        ConsoleUtil.printHeader("RUNNING SMARTCITYX AUTOMATED SELF-TEST SUITE");
        long t0 = System.nanoTime();
        passed = 0;
        failed = 0;

        testDataStructures();
        testStringAlgorithms();
        testDynamicProgramming();
        testNetworkFlowAndGraph();
        testApproximationAndComplexity();
        testRandomizedAndParallel();
        testSmartCityWorkflows();

        long elapsedMs = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("--------------------------------------------------------------------------------");
        if (failed == 0) {
            ConsoleUtil.printSuccess(String.format("ALL %d TESTS PASSED SUCCESSFULLY in %d ms!", passed, elapsedMs));
        } else {
            ConsoleUtil.printError(String.format("COMPLETED WITH %d FAILURES (%d passed, %d failed).", failed, passed, failed));
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void check(String name, boolean condition) {
        if (condition) {
            System.out.printf("  " + ConsoleUtil.BRIGHT_GREEN + "✔ [PASS]" + ConsoleUtil.RESET + " %-55s%n", name);
            passed++;
        } else {
            System.out.printf("  " + ConsoleUtil.RED + "✖ [FAIL]" + ConsoleUtil.RESET + " %-55s%n", name);
            failed++;
        }
    }

    private void testDataStructures() {
        ConsoleUtil.printSection("1. Foundational Data Structures");

        // DynamicArray & IntArrayList
        DynamicArray<String> arr = new DynamicArray<>();
        arr.add("Alpha"); arr.add("Beta"); arr.add("Gamma");
        check("DynamicArray Add & Get", arr.size() == 3 && "Beta".equals(arr.get(1)));
        arr.remove(1);
        check("DynamicArray Remove", arr.size() == 2 && "Gamma".equals(arr.get(1)));

        IntArrayList iarr = new IntArrayList();
        for (int i = 0; i < 50; i++) iarr.add(i * 2);
        check("IntArrayList Capacity Expansion", iarr.size() == 50 && iarr.get(25) == 50);

        // CustomStack & CircularQueue & CustomDeque
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(10); stack.push(20);
        check("CustomStack LIFO", stack.pop() == 20 && stack.peek() == 10);

        CircularQueue<String> queue = new CircularQueue<>(4);
        queue.enqueue("A"); queue.enqueue("B"); queue.enqueue("C");
        check("CircularQueue FIFO", "A".equals(queue.dequeue()) && "B".equals(queue.peek()));

        CustomDeque<Integer> deque = new CustomDeque<>();
        deque.addFirst(1); deque.addLast(2);
        check("CustomDeque", deque.pollFirst() == 1 && deque.pollLast() == 2);

        // CustomHashMap & CustomHashSet
        CustomHashMap<String, Integer> map = new CustomHashMap<>();
        map.put("one", 1); map.put("two", 2);
        check("CustomHashMap Put/Get", map.get("one") == 1 && map.containsKey("two"));
        map.put("one", 100);
        check("CustomHashMap Overwrite", map.get("one") == 100 && map.size() == 2);

        CustomHashSet<String> set = new CustomHashSet<>();
        set.add("X"); set.add("Y"); set.add("X");
        check("CustomHashSet Uniqueness", set.size() == 2 && set.contains("X"));

        // CustomPriorityQueue
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        pq.insert(50); pq.insert(10); pq.insert(30);
        check("CustomPriorityQueue Min-Heap", pq.poll() == 10 && pq.poll() == 30);

        // DisjointSetUnion
        DisjointSetUnion dsu = new DisjointSetUnion(5);
        dsu.union(0, 1); dsu.union(1, 2);
        check("DisjointSetUnion Connected", dsu.connected(0, 2) && !dsu.connected(0, 3));
    }

    private void testStringAlgorithms() {
        ConsoleUtil.printSection("2. String & Suffix Algorithms");
        String text = "abracadabra";
        String pat = "abra";

        IntArrayList naive = NaiveSearch.search(text, pat);
        check("Naive String Search", naive.size() == 2 && naive.get(0) == 0 && naive.get(1) == 7);

        IntArrayList kmp = KMP.search(text, pat);
        check("Knuth-Morris-Pratt Matches", kmp.size() == 2 && kmp.get(0) == 0 && kmp.get(1) == 7);

        IntArrayList z = ZAlgorithm.search(text, pat);
        check("Z-Algorithm String Matches", z.size() == 2 && z.get(0) == 0 && z.get(1) == 7);

        IntArrayList rk = RabinKarp.search(text, pat);
        check("Rabin-Karp Rolling Hash", rk.size() == 2 && rk.get(0) == 0 && rk.get(1) == 7);

        AhoCorasick ac = new AhoCorasick();
        ac.addPattern("abra");
        ac.addPattern("cad");
        ac.buildFailureLinks();
        CustomHashMap<String, IntArrayList> acRes = ac.search(text);
        check("Aho-Corasick Multi-Pattern", acRes.get("abra").size() == 2 && acRes.get("cad").size() == 1);

        int[] sa = SuffixArray.buildSuffixArray(text);
        int[] saLinear = SuffixArray.buildSAIS(text);
        int[] lcp = Kasai.buildLCP(text, sa);
        check("Suffix Array Prefix Doubling O(N log^2 N)", sa.length == text.length());
        check("Suffix Array SA-IS O(N) Construction", saLinear.length == text.length());
        check("Kasai LCP Computation", lcp.length == text.length());

        SuffixAutomaton dawg = new SuffixAutomaton(text);
        check("Suffix Automaton Substring Query", dawg.containsSubstring("cadab") && !dawg.containsSubstring("xyz"));
        check("Suffix Automaton Distinct Substrings", dawg.countDistinctSubstrings() > 0);
    }

    private void testDynamicProgramming() {
        ConsoleUtil.printSection("3. Advanced Dynamic Programming");

        check("Levenshtein Distance", Levenshtein.calculateDistance("kitten", "sitting") == 3);
        check("Damerau-Levenshtein OSA", DamerauLevenshtein.optimalStringAlignmentDistance("ca", "abc") == 3);
        check("Damerau-Levenshtein Unrestricted", DamerauLevenshtein.unrestrictedDistance("ca", "abc") == 2);

        SequenceAlignment.AlignmentResult nw = SequenceAlignment.needlemanWunsch("AGCT", "ATGCT", 1, 1, 1);
        check("Needleman-Wunsch Alignment", nw.score > 0);

        int[] dims = {10, 30, 5, 60};
        MatrixChainMultiplication.Result mc = MatrixChainMultiplication.multiplyOrder(dims);
        check("Matrix Chain Multiplication", mc.minCost == 4500);

        double[][] tspDist = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        TravellingSalesperson.TSPResult tsp = TravellingSalesperson.solveTSP(tspDist);
        check("Travelling Salesperson Bitmask DP", tsp.tourExists && tsp.minCost == 80.0);

        boolean[][] hpAdj = {
                {false, true, false},
                {true, false, true},
                {false, true, false}
        };
        HamiltonianPath.PathResult hp = HamiltonianPath.findHamiltonianPath(hpAdj);
        check("Hamiltonian Path Bitmask DP", hp.exists && hp.path.size() == 3);

        CustomGraph tree = new CustomGraph(5);
        tree.addEdge(0, 1); tree.addEdge(1, 2); tree.addEdge(2, 3); tree.addEdge(3, 4);
        TreeDiameter.DiameterResult dia = TreeDiameter.computeDiameter(tree);
        check("Tree Diameter DP", dia.diameter == 4);

        TreeRerooting.RerootResult reroot = TreeRerooting.solveAllPairsDistances(tree);
        check("Tree Rerooting DP", reroot.bestCentroidNode == 2 && reroot.sumDistances.length == 5);

        long[] sosBase = {1, 2, 3, 4}; // 2 bits
        long[] sosAns = SumOverSubsetsDP.computeSOS(sosBase, 2);
        check("Sum Over Subsets (SOS DP)", sosAns[3] == 1 + 2 + 3 + 4);
    }

    private void testNetworkFlowAndGraph() {
        ConsoleUtil.printSection("4. Network Flow & Project Scheduling");

        CustomFlowNetwork net = new CustomFlowNetwork(4);
        net.addEdge(new CustomFlowEdge(0, 1, 10));
        net.addEdge(new CustomFlowEdge(0, 2, 5));
        net.addEdge(new CustomFlowEdge(1, 2, 15));
        net.addEdge(new CustomFlowEdge(1, 3, 10));
        net.addEdge(new CustomFlowEdge(2, 3, 10));

        Dinic dinic = new Dinic(net, 0, 3);
        check("Dinic Maximum Flow", Math.abs(dinic.value() - 15.0) < 1e-6);

        MinCut.CutAnalysis cut = MinCut.computeMinCut(net, 0, 3);
        check("Max-Flow Min-Cut Duality Verification", cut.dualityVerified);

        boolean[][] bp = {
                {true, false},
                {true, true}
        };
        BipartiteMatching.MatchingResult match = BipartiteMatching.findMatching(bp);
        check("Maximum Bipartite Matching", match.matchCount == 2 && match.isPerfectMatching);

        CriticalPathMethod.Task[] tasks = {
                new CriticalPathMethod.Task(0, "A", 3, new int[]{}),
                new CriticalPathMethod.Task(1, "B", 4, new int[]{0}),
                new CriticalPathMethod.Task(2, "C", 2, new int[]{0}),
                new CriticalPathMethod.Task(3, "D", 5, new int[]{1, 2})
        };
        smartcity.model.ScheduleResult sched = CriticalPathMethod.scheduleProject(tasks);
        check("Critical Path Method (CPM)", sched.getMakespan() == 12);
    }

    private void testApproximationAndComplexity() {
        ConsoleUtil.printSection("5. Approximation & Complexity");

        boolean[][] g = {
                {false, true, true},
                {true, false, false},
                {true, false, false}
        };
        VertexCoverApproximation.ApproxVCResult vc = VertexCoverApproximation.findApproximateVertexCover(g);
        check("Vertex Cover 2-Approximation", vc.allEdgesCovered);

        int[] w = {2, 3, 4};
        int[] v = {3, 4, 5};
        KnapsackFPTAS.FPTASResult fptas = KnapsackFPTAS.solve(w, v, 5, 0.2);
        check("Knapsack FPTAS Approximation", fptas.totalValue >= 7);

        ParameterizedVertexCover.FPTResult fpt = ParameterizedVertexCover.solve(g, 2);
        check("Parameterized Vertex Cover (FPT)", fpt.found);
    }

    private void testRandomizedAndParallel() {
        ConsoleUtil.printSection("6. Randomized & Parallel Algorithms");

        int[] randArr = {5, 2, 9, 1, 5, 6};
        RandomizedQuickSort.sort(randArr);
        boolean sorted = true;
        for (int i = 0; i < randArr.length - 1; i++) {
            if (randArr[i] > randArr[i + 1]) sorted = false;
        }
        check("Randomized QuickSort (3-Way Partitioning)", sorted);

        check("Miller-Rabin Known Prime", MillerRabin.isPrime(1000000007L));
        check("Miller-Rabin Composite Identification", !MillerRabin.isPrime(1000000005L));
        check("Miller-Rabin Carmichael Number 561", !MillerRabin.isPrime(561));
        check("Miller-Rabin Carmichael Number 1729", !MillerRabin.isPrime(1729));

        DynamicArray<Integer> sample = ReservoirSampling.sample(arr(100), 5, new Random(42));
        check("Reservoir Sampling Stream", sample.size() == 5);

        double[] scanInput = {1, 2, 3, 4};
        double[] scanRes = BlellochScan.exclusiveScan(scanInput);
        check("Blelloch Scan Correctness", BlellochScan.verifyScan(scanRes, scanInput));

        double[] reduceInput = {10, 20, 30, 40};
        check("Parallel Reduce Sequential Reference", ParallelReduce.computeSequential(reduceInput, ParallelReduce.Op.SUM) == 100.0);
    }

    private void testSmartCityWorkflows() {
        ConsoleUtil.printSection("7. SmartCityX Integrated Workflows");

        // 1. CSV Parser
        String csvLine = "101,\"Official, Chief Officer\",\"Public Works\",\"Downtown Central\",\"Pothole repair, arterial road\",85,OPEN,12";
        String[] parsed = smartcity.util.CSVParser.parseLine(csvLine);
        check("RFC-4180 CSV Quoted Field Parsing", parsed.length == 8 && parsed[1].equals("Official, Chief Officer"));

        // 2. Traffic Flow Network (Dinic)
        smartcity.structures.CustomFlowNetwork trafficNet = new smartcity.structures.CustomFlowNetwork(4);
        trafficNet.addEdge(new smartcity.structures.CustomFlowEdge(0, 1, 2500));
        trafficNet.addEdge(new smartcity.structures.CustomFlowEdge(0, 2, 1800));
        trafficNet.addEdge(new smartcity.structures.CustomFlowEdge(1, 2, 1200));
        trafficNet.addEdge(new smartcity.structures.CustomFlowEdge(1, 3, 2000));
        trafficNet.addEdge(new smartcity.structures.CustomFlowEdge(2, 3, 1500));
        Dinic dinic = new Dinic(trafficNet, 0, 3);
        int maxFlow = (int) dinic.value();
        check("Traffic Grid Maximum Vehicle Flow (Dinic)", maxFlow == 3500);

        // 3. Emergency Bipartite Matching
        boolean[][] bp = {
                {true, false, false},
                {true, true, false},
                {false, true, true}
        };
        BipartiteMatching.MatchingResult match = BipartiteMatching.findMatching(bp);
        check("Emergency Squad Bipartite Matching", match.matchCount == 3);

        // 4. Suffix Automaton Document Substring
        SuffixAutomaton sam = SuffixAutomaton.build("MUNICIPAL WATER PIPELINE REGULATORY STANDARD");
        check("Civil Document Substring Automaton", sam.contains("PIPELINE") && !sam.contains("TRANSIT"));
    }

    private static Iterable<Integer> arr(int n) {
        DynamicArray<Integer> list = new DynamicArray<>(n);
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }
}
