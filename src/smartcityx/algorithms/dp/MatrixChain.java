package smartcityx.algorithms.dp;

public class MatrixChain {
    public static class Result {
        public int minCost;
        public String parenthesization;

        public Result(int minCost, String parenthesization) {
            this.minCost = minCost;
            this.parenthesization = parenthesization;
        }
    }

    public static Result multiplyOrder(int[] p) {
        int n = p.length - 1;
        int[][] m = new int[n + 1][n + 1];
        int[][] s = new int[n + 1][n + 1];

        for (int l = 2; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                m[i][j] = Integer.MAX_VALUE;
                for (int k = i; k <= j - 1; k++) {
                    int q = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (q < m[i][j]) {
                        m[i][j] = q;
                        s[i][j] = k;
                    }
                }
            }
        }
        String pStr = printOptimalParens(s, 1, n);
        return new Result(m[1][n], pStr);
    }

    private static String printOptimalParens(int[][] s, int i, int j) {
        if (i == j) {
            return "A" + i;
        } else {
            return "(" + printOptimalParens(s, i, s[i][j]) + printOptimalParens(s, s[i][j] + 1, j) + ")";
        }
    }
}
