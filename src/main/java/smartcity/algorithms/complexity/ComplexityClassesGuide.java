package smartcity.algorithms.complexity;

/**
 * Educational guide to Computational Complexity Classes, Cook-Levin Theorem,
 * and NP-Hardness Recognition Heuristics.
 */
public class ComplexityClassesGuide {

    public static String getComplexityClassesOverview() {
        return """
        ================================================================================
         COMPUTATIONAL COMPLEXITY CLASSES & DEFINITIONS
        ================================================================================
        - P (Polynomial Time):
          The class of decision problems solvable by a deterministic Turing machine
          in time O(n^c) for some constant c.
          Examples: 2-SAT, Shortest Path, Maximum Flow, Minimum Spanning Tree.

        - NP (Nondeterministic Polynomial Time):
          The class of decision problems whose candidate 'yes' certificates can be
          VERIFIED in polynomial time by a deterministic Turing machine.
          Examples: 3-SAT, Hamiltonian Path, Traveling Salesperson (decision), Clique.

        - co-NP:
          The class of decision problems whose 'no' instances have polynomial-time
          verifiable certificates (e.g., TAUTOLOGY, Graph Non-Isomorphism).

        - NP-Hard:
          A problem H is NP-hard if every problem in NP can be polynomial-time reduced to H
          (H is at least as hard as the hardest problems in NP). H need not be in NP itself.

        - NP-Complete:
          A problem C is NP-complete if:
          1. C is in NP, AND
          2. C is NP-hard.
          If ANY NP-complete problem can be solved in polynomial time, then P = NP.
          Open Problem: Whether P = NP remains unresolved (Clay Millennium Prize).

        - The Cook-Levin Theorem (1971-1973):
          Independently proved that the Boolean Satisfiability Problem (SAT) is NP-complete
          by directly simulating an arbitrary nondeterministic polynomial-time Turing machine
          as a polynomial-sized Boolean propositional formula in CNF.
        ================================================================================
        """;
    }

    public static String getNPHardnessDiagnosticGuide() {
        return """
        ================================================================================
         NP-HARDNESS RECOGNITION & PROBLEM DIAGNOSTIC HEURISTICS
        ================================================================================
        When facing an unfamiliar optimization or decision problem, check for these
        well-known combinatorial structures:

        1. Subset Selection with Target Constraints:
           - Symptom: Selecting a subset of integers/items achieving a target sum or capacity.
           - Likely Core: SUBSET-SUM, KNAPSACK, or PARTITION.
           - Strategy: Check if numbers are small (use pseudo-polynomial DP) or implement FPTAS.

        2. Permutation / Sequential Ordering with Global Visiting:
           - Symptom: Finding an ordering of vertices/tasks visiting each element exactly once.
           - Likely Core: HAMILTONIAN PATH / CYCLE, TSP.
           - Strategy: Bitmask DP for N <= 20; heuristics (Christofides 1.5-approx, 2-opt) for large N.

        3. Global Set Covering / Facility Location:
           - Symptom: Choosing minimum items such that every requirement is satisfied by at least one.
           - Likely Core: SET COVER, VERTEX COVER, DOMINATING SET.
           - Strategy: 2-approximation for Vertex Cover; O(ln N) greedy for Set Cover; FPT branch-and-bound.

        4. Mutually Compatible Subset Selection:
           - Symptom: Finding the largest group of items that do not conflict with each other.
           - Likely Core: INDEPENDENT SET, CLIQUE.
           - Strategy: Reductions, branch-and-bound, or color-based branch-and-price.

        5. Precedence Constraints with Resource Capacities:
           - Symptom: Scheduling jobs with DAG dependencies under bounded workers.
           - Likely Core: PRECEDENCE-CONSTRAINED SCHEDULING (NP-complete for >= 3 processors).
        ================================================================================
        """;
    }

    public static String getComprehensiveOverview() {
        return getComplexityClassesOverview() + "\n" + getNPHardnessDiagnosticGuide();
    }
}

