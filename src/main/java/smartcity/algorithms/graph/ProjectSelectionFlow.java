package smartcity.algorithms.graph;

import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.IntArrayList;

/**
 * Project Selection Problem (Maximum-Weight Closure) via Minimum Cut reduction.
 * Given projects with positive rewards and prerequisite tasks with negative costs,
 * models optimal selection as an s-t min-cut problem:
 *   Max Net Profit = (Total Positive Profits) - (Min-Cut Capacity).
 */
public class ProjectSelectionFlow {

    public static class ProjectResult {
        public final double maxNetProfit;
        public final IntArrayList selectedProjects;
        public final double minCutCapacity;

        public ProjectResult(double maxNetProfit, IntArrayList selected, double minCutCap) {
            this.maxNetProfit = maxNetProfit;
            this.selectedProjects = selected != null ? selected : new IntArrayList();
            this.minCutCapacity = minCutCap;
        }

        @Override
        public String toString() {
            return String.format("Maximum Net Profit: %,.2f (Min-Cut Capacity: %,.2f)\nSelected Projects: %s",
                    maxNetProfit, minCutCapacity, selectedProjects.toString());
        }
    }

    /**
     * Solves the Project Selection Problem.
     * @param weights Net value of project i (positive = revenue, negative = cost)
     * @param dependencies pairs [u, v] meaning selecting project u requires selecting project v
     */
    public static ProjectResult solveProjectSelection(double[] weights, int[][] dependencies) {
        int n = weights.length;
        int s = 0;
        int t = n + 1;
        CustomFlowNetwork network = new CustomFlowNetwork(t + 1);

        double totalPositive = 0;
        for (int i = 0; i < n; i++) {
            int node = i + 1;
            if (weights[i] > 0) {
                totalPositive += weights[i];
                network.addEdge(new CustomFlowEdge(s, node, weights[i]));
            } else if (weights[i] < 0) {
                network.addEdge(new CustomFlowEdge(node, t, -weights[i]));
            }
        }

        double INF = 1e9;
        if (dependencies != null) {
            for (int[] dep : dependencies) {
                int u = dep[0] + 1;
                int v = dep[1] + 1;
                network.addEdge(new CustomFlowEdge(u, v, INF));
            }
        }

        MinCut.CutAnalysis cut = MinCut.computeMinCut(network, s, t);
        double netProfit = totalPositive - cut.cutCapacity;

        IntArrayList selected = new IntArrayList();
        for (int i = 0; i < n; i++) {
            if (cut.sourcePartition[i + 1]) {
                selected.add(i);
            }
        }

        return new ProjectResult(netProfit, selected, cut.cutCapacity);
    }
}
