package smartcity.core;

import smartcity.algorithms.complexity.KnapsackFPTAS;
import smartcity.algorithms.complexity.ParameterizedVertexCover;
import smartcity.algorithms.complexity.VertexCoverApproximation;
import smartcity.algorithms.dynamicprogramming.*;
import smartcity.algorithms.graph.*;
import smartcity.algorithms.parallel.BlellochScan;
import smartcity.algorithms.parallel.ParallelReduce;
import smartcity.algorithms.randomized.MillerRabin;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.strings.*;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.algorithms.suffix.SuffixAutomaton;
import smartcity.model.*;
import smartcity.repository.CitationNetworkProvider;
import smartcity.repository.DocumentRepository;
import smartcity.repository.IndexRepository;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;

import java.util.Random;

/**
 * Core execution engine implementing the public QueryEngine API.
 *  */
public class SmartCityEngine implements QueryEngine {
    private final DocumentRepository docRepo;
    private final IndexRepository indexRepo;

    public SmartCityEngine(DocumentRepository docRepo, IndexRepository indexRepo) {
        this.docRepo = docRepo;
        this.indexRepo = indexRepo;
    }

    public DocumentRepository getDocumentRepository() { return docRepo; }
    public IndexRepository getIndexRepository() { return indexRepo; }

    @Override
    public QueryResult execute(QueryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("QueryRequest cannot be null");
        }

