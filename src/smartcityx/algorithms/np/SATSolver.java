package smartcityx.algorithms.np;

import java.util.List;

public class SATSolver {
    // Represents a clause as a list of literals where +i means variable i is true, -i means false
    public static boolean solveSAT(int numVars, List<List<Integer>> clauses) {
        int maxMask = 1 << numVars;
        for (int mask = 0; mask < maxMask; mask++) {
            boolean[] assignment = new boolean[numVars + 1];
            for (int i = 0; i < numVars; i++) {
                assignment[i + 1] = (mask & (1 << i)) != 0;
            }

            boolean allSatisfied = true;
            for (List<Integer> clause : clauses) {
                boolean clauseSatisfied = false;
                for (int literal : clause) {
                    int var = Math.abs(literal);
                    boolean expected = literal > 0;
                    if (assignment[var] == expected) {
                        clauseSatisfied = true;
                        break;
                    }
                }
                if (!clauseSatisfied) {
                    allSatisfied = false;
                    break;
                }
            }

            if (allSatisfied) return true;
        }
        return false;
    }
}
