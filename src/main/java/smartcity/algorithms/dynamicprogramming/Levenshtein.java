package smartcity.algorithms.dynamicprogramming;

import smartcity.structures.DynamicArray;

/**
 * Wagner-Fischer Levenshtein Distance & Operation Reconstruction.
 * Supports standard costs, configurable weighted costs, and O(min(n,m)) space variant.
 */
public class Levenshtein {

    public enum EditOpType { MATCH, INSERT, DELETE, SUBSTITUTE }

    public static class EditOp {
        public final EditOpType type;
        public final char sourceChar;
        public final char targetChar;
        public final int cost;

        public EditOp(EditOpType type, char s, char t, int cost) {
            this.type = type;
            this.sourceChar = s;
            this.targetChar = t;
            this.cost = cost;
        }

        @Override
        public String toString() {
            switch (type) {
                case MATCH: return "Match '" + sourceChar + "'";
                case INSERT: return "Insert '" + targetChar + "' (cost: " + cost + ")";
                case DELETE: return "Delete '" + sourceChar + "' (cost: " + cost + ")";
                case SUBSTITUTE: return "Substitute '" + sourceChar + "' -> '" + targetChar + "' (cost: " + cost + ")";
                default: return "";
            }
        }
    }

    public static class AlignmentResult {
        public final int totalDistance;
        public final DynamicArray<EditOp> operations;
        public final String alignedSource;
        public final String alignedTarget;

        public AlignmentResult(int dist, DynamicArray<EditOp> ops, String s, String t) {
            this.totalDistance = dist;
            this.operations = ops;
            this.alignedSource = s;
            this.alignedTarget = t;
        }
    }

    /**
     * Standard Levenshtein distance with unit weights (insert=1, delete=1, sub=1).
     */
    public static int calculateDistance(String s1, String s2) {
        return calculateDistanceSpaceOptimized(s1, s2);
    }

    public static int compute(String s1, String s2) {
        return calculateDistance(s1, s2);
    }

    public static int distance(String s1, String s2) {
        return calculateDistance(s1, s2);
    }


    /**
     * O(min(n, m)) memory distance-only computation.
     */
    public static int calculateDistanceSpaceOptimized(String s1, String s2) {
        if (s1 == null) return s2 == null ? 0 : s2.length();
        if (s2 == null) return s1.length();

        if (s1.length() < s2.length()) {
            String temp = s1; s1 = s2; s2 = temp;
        }
        int n = s1.length();
        int m = s2.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            System.arraycopy(curr, 0, prev, 0, m + 1);
        }
        return prev[m];
    }

    /**
     * Full Wagner-Fischer dynamic programming table with operation reconstruction
     * and configurable operation costs.
     */
    public static AlignmentResult alignWithReconstruction(String s1, String s2,
                                                           int costInsert, int costDelete, int costSub) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * costDelete;
        for (int j = 0; j <= m; j++) dp[0][j] = j * costInsert;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int subCost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : costSub;
                int optSub = dp[i - 1][j - 1] + subCost;
                int optDel = dp[i - 1][j] + costDelete;
                int optIns = dp[i][j - 1] + costInsert;
                dp[i][j] = Math.min(Math.min(optDel, optIns), optSub);
            }
        }

        // Traceback from dp[n][m]
        DynamicArray<EditOp> ops = new DynamicArray<>();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                int subCost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : costSub;
                if (dp[i][j] == dp[i - 1][j - 1] + subCost) {
                    if (subCost == 0) {
                        ops.add(0, new EditOp(EditOpType.MATCH, s1.charAt(i - 1), s2.charAt(j - 1), 0));
                    } else {
                        ops.add(0, new EditOp(EditOpType.SUBSTITUTE, s1.charAt(i - 1), s2.charAt(j - 1), costSub));
                    }
                    sb1.insert(0, s1.charAt(i - 1));
                    sb2.insert(0, s2.charAt(j - 1));
                    i--; j--;
                    continue;
                }
            }
            if (i > 0 && dp[i][j] == dp[i - 1][j] + costDelete) {
                ops.add(0, new EditOp(EditOpType.DELETE, s1.charAt(i - 1), '-', costDelete));
                sb1.insert(0, s1.charAt(i - 1));
                sb2.insert(0, '-');
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + costInsert) {
                ops.add(0, new EditOp(EditOpType.INSERT, '-', s2.charAt(j - 1), costInsert));
                sb1.insert(0, '-');
                sb2.insert(0, s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(dp[n][m], ops, sb1.toString(), sb2.toString());
    }
}
