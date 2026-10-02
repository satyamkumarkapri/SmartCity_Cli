package smartcityx.algorithms.np;

public class IndependentSet {
    public static boolean hasIndependentSet(boolean[][] graph, int k) {
        int n = graph.length;
        boolean[][] complement = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    complement[i][j] = !graph[i][j];
                }
            }
        }
        return Clique.hasClique(complement, k);
    }
}
