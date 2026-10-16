package smartcity.algorithms.strings;

import smartcity.structures.IntArrayList;

/**
 * Knuth-Morris-Pratt (KMP) Linear-Time Pattern Matching Algorithm.
 * Time Complexity: O(n + m) worst case.
 * Space Complexity: O(m) for the Longest Proper Prefix-Suffix (LPS) array.
 */
public class KMP {
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

        int[] lps = computeLPS(pattern, metrics);
        int i = 0; // Text pointer
        int j = 0; // Pattern pointer

        while (i < n) {
            if (metrics != null) metrics.comparisons++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                positions.add(i - j); // Found match at start index (i - j)
                j = lps[j - 1]; // Shift pattern using LPS to find overlapping matches
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1]; // Shift pattern without retreating text index i
                } else {
                    i++;
                }
            }
        }
        return positions;
    }

    public static int[] computeLPS(String pattern) {
        return computeLPS(pattern, null);
    }

    public static int[] computeLPS(String pattern, SearchMetrics metrics) {
        int m = pattern.length();
        int[] lps = new int[m];
        if (m == 0) return lps;

        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < m) {
            if (metrics != null) metrics.comparisons++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
