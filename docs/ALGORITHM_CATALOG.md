# SmartCityX Algorithm Catalog

This catalog documents every algorithm implemented in **SmartCityX — Integrated Smart-City Analytics Platform**, spanning Modules CO1 through CO6. All listed algorithms are **100% fully implemented, verified with automated unit tests, and integrated into municipal workflows, the public QueryEngine, and the interactive CLI**.

---

## Module 2: String & Suffix Algorithms

| Algorithm | Key Class | Input Contract | Output Contract | Time Complexity | Auxiliary Space | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Naive Pattern Search** | `NaiveSearch` | Text $T$ ($N$ chars), Pattern $P$ ($M$ chars) | `IntArrayList` of zero-based starting match indices | Best: $O(N)$<br>Worst: $O(N \cdot M)$ | $O(1)$ | **100% Implemented** |
| **Knuth-Morris-Pratt (KMP)** | `KMP` | Text $T$, Pattern $P$ | `IntArrayList` of match indices, $\pi$ failure array | $O(N + M)$ | $O(M)$ | **100% Implemented** |
| **Z-Algorithm** | `ZAlgorithm` | Text $T$, Pattern $P$ | `IntArrayList` of match indices, $Z$-array values | $O(N + M)$ | $O(N + M)$ | **100% Implemented** |
| **Rabin-Karp** | `RabinKarp` | Text $T$, Pattern $P$, base $B=31$, prime $Q=10^9+7$ | `IntArrayList` of verified match indices, collision count | Expected: $O(N + M)$<br>Worst: $O(N \cdot M)$ | $O(1)$ | **100% Implemented** |
| **Aho-Corasick** | `AhoCorasick` | Text $T$, set of $K$ patterns $\{P_1, \dots, P_K\}$ | `DynamicArray<MatchResult>` with keyword ID, start pos | $O(N + \sum \|P_i\| + \text{matches})$ | $O(\sum \|P_i\| \cdot \Sigma)$ | **100% Implemented** |
| **Suffix Array Construction** | `SuffixArray` | Text $T$ ($N$ chars) | `int[]` of lexicographically sorted suffix indices | $O(N \log^2 N)$ | $O(N)$ | **100% Implemented** |
| **Suffix Array Pattern Search** | `SuffixArray` | Suffix Array, Text $T$, Pattern $P$ | `IntArrayList` of matches via binary search | $O(M \log N)$ | $O(1)$ | **100% Implemented** |
| **Kasai's LCP Algorithm** | `Kasai` | Text $T$, Suffix Array $SA$ | `int[]` of Longest Common Prefixes between adjacent suffixes | $O(N)$ | $O(N)$ | **100% Implemented** |
| **Suffix Automaton (DAWG)** | `SuffixAutomaton` | Text $T$ (built online per char) | Substring existence checks, distinct substring counts | Build: $O(N)$<br>Query: $O(M)$ | $O(N \cdot \Sigma)$ (max $2N$ states, $3N$ transitions) | **100% Implemented** |
| **Algorithm Comparison Engine**| `StringComparisonEngine` | Text $T$, Pattern $P$ | Side-by-side benchmark table, comparison counts, agreement flag | $O(N \cdot M)$ (bounded by Naive) | $O(N + M)$ | **100% Implemented** |

---

## Module 3: Advanced Dynamic Programming

