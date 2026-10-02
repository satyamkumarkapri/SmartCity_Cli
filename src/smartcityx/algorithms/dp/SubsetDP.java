package smartcityx.algorithms.dp;

import java.util.ArrayList;
import java.util.List;

public class SubsetDP {
    public static List<Integer> findSubsetSum(int[] arr, int target) {
        int n = arr.length;
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < arr[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - arr[i - 1]];
                }
            }
        }

        if (!dp[n][target]) return new ArrayList<>();

        List<Integer> subset = new ArrayList<>();
        int res = target;
        for (int i = n; i > 0 && res > 0; i--) {
            if (res == 0) break;
            if (!dp[i - 1][res]) {
                subset.add(i - 1);
                res = res - arr[i - 1];
            }
        }
        return subset;
    }
}
