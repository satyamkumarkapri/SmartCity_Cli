package smartcity.algorithms.dynamicprogramming;

/**
 * Educational Foundations & Decision Guide for Dynamic Programming.
 * Explains state design, recurrence, tabulation vs memoization, and criteria for when NOT to use DP.
 */
public class DPDecisionGuide {

    public static String getFoundationsOverview() {
        return """
        ================================================================================
         DYNAMIC PROGRAMMING (DP) FOUNDATIONS & CORE TENETS
        ================================================================================
        1. Optimal Substructure:
           An optimal solution to the overall problem incorporates optimal solutions
           to its constituent subproblems.
           Examples: Shortest paths, matrix chain splits, edit distance.

        2. Overlapping Subproblems:
           A recursive algorithm revisits the same subproblems repeatedly rather than
           generating new subproblems indefinitely.
           Contrast: MergeSort has non-overlapping subproblems (Divide & Conquer).

        3. State Design:
           The state must represent all relevant historical information required to
           determine future optimal decisions, without redundant information.
           Rule of thumb: Minimize dimension and range of state variables.

        4. Tabulation (Bottom-Up) vs. Memoization (Top-Down):
           - Tabulation: Iterates iteratively through topological order of state DAG.
             Benefits: Cache friendly, no recursion stack limit, easy space optimization.
           - Memoization: Computes lazily via recursion with cache.
             Benefits: Only computes reachable states in sparse state spaces.

        5. State Reconstruction:
           Instead of saving entire sub-solutions inside each DP cell, record only the
           optimal decision/choice index, then backtrack from the final answer cell.
        ================================================================================
        """;
    }

    public static String getWhenNotToUseDP() {
        return """
        ================================================================================
         WHEN IS DYNAMIC PROGRAMMING INAPPROPRIATE?
        ================================================================================
        1. Greedy Choice Property Holds:
           If locally optimal choices provably lead to a globally optimal solution
           (e.g., Dijkstra's algorithm, Kruskal/Prim MST, Huffman coding, Fractional Knapsack),
           running O(N^2) or O(NW) DP is an unnecessary performance penalty.

        2. No Overlapping Subproblems:
           If subproblems are strictly disjoint (e.g., standard Binary Search, QuickSort,
           MergeSort), simple Divide-and-Conquer without memoization tables is optimal.

        3. Excessive or Continuous State Spaces:
           If state variables take continuous real values or combinatorial dimensions
           exceeding available RAM (e.g. 0/1 Knapsack with weight W = 10^12), pure DP fails.
           Remedies: Approximation schemes (FPTAS), Branch & Bound, or ILP solvers.

        4. Graph Problems with Cycles and Arbitrary Cost Structure:
           When transitions contain cyclic dependencies that violate topological ordering
           without monotonic progress, DAG-based DP recurrences cannot be evaluated.
           Remedies: Bellman-Ford, SPFA, or Floyd-Warshall.
        ================================================================================
        """;
    }
}
