package smartcity.algorithms.strings;

import smartcity.structures.IntArrayList;

/**
 * Z-Algorithm (Z-Function) for Linear-Time String Matching.
 * Time Complexity: O(n + m).
 * Space Complexity: O(n + m) for the Z-array.
 */
public class ZAlgorithm {
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

        // Choose a sentinel delimiter not present in either pattern or text
        char delimiter = findSafeDelimiter(text, pattern);
        String concat = pattern + delimiter + text;
        int totalLen = concat.length();

        int[] z = calculateZ(concat, metrics);

        for (int i = m + 1; i < totalLen; i++) {
            if (z[i] == m) {
                positions.add(i - m - 1);
            }
        }
        return positions;
    }

    public static int[] calculateZ(String s) {
        return calculateZ(s, null);
    }

    public static int[] computeZArray(String s) {
        return calculateZ(s, null);
    }


    public static int[] calculateZ(String s, SearchMetrics metrics) {
        int n = s.length();
        int[] z = new int[n];
        if (n == 0) return z;
        z[0] = n;

        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n) {
                if (metrics != null) metrics.comparisons++;
                if (s.charAt(z[i]) == s.charAt(i + z[i])) {
                    z[i]++;
                } else {
                    break;
                }
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    private static char findSafeDelimiter(String t, String p) {
        // Try common sentinels
        char[] candidates = {'$', '#', '\0', '\u0001', '\u001F', '@'};
        for (char c : candidates) {
            if (t.indexOf(c) == -1 && p.indexOf(c) == -1) {
                return c;
            }
        }
        // Fallback: search for any unused char
        for (char c = 1; c < 256; c++) {
            if (t.indexOf(c) == -1 && p.indexOf(c) == -1) {
                return c;
            }
        }
        return '\uFFFF';
    }
}
