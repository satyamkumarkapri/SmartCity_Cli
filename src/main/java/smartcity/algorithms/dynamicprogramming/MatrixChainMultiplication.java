package smartcity.algorithms.dynamicprogramming;

/**
 * Matrix Chain Multiplication (Interval DP).
 * Minimizes scalar multiplications for matrix products A1 * A2 * ... * An.
 * Reconstructs optimal parenthesization string in O(n^3) time and O(n^2) space.
 */
public class MatrixChainMultiplication {

    public static class Result {
        public final int minCost;
        public final int minMultiplications;
        public final String parenthesization;

        public Result(int minCost, String parenthesization) {
            this.minCost = minCost;
            this.minMultiplications = minCost;
            this.parenthesization = parenthesization;
        }

        @Override
        public String toString() {
            return String.format("Minimum Multiplication Cost: %,d\nOptimal Ordering: %s", minCost, parenthesization);
        }
    }

    public static Result optimizeOrder(int[] p) {
        return multiplyOrder(p);
    }


    /**
     * Solves Matrix Chain Multiplication.
     * @param p Array of dimensions where matrix i has size p[i-1] x p[i].
     */
    public static Result multiplyOrder(int[] p) {
        if (p == null || p.length <= 2) {
            return new Result(0, (p != null && p.length == 2) ? "A1" : "");
        }

        int n = p.length - 1; // Number of matrices
        int[][] m = new int[n + 1][n + 1];
        int[][] s = new int[n + 1][n + 1];

        // l is chain length
        for (int l = 2; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                m[i][j] = Integer.MAX_VALUE;
                for (int k = i; k <= j - 1; k++) {
                    int cost = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < m[i][j]) {
                        m[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }

        String optimalParens = buildParensString(s, 1, n);
        return new Result(m[1][n], optimalParens);
    }

    private static String buildParensString(int[][] s, int i, int j) {
        if (i == j) {
            return "A" + i;
        } else {
            return "(" + buildParensString(s, i, s[i][j]) + " x " + buildParensString(s, s[i][j] + 1, j) + ")";
        }
    }
}
