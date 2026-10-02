package smartcityx.algorithms.np;

import java.util.ArrayList;
import java.util.List;

public class VertexCover {
    public static List<Integer> findExactVertexCover(boolean[][] graph) {
        int n = graph.length;
        int minCoverSize = Integer.MAX_VALUE;
        List<Integer> bestCover = new ArrayList<>();

        int maxMask = 1 << n;
        for (int mask = 0; mask < maxMask; mask++) {
            boolean valid = true;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (graph[i][j]) {
                        if (((mask & (1 << i)) == 0) && ((mask & (1 << j)) == 0)) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (!valid) break;
            }

            if (valid) {
                int size = Integer.bitCount(mask);
                if (size < minCoverSize) {
                    minCoverSize = size;
                    bestCover.clear();
                    for (int i = 0; i < n; i++) {
                        if ((mask & (1 << i)) != 0) bestCover.add(i);
                    }
                }
            }
        }
        return bestCover;
    }
}
