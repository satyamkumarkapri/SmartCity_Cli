package smartcity.algorithms.parallel;

/**
 * Blelloch Work-Efficient Parallel Prefix Sum (Exclusive Scan).
 * Two-phase tree algorithm:
 *   1. Up-Sweep (Reduce): Computes sum of subtrees bottom-up in O(log n) steps.
 *   2. Down-Sweep: Distributes prefix sums top-down from root in O(log n) steps.
 * Work: O(N) operations.
 * Span: O(log N) depth.
 * Correctly handles non-power-of-two inputs by internal padding.
 */
public class BlellochScan {

    /**
     * Computes exclusive prefix scan where result[i] = sum(input[0..i-1]), result[0] = 0.
     */
    public static double[] exclusiveScan(double[] input) {
        if (input == null || input.length == 0) return new double[0];
        int n = input.length;

        int size = 1;
        while (size < n) size *= 2;

        double[] tree = new double[size * 2];
        System.arraycopy(input, 0, tree, size, n);

        // Phase 1: Up-Sweep (Reduce pass)
        for (int d = size / 2; d > 0; d /= 2) {
            for (int i = d; i < 2 * d; i++) {
                tree[i] = tree[2 * i] + tree[2 * i + 1];
            }
        }

        // Set root to 0 (identity for addition)
        tree[1] = 0;

        // Phase 2: Down-Sweep pass
        for (int d = 1; d < size; d *= 2) {
            for (int i = d; i < 2 * d; i++) {
                double leftVal = tree[2 * i];
                tree[2 * i] = tree[i];
                tree[2 * i + 1] = tree[i] + leftVal;
            }
        }

        double[] result = new double[n];
        System.arraycopy(tree, size, result, 0, n);
        return result;
    }

    /**
     * Sequential prefix sum reference for verification.
     */
    public static double[] sequentialScan(double[] input) {
        if (input == null || input.length == 0) return new double[0];
        double[] res = new double[input.length];
        res[0] = 0;
        for (int i = 1; i < input.length; i++) {
            res[i] = res[i - 1] + input[i - 1];
        }
        return res;
    }

    public static boolean verifyScan(double[] parallelScan, double[] input) {
        double[] seq = sequentialScan(input);
        if (parallelScan.length != seq.length) return false;
        for (int i = 0; i < seq.length; i++) {
            if (Math.abs(parallelScan[i] - seq[i]) > 1e-6) return false;
        }
        return true;
    }
}