| Algorithm | Key Class | Input Contract | Output Contract | Time Complexity | Auxiliary Space | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Levenshtein Distance** | `Levenshtein` | Source string $S_1$, Target string $S_2$ | Edit distance (int) and sequence of edit operations | $O(\|S_1\| \cdot \|S_2\|)$ | Standard: $O(\|S_1\| \cdot \|S_2\|)$<br>Optimized: $O(\min(N, M))$ | **100% Implemented** |
| **Damerau-Levenshtein (OSA)** | `DamerauLevenshtein` | String $S_1$, String $S_2$ | Optimal String Alignment distance with single transpositions | $O(\|S_1\| \cdot \|S_2\|)$ | $O(\|S_1\| \cdot \|S_2\|)$ | **100% Implemented** |
| **Damerau-Levenshtein (Unrestricted)**| `DamerauLevenshtein` | String $S_1$, String $S_2$ | True edit distance with unrestricted transpositions & insertions | $O(\|S_1\| \cdot \|S_2\|)$ | $O(\|S_1\| \cdot \|S_2\| + \Sigma)$ | **100% Implemented** |
| **Needleman-Wunsch Alignment** | `SequenceAlignment` | Strings $S_1, S_2$, match score, mismatch penalty, gap penalty | Global alignment score and paired aligned string traceback | $O(\|S_1\| \cdot \|S_2\|)$ | $O(\|S_1\| \cdot \|S_2\|)$ | **100% Implemented** |
| **Smith-Waterman Alignment** | `SequenceAlignment` | Strings $S_1, S_2$, scoring parameters | Local alignment score and highest-scoring local substrings | $O(\|S_1\| \cdot \|S_2\|)$ | $O(\|S_1\| \cdot \|S_2\|)$ | **100% Implemented** |
| **Matrix Chain Multiplication** | `MatrixChainMultiplication`| Array of matrix dimensions $p_0, \dots, p_N$ | Minimum scalar multiplications and full parenthesization string | $O(N^3)$ | $O(N^2)$ | **100% Implemented** |
| **Optimal Binary Search Tree** | `OptimalBST` | Keys, search frequencies $p_i$, unsucc. freqs $q_i$ | Minimum expected search cost and reconstructed BST root structure | $O(N^3)$ | $O(N^2)$ | **100% Implemented** |
| **Travelling Salesperson (Bitmask)** | `TravellingSalesperson` | Distance matrix ($N \times N$, $N \le 20$) | Minimum Hamiltonian cycle cost and optimal tour vertex order | $O(N^2 \cdot 2^N)$ | $O(N \cdot 2^N)$ | **100% Implemented** |
| **Hamiltonian Path (Bitmask)** | `HamiltonianPath` | Directed adjacency matrix ($N \le 20$) | Boolean solvability and valid full path vertex list | $O(N^2 \cdot 2^N)$ | $O(N \cdot 2^N)$ | **100% Implemented** |
| **Set Cover (Bitmask DP)** | `SetCoverBitmask` | Universe size $U \le 20$, subset collection | Minimum subsets to cover universe and selected indices | $O(M \cdot 2^U)$ | $O(2^U)$ | **100% Implemented** |
| **Tree Diameter DP** | `TreeDiameter` | Undirected tree graph | Longest path length in edges/weights, endpoints | $O(V)$ | $O(V)$ | **100% Implemented** |
| **Tree Knapsack DP** | `TreeKnapsack` | Rooted tree with weights, values, and budget | Maximum value attainable respecting subtree dependencies | $O(V \cdot W^2)$ | $O(V \cdot W)$ | **100% Implemented** |
| **Tree Rerooting DP** | `TreeRerooting` | Undirected tree | Sum of distances from *every* node to all other nodes | $O(V)$ (two DFS passes) | $O(V)$ | **100% Implemented** |
| **Sum Over Subsets (SOS DP)** | `SumOverSubsetsDP` | Array $A$ of size $2^N$ ($N \le 20$) | Array $F[\text{mask}] = \sum_{sub \subseteq mask} A[sub]$ | $O(N \cdot 2^N)$ | $O(2^N)$ | **100% Implemented** |

---

## Module 4: Network Flow & Scheduling

| Algorithm | Key Class | Input Contract | Output Contract | Time Complexity | Auxiliary Space | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Ford-Fulkerson** | `FordFulkerson` | Residual network, source $s$, sink $t$ | Maximum flow value, edge residual flows | $O(E \cdot \text{max\_flow})$ | $O(V + E)$ | **100% Implemented** |
| **Edmonds-Karp** | `EdmondsKarp` | Residual network, source $s$, sink $t$ | Maximum flow, BFS augmentation count | $O(V \cdot E^2)$ | $O(V)$ | **100% Implemented** |
| **Dinic's Algorithm** | `Dinic` | Residual network, source $s$, sink $t$ | Maximum flow via level graphs & blocking flows | $O(V^2 \cdot E)$ ($O(E \sqrt{V})$ for unit networks) | $O(V)$ | **100% Implemented** |
| **Min-Cut Computation** | `MinCut` | Residual network, source $s$, sink $t$ | Source-side set $S$, Cut edges, Capacity, Duality verified | $O(V + E)$ (post max-flow) | $O(V)$ | **100% Implemented** |
| **Maximum Bipartite Matching** | `BipartiteMatching` | Bipartite graph $L, R$, bipartite edges | Maximum matching cardinality, matched pairs | $O(V \cdot E)$ | $O(V)$ | **100% Implemented** |
| **Min-Cost Max-Flow (MCMF)** | `MinCostMaxFlow` | Network with capacities and edge costs | Maximum flow and minimum total cost (SPFA/Bellman-Ford) | $O(F \cdot V \cdot E)$ | $O(V)$ | **100% Implemented** |
| **Critical Path Method (CPM)** | `CriticalPathMethod` | Directed Acyclic Graph of tasks, durations | Critical path duration, Early/Late schedules, Slack, Critical path | $O(V + E)$ | $O(V)$ | **100% Implemented** |
| **Project Selection Flow** | `ProjectSelectionFlow` | Projects with profits, requirements with costs | Optimal subset of projects maximizing net profit | $O(V^2 \cdot E)$ via Min-Cut | $O(V + E)$ | **100% Implemented** |
| **Image Segmentation Flow** | `ImageSegmentationFlow` | Pixel grid, foreground/background likelihoods | Foreground/background segmentation mask | $O(V^2 \cdot E)$ via Min-Cut | $O(V + E)$ | **100% Implemented** |

