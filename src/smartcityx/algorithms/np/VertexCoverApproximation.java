package smartcityx.algorithms.np;

import java.util.ArrayList;
import java.util.List;

public class VertexCoverApproximation {
    public static List<Integer> findApproximateVertexCover(boolean[][] graph) {
        int n = graph.length;
        boolean[] inCover = new boolean[n];
        boolean[][] edges = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, edges[i], 0, n);
        }

        List<Integer> cover = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (edges[u][v]) {
                    cover.add(u);
                    cover.add(v);
                    inCover[u] = true;
                    inCover[v] = true;
                    
                    for (int i = 0; i < n; i++) {
                        edges[u][i] = false;
                        edges[i][u] = false;
                        edges[v][i] = false;
                        edges[i][v] = false;
                    }
                }
            }
        }
        return cover;
    }
}
