package smartcity.algorithms.suffix;

/**
 * Longest Common Prefix (LCP) builder delegating to Kasai's O(n) algorithm.
 */
public class LCP {
    public static int[] buildLCP(String text, int[] suffixArr) {
        return Kasai.buildLCP(text, suffixArr);
    }
}
