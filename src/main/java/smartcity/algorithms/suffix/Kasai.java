package smartcity.algorithms.suffix;

/**
 * Kasai's algorithm for constructing the Longest Common Prefix (LCP) array
 * from a Suffix Array in linear O(n) time.
 *
 * Indexing convention:
 * LCP[i] stores the length of the longest common prefix between
 * the suffix starting at SA[i] and the suffix starting at SA[i+1].
 * LCP[n-1] is set to 0.
 */
public class Kasai {
    public static int[] buildLCP(String text, int[] suffixArr) {
        if (text == null || suffixArr == null) return new int[0];
        int n = suffixArr.length;
        if (n <= 1) return new int[n];

        int[] lcp = new int[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArr[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                int j = suffixArr[rank[i] + 1];
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                h = 0;
            }
        }
        return lcp;
    }
}
