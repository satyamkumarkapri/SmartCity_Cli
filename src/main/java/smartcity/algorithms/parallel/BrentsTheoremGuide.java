package smartcity.algorithms.parallel;

/**
 * Educational guide to Parallel Algorithm Analysis, Work, Span,
 * Brent's Theorem, and Amdahl's Law.
 */
public class BrentsTheoremGuide {

    public static String getParallelAnalysisOverview() {
        return """
        ================================================================================
         THEORETICAL PARALLEL ALGORITHM ANALYSIS: WORK, SPAN & LAWS
        ================================================================================
        1. Work (T_1):
           Total number of operations executed by the algorithm on a single processor.
           Corresponds to the standard sequential time complexity.

        2. Span / Critical Path Length (T_inf):
           The longest chain of sequential dependencies in the computation DAG.
           Represents the minimum execution time achievable with an infinite number of processors.

        3. Speedup (S_p) & Parallel Efficiency (E_p):
           - Speedup: S_p = T_1 / T_p  (Maximum theoretical speedup is p, called linear speedup).
           - Efficiency: E_p = S_p / p = T_1 / (p * T_p) <= 1.0 (or 100%).

        4. Brent's Theorem (Scheduling Theorem):
           For any computation DAG with work T_1 and span T_inf, the execution time T_p
           on p processors using a greedy/work-stealing scheduler satisfies:
               T_p <= (T_1 - T_inf) / p + T_inf < (T_1 / p) + T_inf

           Takeaway: As long as work dominates (T_1 / p >> T_inf), near-linear speedup is attained.

        5. Amdahl's Law:
           If a fraction s of an application is inherently sequential and cannot be parallelized:
               Speedup <= 1 / (s + (1 - s) / p) <= 1 / s
           Takeaway: Even with infinite processors, a 5% serial fraction caps max speedup at 20x.
        ================================================================================
        """;
    }

    public static String getOverview() {
        return getParallelAnalysisOverview();
    }
}

