package smartcityx.algorithms.parallel;

public class BlellochScan {
    public static double[] prefixSum(double[] input) {
        int n = input.length;
        int size = 1;
        while (size < n) size *= 2;
        
        double[] tree = new double[size * 2];
        System.arraycopy(input, 0, tree, size, n);
        
        for (int d = size / 2; d > 0; d /= 2) {
            for (int i = d; i < 2 * d; i++) {
                tree[i] = tree[2 * i] + tree[2 * i + 1];
            }
        }
        
        tree[1] = 0;
        
        for (int d = 1; d < size; d *= 2) {
            for (int i = d; i < 2 * d; i++) {
                double left = tree[2 * i];
                tree[2 * i] = tree[i];
                tree[2 * i + 1] = tree[i] + left;
            }
        }
        
        double[] result = new double[n];
        System.arraycopy(tree, size, result, 0, n);
        return result;
    }
}
