package smartcity.algorithms.strings;

import smartcity.structures.IntArrayList;

/**
 * Rabin-Karp Polynomial Rolling Hash String Search.
 * Features rolling-window updates, collision instrumentation,
 * full char-by-char verification, and optional double-hashing.
 * Time Complexity: O(n + m) expected, O(n * m) worst case.
 * Space Complexity: O(1) auxiliary.
 */
public class RabinKarp {
    public static final long BASE1 = 257;
    public static final long MOD1 = 1_000_000_007L;

    public static final long BASE2 = 313;
    public static final long MOD2 = 1_000_000_009L;

    public static class SearchMetrics {
        public long comparisons = 0;
        public long collisions = 0;
    }

    public static IntArrayList search(String text, String pattern) {
        return search(text, pattern, false, null);
    }

    public static IntArrayList search(String text, String pattern, boolean useDoubleHash, SearchMetrics metrics) {
        IntArrayList positions = new IntArrayList();
        if (text == null || pattern == null) return positions;
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) return positions;

        long patHash1 = 0;
        long patHash2 = 0;
        long txtHash1 = 0;
        long txtHash2 = 0;
        long h1 = 1;
        long h2 = 1;

        // Precompute h1 = BASE1^(m-1) % MOD1 and h2 = BASE2^(m-1) % MOD2
        for (int i = 0; i < m - 1; i++) {
            h1 = (h1 * BASE1) % MOD1;
            if (useDoubleHash) {
                h2 = (h2 * BASE2) % MOD2;
            }
        }

        // Calculate initial hash values for pattern and first window of text
        for (int i = 0; i < m; i++) {
            patHash1 = (BASE1 * patHash1 + pattern.charAt(i)) % MOD1;
            txtHash1 = (BASE1 * txtHash1 + text.charAt(i)) % MOD1;
            if (useDoubleHash) {
                patHash2 = (BASE2 * patHash2 + pattern.charAt(i)) % MOD2;
                txtHash2 = (BASE2 * txtHash2 + text.charAt(i)) % MOD2;
            }
        }

        // Slide the pattern over text
        for (int i = 0; i <= n - m; i++) {
            boolean hashMatch = (patHash1 == txtHash1);
            if (useDoubleHash && hashMatch) {
                hashMatch = (patHash2 == txtHash2);
            }

            if (hashMatch) {
                // Character-by-character verification of candidate match
                boolean exactMatch = true;
                for (int j = 0; j < m; j++) {
                    if (metrics != null) metrics.comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        exactMatch = false;
                        if (metrics != null) metrics.collisions++;
                        break;
                    }
                }
                if (exactMatch) {
                    positions.add(i);
                }
            }

            // Calculate rolling hash value for next window
            if (i < n - m) {
                // Remove leading digit, add trailing digit
                txtHash1 = (BASE1 * (txtHash1 - text.charAt(i) * h1 % MOD1 + MOD1) + text.charAt(i + m)) % MOD1;
                if (useDoubleHash) {
                    txtHash2 = (BASE2 * (txtHash2 - text.charAt(i) * h2 % MOD2 + MOD2) + text.charAt(i + m)) % MOD2;
                }
            }
        }
        return positions;
    }
}
