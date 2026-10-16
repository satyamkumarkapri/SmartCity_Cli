package smartcity.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.algorithms.dynamicprogramming.*;
import smartcity.structures.CustomGraph;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module 3: Advanced Dynamic Programming Tests")
public class DynamicProgrammingTest {

    @Test
    @DisplayName("Levenshtein: Standard, weighted, traceback, space-optimized")
    public void testLevenshtein() {
        assertEquals(0, Levenshtein.calculateDistance("", ""));
        assertEquals(4, Levenshtein.calculateDistance("", "test"));
        assertEquals(3, Levenshtein.calculateDistance("kitten", "sitting"));
        assertEquals(3, Levenshtein.calculateDistanceSpaceOptimized("kitten", "sitting"));

        Levenshtein.AlignmentResult align = Levenshtein.alignWithReconstruction("ABC", "ADC", 1, 1, 2);
        assertEquals(2, align.totalDistance); // Substitution cost 2
        assertEquals(3, align.operations.size());
    }

    @Test
    @DisplayName("Damerau-Levenshtein: Restricted OSA vs Unrestricted True Distance")
    public void testDamerauLevenshteinDistinction() {
        // "ca" to "abc":
        // OSA: Adjacent transposition cannot have intervening operations.
        // Needs 3 ops: insert 'a', substitute 'c'->'b', etc. (Cost = 3)
        // True unrestricted: Transposition allows intervening operations.
        // Transpose 'c' and 'a' (cost 1) + insert 'b' (cost 1) = Cost 2!
        int osaDist = DamerauLevenshtein.optimalStringAlignmentDistance("ca", "abc");
        int trueDist = DamerauLevenshtein.unrestrictedDistance("ca", "abc");

        assertEquals(3, osaDist, "Restricted OSA must equal 3");
        assertEquals(2, trueDist, "Unrestricted true Damerau-Levenshtein must equal 2");
    }

    @Test
    @DisplayName("Sequence Alignment: Needleman-Wunsch & Smith-Waterman")
    public void testSequenceAlignment() {
        // Global Alignment
        SequenceAlignment.AlignmentResult global = SequenceAlignment.needlemanWunsch("AGCT", "ATGCT", 2, 1, 1);
        assertTrue(global.score > 0);
        assertEquals("A-GCT", global.alignedSeq1);
        assertEquals("ATGCT", global.alignedSeq2);

        // Local Alignment
        SequenceAlignment.AlignmentResult local = SequenceAlignment.smithWaterman("XXXXXALGORITHMZZZZZ", "YYYYYALGORITHMQQQQQ", 2, 1, 1);
        assertTrue(local.score >= 18);
        assertTrue(local.alignedSeq1.contains("ALGORITHM"));
        assertTrue(local.alignedSeq2.contains("ALGORITHM"));
    }

    @Test
    @DisplayName("Matrix Chain Multiplication: Split & Parenthesization")
    public void testMatrixChain() {
        int[] p = {10, 20, 30, 40, 30};
        MatrixChainMultiplication.Result res = MatrixChainMultiplication.multiplyOrder(p);
        assertEquals(30000, res.minCost);
        assertNotNull(res.parenthesization);
        assertTrue(res.parenthesization.contains("A1"));
    }

    @Test
    @DisplayName("Optimal Binary Search Tree")
    public void testOptimalBST() {
        double[] p = {0, 0.15, 0.10, 0.05, 0.10, 0.20};
        double[] q = {0.05, 0.10, 0.05, 0.05, 0.05, 0.10};
        String[] keys = {"k1", "k2", "k3", "k4", "k5"};

        OptimalBST.Result res = OptimalBST.computeOptimalBST(p, q, keys);
        assertTrue(res.expectedCost > 0);
        assertEquals(2, res.rootTable[1][5]); // Root of optimal tree for these standard values is k2
    }

    @Test
    @DisplayName("Travelling Salesperson Bitmask DP: Tour Reconstruction & Unreachable")
    public void testTravellingSalesperson() {
        double INF = TravellingSalesperson.INF;
        double[][] graph = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        TravellingSalesperson.TSPResult res = TravellingSalesperson.solveTSP(graph);
        assertTrue(res.tourExists);
        assertEquals(80.0, res.minCost, 1e-6);
        assertEquals(5, res.tour.size()); // 0 -> ... -> 0
        assertEquals(0, res.tour.get(0));
        assertEquals(0, res.tour.get(4));

        // Disconnected graph
        double[][] disconnected = {
                {0, INF, INF},
                {INF, 0, INF},
                {INF, INF, 0}
        };
        TravellingSalesperson.TSPResult discRes = TravellingSalesperson.solveTSP(disconnected);
        assertFalse(discRes.tourExists);
    }

    @Test
    @DisplayName("Hamiltonian Path Bitmask DP")
    public void testHamiltonianPath() {
        boolean[][] g = {
                {false, true, false, false},
                {true, false, true, false},
                {false, true, false, true},
                {false, false, true, false}
        };
        HamiltonianPath.PathResult res = HamiltonianPath.findHamiltonianPath(g);
        assertTrue(res.exists);
        assertEquals(4, res.path.size());
    }

    @Test
    @DisplayName("Set Cover Bitmask DP")
    public void testSetCoverBitmask() {
        // Universe of 3 items (0, 1, 2)
        // Subsets: S0 = {0, 1} (mask 3), S1 = {1, 2} (mask 6), S2 = {0, 2} (mask 5)
        int[] subsets = {3, 6, 5};
        int[] costs = {5, 5, 5};
        SetCoverBitmask.SetCoverResult res = SetCoverBitmask.solveSetCover(3, subsets, costs);
        assertTrue(res.possible);
        assertEquals(10, res.minCost); // Any two subsets cover universe
        assertEquals(2, res.selectedSubsets.size());
    }

    @Test
    @DisplayName("Tree Diameter & Tree Rerooting DP")
    public void testTreeDP() {
        // Tree: 0 - 1 - 2 - 3
        CustomGraph tree = new CustomGraph(4);
        tree.addEdge(0, 1);
        tree.addEdge(1, 2);
        tree.addEdge(2, 3);

        TreeDiameter.DiameterResult dia = TreeDiameter.computeDiameter(tree);
        assertEquals(3, dia.diameter);

        TreeRerooting.RerootResult reroot = TreeRerooting.solveAllPairsDistances(tree);
        // Distances from node 1: to 0 is 1, to 2 is 1, to 3 is 2. Sum = 4.
        // Distances from node 0: to 1 is 1, to 2 is 2, to 3 is 3. Sum = 6.
        assertEquals(6, reroot.sumDistances[0]);
        assertEquals(4, reroot.sumDistances[1]);
        assertEquals(4, reroot.sumDistances[2]);
        assertEquals(6, reroot.sumDistances[3]);
        assertEquals(4, reroot.minDistanceSum);
    }

    @Test
    @DisplayName("Sum Over Subsets (SOS DP)")
    public void testSumOverSubsets() {
        long[] a = {1, 2, 4, 8}; // n = 2 bits
        long[] f = SumOverSubsetsDP.computeSOS(a, 2);
        // f[0] = a[0] = 1
        // f[1] = a[0] + a[1] = 3
        // f[2] = a[0] + a[2] = 5
        // f[3] = a[0] + a[1] + a[2] + a[3] = 15
        assertEquals(1, f[0]);
        assertEquals(3, f[1]);
        assertEquals(5, f[2]);
        assertEquals(15, f[3]);
    }
}
