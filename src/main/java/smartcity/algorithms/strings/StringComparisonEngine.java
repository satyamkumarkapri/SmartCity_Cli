package smartcity.algorithms.strings;

import smartcity.model.AlgorithmMetrics;
import smartcity.model.ComparisonResult;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;

/**
 * Head-to-head benchmarking engine comparing Naive, KMP, Z-Algorithm, and Rabin-Karp.
 */
public class StringComparisonEngine {

    public static ComparisonResult compareAll(String text, String pattern) {
        if (text == null) text = "";
        if (pattern == null) pattern = "";

        DynamicArray<ComparisonResult.Entry> entries = new DynamicArray<>();

        // 1. Naive Search
        NaiveSearch.SearchMetrics naiveMetrics = new NaiveSearch.SearchMetrics();
        long t0 = System.nanoTime();
        IntArrayList naiveMatches = NaiveSearch.search(text, pattern, naiveMetrics);
        long tNaive = System.nanoTime() - t0;
        entries.add(new ComparisonResult.Entry(
                "Naive Match", naiveMatches.size(), tNaive, naiveMetrics.comparisons, "O(n * m)", "O(1)"));

        // 2. KMP
        KMP.SearchMetrics kmpMetrics = new KMP.SearchMetrics();
        t0 = System.nanoTime();
        IntArrayList kmpMatches = KMP.search(text, pattern, kmpMetrics);
        long tKmp = System.nanoTime() - t0;
        entries.add(new ComparisonResult.Entry(
                "Knuth-Morris-Pratt", kmpMatches.size(), tKmp, kmpMetrics.comparisons, "O(n + m)", "O(m)"));

        // 3. Z-Algorithm
        ZAlgorithm.SearchMetrics zMetrics = new ZAlgorithm.SearchMetrics();
        t0 = System.nanoTime();
        IntArrayList zMatches = ZAlgorithm.search(text, pattern, zMetrics);
        long tZ = System.nanoTime() - t0;
        entries.add(new ComparisonResult.Entry(
                "Z-Algorithm", zMatches.size(), tZ, zMetrics.comparisons, "O(n + m)", "O(n + m)"));

        // 4. Rabin-Karp
        RabinKarp.SearchMetrics rkMetrics = new RabinKarp.SearchMetrics();
        t0 = System.nanoTime();
        IntArrayList rkMatches = RabinKarp.search(text, pattern, false, rkMetrics);
        long tRk = System.nanoTime() - t0;
        entries.add(new ComparisonResult.Entry(
                "Rabin-Karp", rkMatches.size(), tRk, rkMetrics.comparisons, "O(n + m)", "O(1)"));

        // Verify 100% agreement between all implementations
        boolean agreed = checkAgreement(naiveMatches, kmpMatches) &&
                         checkAgreement(kmpMatches, zMatches) &&
                         checkAgreement(zMatches, rkMatches);

        AlgorithmMetrics overallMetrics = new AlgorithmMetrics(
                tNaive + tKmp + tZ + tRk,
                naiveMetrics.comparisons + kmpMetrics.comparisons + zMetrics.comparisons + rkMetrics.comparisons,
                rkMetrics.collisions,
                -1,
                "Varies by algo", "Varies by algo"
        );

        String explanation = agreed ?
                "All 4 algorithms produced identical match sets. KMP and Z-Algorithm exhibit linear comparison bounds, while Naive performs redundant character re-evaluations on repetitive prefixes." :
                "Warning: Discrepancy detected among match outputs.";

        return new ComparisonResult(true, overallMetrics, explanation, pattern, text.length(), entries, agreed);
    }

    private static boolean checkAgreement(IntArrayList a, IntArrayList b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) != b.get(i)) return false;
        }
        return true;
    }
}
