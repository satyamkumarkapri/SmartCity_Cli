package smartcityx.algorithms.dp;

import java.util.ArrayList;
import java.util.List;

public class BitmaskDP {
    public static List<Integer> selectOptimalResources(double[] costs, double[] benefits, double budget) {
        int n = costs.length;
        int numCombinations = 1 << n;
        double maxBenefit = -1;
        int bestMask = 0;

        for (int mask = 0; mask < numCombinations; mask++) {
            double currentCost = 0;
            double currentBenefit = 0;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    currentCost += costs[i];
                    currentBenefit += benefits[i];
                }
            }

            if (currentCost <= budget && currentBenefit > maxBenefit) {
                maxBenefit = currentBenefit;
                bestMask = mask;
            }
        }

        List<Integer> selectedIndices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                selectedIndices.add(i);
            }
        }
        return selectedIndices;
    }
}
