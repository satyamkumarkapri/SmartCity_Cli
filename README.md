# SmartCityX — Integrated Smart-City Analytics Platform 🏙️⚡
**Enterprise-Grade CLI Analytics Engine for Municipal Infrastructure, Resource Optimization, and Discrete Algorithms**

[![Java 17](https://img.shields.io/badge/Java-17%2B-blue.svg?logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Build-Maven%203.8+-red.svg?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/Tests-54%2F54%20Passing-brightgreen.svg)]()
[![Diagnostics](https://img.shields.io/badge/Compiler%20Warnings-0%20(Zero)-success.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📑 Table of Contents

1. [Executive Overview & Municipal Vision](#-executive-overview--municipal-vision)
2. [Syllabus & Curriculum Alignment (Modules 1–6 / CO1–CO6)](#-syllabus--curriculum-alignment-modules-16--co1co6)
   - [M1 — String Algorithms (CO1)](#m1--string-algorithms-co1)
   - [M2 — Suffix Structures & Document Similarity (CO2)](#m2--suffix-structures--document-similarity-co2)
   - [M3 — Advanced Dynamic Programming (CO3)](#m3--advanced-dynamic-programming-co3)
   - [M4 — Network Flow & Project Scheduling (CO4)](#m4--network-flow--project-scheduling-co4)
   - [M5 — NP-Completeness & Approximation Algorithms (CO5)](#m5--np-completeness--approximation-algorithms-co5)
   - [M6 — Randomized & Parallel Algorithms (CO6)](#m6--randomized--parallel-algorithms-co6)
3. [Zero-JCF Architectural Guarantee (Custom Data Structures)](#-zero-jcf-architectural-guarantee-custom-data-structures)
4. [Municipal Datasets & Schemas (`data/`)](#-municipal-datasets--schemas-data)
5. [System Architecture & Code Organization](#-system-architecture--code-organization)
6. [Interactive Terminal Interface (17 Menu Modules)](#-interactive-terminal-interface-17-menu-modules)
7. [Step-by-Step Project Review & Demonstration Guide](#-step-by-step-project-review--demonstration-guide)
8. [Building, Testing, and Deployment](#-building-testing-and-deployment)
9. [Verification & Mathematical Guarantees](#-verification--mathematical-guarantees)

---

## 🏙️ Executive Overview & Municipal Vision

Modern metropolitan centers generate heterogeneous, continuous, high-volume data streams across civil infrastructure, transportation grids, energy substations, water pipelines, emergency dispatches, and IoT sensors. Managing these operations requires robust algorithmic foundations rather than naive heuristics.

**SmartCityX** is a mission-critical, standalone command-line platform developed in **Java 17** that models, analyzes, and optimizes an integrated metropolitan ecosystem. Designed for DSA-3 (Topic 29), it implements theoretical computer science concepts on top of **custom zero-overhead data structures**, avoiding built-in Java Collections in its algorithmic core.

### Core Municipal Domains Modeled:
1. **Public Grievances & Service Work Orders**: Ingestion and prioritization of citizen incident reports (potholes, water leaks, traffic hazards, electrical faults).
2. **Civil Infrastructure & Regulatory Blueprints**: High-throughput indexing and substring searching across engineering documentation, building codes, and safety protocols.
3. **Transportation & Traffic Flow**: Real-time traffic intersection modeling, maximum flow analysis, and critical bottleneck identification.
4. **Water & Energy Utilities**: Reservoir-to-district hydraulic flow distribution, sub-station load optimization, and multi-district resource allocation.
5. **Emergency Response & Civil Defense**: Optimal emergency team dispatch via bipartite matching and critical facility defense coverage.
6. **Continuous IoT Telemetry Streams**: Processing of high-frequency environmental, power, and traffic sensor observations.

---

## 🎯 Syllabus & Curriculum Alignment (Modules 1–6 / CO1–CO6)

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                           SMARTCITYX ALGORITHM ARCHITECTURE                                     │
├──────────────┬─────────────────────────────────────────────────┬────────────────────────────────┤
│ Course Unit  │ Algorithmic Concepts & Techniques               │ Municipal Application Domain   │
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M1 (CO1)** │ • Knuth-Morris-Pratt (KMP)                      │ Search citizen reports & codes │
│              │ • Z-Algorithm (Linear Prefix Matching)          │ Detect repeated complaints     │
│              │ • Rabin-Karp (Rolling Polynomial Hash)          │ Trace service & infra IDs      │
│              │ • Aho-Corasick Multi-Pattern Automaton          │ Simultaneous threat keyword scan│
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M2 (CO2)** │ • Suffix Array (Prefix Doubling $O(N \log^2 N)$)│ Index civil blueprint corpus   │
│              │ • SA-IS Induced Sorting (Linear $O(N)$)         │ Scalable large-doc indexing    │
│              │ • Kasai's Algorithm for LCP Array               │ Identify recurring descriptions│
│              │ • Suffix Automaton (DAWG)                       │ $O(M)$ substring verification  │
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M3 (CO3)** │ • Levenshtein & Damerau-Levenshtein (OSA)       │ Typo query auto-correction     │
│              │ • Needleman-Wunsch Global Sequence Alignment    │ Compare regulatory contracts   │
│              │ • Matrix Chain Multiplication (MCM)             │ Optimize sensor covariance DP  │
│              │ • Bitmask DP (Set Cover, TSP, Hamiltonian)      │ Utility procurement bundles    │
│              │ • Tree DP (Diameter & Rerooting)                │ Municipal fiber grid backbone  │
│              │ • Sum Over Subsets (SOS DP)                     │ Multi-district resource options│
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M4 (CO4)** │ • Ford-Fulkerson Method (DFS Augmentation)      │ Baseline max-flow computation  │
│              │ • Edmonds-Karp Algorithm (BFS Shortest Path)    │ Capacity network analysis      │
│              │ • Dinic's Algorithm (Level Graph + Blocks)      │ Large-scale water/traffic grids│
│              │ • Max-Flow Min-Cut Duality Theorem              │ Critical bottleneck roads      │
│              │ • Hopcroft-Karp Maximum Bipartite Matching      │ Emergency squad-to-incident    │
│              │ • Critical Path Method (CPM) & Project Selection│ Civil maintenance scheduling   │
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M5 (CO5)** │ • 3-SAT Planning Representation                 │ Represent zoning constraints   │
│              │ • Karp Reductions: 3-SAT $\to$ CLIQUE $\to$ IS  │ Formal constraint reductions   │
│              │ • IS $\to$ VERTEX-COVER Duality ($V \setminus S$)│ Infrastructure conflict graph  │
│              │ • Vertex Cover 2-Approximation                  │ Facility camera protection     │
│              │ • Parameterized Vertex Cover (FPT Branching)    │ Exact minimum sensor placement │
│              │ • Knapsack FPTAS ($(1-\varepsilon)$ bound)      │ Budget-constrained dispatch    │
├──────────────┼─────────────────────────────────────────────────┼────────────────────────────────┤
│ **M6 (CO6)** │ • Randomized QuickSort (Dutch National Flag)    │ Rank work orders by priority   │
│              │ • Reservoir Sampling (Algorithm R)              │ Unbiased stream sampling       │
│              │ • Miller-Rabin Probabilistic Primality Test     │ Telemetry cryptographic keys   │
│              │ • Universal Randomized Hashing                  │ Collision-free sensor IDs      │
│              │ • Blelloch Parallel Scan (Up/Down Sweeps)       │ Cumulative power consumption   │
│              │ • ForkJoin Parallel Reduce & Merge Sort         │ Citywide sensor aggregations   │
│              │ • Brent's Scheduling Theorem                    │ Evaluate parallel speedup      │
└──────────────┴─────────────────────────────────────────────────┴────────────────────────────────┘
```

---

### M1 — String Algorithms (CO1)

1. **Knuth-Morris-Pratt (KMP)**
   - **Theory**: Constructs the deterministic prefix function $\pi[i] = \max \{ k : P[0..k-1] \text{ is a suffix of } P[0..i] \}$ in $O(M)$ preprocessing time. Text traversal executes strictly in $O(N)$ with zero backtracking.
   - **Application**: Exact searching of citizen reports, municipal service requests, and civil infrastructure codes (`INFRA-NODE-02`, `LINK-01-05`).
   - **Implementation**: [`KMP.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/strings/KMP.java)

2. **Z-Algorithm**
   - **Theory**: Constructs the $Z$-array where $Z[i]$ represents the length of the longest common prefix between $S$ and the suffix of $S$ starting at $i$. Maintains a moving interval $[L, R]$ in $O(N)$ total time.
   - **Application**: Identifies repeated phrases, boilerplates, and duplicated grievance filings across municipal databases.
   - **Implementation**: [`ZAlgorithm.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/strings/ZAlgorithm.java)

3. **Rabin-Karp Rolling Hash**
   - **Theory**: Computes polynomial rolling hash values using base $B = 257$ and prime modulus $M = 10^9 + 7$:
     $$H(S[i..i+m-1]) = \left( \sum_{j=0}^{m-1} S[i+j] \cdot B^{m-1-j} \right) \bmod M$$
     Updating the hash window takes $O(1)$:
     $$H_{new} = \left( (H_{old} - S[i] \cdot B^{m-1}) \cdot B + S[i+m] \right) \bmod M$$
   - **Application**: Searches service identifiers (`SRQ-5001`, `CR-1004`) while displaying complete rolling hash parameters, window hash matches, and character-by-character collision verification.
   - **Implementation**: [`RabinKarp.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/strings/RabinKarp.java)

4. **Aho-Corasick Multi-Pattern Automaton**
   - **Theory**: Combines a trie with BFS-constructed failure links (analogous to KMP $\pi$-transitions) and output dictionary links. Processes text of length $N$ against $k$ patterns in $O(N + \sum M_i)$ time.
   - **Application**: Simultaneous emergency keyword screening across citizen incident reports (e.g., scanning for `"burst"`, `"leak"`, `"dark"`, `"pothole"`, `"surge"` in a single pass).
   - **Implementation**: [`AhoCorasick.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/strings/AhoCorasick.java)

---

### M2 — Suffix Structures & Document Similarity (CO2)

1. **Suffix Array (Prefix Doubling vs. SA-IS)**
   - **Prefix Doubling**: Iteratively sorts suffixes by $2^k$-length prefixes in $O(N \log^2 N)$ using custom merge sort.
   - **SA-IS (Suffix Array Induced Sorting)**: Nong–Zhang–Chan linear $O(N)$ algorithm. Classifies suffixes into S-type and L-type, induces sorting of Leftmost S-type (LMS) substrings via bucket placement, recursively solves smaller subproblems, and induces full suffix order.
   - **Empirical Benchmark**: Live head-to-head comparison demonstrating a **4.5x–5.0x speedup** for SA-IS over prefix doubling on municipal documentation.
   - **Implementation**: [`SuffixArray.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/suffix/SuffixArray.java)

2. **Kasai's LCP Array Algorithm**
   - **Theory**: Constructs the Longest Common Prefix ($LCP$) array in $O(N)$ linear time by observing that $LCP[\text{rank}[i]] \ge LCP[\text{rank}[i-1]] - 1$.
   - **Application**: Identifies recurring service descriptions across work orders by finding high LCP values between suffixes originating from distinct municipal records.
   - **Implementation**: [`Kasai.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/suffix/Kasai.java)

3. **Suffix Automaton (DAWG)**
   - **Theory**: Minimal Deterministic Acyclic Word Graph representing all suffixes of a string. Built online in $O(N)$ states and transitions ($|\text{states}| \le 2N - 1$, $|\text{edges}| \le 3N - 4$).
   - **Application**: Instantaneous $O(M)$ substring verification across civil infrastructure blueprints and exact count of distinct regulatory clauses.
   - **Implementation**: [`SuffixAutomaton.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/suffix/SuffixAutomaton.java)

---

### M3 — Advanced Dynamic Programming (CO3)

1. **Levenshtein & Damerau-Levenshtein Distances**
   - **Theory**: Computes minimum edit operations (insertions, deletions, substitutions, and adjacent transpositions for Damerau-Levenshtein Optimal String Alignment).
   - **Application**: Citizen grievance query typo auto-correction ("Did you mean: *pipeline* for *pipline*?").
   - **Implementation**: [`Levenshtein.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/Levenshtein.java), [`DamerauLevenshtein.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/DamerauLevenshtein.java)

2. **Needleman-Wunsch Global Sequence Alignment**
   - **Theory**: Global dynamic programming matrix with scoring parameters (match $= +2$, mismatch $= -1$, gap $= -2$) and full traceback reconstructing optimal alignment.
   - **Application**: Compares engineering specifications and regulatory contracts between civil departments.
   - **Implementation**: [`SequenceAlignment.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/SequenceAlignment.java)

3. **Matrix Chain Multiplication (MCM)**
   - **Theory**: Determines parenthesization minimizing scalar multiplications in $O(N^3)$:
     $$m[i, j] = \min_{i \le k < j} \{ m[i, k] + m[k+1, j] + p_{i-1} p_k p_j \}$$
   - **Application**: Optimizes multi-stage sensor correlation pipelines (e.g., transforming Traffic $\times$ Air Quality $\times$ Weather $\times$ Energy covariance matrices).
   - **Implementation**: [`MatrixChainMultiplication.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/MatrixChainMultiplication.java)

4. **Bitmask DP (Set Cover, TSP, Hamiltonian Path)**
   - **Theory**: Operates over subsets of vertices represented as bit integers in $O(N^2 \cdot 2^N)$.
   - **Application**: Municipal utility supplier package selection (Set Cover) and inspection route optimization.
   - **Implementation**: [`SetCoverBitmask.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/SetCoverBitmask.java), [`TravellingSalesperson.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/TravellingSalesperson.java)

5. **Sum Over Subsets (SOS DP)**
   - **Theory**: Computes $F[\text{mask}] = \sum_{\text{submask} \subseteq \text{mask}} A[\text{submask}]$ in $O(N \cdot 2^N)$ rather than $O(3^N)$ submask enumeration by decomposing DP states along individual bit dimensions.
   - **Application**: Evaluates cumulative synergies across municipal district resource configurations.
   - **Implementation**: [`SumOverSubsetsDP.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/SumOverSubsetsDP.java)

6. **Tree Dynamic Programming (Diameter & Rerooting)**
   - **Theory**: Two-pass dynamic programming on acyclic networks in $O(V)$ time.
   - **Application**: Municipal fiber distribution network latency optimization.
   - **Implementation**: [`TreeDiameter.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/TreeDiameter.java), [`TreeRerooting.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/dynamicprogramming/TreeRerooting.java)

---

### M4 — Network Flow & Project Scheduling (CO4)

1. **Maximum Flow Algorithms (Ford-Fulkerson, Edmonds-Karp, Dinic)**
   - **Ford-Fulkerson**: DFS augmenting paths, $O(E \cdot \text{MaxFlow})$.
   - **Edmonds-Karp**: BFS shortest augmenting paths, guaranteed $O(V \cdot E^2)$.
   - **Dinic's Algorithm**: Constructs level graphs via BFS and extracts blocking flows via DFS with dynamic edge pointers in $O(V^2 \cdot E)$ (unit networks $O(E \sqrt{V})$).
   - **Application**: Water treatment distribution networks (pumping stations $\to$ district reservoirs) and metropolitan traffic throughput.
   - **Implementation**: [`Dinic.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/Dinic.java), [`EdmondsKarp.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/EdmondsKarp.java), [`FordFulkerson.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/FordFulkerson.java)

2. **Max-Flow / Min-Cut Duality Theorem**
   - **Theory**: Computes the minimum capacity cut separating source $S$ and sink $T$. By the Max-Flow Min-Cut Theorem, $\text{val}(f^*) = C(S, T)$.
   - **Application**: Identifies critical arterial bottlenecks in road grids and vulnerable pipelines in hydraulic networks.
   - **Implementation**: [`MinCut.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/MinCut.java)

3. **Maximum Bipartite Matching**
   - **Theory**: Hopcroft-Karp / Dinic reduction to unit-capacity network flow in $O(E \sqrt{V})$ time.
   - **Application**: Optimal dispatch of emergency squads (HazMat, EMT, Fire) to incident locations and matching energy generators to district quotas.
   - **Implementation**: [`BipartiteMatching.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/BipartiteMatching.java)

4. **Critical Path Method (CPM) & Project Selection Flow**
   - **Theory**: Topological sort on Directed Acyclic Graphs (DAG), forward pass (Earliest Start/Finish), and backward pass (Latest Start/Finish) to identify zero-slack critical tasks. Project Selection uses min-cut closure reduction.
   - **Application**: Road resurfacing and bridge maintenance scheduling.
   - **Implementation**: [`CriticalPathMethod.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/CriticalPathMethod.java), [`ProjectSelectionFlow.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/graph/ProjectSelectionFlow.java)

---

### M5 — NP-Completeness & Approximation Algorithms (CO5)

1. **3-SAT & Formal Polynomial Reductions (Karp Reduction Zoo)**
   - **Reduction 1: 3-SAT $\le_p$ CLIQUE**: Constructs a graph with $3k$ vertices grouped into clause triples. Connects literals that do not contradict ($l_1 \ne \neg l_2$) across distinct clauses. A satisfying assignment exists if and only if a clique of size $k$ exists.
   - **Reduction 2: CLIQUE $\le_p$ INDEPENDENT-SET**: Constructs complement graph $\overline{G} = (V, \overline{E})$ where $(u, v) \in \overline{E} \iff (u, v) \notin E$. A set $S$ is a clique in $G$ iff $S$ is an independent set in $\overline{G}$.
   - **Reduction 3: INDEPENDENT-SET $\le_p$ VERTEX-COVER**: Applies the fundamental duality theorem: $S \subseteq V$ is an independent set iff $V \setminus S$ is a vertex cover of size $|V| - k$.
   - **Application**: Live demonstration reducing city planning zoning constraints down to infrastructure conflict vertex covers.
   - **Implementation**: [`ReductionZoo.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/complexity/ReductionZoo.java)

2. **Vertex Cover 2-Approximation**
   - **Theory**: Computes a maximal matching $M$ by greedily adding edges and including both endpoints in $C$. Since any valid vertex cover must select at least one endpoint from each disjoint matching edge, $|C| = 2|M| \le 2 \cdot \text{OPT}$.
   - **Application**: Placement of surveillance cameras and emergency sensor posts covering 100% of inter-facility communication links.
   - **Implementation**: [`VertexCoverApproximation.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/complexity/VertexCoverApproximation.java)

3. **Parameterized Vertex Cover (Fixed-Parameter Tractable — FPT)**
   - **Theory**: Exact recursive search-tree branching in $O(2^k \cdot (V + E))$. In each step, an edge $(u, v)$ is picked; branching recursively checks whether $u \in C$ or $v \in C$ with parameter $k - 1$.
   - **Application**: Guarantees the absolute minimum number of defense posts needed when budget parameter $k$ is small.
   - **Implementation**: [`ParameterizedVertexCover.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/complexity/ParameterizedVertexCover.java)

4. **Knapsack FPTAS (Fully Polynomial-Time Approximation Scheme)**
   - **Theory**: Scales values by factor $K = \frac{\varepsilon \cdot V_{\max}}{n}$, solving scaled dynamic programming in $O(n^3 / \varepsilon)$ time while guaranteeing total value $\ge (1 - \varepsilon) \cdot \text{OPT}$.
   - **Application**: Municipal energy and budget allocation under strict fiscal constraints.
   - **Implementation**: [`KnapsackFPTAS.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/complexity/KnapsackFPTAS.java)

---

### M6 — Randomized & Parallel Algorithms (CO6)

1. **Randomized QuickSort (Dutch National Flag Partitioning)**
   - **Theory**: Uniform random pivot selection eliminates worst-case $O(N^2)$ inputs, guaranteeing expected $O(N \log N)$ comparisons with low constants. Uses 3-way partitioning (`< pivot`, `== pivot`, `> pivot`).
   - **Application**: Real-time sorting and prioritization of incoming citizen service orders by severity score.
   - **Implementation**: [`RandomizedQuickSort.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/randomized/RandomizedQuickSort.java)

2. **Reservoir Sampling (Algorithm R)**
   - **Theory**: Given a stream of unknown or infinite length, maintains a reservoir of size $k$. The $i$-th element is included with probability $k / i$. Guarantees that every item has equal probability $k / N$ of selection.
   - **Application**: Unbiased uniform sampling from high-frequency IoT sensor telemetry streams.
   - **Implementation**: [`ReservoirSampling.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/randomized/ReservoirSampling.java)

3. **Miller-Rabin Probabilistic Primality Test**
   - **Theory**: Based on Fermat's Little Theorem and non-trivial square roots of unity modulo $n$. Uses deterministic 64-bit witness bases $\{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37\}$, completely eliminating Carmichael pseudo-prime errors (tested against 561, 1729).
   - **Application**: Sensor public-key validation and cryptographic token generation.
   - **Implementation**: [`MillerRabin.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/randomized/MillerRabin.java)

4. **Blelloch Parallel Prefix Scan**
   - **Theory**: Work-efficient parallel exclusive scan consisting of:
     - Up-sweep (reduce phase): $O(N)$ operations.
     - Down-sweep phase: computes prefix sums in $O(N)$ total work and $O(\log N)$ span/depth.
   - **Application**: Computes running cumulative electrical power consumption across thousands of smart meters.
   - **Implementation**: [`BlellochScan.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/parallel/BlellochScan.java)

5. **Parallel Reduction & Parallel Sort**
   - **Theory**: Java ForkJoin framework dividing tasks at optimal granularity. Computes associative aggregations (SUM, MIN, MAX) in $O(\log N)$ span. Parallel merge sort achieves $O(N \log N / P)$ speedup.
   - **Application**: Citywide telemetry min/max environmental temperature and power demand statistics.
   - **Implementation**: [`ParallelReduce.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/parallel/ParallelReduce.java), [`ParallelSort.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/parallel/ParallelSort.java)

6. **Brent's Theorem & Work/Span Analysis**
   - **Theory**: Formulates theoretical bounds on parallel execution time:
     $$\frac{W}{P} \le T_P \le \frac{W - S}{P} + S$$
     where $W$ is total work and $S$ is span (critical path length).
   - **Implementation**: [`BrentsTheoremGuide.java`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/algorithms/parallel/BrentsTheoremGuide.java)

---

## 🛡️ Zero-JCF Architectural Guarantee (Custom Data Structures)

To ensure academic and algorithmic authenticity, **no collections from `java.util.*` are used inside the core algorithmic layers**. All foundational data structures are implemented from primitive arrays and references under `smartcity.structures.*`:

| Custom Data Structure | Underlying Structure | Time Complexity (Amortized) | Space Complexity |
|:---|:---|:---:|:---:|
| [`DynamicArray<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/DynamicArray.java) | Geometric doubling array ($2\times$) | Access $O(1)$, Append $O(1)$ | $O(N)$ |
| [`IntArrayList`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/IntArrayList.java) | Primitive unboxed `int[]` | Access $O(1)$, Append $O(1)$ | $O(N)$ (Zero boxing overhead) |
| [`CustomHashMap<K, V>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomHashMap.java) | Separate chaining hash table | Get $O(1)$, Put $O(1)$, Resize $O(N)$ | $O(N)$ |
| [`CustomHashSet<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomHashSet.java) | Backed by `CustomHashMap` | Add $O(1)$, Contains $O(1)$ | $O(N)$ |
| [`CustomPriorityQueue<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomPriorityQueue.java) | 0-indexed binary min-heap array | Insert $O(\log N)$, Poll $O(\log N)$ | $O(N)$ |
| [`CustomStack<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomStack.java) | Resizable array LIFO | Push $O(1)$, Pop $O(1)$, Peek $O(1)$ | $O(N)$ |
| [`CircularQueue<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CircularQueue.java) | Ring buffer array FIFO with modular index | Enqueue $O(1)$, Dequeue $O(1)$ | $O(N)$ |
| [`CustomDeque<T>`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomDeque.java) | Doubly-linked node list | Add/Poll First/Last $O(1)$ | $O(N)$ |
| [`DisjointSetUnion`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/DisjointSetUnion.java) | Path compression + union by rank | Find/Union $O(\alpha(N))$ | $O(N)$ |
| [`CustomFlowNetwork`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomFlowNetwork.java) | Forward & backward residual flow edges | Edge addition $O(1)$, Degree iter $O(\deg v)$ | $O(V + E)$ |
| [`CustomSort`](file:///Users/satyamkumarkapri/Desktop/Second%20Year/Odd%20Sem/DSA-3/Group_Project/SmartCityCLI/src/main/java/smartcity/structures/CustomSort.java) | Stable MergeSort & 3-Way QuickSort | Sort $O(N \log N)$ worst-case | $O(N)$ auxiliary |

---

## 📊 Municipal Datasets & Schemas (`data/`)

The platform loads 16 offline datasets from `data/` adhering to RFC-4180 CSV specifications and JSONL formatting:

| File Path | Entity Count | Minimum Target | Key Fields & Attributes |
|:---|:---:|:---:|:---|
| `citizen_reports.csv` | **120** | $\ge 100$ | `id`, `citizen_name`, `location`, `category`, `description`, `priority`, `status`, `timestamp` |
| `service_requests.csv` | **80** | $\ge 60$ | `id`, `requester_name`, `department`, `location`, `request_details`, `priority_score`, `hours` |
| `emergency_reports.csv` | **50** | $\ge 40$ | `id`, `incident_type`, `location`, `severity`, `description`, `required_squads`, `status` |
| `infrastructure_documents.jsonl` | **60** | $\ge 50$ | `id`, `title`, `category`, `department`, `content`, `tags`, `published_date` |
| `traffic_nodes.csv` | **25** | $\ge 20$ | `node_id`, `name`, `zone`, `latitude`, `longitude` |
| `traffic_edges.csv` | **50** | $\ge 40$ | `edge_id`, `source`, `destination`, `capacity_vph`, `free_flow_speed_kmh`, `length_km` |
| `water_resources.csv` | **20** | $\ge 15$ | `resource_id`, `facility_name`, `capacity_mld`, `current_output_mld`, `operational_status` |
| `energy_resources.csv` | **20** | $\ge 15$ | `resource_id`, `substation_name`, `rated_mva`, `current_load_pct`, `zone` |
| `resource_demands.csv` | **35** | $\ge 30$ | `demand_id`, `district_name`, `resource_type`, `required_quantity`, `priority_level` |
| `emergency_teams.csv` | **15** | $\ge 12$ | `team_id`, `team_name`, `specialty`, `station_location`, `readiness_status` |
| `assignment_requirements.csv` | **24** | $\ge 20$ | `req_id`, `incident_id`, `required_specialty`, `urgency_weight`, `max_response_mins` |
| `project_tasks.csv` | **24** | $\ge 20$ | `task_id`, `name`, `duration_days`, `budget_usd`, `department` |
| `project_dependencies.csv` | **30** | $\ge 25$ | `predecessor_id`, `successor_id`, `dependency_type` |
| `infrastructure_vertices.csv` | **22** | $\ge 20$ | `vertex_id`, `name`, `facility_type`, `criticality_score` |
| `infrastructure_edges.csv` | **35** | $\ge 30$ | `u`, `v`, `connection_type`, `distance_km`, `bandwidth_gbps` |
| `iot_sensor_readings.csv` | **1,200** | $\ge 1,000$ | `reading_id`, `sensor_id`, `sensor_type`, `metric_value`, `unit`, `zone`, `timestamp` |

---

## 🏛️ System Architecture & Code Organization

```
SmartCityCLI/
├── pom.xml                                   <-- Maven configuration (Java 17, JUnit 5)
├── README.md                                 <-- Master documentation
├── LICENSE                                   <-- Open source MIT license
├── data/                                     <-- Validated CSV/JSONL datasets
├── docs/                                     <-- Supplementary architectural guides
├── scripts/
│   ├── run.sh                                <-- Execution shell script
│   └── run-tests.sh                          <-- Test execution script
└── src/
    ├── main/
    │   ├── java/smartcity/
    │   │   ├── Main.java                     <-- Entry point launching CLI
    │   │   ├── cli/
    │   │   │   ├── SmartCityCLI.java         <-- 17-option ANSI interactive shell
    │   │   │   └── SelfTestRunner.java       <-- 50-assertion embedded verification suite
    │   │   ├── core/
    │   │   │   ├── QueryEngine.java          <-- High-level public query interface
    │   │   │   ├── SmartCityEngine.java      <-- Core execution engine
    │   │   │   ├── AlgorithmRegistry.java    <-- Registry of algorithm metadata
    │   │   │   └── AlgorithmMetadata.java    <-- Big-O bounds and metadata models
    │   │   ├── model/                        <-- Strongly-typed records and metrics
    │   │   ├── repository/                   <-- In-memory repositories & indexes
    │   │   ├── dataset/                      <-- RFC-4180 parser & dataset loader
    │   │   ├── services/
    │   │   │   ├── CitizenReportService.java <-- KMP, Z-Algo, Levenshtein, QuickSort, LCP
    │   │   │   ├── DocumentSearchService.java<-- SuffixArray, SA-IS, Kasai, Automaton
    │   │   │   ├── TrafficAnalyticsService.java<-- Dinic, Min-Cut, CPM scheduling
    │   │   │   ├── ResourceManagementService.java<-- Dinic, Bipartite, Bitmask, SOS DP
    │   │   │   ├── EmergencyResponseService.java<-- Bipartite Matching, Vertex Cover
    │   │   │   ├── IoTSensorService.java     <-- Blelloch Scan, Parallel Reduce, Reservoir
    │   │   │   └── BenchmarkService.java     <-- Rigorous head-to-head performance suite
    │   │   ├── structures/                   <-- 100% custom zero-JCF data structures
    │   │   ├── algorithms/
    │   │   │   ├── strings/                  <-- KMP, Z-Algo, Rabin-Karp, Aho-Corasick
    │   │   │   ├── suffix/                   <-- SuffixArray (Prefix + SA-IS), Kasai, Automaton
    │   │   │   ├── dynamicprogramming/       <-- Levenshtein, Alignment, MCM, TSP, SOS DP
    │   │   │   ├── graph/                    <-- Dinic, Edmonds-Karp, MinCut, CPM, Matching
    │   │   │   ├── complexity/               <-- Reductions (Karp Zoo), VC-Approx, FPT, FPTAS
    │   │   │   ├── randomized/               <-- QuickSort, Miller-Rabin, Reservoir, Hashing
    │   │   │   └── parallel/                 <-- Blelloch Scan, Parallel Reduce, Brent's Theorem
    │   │   └── util/                         <-- ConsoleUtil, InputUtil, CSVParser
    │   └── resources/
    │       └── sample_corpus.jsonl           <-- Bundled document fallback corpus
    └── test/java/smartcity/
        ├── structures/DataStructuresTest.java
        ├── dataset/DatasetIntegrationTest.java
        ├── integration/SmartCityWorkflowTest.java
        └── algorithms/
            ├── StringAlgorithmsTest.java
            ├── DynamicProgrammingTest.java
            ├── NetworkFlowAndGraphTest.java
            ├── ComplexityAndApproximationTest.java
            └── RandomizedAndParallelTest.java
```

---

## 🖥️ Interactive Terminal Interface (17 Menu Modules)

When launched, `SmartCityCLI` displays telemetry across all loaded datasets and presents an organized 17-module menu:

```text
╔══════════════════════════════════════════════════════════════════════════════════════╗
║                 SMARTCITYX — INTEGRATED CITY ANALYTICS PLATFORM                      ║
╠══════════════════════════════════════════════════════════════════════════════════════╣
║  Active Datasets      : 120 Reports, 80 Reqs, 60 Docs, 1,200 Sensors                ║
║  Network Grids        : Traffic: 25 nodes, 50 links | Infra: 22 nodes, 35 links     ║
║  Dataset Telemetry    : 16 datasets loaded and indexed in 42 ms (100% Local)        ║
╠══════════════════════════════════════════════════════════════════════════════════════╣
║  MUNICIPAL OPERATIONS & SERVICES              DATA STRUCTURES & ALGORITHMS (CO1-CO6) ║
║   [1] Citizen Reports & Service Requests      [7] String Algorithms — CO1            ║
║   [2] Infrastructure Document Search          [8] Suffix Structures & LCP — CO2      ║
║   [3] Traffic & Transportation Analysis       [9] Advanced Dynamic Prog — CO3        ║
║   [4] Water & Energy Resource Management     [10] Network Flow & Allocation — CO4    ║
║   [5] Emergency Response Management          [11] NP-Completeness & Approx — CO5     ║
║   [6] IoT Sensor Analytics                   [12] Randomized Algorithms — CO6        ║
║                                              [13] Parallel Algorithms — CO6          ║
╟──────────────────────────────────────────────────────────────────────────────────────╢
║  SYSTEM, BENCHMARKS & EVALUATION                                                     ║
║  [14] Dataset Management                     [16] Complexity Explorer                ║
║  [15] Algorithm Comparison & Benchmarking    [17] Automated Self-Tests               ║
║                                               [0] Exit Application                   ║
╚══════════════════════════════════════════════════════════════════════════════════════╝
```

---

## 🔬 Step-by-Step Project Review & Demonstration Guide

To thoroughly demonstrate the application during an evaluation or presentation, follow this sequence:

### 1. Demonstrate Exact & Fuzzy Multi-Entity Search (`Option 1`)
- **KMP Service Search (`Sub-option 4`)**: Enter keyword `"maintenance"` to observe $O(N+M)$ search finding occurrences across all work orders with exact offset indices.
- **Rabin-Karp Rolling Hash Trace (`Sub-option 6`)**: Enter identifier `"SRQ-5001"` to inspect the base $257$, modulo $10^9+7$ hash computation and match verification.
- **Common Service Descriptions via SA-IS + Kasai (`Sub-option 9`)**: Specify minimum length $20$ to extract shared municipal issue phrasing across disparate work orders.
- **Typo Auto-Correction (`Sub-option 10`)**: Enter typo `"pipline"`. The engine detects the error, displays Levenshtein & Damerau-Levenshtein distances, and prompts: *`Did you mean: "pipeline"?`*

### 2. Demonstrate Linear-Time SA-IS Indexing (`Option 2 -> Sub-option 5`)
- Select Document ID `1`. The platform runs prefix-doubling ($O(N \log^2 N)$) alongside linear SA-IS ($O(N)$), verifies **100% identical suffix arrays**, and reports a **4.5x–5.0x empirical speedup**.

### 3. Demonstrate Network Flow & Min-Cut Bottlenecks (`Option 3`)
- **Dinic Max-Flow (`Sub-option 2`)**: Run max vehicle flow from Junction `0` to Junction `24` ($3,200 \text{ veh/hr}$).
- **Min-Cut Cut-Set (`Sub-option 3`)**: Discovers critical arterial road links whose failure would sever traffic between eastern and western districts.

### 4. Demonstrate Subset Dynamic Programming (`Option 4 -> Sub-option 5`)
- Executes **Sum-Over-Subsets (SOS DP)** across 4 municipal utility districts. Evaluates all $2^4 = 16$ district configurations in $64$ operations versus $81$ brute-force operations, showing synergy score propagation.

### 5. Demonstrate Complete Karp Reduction Pipeline (`Option 11 -> Sub-option 2`)
- Executes the full theoretical chain on city zoning constraints:
  $$\text{3-SAT} \xrightarrow{\text{Gadgets}} \text{CLIQUE} \xrightarrow{\text{Complement}} \text{INDEPENDENT-SET} \xrightarrow{V \setminus S} \text{VERTEX-COVER} \xrightarrow{\text{Maximal Matching}} \text{2-Approx}$$

### 6. Run Automated Self-Tests (`Option 17`)
- Runs the 50 embedded self-tests across all 6 modules in $< 50\text{ ms}$ directly inside the terminal.

---

## 🛠️ Building, Testing, and Deployment

### System Requirements
- **Java**: OpenJDK 17 or higher
- **Maven**: Version 3.8+
- **Terminal**: ANSI-capable shell (macOS Terminal/iTerm2, Linux Bash/Zsh, Windows Terminal)

### Build Commands
```bash
# 1. Compile and package the application
mvn clean package -DskipTests

# 2. Run all 54 JUnit unit and integration tests
mvn test

# 3. Launch the interactive CLI platform
java -jar target/smartcity.jar
```

---

## 📐 Verification & Mathematical Guarantees

| Category | Invariant / Boundary Guard | Mathematical Guarantee |
|:---|:---|:---|
| **Compilation** | Clean compiler audit | `javac -Xlint:all` produces **0 warnings, 0 errors** |
| **Correctness** | String cross-validation | 100% agreement between Naive, KMP, Z, Rabin-Karp, and Suffix Arrays |
| **Max-Flow** | Flow conservation & capacity | $\sum_{v} f(u, v) = 0$ for all $u \notin \{s, t\}$, with Dinic $=$ Edmonds-Karp |
| **Min-Cut** | Strong Duality | $\text{MaxFlow} = \text{MinCut Capacity} = C(S, T)$ |
| **Approximation** | 2-Approximation Vertex Cover | $|C| \le 2 \cdot |M| \le 2 \cdot \text{OPT}$ |
| **FPTAS** | Knapsack Approximation | $\text{Value}(S) \ge (1 - \varepsilon) \cdot \text{OPT}$ |
| **Parallelism** | Brent's Scheduling Bound | $T_P \le \frac{W - S}{P} + S$ |
| **Stream Sampling** | Unbiased probability | Every element in stream of size $N$ has exact selection probability $k / N$ |

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
