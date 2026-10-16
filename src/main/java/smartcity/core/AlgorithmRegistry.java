package smartcity.core;

import smartcity.structures.CustomHashMap;
import smartcity.structures.DynamicArray;

/**
 * Central catalog of every algorithm implemented in SmartCityX.
 * Maintains contracts, theoretical bounds, invariants, and implementation statuses.
 */
public class AlgorithmRegistry {
    private static final CustomHashMap<String, AlgorithmMetadata> REGISTRY = new CustomHashMap<>();
    private static final DynamicArray<AlgorithmMetadata> ALL_METADATA = new DynamicArray<>();

    static {
        // Module 2 — String Algorithms
        register(new AlgorithmMetadata(
                "naive", "Naive Pattern Matching", "Module 2: String Algorithms",
                "Find all occurrences of a pattern P of length m in a text T of length n.",
                "Slide a window of length m character by character across text T.",
                "Non-null String text T, non-null String pattern P.",
                "Zero-based list of all matching start indices (including overlapping).",
                "At step i, all substrings T[k..k+m-1] for k < i have been checked.",
                "Exhaustively verifies every possible starting alignment.",
                "O(n)", "O(n + m)", "O(n * m)", "O(1) excluding match results",
                "Inefficient on repetitive texts with heavy backtracking.", true
        ));

        register(new AlgorithmMetadata(
                "kmp", "Knuth-Morris-Pratt (KMP)", "Module 2: String Algorithms",
                "Linear-time exact pattern matching using a precomputed prefix function (pi / LPS).",
                "Avoid backtracking text index by shifting pattern according to longest proper prefix that is also a suffix.",
                "Non-null String text T, non-null String pattern P.",
                "Zero-based list of all matching start indices (including overlapping).",
                "j always equals the length of the longest proper prefix of P matching text ending at i-1.",
                "LPS array ensures no character comparisons are ever repeated in text.",
                "O(n)", "O(n + m)", "O(n + m)", "O(m) for the LPS failure table",
                "Precomputes LPS table of size m; overhead noticeable on very short patterns.", true
        ));

        register(new AlgorithmMetadata(
                "z_algorithm", "Z-Function String Matching", "Module 2: String Algorithms",
                "Constructs the Z-array where Z[i] is the length of the longest common prefix of S and S[i..].",
                "Maintains a [L, R] interval of the rightmost match segment to reuse previous match comparisons.",
                "Non-null String text T, non-null String pattern P.",
                "Complete Z-array and zero-based match positions via S = P + '$' + T.",
                "R is the maximum reach of any prefix match found so far.",
                "Each text character is matched at most once outside [L, R], bounding comparisons to 2N.",
                "O(n + m)", "O(n + m)", "O(n + m)", "O(n + m) for Z-array",
                "Requires a unique delimiter character not present in the pattern or text.", true
        ));

        register(new AlgorithmMetadata(
                "rabin_karp", "Rabin-Karp Rolling Hash", "Module 2: String Algorithms",
                "Multi-character hash comparison using a rolling polynomial hash.",
                "Compute hash of window in O(1) by subtracting outgoing character and adding incoming character.",
                "Non-null String text T, non-null String pattern P, modulus Q.",
                "List of all matching start indices with verification on hash match.",
                "Hash of window T[i..i+m-1] matches polynomial formula mod Q.",
                "Hash collision does not imply match; explicit char-by-char verification guarantees 100% correctness.",
                "O(n + m)", "O(n + m)", "O(n * m) under adversarial collisions", "O(1) auxiliary",
                "Worst-case O(nm) if hash function suffers frequent collisions.", true
        ));

        register(new AlgorithmMetadata(
                "aho_corasick", "Aho-Corasick Multi-Pattern Search", "Module 2: String Algorithms",
                "Simultaneous multi-keyword exact search in linear time.",
                "Construct a Trie of keywords, augmented with BFS failure links and dictionary output links.",
                "Text T and a set/array of pattern strings.",
                "Map of each pattern to its list of matching positions in T.",
                "Failure link points to the longest proper suffix of the current prefix that is also in the Trie.",
                "Automaton transitions process each character of T in amortized O(1) time.",
                "O(n + sum(m))", "O(n + sum(m))", "O(n + sum(m) + total_matches)", "O(sigma * sum(m))",
                "Trie memory footprint scales with alphabet size and pattern dictionary size.", true
        ));

        register(new AlgorithmMetadata(
                "suffix_array", "Suffix Array & LCP (Kasai)", "Module 2: String Algorithms",
                "Sorted array of all suffixes of text T, enabling O(m log n) substring queries.",
                "Prefix doubling O(n log^2 n) construction with Kasai's O(n) LCP computation.",
                "Non-null String text T.",
                "Sorted suffix index array SA and longest-common-prefix array LCP.",
                "SA[i] gives starting index of i-th lexicographically smallest suffix.",
                "Kasai's invariant: LCP of suffix at pos i+1 is at least LCP of suffix at pos i minus 1.",
                "O(n log n)", "O(n log^2 n)", "O(n log^2 n)", "O(n) auxiliary",
                "Memory overhead is several times the text size; SA-IS achieves linear time.", true
        ));

        register(new AlgorithmMetadata(
                "suffix_automaton", "Suffix Automaton (DAWG)", "Module 2: String Algorithms",
                "Directed acyclic word graph representing all substrings of a text compactly in O(n) states.",
                "Incremental character extension with suffix links connecting right-equivalence classes.",
                "Non-null String text T.",
                "Minimal automaton with at most 2n-1 states and 3n-4 transitions.",
                "Each state represents an equivalence class of substrings having identical endpos sets.",
                "Linearity proven by bound on endpos equivalence classes.",
                "O(n)", "O(n)", "O(n)", "O(n * sigma)",
                "Alphabet size sigma affects memory per state.", true
        ));

        // Module 3 — Advanced Dynamic Programming
        register(new AlgorithmMetadata(
                "levenshtein", "Levenshtein Distance & Reconstruction", "Module 3: Dynamic Programming",
                "Minimum edit operations (insert, delete, replace) to transform string S1 into S2.",
                "Wagner-Fischer 2D table recurrence with operation traceback.",
                "Strings s1 and s2, configurable operation costs.",
                "Minimum edit distance and list of alignment operations.",
                "dp[i][j] is the exact minimal edit distance between s1[0..i-1] and s2[0..j-1].",
                "Optimal substructure: minimal path to (i,j) must come from minimal path to subproblems.",
                "O(nm)", "O(nm)", "O(nm)", "O(min(n,m)) space optimized / O(nm) for full traceback",
                "O(nm) time prohibitive for strings with lengths in tens of thousands.", true
        ));

        register(new AlgorithmMetadata(
                "needleman_wunsch", "Needleman-Wunsch Global Alignment", "Module 3: Dynamic Programming",
                "Optimal global sequence alignment with match rewards, mismatch penalties, and gap penalties.",
                "Dynamic programming matrix with diagonal (match/mismatch), left (gap in S2), and up (gap in S1).",
                "Sequences S1, S2, match score, mismatch penalty, gap penalty.",
                "Max similarity score and reconstructed aligned sequences with dashes.",
                "Score matrix captures optimal score for prefix alignments.",
                "Standard dynamic programming optimality proof.",
                "O(nm)", "O(nm)", "O(nm)", "O(nm)",
                "Global alignment forces full length alignment, not suitable for finding local sub-regions.", true
        ));

        register(new AlgorithmMetadata(
                "smith_waterman", "Smith-Waterman Local Alignment", "Module 3: Dynamic Programming",
                "Optimal local sequence alignment for detecting highly conserved subsequences.",
                "Similar to Needleman-Wunsch but cell scores floored at 0; traceback begins at matrix maximum.",
                "Sequences S1, S2, match score, mismatch penalty, gap penalty.",
                "Highest local alignment score and optimal local sub-aligned strings.",
                "dp[i][j] >= 0; 0 denotes starting a new local alignment.",
                "Identifies highest scoring local alignment without penalizing unaligned surrounding prefixes/suffixes.",
                "O(nm)", "O(nm)", "O(nm)", "O(nm)",
                "O(nm) time complexity.", true
        ));

        register(new AlgorithmMetadata(
                "matrix_chain", "Matrix Chain Multiplication", "Module 3: Dynamic Programming",
                "Find optimal parenthesization of matrix product A1 * A2 * ... * An to minimize scalar multiplications.",
                "Interval DP computing optimal split point k for each sub-chain [i, j].",
                "Array p of dimensions where matrix i has size p[i-1] x p[i].",
                "Minimum multiplication cost and fully parenthesized formula string.",
                "m[i][j] = min_{i<=k<j} (m[i][k] + m[k+1][j] + p[i-1]*p[k]*p[j]).",
                "Matrix multiplication is associative; all parenthesizations yield the same final product.",
                "O(n^3)", "O(n^3)", "O(n^3)", "O(n^2)",
                "Only optimizes multiplication cost; assumes matrices are valid.", true
        ));

        register(new AlgorithmMetadata(
                "tsp_bitmask", "Travelling Salesperson Problem (Bitmask DP)", "Module 3: Dynamic Programming",
                "Held-Karp algorithm for exact minimum-cost Hamiltonian cycle visiting every city.",
                "Bitmask DP where state (mask, u) represents set of visited cities ending at vertex u.",
                "n x n cost matrix with fixed starting vertex.",
                "Minimum tour cost and reconstructed sequence of vertices.",
                "dp[mask][u] is the minimum path cost visiting exactly the subset 'mask' ending at 'u'.",
                "Every valid tour corresponds to an optimal path visiting the full mask (1<<n)-1.",
                "O(n^2 * 2^n)", "O(n^2 * 2^n)", "O(n^2 * 2^n)", "O(n * 2^n)",
                "Exponential scaling; strictly capped at n <= 20 to prevent memory exhaustion.", true
        ));

        // Module 4 — Network Flow
        register(new AlgorithmMetadata(
                "dinic", "Dinic's Maximum Flow Algorithm", "Module 4: Network Flow",
                "Computes maximum flow in a directed capacitated network using level graphs and blocking flows.",
                "BFS constructs level graph; DFS with work pointers pushes blocking flow along admissible edges.",
                "CustomFlowNetwork G, source s, sink t.",
                "Maximum flow value, residual flow on every edge, augmentation count.",
                "Level of sink strictly increases each BFS iteration; at most V iterations.",
                "Current-edge work pointer prevents re-examining saturated edges during blocking flow.",
                "O(V^2 * E)", "O(V * E^2)", "O(V^2 * E) (O(E * sqrt(V)) on unit networks)", "O(V + E)",
                "Capacities must be non-negative.", true
        ));

        register(new AlgorithmMetadata(
                "min_cut", "Minimum Cut & Max-Flow Min-Cut Theorem", "Module 4: Network Flow",
                "Partition vertices into (S, T) minimizing cut capacity, verified equal to max flow.",
                "Run max-flow, then find all vertices reachable from source in residual graph via BFS.",
                "CustomFlowNetwork G, source s, sink t.",
                "Cut capacity, list of cut edges, and formal verification of max-flow min-cut theorem.",
                "Any flow across cut (S, T) equals total net flow value; saturated edges form minimum cut.",
                "Duality theorem proven by absence of augmenting paths from S to T in residual graph.",
                "O(V^2 * E)", "O(V^2 * E)", "O(V^2 * E)", "O(V + E)",
                "Directly depends on maximum flow completion.", true
        ));

        register(new AlgorithmMetadata(
                "cpm", "Critical Path Method (Project Scheduling)", "Module 4: Network Flow & Scheduling",
                "Computes project makespan, earliest/latest task schedules, total float/slack, and critical path.",
                "Topological sort forward pass for ES/EF, backward pass for LF/LS.",
                "Directed acyclic task graph with durations.",
                "Makespan, critical path sequence, task float table.",
                "Tasks with zero slack are critical; delaying them delays the entire project.",
                "DAG topological ordering guarantees dependency satisfaction.",
                "O(V + E)", "O(V + E)", "O(V + E)", "O(V + E)",
                "Input must be an acyclic graph (no circular task dependencies).", true
        ));

        // Module 5 — NP-Completeness and Approximation
        register(new AlgorithmMetadata(
                "vertex_cover_approx", "Vertex Cover 2-Approximation", "Module 5: NP-Completeness",
                "Finds a vertex cover of size at most 2 * OPT in polynomial time.",
                "Greedily select an uncovered edge, add BOTH endpoints to cover, delete incident edges.",
                "Undirected graph G = (V, E).",
                "Vertex cover C of G and edge coverage verification.",
                "Selected edges form a maximal matching M; OPT must include at least one vertex from each edge in M.",
                "Size of cover is 2|M| <= 2 * OPT, guaranteeing factor 2 approximation.",
                "O(V + E)", "O(V + E)", "O(V + E)", "O(V)",
                "Approximation ratio is 2; does not guarantee minimum possible cover.", true
        ));

        register(new AlgorithmMetadata(
                "knapsack_fptas", "Knapsack FPTAS (Fully Polynomial Approximation)", "Module 5: NP-Completeness",
                "Approximates 0/1 Knapsack within (1 - epsilon) factor of optimal in polynomial time.",
                "Scale item values by factor K = (epsilon * maxVal) / n, then solve value-based DP table.",
                "Item weights, values, knapsack capacity W, approximation parameter epsilon (0 < eps <= 1).",
                "Selected items, total value >= (1 - eps) * OPT, and error guarantee.",
                "Rounding error per item is at most K; total error across n items is <= n * K = eps * maxVal <= eps * OPT.",
                "Guarantees (1 - eps) * OPT value.",
                "O(n^3 / epsilon)", "O(n^3 / epsilon)", "O(n^3 / epsilon)", "O(n^2 / epsilon)",
                "Memory and runtime scale inversely with epsilon.", true
        ));

        // Module 6 — Randomized and Parallel Algorithms
        register(new AlgorithmMetadata(
                "randomized_quicksort", "Randomized QuickSort", "Module 6: Randomized Algorithms",
                "In-place sorting using uniformly random pivot selection to avoid worst-case O(n^2).",
                "Pick random pivot index in [low, high], partition into <= and >= sections, recurse.",
                "Array of comparable items or primitive integers.",
                "Sorted array with comparison and swap instrumentation.",
                "Every element has equal probability 1/(high - low + 1) of being chosen as pivot.",
                "Expected recursion depth is O(log n); expected comparisons 2n ln n.",
                "O(n log n)", "O(n log n)", "O(n^2) (negligible probability)", "O(log n) stack",
                "Worst-case O(n^2) exists with probability < 1/2^n.", true
        ));

        register(new AlgorithmMetadata(
                "miller_rabin", "Miller-Rabin Primality Test", "Module 6: Randomized Algorithms",
                "Probabilistic Monte Carlo primality test with deterministic guarantees for 64-bit integers.",
                "Factor n-1 = 2^s * d. Check if a^d = 1 (mod n) or a^(2^r * d) = -1 (mod n).",
                "Integer n >= 2, number of witness rounds k or deterministic base set.",
                "Boolean isPrime with error bound <= 4^-k for random bases.",
                "If n is prime, it has no non-trivial square roots of 1 modulo n.",
                "At least 3/4 of bases in [2, n-2] are Miller-Rabin witnesses for composite n.",
                "O(k log^3 n)", "O(k log^3 n)", "O(k log^3 n)", "O(1)",
                "Probabilistic for arbitrary BigIntegers; 100% deterministic using known base sets for <= 64-bit numbers.", true
        ));

        register(new AlgorithmMetadata(
                "reservoir_sampling", "Reservoir Sampling (Algorithm R)", "Module 6: Randomized Algorithms",
                "Select a uniform random sample of size k from a stream of unknown or infinite length.",
                "Store first k elements; for i-th element (i > k), replace random reservoir element with prob k/i.",
                "Data stream iterator, sample size k.",
                "Array of k uniformly sampled items.",
                "At step n, each observed item has probability exactly k/n of being in the reservoir.",
                "Induction: P(item in reservoir at n) = k/n * (1 - 1/k * k/(n+1)) = k/(n+1).",
                "O(n)", "O(n)", "O(n)", "O(k)",
                "Single pass; memory O(k).", true
        ));

        register(new AlgorithmMetadata(
                "parallel_reduce", "Parallel Reduction (ForkJoin)", "Module 6: Parallel Algorithms",
                "Parallel associative reduction (Sum, Min, Max) over array using binary tree divide-and-conquer.",
                "Recursively split array into halves until threshold; invoke ForkJoin tasks in parallel.",
                "Array of numbers, reduction operator (Sum, Min, Max).",
                "Single reduced scalar result matching sequential reference.",
                "Associativity guarantees identical results regardless of evaluation tree topology.",
                "Work T1 = O(n), Span Tinf = O(log n), Brent's Theorem gives speedup.",
                "O(n / p + log n)", "O(n / p + log n)", "O(n)", "O(log n) call stack",
                "Overhead of task creation dominates for small arrays (below threshold).", true
        ));

        register(new AlgorithmMetadata(
                "blelloch_scan", "Blelloch Parallel Prefix Scan", "Module 6: Parallel Algorithms",
                "Work-efficient parallel exclusive prefix sum using up-sweep (reduce) and down-sweep tree passes.",
                "Up-sweep computes subtree sums; down-sweep distributes prefixes down from the root.",
                "Array of numbers.",
                "Exclusive prefix sum array where result[i] = sum(input[0..i-1]).",
                "After up-sweep, root holds total sum; after down-sweep, each node receives sum of preceding elements.",
                "Work T1 = O(n) (2n additions), Span Tinf = O(log n).",
                "O(n / p + log n)", "O(n / p + log n)", "O(n)", "O(n) auxiliary tree",
                "Requires array size to be padded to power of 2 internally.", true
        ));
    }

    private static void register(AlgorithmMetadata meta) {
        REGISTRY.put(meta.getId().toLowerCase(), meta);
        ALL_METADATA.add(meta);
    }

    public static AlgorithmMetadata get(String id) {
        if (id == null) return null;
        return REGISTRY.get(id.toLowerCase());
    }

    public static DynamicArray<AlgorithmMetadata> getAll() {
        return ALL_METADATA;
    }
}
