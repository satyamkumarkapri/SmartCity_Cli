package smartcity.algorithms.graph;

import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;

/**
 * Image Segmentation demo using Min-Cut on a synthetic 2D pixel grid.
 * Pixels are partitioned into Foreground (source S) and Background (sink T),
 * balancing regional pixel likelihoods with boundary smoothness penalties.
 */
public class ImageSegmentationFlow {

    public static class SegmentationResult {
        public final int rows;
        public final int cols;
        public final boolean[][] isForeground;
        public final double energy;

        public SegmentationResult(int rows, int cols, boolean[][] isForeground, double energy) {
            this.rows = rows;
            this.cols = cols;
            this.isForeground = isForeground;
            this.energy = energy;
        }

        public String formatGrid() {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    sb.append(isForeground[r][c] ? "[#]" : "[ ]");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * Segments a synthetic rows x cols grid.
     * @param fgLikelihood Likelihood of each pixel being foreground
     * @param bgLikelihood Likelihood of each pixel being background
     * @param boundarySmoothness Penalty cost for neighboring pixels assigned to different labels
     */
    public static SegmentationResult segment(int rows, int cols, double[][] fgLikelihood,
                                             double[][] bgLikelihood, double boundarySmoothness) {
        int numPixels = rows * cols;
        int s = 0;
        int t = numPixels + 1;
        CustomFlowNetwork network = new CustomFlowNetwork(t + 1);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int p = r * cols + c + 1;
                // Regional likelihood edges
                network.addEdge(new CustomFlowEdge(s, p, fgLikelihood[r][c]));
                network.addEdge(new CustomFlowEdge(p, t, bgLikelihood[r][c]));

                // Neighbor boundary smoothness edges (4-connected grid)
                if (c + 1 < cols) {
                    int neighbor = r * cols + (c + 1) + 1;
                    network.addEdge(new CustomFlowEdge(p, neighbor, boundarySmoothness));
                    network.addEdge(new CustomFlowEdge(neighbor, p, boundarySmoothness));
                }
                if (r + 1 < rows) {
                    int neighbor = (r + 1) * cols + c + 1;
                    network.addEdge(new CustomFlowEdge(p, neighbor, boundarySmoothness));
                    network.addEdge(new CustomFlowEdge(neighbor, p, boundarySmoothness));
                }
            }
        }

        MinCut.CutAnalysis cut = MinCut.computeMinCut(network, s, t);

        boolean[][] fgMask = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int p = r * cols + c + 1;
                fgMask[r][c] = cut.sourcePartition[p];
            }
        }

        return new SegmentationResult(rows, cols, fgMask, cut.cutCapacity);
    }
}
