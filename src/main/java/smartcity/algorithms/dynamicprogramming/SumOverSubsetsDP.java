package smartcity.algorithms.dynamicprogramming;

/**
 * Sum Over Subsets Dynamic Programming (SOS DP).
 * Given an array A of size 2^N, computes for every mask:
 *   F[mask] = sum_{submask subseteq mask} A[submask]
 *
 * Baseline brute force checks all submasks in O(3^N) time.
 * SOS DP computes all 2^N sums in O(N * 2^N) time by decomposing along individual bit dimensions.
 */
public class SumOverSubsetsDP {

    /**
     * Computes SOS DP for an array of size 2^n.
     * @param a Input values indexed by mask 0..(2^n - 1)
     * @param n Number of bits
     * @return Array F where F[mask] is the sum of A[submask] for all submask in mask
     */
    public static long[] computeSOS(long[] a, int n) {
        if (a == null || n < 0) return new long[0];
        int numMasks = 1 << n;
        long[] f = new long[numMasks];
        System.arraycopy(a, 0, f, 0, Math.min(a.length, numMasks));

        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < numMasks; mask++) {
                if ((mask & (1 << i)) != 0) {
                    f[mask] += f[mask ^ (1 << i)];
                }
            }
        }
        return f;
    }

    public static int[] computeSOS(int[] a) {
        if (a == null) return new int[0];
        int n = 0;
        while ((1 << n) < a.length) n++;
        long[] l = new long[1 << n];
        for (int i = 0; i < a.length; i++) l[i] = a[i];
        long[] res = computeSOS(l, n);
        int[] out = new int[res.length];
        for (int i = 0; i < res.length; i++) out[i] = (int) res[i];
        return out;
    }


    /**
     * Demonstrates an application to subset aggregation / feature combination scoring.
     */
    public static String demonstrateFeatureAggregation(String[] featureNames, long[] baseScores) {
        int n = featureNames.length;
        long[] f = computeSOS(baseScores, n);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SOS DP computed across %d features (2^%d = %d combinations) in O(%d * %d) = %d operations:\n",
                n, n, 1 << n, n, 1 << n, n * (1 << n)));

        int displayLimit = Math.min(8, 1 << n);
        for (int mask = 0; mask < displayLimit; mask++) {
            StringBuilder feats = new StringBuilder("{");
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    if (feats.length() > 1) feats.append(", ");
                    feats.append(featureNames[i]);
                }
            }
            feats.append("}");
            sb.append(String.format(" Mask %3d %-25s -> Cumulative Sub-Feature Sum = %,d\n", mask, feats.toString(), f[mask]));
        }
        return sb.toString();
    }
}