---

## Module 5: NP-Completeness & Approximation

| Algorithm | Key Class | Input Contract | Output Contract | Complexity / Bound | Guarantee | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Vertex Cover 2-Approximation**| `VertexCoverApproximation` | Undirected graph $G=(V, E)$ | Selected vertex cover $C \subseteq V$ | $O(V + E)$ | $|C| \le 2 \cdot |C^*|$ (factor 2) | **100% Implemented** |
| **Knapsack FPTAS** | `KnapsackFPTAS` | Items (weights, values), budget $W$, precision $\varepsilon > 0$ | Selected items and approximated value | $O(N^3 / \varepsilon)$ | $(1 - \varepsilon) \cdot \text{OPT}$ | **100% Implemented** |
| **Parameterized Vertex Cover** | `ParameterizedVertexCover` | Graph $G=(V, E)$, parameter budget $k$ | Boolean solvability and cover of size $\le k$ | $O(2^k \cdot (V + E))$ | Exact FPT decision solver | **100% Implemented** |
| **Karp Reduction Zoo** | `ReductionZoo` | Problem instances (3-SAT, Clique, IS, VC) | Polynomial transformations and mapped solutions | Polynomial-time $O(N^c)$ | Sound and complete mapping | **100% Implemented** |
| **Complexity Classes Explorer** | `ComplexityClassesGuide` | Taxonomy requests (P, NP, co-NP, NP-Hard, NPC) | Formal definitions, verifiers, certificates | $O(1)$ lookup | Educational Reference | **100% Implemented** |

---

## Module 6: Randomized & Parallel Algorithms

| Algorithm | Key Class | Input Contract | Output Contract | Work ($T_1$) / Span ($T_\infty$) | Probabilistic Bound | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Randomized QuickSort** | `RandomizedQuickSort` | Array of comparable elements | In-place sorted array using random 3-way partition | $T_1 = O(N \log N)$ expected<br>Worst: $O(N^2)$ | Invariant: zero degradation on duplicates | **100% Implemented** |
| **Miller-Rabin Primality** | `MillerRabin` | Candidate integer $N$, rounds $k$ | Boolean prime verdict, witness value | $O(k \log^3 N)$ | Error $\le 4^{-k}$; 100% deterministic for $N < 2^{64}$ | **100% Implemented** |
| **Universal Randomized Hashing** | `RandomizedHashing` | Key integers, prime $P$, slot count $M$ | Hash slot index via $((ax + b) \bmod P) \bmod M$ | $O(1)$ per operation | Expected collisions per bucket $\le 1$ | **100% Implemented** |
| **Reservoir Sampling** | `ReservoirSampling` | Stream of unknown length $N$, sample size $K$ | Array of $K$ elements uniformly selected | $O(N)$ streaming | Every item selected with prob. $K/N$ | **100% Implemented** |
| **Parallel Reduction** | `ParallelReduce` | Array of numbers, associative operator (SUM, MIN, MAX) | Aggregated scalar result | Work: $O(N)$<br>Span: $O(\log N)$ | Sequential equivalence verified | **100% Implemented** |
| **Blelloch Exclusive Scan** | `BlellochScan` | Array of $N$ integers (padded to $2^k$) | Exclusive prefix sum array | Work: $O(N)$<br>Span: $O(\log N)$ | Preserves work-efficiency | **100% Implemented** |
| **Parallel Merge Sort** | `ParallelSort` | Array of comparable items | Sorted array using parallel merge sort | Work: $O(N \log N)$<br>Span: $O(\log^2 N)$ | Exact sort verification | **100% Implemented** |
| **Brent's Theorem Guide** | `BrentsTheoremGuide` | Work $T_1$, Span $T_\infty$, Processor count $P$ | Theoretical runtime bounds $T_P \le \frac{T_1 - T_\infty}{P} + T_\infty$ | $O(1)$ analysis | Amdahl & Brent model | **100% Implemented** |
