package smartcityx.algorithms.flow;

public class BipartiteMatching {
    public static int maxMatching(boolean[][] bipartiteGraph) {
        int uCount = bipartiteGraph.length;
        if (uCount == 0) return 0;
        int vCount = bipartiteGraph[0].length;
        int[] match = new int[vCount];
        for (int i = 0; i < vCount; ++i)
            match[i] = -1;

        int result = 0;
        for (int u = 0; u < uCount; u++) {
            boolean[] visited = new boolean[vCount];
            if (bpm(bipartiteGraph, u, visited, match))
                result++;
        }
        return result;
    }

    private static boolean bpm(boolean[][] bipartiteGraph, int u, boolean[] visited, int[] match) {
        int vCount = bipartiteGraph[0].length;
        for (int v = 0; v < vCount; v++) {
            if (bipartiteGraph[u][v] && !visited[v]) {
                visited[v] = true;
                if (match[v] < 0 || bpm(bipartiteGraph, match[v], visited, match)) {
                    match[v] = u;
                    return true;
                }
            }
        }
        return false;
    }
}
