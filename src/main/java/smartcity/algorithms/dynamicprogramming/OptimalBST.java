package smartcity.algorithms.dynamicprogramming;

/**
 * Optimal Binary Search Tree (OBST) Dynamic Programming.
 * Minimizes expected search cost given key frequencies/probabilities.
 * Time Complexity: O(n^3) standard baseline.
 * Space Complexity: O(n^2).
 */
public class OptimalBST {

    public static class Result {
        public final double expectedCost;
        public final String treeStructure;
        public final int[][] rootTable;

        public Result(double expectedCost, String treeStructure, int[][] rootTable) {
            this.expectedCost = expectedCost;
            this.treeStructure = treeStructure;
            this.rootTable = rootTable;
        }

        @Override
        public String toString() {
            return String.format("Minimum Expected Search Cost: %.4f\nTree Structure:\n%s", expectedCost, treeStructure);
        }
    }

    /**
     * Solves Optimal BST using key probabilities p and dummy key probabilities q.
     * @param p Probabilities for keys k1..kn (1-indexed, length n + 1, p[0]=0)
     * @param q Probabilities for dummy keys d0..dn (length n + 1)
     * @param keys Optional names of keys
     */
    public static Result computeOptimalBST(double[] p, double[] q, String[] keys) {
        int n = p.length - 1;
        double[][] e = new double[n + 2][n + 2];
        double[][] w = new double[n + 2][n + 2];
        int[][] root = new int[n + 1][n + 1];

        // Base cases
        for (int i = 1; i <= n + 1; i++) {
            e[i][i - 1] = q[i - 1];
            w[i][i - 1] = q[i - 1];
        }

        // Subtree lengths from 1 to n
        for (int l = 1; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                e[i][j] = Double.MAX_VALUE;
                w[i][j] = w[i][j - 1] + p[j] + q[j];

                for (int r = i; r <= j; r++) {
                    double t = e[i][r - 1] + e[r + 1][j] + w[i][j];
                    if (t < e[i][j]) {
                        e[i][j] = t;
                        root[i][j] = r;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        reconstructTree(root, keys, 1, n, -1, false, sb, 0);

        return new Result(e[1][n], sb.toString(), root);
    }

    private static void reconstructTree(int[][] root, String[] keys, int i, int j, int parent, boolean isLeft, StringBuilder sb, int depth) {
        if (i <= j) {
            int r = root[i][j];
            String keyName = (keys != null && r - 1 < keys.length) ? keys[r - 1] : ("k" + r);
            for (int d = 0; d < depth; d++) sb.append("  ");
            if (parent == -1) {
                sb.append("Root: ").append(keyName).append("\n");
            } else {
                sb.append(isLeft ? "|-- Left: " : "\\-- Right: ").append(keyName).append("\n");
            }
            reconstructTree(root, keys, i, r - 1, r, true, sb, depth + 1);
            reconstructTree(root, keys, r + 1, j, r, false, sb, depth + 1);
        }
    }
}
