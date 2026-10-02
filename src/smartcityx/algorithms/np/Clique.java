package smartcityx.algorithms.np;

public class Clique {
    public static boolean hasClique(boolean[][] graph, int k) {
        int n = graph.length;
        int maxMask = 1 << n;

        for (int mask = 0; mask < maxMask; mask++) {
            int count = Integer.bitCount(mask);
            if (count == k) {
                boolean isClique = true;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        if (((mask & (1 << i)) != 0) && ((mask & (1 << j)) != 0)) {
                            if (!graph[i][j]) {
                                isClique = false;
                                break;
                            }
                        }
                    }
                    if (!isClique) break;
                }
                if (isClique) return true;
            }
        }
        return false;
    }
}
