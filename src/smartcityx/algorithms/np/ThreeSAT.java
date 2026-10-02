package smartcityx.algorithms.np;

import java.util.List;

public class ThreeSAT {
    public static boolean solve3SAT(int numVars, List<List<Integer>> clauses) {
        for (List<Integer> clause : clauses) {
            if (clause.size() != 3) {
                throw new IllegalArgumentException("Each clause must contain exactly 3 literals.");
            }
        }
        return SATSolver.solveSAT(numVars, clauses);
    }
}
