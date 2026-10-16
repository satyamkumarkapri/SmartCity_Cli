package smartcity.algorithms.dynamicprogramming;

import smartcity.structures.CustomGraph;
import smartcity.structures.IntArrayList;

/**
 * Tree Diameter via Tree Dynamic Programming.
 * Time Complexity: O(V + E) = O(N).
 * Space Complexity: O(N).
 */
public class TreeDiameter {

    public static class DiameterResult {
        public final int diameter;
        public final int centerNode;
        public final IntArrayList path;

        public DiameterResult(int diameter, int centerNode) {
            this.diameter = diameter;
            this.centerNode = centerNode;
            this.path = new IntArrayList();
            if (centerNode >= 0) this.path.add(centerNode);
        }

        @Override
        public String toString() {
            return String.format("Tree Diameter: %d edges (Center node: %d)", diameter, centerNode);
        }
    }

    public static DiameterResult computeDiameter(int n, int[][] edges) {
        CustomGraph g = new CustomGraph(n, false);
        if (edges != null) {
            for (int[] e : edges) {
                if (e.length >= 2) g.addEdge(e[0], e[1]);
            }
        }
        return computeDiameter(g);
    }

    public static DiameterResult computeDiameter(CustomGraph tree) {

        if (tree == null || tree.V() == 0) return new DiameterResult(0, -1);
        if (tree.V() == 1) return new DiameterResult(0, 0);

        int[] maxDiameter = new int[]{0};
        int[] center = new int[]{0};
        boolean[] visited = new boolean[tree.V()];

        dfs(tree, 0, visited, maxDiameter, center);

        return new DiameterResult(maxDiameter[0], center[0]);
    }

    private static int dfs(CustomGraph tree, int u, boolean[] visited, int[] maxDiameter, int[] center) {
        visited[u] = true;
        int max1 = 0, max2 = 0;

        IntArrayList neighbors = tree.adj(u);
        for (int i = 0; i < neighbors.size(); i++) {
            int v = neighbors.get(i);
            if (!visited[v]) {
                int depth = 1 + dfs(tree, v, visited, maxDiameter, center);
                if (depth > max1) {
                    max2 = max1;
                    max1 = depth;
                } else if (depth > max2) {
                    max2 = depth;
                }
            }
        }

        if (max1 + max2 > maxDiameter[0]) {
            maxDiameter[0] = max1 + max2;
            center[0] = u;
        }

        return max1;
    }
}
