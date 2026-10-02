package smartcityx.services;

import smartcityx.algorithms.np.*;
import smartcityx.util.ComplexityUtil;
import smartcityx.util.ConsoleUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintService {
    public void solveSATProblem() {
        System.out.println("Solving sample 3-SAT problem...\n");
        ComplexityUtil.display("3-SAT (Brute Force)", "O(2^N * C)", "O(2^N * C)", "O(2^N * C)", "O(N)");

        int numVars = 3;
        List<List<Integer>> clauses = new ArrayList<>();
        clauses.add(Arrays.asList(1, 2, 3));
        clauses.add(Arrays.asList(-1, 2, -3));

        long start = System.nanoTime();
        boolean satisfiable = ThreeSAT.solve3SAT(numVars, clauses);
        long end = System.nanoTime();

        System.out.println("Satisfiable: " + satisfiable);
        System.out.println("Execution Time: " + (end - start) + " ns");
    }

    public void solveGraphProblems() {
        System.out.println("Graph Constraint Problems (Exact algorithms are intended for small input sizes)\n");
        boolean[][] graph = {
            {false, true, true, false, false},
            {true, false, true, true, false},
            {true, true, false, false, true},
            {false, true, false, false, true},
            {false, false, true, true, false}
        };

        ComplexityUtil.display("Clique (Brute Force)", "O(2^N * N^2)", "O(2^N * N^2)", "O(2^N * N^2)", "O(1)");
        long start = System.nanoTime();
        boolean hasClique3 = Clique.hasClique(graph, 3);
        long end = System.nanoTime();
        System.out.println("Has Clique of size 3: " + hasClique3 + " | Time: " + (end - start) + " ns\n");

        ComplexityUtil.display("Independent Set (via Clique)", "O(2^N * N^2)", "O(2^N * N^2)", "O(2^N * N^2)", "O(N^2)");
        start = System.nanoTime();
        boolean hasIS2 = IndependentSet.hasIndependentSet(graph, 2);
        end = System.nanoTime();
        System.out.println("Has Independent Set of size 2: " + hasIS2 + " | Time: " + (end - start) + " ns\n");

        ComplexityUtil.display("Vertex Cover (Exact)", "O(2^N * E)", "O(2^N * E)", "O(2^N * E)", "O(N)");
        start = System.nanoTime();
        List<Integer> exactVC = VertexCover.findExactVertexCover(graph);
        end = System.nanoTime();
        System.out.println("Exact Vertex Cover: " + exactVC + " (Size: " + exactVC.size() + ") | Time: " + (end - start) + " ns\n");

        ComplexityUtil.display("Vertex Cover (2-Approximation)", "O(V + E)", "O(V + E)", "O(V + E)", "O(V)");
        start = System.nanoTime();
        List<Integer> approxVC = VertexCoverApproximation.findApproximateVertexCover(graph);
        end = System.nanoTime();
        System.out.println("Approximate Vertex Cover: " + approxVC + " (Size: " + approxVC.size() + ") | Time: " + (end - start) + " ns");
        System.out.println("Approximation Ratio: " + ((double) approxVC.size() / exactVC.size()));
    }
}
