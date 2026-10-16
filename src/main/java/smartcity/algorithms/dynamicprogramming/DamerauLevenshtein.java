package smartcity.algorithms.dynamicprogramming;

/**
 * Damerau-Levenshtein Distance.
 * Provides both:
 * 1. Restricted Distance (Optimal String Alignment - OSA):
 *    Adjacent transpositions cannot be modified again by intervening operations.
 * 2. Unrestricted True Damerau-Levenshtein Distance:
 *    Transpositions of two characters can occur with intervening insertions and deletions.
 */
public class DamerauLevenshtein {

    public static int distance(String s1, String s2) {
        return optimalStringAlignmentDistance(s1, s2);
    }

    /**
     * Optimal String Alignment (OSA) Distance.
     * Time Complexity: O(nm).
     * Space Complexity: O(nm).
     */
    public static int optimalStringAlignmentDistance(String s1, String s2) {
        if (s1 == null) return s2 == null ? 0 : s2.length();
        if (s2 == null) return s1.length();

        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);

                // Adjacent transposition
                if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2) && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }
        return dp[n][m];
    }

    /**
     * True Unrestricted Damerau-Levenshtein Distance.
     * Uses a full alphabet last-seen table to handle arbitrary non-adjacent transpositions.
     * Time Complexity: O(nm * sigma).
     */
    public static int unrestrictedDistance(String s1, String s2) {
        if (s1 == null) return s2 == null ? 0 : s2.length();
        if (s2 == null) return s1.length();

        int n = s1.length();
        int m = s2.length();
        int maxDist = n + m;

        int[] da = new int[256];
        int[][] h = new int[n + 2][m + 2];

        h[0][0] = maxDist;
        for (int i = 0; i <= n; i++) {
            h[i + 1][0] = maxDist;
            h[i + 1][1] = i;
        }
        for (int j = 0; j <= m; j++) {
            h[0][j + 1] = maxDist;
            h[1][j + 1] = j;
        }

        for (int i = 1; i <= n; i++) {
            int db = 0;
            for (int j = 1; j <= m; j++) {
                char c1 = s1.charAt(i - 1);
                char c2 = s2.charAt(j - 1);
                int k = (c2 < 256) ? da[c2] : 0;
                int l = db;
                int cost = (c1 == c2) ? 0 : 1;
                if (cost == 0) db = j;

                int deletion = h[i][j + 1] + 1;
                int insertion = h[i + 1][j] + 1;
                int substitution = h[i][j] + cost;
                int transposition = (k > 0 && l > 0) ? h[k][l] + (i - k - 1) + 1 + (j - l - 1) : maxDist;

                h[i + 1][j + 1] = Math.min(Math.min(deletion, insertion), Math.min(substitution, transposition));
            }
            if (s1.charAt(i - 1) < 256) {
                da[s1.charAt(i - 1)] = i;
            }
        }
        return h[n + 1][m + 1];
    }
}
