package smartcity.algorithms.complexity;

/**
 * The Polynomial-Time Reduction Zoo.
 * Demonstrates formal transformations between foundational NP-complete problems:
 * 1. 3-SAT -> CLIQUE
 * 2. CLIQUE -> INDEPENDENT SET
 * 3. INDEPENDENT SET -> VERTEX COVER
 * 4. SUBSET-SUM -> 0/1 KNAPSACK
 * 5. HAMILTONIAN CYCLE -> TRAVELLING SALESPERSON (TSP)
 */
public class ReductionZoo {

    public static class ReductionMapping {
        public final String name;
        public final String sourceProblem;
        public final String targetProblem;
        public final String transformationLogic;
        public final String equivalenceProof;
        public final Object transformedInstance;

        public ReductionMapping(String name, String src, String tgt, String logic, String proof, Object instance) {
            this.name = name;
            this.sourceProblem = src;
            this.targetProblem = tgt;
            this.transformationLogic = logic;
            this.equivalenceProof = proof;
            this.transformedInstance = instance;
        }

        @Override
        public String toString() {
            return String.format("""
            ================================================================================
             REDUCTION: %s
            ================================================================================
             Source Problem : %s
             Target Problem : %s
            --------------------------------------------------------------------------------
             Transformation Mapping:
             %s

             Equivalence Argument (f(x) is YES iff x is YES):
             %s
            ================================================================================
            """, name, sourceProblem, targetProblem, transformationLogic, equivalenceProof);
        }
    }

    /**
     * Reduces 3-SAT with k clauses to CLIQUE of size k.
     */
    public static ReductionMapping reduce3SATtoClique(int numVars, int[][] clauses) {
        int k = clauses.length;
        int totalVertices = k * 3;
        boolean[][] graph = new boolean[totalVertices][totalVertices];

        for (int c1 = 0; c1 < k; c1++) {
            for (int l1 = 0; l1 < 3; l1++) {
                int u = c1 * 3 + l1;
                int lit1 = clauses[c1][l1];

                for (int c2 = c1 + 1; c2 < k; c2++) {
                    for (int l2 = 0; l2 < 3; l2++) {
                        int v = c2 * 3 + l2;
                        int lit2 = clauses[c2][l2];

                        // Connect iff from different clauses and literals are not contradictory
                        if (lit1 != -lit2) {
                            graph[u][v] = true;
                            graph[v][u] = true;
                        }
                    }
                }
            }
        }

        String logic = String.format("For each of the %d clauses, create 3 vertices representing its literals (Total %d vertices).\n" +
                "Draw an undirected edge between two vertices (c1, l1) and (c2, l2) iff c1 != c2 and lit1 != -lit2.", k, totalVertices);

        String proof = String.format("If the formula is satisfiable, choose one true literal per clause; these %d vertices share edges " +
                "(since true literals never contradict), forming a k-clique of size %d.\n" +
                "Conversely, any k-clique contains exactly one vertex per clause with non-contradictory literals, yielding a satisfying assignment.", k, k);

        return new ReductionMapping("3-SAT -> CLIQUE", "3-SAT (Formula with " + k + " clauses)",
                "CLIQUE (Graph with " + totalVertices + " vertices, target clique size k = " + k + ")",
                logic, proof, graph);
    }

    /**
     * Reduces CLIQUE of size k on G to INDEPENDENT SET of size k on complement graph G'.
     */
    public static ReductionMapping reduceCliqueToIndependentSet(boolean[][] graph, int k) {
        int n = graph.length;
        boolean[][] complement = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    complement[i][j] = !graph[i][j];
                }
            }
        }

        String logic = String.format("Construct the graph complement G' = (V, E') where e in E' iff e not in E (for %d vertices).", n);
        String proof = "A subset S of vertices forms a clique in G iff every pair in S is connected in G, " +
                "which means no pair in S is connected in G', which is precisely the definition of an Independent Set in G'.";

        return new ReductionMapping("CLIQUE -> INDEPENDENT SET", "CLIQUE of size k=" + k,
                "INDEPENDENT SET of size k=" + k + " on Complement Graph G'", logic, proof, complement);
    }

    /**
     * Reduces INDEPENDENT SET of size k on G to VERTEX COVER of size |V| - k on G.
     */
    public static ReductionMapping reduceIndependentSetToVertexCover(int numVertices, int k) {
        int targetVCSize = numVertices - k;
        String logic = String.format("Input graph G = (V, E) is preserved without modification. Target size is mapped from k = %d to k' = |V| - k = %d.", k, targetVCSize);
        String proof = "In any graph G = (V, E), a subset S subseteq V is an independent set if and only if V \\ S is a vertex cover.\n" +
                "Proof: If V \\ S were not a vertex cover, there would exist an edge (u, v) with neither u nor v in V \\ S, " +
                "meaning both u and v are in S, violating that S is independent.";

        return new ReductionMapping("INDEPENDENT SET -> VERTEX COVER",
                "INDEPENDENT SET of size k=" + k, "VERTEX COVER of size k'=" + targetVCSize, logic, proof, targetVCSize);
    }

    /**
     * Reduces SUBSET-SUM to 0/1 KNAPSACK.
     */
    public static ReductionMapping reduceSubsetSumToKnapsack(int[] numbers, int target) {
        String logic = String.format("For each of the %d integers in S, create a knapsack item with weight w_i = S_i and value v_i = S_i.\n" +
                "Set the knapsack capacity W = %d and target value threshold V = %d.", numbers.length, target, target);
        String proof = "A packing into the knapsack has total weight <= target and total value >= target iff the selected items " +
                "have both total weight and total value exactly equal to target, solving the SUBSET-SUM instance.";

        return new ReductionMapping("SUBSET-SUM -> 0/1 KNAPSACK", "SUBSET-SUM with target " + target,
                "0/1 KNAPSACK with capacity W = " + target + " and target value V = " + target, logic, proof, target);
    }

    /**
     * Reduces HAMILTONIAN CYCLE to TSP.
     */
    public static ReductionMapping reduceHamiltonianCycleToTSP(boolean[][] adj) {
        int n = adj.length;
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) dist[i][j] = 0;
                else dist[i][j] = adj[i][j] ? 1.0 : 2.0;
            }
        }
        String logic = "Create a complete distance matrix where dist[u][v] = 1 if edge (u, v) in E, else dist[u][v] = 2. Set TSP cost target = |V|.";
        String proof = "A Hamiltonian cycle visits all |V| vertices using only original graph edges of weight 1, " +
                "giving total cost |V|. Any non-Hamiltonian tour must use at least one non-edge of weight 2, yielding total cost >= |V| + 1.";

        return new ReductionMapping("HAMILTONIAN CYCLE -> TSP", "HAMILTONIAN CYCLE in G = (V, E)",
                "TSP on complete metric graph with cost target = " + n, logic, proof, dist);
    }
}
