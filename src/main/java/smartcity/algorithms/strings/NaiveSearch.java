package smartcity.algorithms.strings;

import smartcity.structures.IntArrayList;

/**
 * Naive (Brute-Force) Pattern Matching Algorithm.
 * Time Complexity: O(n * m) worst case, O(n) best case.
 * Space Complexity: O(1) auxiliary.
 */
public class NaiveSearch {
    public static class SearchMetrics {
        public long comparisons = 0;
    }

    public static IntArrayList search(String text, String pattern) {
        return search(text, pattern, null);
    }

    public static IntArrayList search(String text, String pattern, SearchMetrics metrics) {
        IntArrayList positions = new IntArrayList();
        if (text == null || pattern == null) return positions;
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) return positions;

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                if (metrics != null) metrics.comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                positions.add(i); // Zero-based matching position
            }
        }
        return positions;
    }
}
