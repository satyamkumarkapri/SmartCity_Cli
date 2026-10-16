package smartcity.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.algorithms.complexity.KnapsackFPTAS;
import smartcity.algorithms.complexity.ParameterizedVertexCover;
import smartcity.algorithms.complexity.ReductionZoo;
import smartcity.algorithms.complexity.VertexCoverApproximation;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module 5: NP-Completeness and Approximation Tests")
public class ComplexityAndApproximationTest {

    @Test
    @DisplayName("Vertex Cover 2-Approximation: Complete edge coverage and factor-2 bound")
    public void testVertexCoverApproximation() {
        // Triangle graph with a tail: 0-1, 1-2, 2-0, 2-3
        boolean[][] g = {
                {false, true, true, false},
                {true, false, true, false},
                {true, true, false, true},
                {false, false, true, false}
        };

        VertexCoverApproximation.ApproxVCResult res = VertexCoverApproximation.findApproximateVertexCover(g);
        assertTrue(res.allEdgesCovered);
        assertTrue(res.vertexCover.size() <= 2 * res.matchingSize);
    }

    @Test
    @DisplayName("0/1 Knapsack FPTAS: Approximates within (1 - epsilon) factor")
    public void testKnapsackFPTAS() {
        int[] w = {10, 20, 30};
        int[] v = {60, 100, 120};
        int capacity = 50;
        double eps = 0.20;

        KnapsackFPTAS.FPTASResult res = KnapsackFPTAS.solve(w, v, capacity, eps);
        assertTrue(res.totalWeight <= capacity);
        // Optimal choice: item 1 (w=20, v=100) and item 2 (w=30, v=120) -> weight=50, value=220
        // (1 - eps) * 220 = 176
        assertTrue(res.totalValue >= 176);
    }

    @Test
    @DisplayName("Parameterized Vertex Cover: Terminates on budget exhaustion and identifies valid covers")
    public void testParameterizedVertexCover() {
        // Cycle of length 4: 0-1-2-3-0
        // Optimal vertex cover size is 2 (e.g. {0, 2} or {1, 3})
        boolean[][] c4 = {
                {false, true, false, true},
                {true, false, true, false},
                {false, true, false, true},
                {true, false, true, false}
        };

        // With k = 1, cannot cover C4 -> budget exhaustion
        ParameterizedVertexCover.FPTResult failRes = ParameterizedVertexCover.solve(c4, 1);
        assertFalse(failRes.found);
        assertTrue(failRes.branchesExplored > 0);

        // With k = 2, succeeds
        ParameterizedVertexCover.FPTResult passRes = ParameterizedVertexCover.solve(c4, 2);
        assertTrue(passRes.found);
        assertEquals(2, passRes.vertexCover.size());
    }

    @Test
    @DisplayName("Polynomial Reductions: Reduction Zoo mapping invariants")
    public void testReductionZoo() {
        // 3-SAT to CLIQUE
        int[][] clauses = {{1, 2, 3}, {-1, -2, 3}};
        ReductionZoo.ReductionMapping satMap = ReductionZoo.reduce3SATtoClique(3, clauses);
        assertEquals("3-SAT -> CLIQUE", satMap.name);
        boolean[][] cliqueGraph = (boolean[][]) satMap.transformedInstance;
        assertEquals(6, cliqueGraph.length); // 2 clauses * 3 literals = 6 vertices

        // CLIQUE to INDEPENDENT SET
        boolean[][] triangle = {
                {false, true, true},
                {true, false, true},
                {true, true, false}
        };
        ReductionZoo.ReductionMapping isMap = ReductionZoo.reduceCliqueToIndependentSet(triangle, 3);
        boolean[][] comp = (boolean[][]) isMap.transformedInstance;
        // In complement of triangle (K3), all edges are absent
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertFalse(comp[i][j]);
            }
        }
    }
}