        switch (request.getQueryType()) {
            case EXACT_SEARCH:
                return executeExactSearch(request);
            case FUZZY_SEARCH:
                return executeFuzzySearch(request);
            case DOCUMENT_SIMILARITY:
                return executeDocumentSimilarity(request);
            case SUFFIX_ANALYSIS:
                return executeSuffixAnalysis(request);
            case CITATION_FLOW:
                return executeCitationFlow(request);
            case PROJECT_SCHEDULING:
                return executeProjectScheduling(request);
            case NP_APPROXIMATION:
                return executeNPApproximation(request);
            case RANDOMIZED_ALGORITHM:
                return executeRandomized(request);
            case PARALLEL_ALGORITHM:
                return executeParallel(request);
            case ALGORITHM_COMPARISON:
                return executeComparison(request);
            default:
                throw new UnsupportedOperationException("Unsupported QueryType: " + request.getQueryType());
        }
    }

    private QueryResult executeExactSearch(QueryRequest req) {
        String pattern = req.getPrimaryInput();
        String algo = req.getAlgorithmVariant().toUpperCase();
        long t0 = System.nanoTime();

        DynamicArray<MatchResult> allMatches = new DynamicArray<>();
        long totalComparisons = 0;
        long totalCollisions = 0;

        DynamicArray<Document> docs = docRepo.getAll();
        for (Document d : docs) {
            String text = d.getContent();
            IntArrayList positions;

            if ("KMP".equals(algo)) {
                KMP.SearchMetrics m = new KMP.SearchMetrics();
                positions = KMP.search(text, pattern, m);
                totalComparisons += m.comparisons;
            } else if ("Z_ALGORITHM".equals(algo) || "Z".equals(algo)) {
                ZAlgorithm.SearchMetrics m = new ZAlgorithm.SearchMetrics();
                positions = ZAlgorithm.search(text, pattern, m);
                totalComparisons += m.comparisons;
            } else if ("RABIN_KARP".equals(algo) || "RK".equals(algo)) {
                RabinKarp.SearchMetrics m = new RabinKarp.SearchMetrics();
                positions = RabinKarp.search(text, pattern, false, m);
                totalComparisons += m.comparisons;
                totalCollisions += m.collisions;
            } else if ("SUFFIX_ARRAY".equals(algo) || "SA".equals(algo)) {
                int[] sa = SuffixArray.buildSuffixArray(text);
                positions = SuffixArray.search(text, pattern, sa);
            } else {
                NaiveSearch.SearchMetrics m = new NaiveSearch.SearchMetrics();
                positions = NaiveSearch.search(text, pattern, m);
                totalComparisons += m.comparisons;
                algo = "Naive Search";
            }

            for (int i = 0; i < positions.size(); i++) {
                int pos = positions.get(i);
                String snippet = d.getSnippet(Math.max(0, pos - 20), Math.min(d.getLength() - pos + 20, 70));
                allMatches.add(new MatchResult(d.getId(), d.getTitle(), pos, pattern, snippet));
            }
        }

        long elapsedNs = System.nanoTime() - t0;
        AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, totalComparisons, totalCollisions, -1,
                "KMP".equals(algo) ? "O(N + M)" : "O(N * M)", "O(M)");

        return new StringSearchResult(true, algo, metrics,
                "Executed exact pattern match across " + docs.size() + " active corpus documents.",
                pattern, docs.size(), allMatches);
    }

    private QueryResult executeFuzzySearch(QueryRequest req) {
        String pattern = req.getPrimaryInput();
        int maxDistance = (int) req.getNumericParam1();
        if (maxDistance <= 0) maxDistance = 2;

        long t0 = System.nanoTime();
        DynamicArray<MatchResult> fuzzyMatches = new DynamicArray<>();
        DynamicArray<Document> docs = docRepo.getAll();

        for (Document d : docs) {
            String title = d.getTitle();
            int wholeDist = Levenshtein.calculateDistance(title.toLowerCase(), pattern.toLowerCase());
            if (wholeDist <= maxDistance) {
                fuzzyMatches.add(new MatchResult(d.getId(), title, 0, pattern, "Title Match (Edit Distance: " + wholeDist + ")"));
                continue;
            }

            // Word-level matching within title
            String[] titleWords = title.split("[\\s\\p{Punct}]+");
            boolean matched = false;
            for (String tw : titleWords) {
                if (tw.isEmpty()) continue;
                int wordDist = Levenshtein.calculateDistance(tw.toLowerCase(), pattern.toLowerCase());
                if (wordDist <= maxDistance) {
                    fuzzyMatches.add(new MatchResult(d.getId(), title, 0, pattern, "Title Word '" + tw + "' (Edit Distance: " + wordDist + ")"));
                    matched = true;
                    break;
                }
            }
            if (matched) continue;

            // Content word matching (first 100 tokens)
            String[] contentWords = d.getContent().split("[\\s\\p{Punct}]+");
            int wordLimit = Math.min(contentWords.length, 100);
            for (int i = 0; i < wordLimit; i++) {
                String cw = contentWords[i];
                if (cw.isEmpty() || Math.abs(cw.length() - pattern.length()) > maxDistance) continue;
                int cDist = Levenshtein.calculateDistance(cw.toLowerCase(), pattern.toLowerCase());
                if (cDist <= maxDistance) {
                    fuzzyMatches.add(new MatchResult(d.getId(), title, i, pattern, "Content Word '" + cw + "' (Edit Distance: " + cDist + ")"));
                    break;
                }
            }
        }

        long elapsedNs = System.nanoTime() - t0;
        AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, "O(V * L1 * L2)", "O(min(L1, L2))");

        return new StringSearchResult(true, "Levenshtein Fuzzy Match", metrics,
                "Fuzzy matching with maximum distance threshold = " + maxDistance,
                pattern, docs.size(), fuzzyMatches);
    }

    private QueryResult executeDocumentSimilarity(QueryRequest req) {
        int docId1 = (int) req.getNumericParam1();
        int docId2 = (int) req.getNumericParam2();

        Document d1 = docRepo.get(docId1);
        Document d2 = docRepo.get(docId2);

        if (d1 == null || d2 == null) {
            // Default to first two documents if available
            DynamicArray<Document> all = docRepo.getAll();
            if (all.size() >= 2) {
                d1 = all.get(0);
                d2 = all.get(1);
            } else {
                return new SimilarityResult(false, "Similarity", null,
                        "Need at least 2 documents in repository.", "N/A", "N/A", "N/A", 0, 0, "");
            }
        }

        String content1 = d1.getContent();
        String content2 = d2.getContent();
        String metric = req.getAlgorithmVariant();

        long t0 = System.nanoTime();
        double dist;
        double score;
        String details = "";

        if ("NEEDLEMAN_WUNSCH".equalsIgnoreCase(metric)) {
            // Global alignment on sample snippet (up to 200 chars for clean display)
            String s1 = content1.substring(0, Math.min(100, content1.length()));
            String s2 = content2.substring(0, Math.min(100, content2.length()));
            SequenceAlignment.AlignmentResult align = SequenceAlignment.needlemanWunsch(s1, s2, 2, 1, 1);
            dist = align.score;
            score = Math.max(0.0, (double) align.score / (2.0 * Math.max(s1.length(), s2.length())));
            details = "Aligned S1: " + align.alignedSeq1 + "\nAligned S2: " + align.alignedSeq2;
            metric = "Needleman-Wunsch Alignment";
        } else if ("SUBSTRING_LCP".equalsIgnoreCase(metric)) {
            // Suffix Array / LCS approach
            String combined = content1 + "\0" + content2;
            int[] sa = SuffixArray.buildSuffixArray(combined);
            int[] lcp = Kasai.buildLCP(combined, sa);
            int maxLcp = 0;
            int split = content1.length();
            int bestIdx = -1;
            for (int i = 0; i < lcp.length - 1; i++) {
                boolean fromDoc1 = (sa[i] < split);
                boolean nextFromDoc1 = (sa[i + 1] < split);
                if (fromDoc1 != nextFromDoc1 && lcp[i] > maxLcp) {
                    maxLcp = lcp[i];
                    bestIdx = sa[i];
                }
            }
            dist = maxLcp;
            score = Math.min(1.0, (double) maxLcp / Math.min(content1.length(), content2.length()));
            if (bestIdx >= 0) {
                details = "Longest Common Substring: \"" + combined.substring(bestIdx, bestIdx + maxLcp) + "\" (length: " + maxLcp + ")";
            }
            metric = "Suffix-Array / LCP Substring Match";
        } else {
            // Levenshtein on text prefix
            String s1 = content1.substring(0, Math.min(150, content1.length()));
            String s2 = content2.substring(0, Math.min(150, content2.length()));
            Levenshtein.AlignmentResult align = Levenshtein.alignWithReconstruction(s1, s2, 1, 1, 1);
            dist = align.totalDistance;
            int maxLen = Math.max(s1.length(), s2.length());
            score = maxLen > 0 ? (maxLen - dist) / maxLen : 1.0;
            details = "Aligned A: " + align.alignedSource + "\nAligned B: " + align.alignedTarget;
            metric = "Wagner-Fischer Levenshtein Distance";
        }

        long elapsedNs = System.nanoTime() - t0;
        AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, "O(N * M)", "O(min(N, M))");

        return new SimilarityResult(true, metric, metrics,
                "Compared document #" + d1.getId() + " against #" + d2.getId(),
                d1.getTitle(), d2.getTitle(), metric, score, dist, details);
    }

    private QueryResult executeSuffixAnalysis(QueryRequest req) {
        String text = req.getPrimaryInput();
        if (text == null || text.isEmpty()) {
            if (docRepo.size() > 0) {
                text = docRepo.getAll().get(0).getContent();
            } else {
                text = "ALGORITHM";
            }
        }

        String sample = text.substring(0, Math.min(100, text.length()));
        long t0 = System.nanoTime();

        int[] sa = SuffixArray.buildSuffixArray(sample);
        int[] lcp = Kasai.buildLCP(sample, sa);

        SuffixAutomaton automaton = new SuffixAutomaton(sample);
        long distinctSubstrings = automaton.countDistinctSubstrings();

        String queryPattern = req.getSecondaryInput();
        boolean found = false;
        if (queryPattern != null && !queryPattern.isEmpty()) {
            found = automaton.containsSubstring(queryPattern);
        }

        long elapsedNs = System.nanoTime() - t0;
        AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, "O(N log^2 N) SA / O(N) LCP / O(N) DAWG", "O(N)");

        return new SuffixResult(true, "Suffix Array & Suffix Automaton", metrics,
                "Suffix structures construct complete index of all substrings.",
                sample, sa, lcp, distinctSubstrings, found);
    }

    private QueryResult executeCitationFlow(QueryRequest req) {
        long t0 = System.nanoTime();
        CustomFlowNetwork network = CitationNetworkProvider.buildCitationFlowNetwork();
        CustomFlowNetwork cutNetwork = CitationNetworkProvider.buildCitationFlowNetwork();
        int s = 0;
        int t = 5;

        Dinic dinic = new Dinic(network, s, t);
        double maxFlow = dinic.value();
        MinCut.CutAnalysis cut = MinCut.computeMinCut(cutNetwork, s, t);

        // Also run MinCostMaxFlow
        CustomFlowNetwork costNetwork = CitationNetworkProvider.buildCitationFlowNetwork();
        MinCostMaxFlow.FlowCostResult costRes = MinCostMaxFlow.computeMinCostMaxFlow(costNetwork, s, t);

        long elapsedNs = System.nanoTime() - t0;
        AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, "O(V^2 * E)", "O(V + E)");

        return new FlowResult(true, "Dinic Maximum Flow & Min-Cut", metrics,
                "Citation flow models information bottleneck between early foundational papers and knowledge graph sink.",
                maxFlow, costRes.cost, s, t, dinic.getAugmentations(), cut.cutEdges, cut.cutCapacity, cut.dualityVerified);
    }

    private QueryResult executeProjectScheduling(QueryRequest req) {
        long t0 = System.nanoTime();

        CriticalPathMethod.Task[] tasks = new CriticalPathMethod.Task[]{
                new CriticalPathMethod.Task(0, "Corpus Fetch", 5, new int[]{}),
                new CriticalPathMethod.Task(1, "Parse JSONL", 8, new int[]{0}),
                new CriticalPathMethod.Task(2, "Build Suffix", 12, new int[]{1}),
                new CriticalPathMethod.Task(3, "Invert Index", 10, new int[]{1}),
                new CriticalPathMethod.Task(4, "Build Graph", 7, new int[]{0}),
                new CriticalPathMethod.Task(5, "Deploy Server", 4, new int[]{2, 3, 4})
        };

        ScheduleResult res = CriticalPathMethod.scheduleProject(tasks);
        long elapsedNs = System.nanoTime() - t0;

        return new ScheduleResult(true, res.getAlgorithmName(),
                new AlgorithmMetrics(elapsedNs, "O(V + E)", "O(V + E)"),
                res.getExplanation(), res.getMakespan(), res.getCriticalPath(), res.getTasks());
    }

    private QueryResult executeNPApproximation(QueryRequest req) {
        long t0 = System.nanoTime();
        String variant = req.getAlgorithmVariant();

        if ("KNAPSACK_FPTAS".equalsIgnoreCase(variant)) {
            int[] weights = {10, 20, 30, 40, 50};
            int[] values = {60, 100, 120, 240, 300};
            int capacity = 80;
            double eps = req.getNumericParam1() > 0 ? req.getNumericParam1() : 0.20;

            KnapsackFPTAS.FPTASResult fptas = KnapsackFPTAS.solve(weights, values, capacity, eps);
            long elapsedNs = System.nanoTime() - t0;

            return new QueryResult(true, "Knapsack FPTAS (eps=" + eps + ")",
                    new AlgorithmMetrics(elapsedNs, "O(n^3 / eps)", "O(n^2 / eps)"),
                    "Approximates 0/1 knapsack in polynomial time within guaranteed (1 - eps) * OPT bound.") {
                @Override
                public String formatOutput() {
                    return formatCommonHeader("0/1 KNAPSACK FPTAS APPROXIMATION") +
                            "\n───◆ Approximation Solution ◆──────────────────────────────────────────────────\n" + fptas.toString() + "\n";
                }
            };
        } else if ("BRANCHING_VC".equalsIgnoreCase(variant)) {
            // Parameterized Vertex Cover
            boolean[][] graph = {
                    {false, true, true, false, false},
                    {true, false, true, true, false},
                    {true, true, false, false, true},
                    {false, true, false, false, true},
                    {false, false, true, true, false}
            };
            int k = (int) (req.getNumericParam1() > 0 ? req.getNumericParam1() : 3);
            ParameterizedVertexCover.FPTResult fpt = ParameterizedVertexCover.solve(graph, k);
            long elapsedNs = System.nanoTime() - t0;

            return new QueryResult(true, "Parameterized Vertex Cover (FPT)",
                    new AlgorithmMetrics(elapsedNs, "O(2^k * E)", "O(k)"),
                    "Bounded search tree terminating when parameter budget k is exhausted.") {
                @Override
                public String formatOutput() {
                    return formatCommonHeader("PARAMETERIZED VERTEX COVER (FPT)") +
                            "\n───◆ Bounded Search Tree Result ◆──────────────────────────────────────────────\n" + fpt.toString() + "\n";
                }
            };
        } else {
            // Vertex Cover 2-approximation
            boolean[][] graph = {
                    {false, true, true, false, false},
                    {true, false, true, true, false},
                    {true, true, false, false, true},
                    {false, true, false, false, true},
                    {false, false, true, true, false}
            };
            VertexCoverApproximation.ApproxVCResult vc = VertexCoverApproximation.findApproximateVertexCover(graph);
            long elapsedNs = System.nanoTime() - t0;

            return new QueryResult(true, "Vertex Cover 2-Approximation",
                    new AlgorithmMetrics(elapsedNs, "O(V + E)", "O(V)"),
                    "Maximal matching guarantees cover size |C| <= 2 * OPT.") {
                @Override
                public String formatOutput() {
                    return formatCommonHeader("VERTEX COVER 2-APPROXIMATION") +
                            "\n───◆ Maximal Matching Cover ◆──────────────────────────────────────────────────\n" + vc.toString() + "\n";
                }
            };
        }
    }

    private QueryResult executeRandomized(QueryRequest req) {
        long t0 = System.nanoTime();
        String variant = req.getAlgorithmVariant();

        if ("MILLER_RABIN".equalsIgnoreCase(variant)) {
            long n = req.getNumericParam1() > 1 ? (long) req.getNumericParam1() : 1000000007L;
            int rounds = req.getNumericParam2() > 0 ? (int) req.getNumericParam2() : 12;

            PrimeTestResult res = MillerRabin.testNumber(n, rounds);
            long elapsedNs = System.nanoTime() - t0;
            return new PrimeTestResult(res.isSuccess(), res.getAlgorithmName(),
                    new AlgorithmMetrics(elapsedNs, "O(k log^3 N)", "O(1)"),
                    res.getExplanation(), res.getCandidate(), res.isPrime(),
                    res.isDeterministic(), res.getRounds(), res.getWitnesses(), res.getErrorProbabilityBound());
        } else {
            // Randomized QuickSort vs Deterministic
            int size = (int) (req.getNumericParam1() > 0 ? req.getNumericParam1() : 10000);
            int[] data1 = new int[size];
            int[] data2 = new int[size];
            Random rng = new Random(42);
            for (int i = 0; i < size; i++) {
                int val = rng.nextInt(size);
                data1[i] = val;
                data2[i] = val;
            }

            RandomizedQuickSort.SortMetrics mRand = new RandomizedQuickSort.SortMetrics();
            long tR0 = System.nanoTime();
            RandomizedQuickSort.sort(data1, new Random(42), mRand);
            long tR = System.nanoTime() - tR0;

            RandomizedQuickSort.SortMetrics mDet = new RandomizedQuickSort.SortMetrics();
            long tD0 = System.nanoTime();
            RandomizedQuickSort.sortDeterministic(data2, mDet);
            long tD = System.nanoTime() - tD0;

            long elapsedNs = System.nanoTime() - t0;
            AlgorithmMetrics metrics = new AlgorithmMetrics(elapsedNs, mRand.comparisons, 0, -1, "O(N log N)", "O(log N)");

            return new QueryResult(true, "Randomized QuickSort", metrics,
                    "Randomized pivot prevents worst-case O(N^2) degradation.") {
                @Override
                public String formatOutput() {
                    return formatCommonHeader("RANDOMIZED QUICKSORT BENCHMARK") +
                            "\n───◆ Benchmark Performance (N=" + String.format("%,d", size) + ") ◆──────────────────────────────────────────\n" +
                            String.format("   Randomized Pivot   : %,d ns (%,d comparisons, %,d swaps)\n" +
                                            "   Deterministic Pivot: %,d ns (%,d comparisons, %,d swaps)\n",
                                    tR, mRand.comparisons, mRand.swaps, tD, mDet.comparisons, mDet.swaps);
                }
            };
        }
    }

    private QueryResult executeParallel(QueryRequest req) {
        String opStr = req.getAlgorithmVariant();
        int size = (int) (req.getNumericParam1() > 0 ? req.getNumericParam1() : 500000);
        double[] data = new double[size];
        Random rng = new Random(42);
        for (int i = 0; i < size; i++) data[i] = rng.nextDouble() * 100.0;

        if ("BLELLOCH_SCAN".equalsIgnoreCase(opStr)) {
            long t0 = System.nanoTime();
            double[] scan = BlellochScan.exclusiveScan(data);
            long elapsed = System.nanoTime() - t0;
            boolean verified = BlellochScan.verifyScan(scan, data);

            return new ParallelResult(true, "Blelloch Parallel Scan",
                    new AlgorithmMetrics(elapsed, "O(N) Work", "O(log N) Span"),
                    "Work-efficient up-sweep and down-sweep prefix sum.",
                    "EXCLUSIVE_SCAN", size, scan[scan.length - 1], elapsed, elapsed, 1.0,
                    Runtime.getRuntime().availableProcessors(), "O(N)", "O(log N)", verified);
        } else {
            ParallelReduce.Op op = ParallelReduce.Op.SUM;
            if ("MIN".equalsIgnoreCase(opStr)) op = ParallelReduce.Op.MIN;
            else if ("MAX".equalsIgnoreCase(opStr)) op = ParallelReduce.Op.MAX;

            return ParallelReduce.runReduction(data, op);
        }
    }

    private QueryResult executeComparison(QueryRequest req) {
        String pattern = req.getPrimaryInput();
        if (pattern == null || pattern.isEmpty()) pattern = "algorithm";

        String corpusSample;
        if (docRepo.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Document d : docRepo.getAll()) {
                sb.append(d.getContent()).append(" ");
            }
            corpusSample = sb.toString();
        } else {
            corpusSample = "an algorithm is a finite sequence of rigorous instructions to perform an algorithm computation.";
        }

        return StringComparisonEngine.compareAll(corpusSample, pattern);
    }
}
