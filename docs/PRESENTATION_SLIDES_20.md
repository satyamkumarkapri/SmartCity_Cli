# SmartCityX — 20-Slide Project Presentation Deck 🏙️⚡
**Comprehensive Slide-by-Slide Defense and Walkthrough for DSA-3 Group Project Evaluation**

---

## Slide 1: Title Slide & Project Overview
- **Header**: SmartCityX — Integrated Smart-City Analytics Platform
- **Sub-header**: Enterprise-Grade Decision Support & Algorithm Engine for Municipal Operations
- **Course**: Data Structures and Algorithms III (DSA-3) — Topic 29
- **Tech Stack**: Pure Java 17, Apache Maven, JUnit 5, Zero-JCF Core Engine, ANSI CLI Terminal
- **Presenter**: Project Development Team
- **Core Vision**:
  - Bridging advanced discrete mathematical theory and high-stakes municipal operations.
  - A unified platform that models, monitors, and optimizes heterogeneous metropolitan data streams: traffic networks, water/power utilities, civil infrastructure blueprints, citizen grievances, emergency squads, and continuous IoT sensors.
- **Key Architectural Milestone**:
  - **100% Zero-JCF**: All core algorithmic abstractions run on custom-engineered primitive data structures without using `java.util` collections.
  - **100% Warning-Free**: Strict compiler verification (`javac -Xlint:all` produces 0 warnings and 0 errors).
  - **Robust Verification**: 54/54 automated JUnit tests and 50 embedded terminal self-test assertions.
- **Speaker Notes**:
  > *"Good morning professors and reviewers. Today we present SmartCityX, an integrated smart-city analytics platform built in pure Java 17. Unlike standard CRUD applications, SmartCityX serves as an algorithmic decision engine designed to solve mission-critical urban problems—such as water pipeline bottlenecks, power allocation, and emergency team routing—using the most rigorous algorithms across String Matching, Suffix Structures, Dynamic Programming, Network Flow, NP-Completeness, and Randomized/Parallel computation."*

---

## Slide 2: Real-World Motivation & Municipal Challenges
- **Slide Title**: Problem Statement: The Algorithmic Complexity of Modern Cities
- **The Modern Urban Dilemma**:
  - Contemporary cities generate massive, heterogeneous, high-velocity datasets that legacy monolithic software cannot process efficiently.
  - Subsystems operate in silos: citizen grievance portals cannot cross-correlate with civil engineering drawings; traffic signals cannot communicate with water main burst repair crews.
- **Core Operational Bottlenecks**:
  1. **Unstructured Text & Blueprints**: Tens of thousands of citizen complaints, work orders, and 500-page civil building codes requiring instant keyword and pattern matching.
  2. **Network Flow Imbalances**: Bottlenecks in arterial roadways and water treatment plants during peak hours causing systemic infrastructure strain.
  3. **Combinatorial Explosion**: Optimal placement of surveillance/sensor posts and municipal utility packages are NP-hard combinatorial optimization problems.
  4. **High-Frequency Telemetry**: IoT sensor streams (temperature, power, air quality) generating continuous readings requiring real-time, unbiased sampling and prefix aggregation.
- **The SmartCityX Solution**:
  - A unified decision-support engine applying the optimal, mathematically proven algorithm to each municipal domain, guaranteeing minimum time and space complexities.
- **Speaker Notes**:
  > *"Modern municipal governance faces four core computational bottlenecks: searching massive unstructured texts, resolving physical network flow congestion, handling NP-hard resource allocation, and processing high-throughput IoT telemetry. SmartCityX demonstrates that applying advanced discrete algorithms—such as Dinic's blocking flows, Suffix Automata, and SOS Dynamic Programming—transforms urban management from reactive guessing into deterministic optimization."*

---

## Slide 3: System Architecture & Architectural Principles
- **Slide Title**: System Architecture & Clean Domain Layering
- **Architectural Flow**:
  ```
  [ Offline Synthetic Datasets (16 RFC-4180 CSV / JSONL Files in data/) ]
                                  │
                                  ▼
  [ Dataset Ingestion Layer: SmartCityDatasetLoader & RFC-4180 CSVParser ]
                                  │
                                  ▼
  [ In-Memory Repository Layer: CitizenRepo, TrafficRepo, InfraRepo, SensorRepo ]
                                  │
                                  ▼
  [ Foundational Data Structure Layer: 11 Custom Zero-JCF Data Structures ]
                                  │
                                  ▼
  [ Advanced Algorithms Engine: Modules M1 (CO1) through M6 (CO6) ]
                                  │
                                  ▼
  [ Municipal Domain Services: CitizenService, TrafficService, ResourceService ]
                                  │
                                  ▼
  [ User Presentation Layer: SmartCityCLI (17 ANSI Modules) & SelfTestRunner ]
  ```
- **Architectural Tenets**:
  - **Decoupled Service Model**: Algorithms are isolated from I/O and domain models, ensuring unit-testability.
  - **Zero External Dependencies**: Standalone execution with zero third-party libraries (only JUnit 5 for testing).
  - **Deterministic Local Ingestion**: 16 validated datasets ingested and indexed in $< 45\text{ ms}$ upon startup.
- **Speaker Notes**:
  > *"Our architecture follows a clean, layered pipeline. Data flows from RFC-4180 CSV files into in-memory repositories. Crucially, the algorithmic layer operates exclusively on our custom zero-JCF data structures. Domain services orchestrate these algorithms to solve municipal problems, which are exposed through an interactive 17-module CLI shell."*

---

## Slide 4: Zero-JCF Custom Data Structures Engine
- **Slide Title**: Algorithmic Integrity: 100% Custom Data Structures
- **The Philosophy**:
  - In strict compliance with academic and project requirements, **no Java Collections Framework (`java.util.ArrayList`, `HashMap`, `PriorityQueue`) is used in the algorithmic core**.
  - All data structures are engineered from primitive arrays and pointer references under `smartcity.structures.*`.
- **Engineering Highlights**:
  | Structure | Design & Underlying Invariant | Complexity |
  |:---|:---|:---:|
  | `DynamicArray<T>` | Geometric doubling buffer ($2\times$) with bounds checking | Amortized $O(1)$ add |
  | `IntArrayList` | Primitive unboxed `int[]` avoiding wrapper object overhead | Zero-allocation append |
  | `CustomHashMap<K,V>` | Separate-chaining hash table with prime modulus resizing | $O(1)$ average get/put |
  | `CustomHashSet<T>` | Set backed by `CustomHashMap` for distinct element tracking | $O(1)$ uniqueness check |
  | `CustomPriorityQueue<T>` | Array-based 0-indexed binary min-heap with bubble-up/down | $O(\log N)$ insert/poll |
  | `CircularQueue<T>` | Modular arithmetic ring-buffer array for FIFO queues | $O(1)$ enqueue/dequeue |
  | `DisjointSetUnion` | Union-by-Rank with full Two-Pass Path Compression | $O(\alpha(N))$ amortized |
  | `CustomFlowNetwork` | Forward and backward residual flow edge pointers | $O(1)$ residual lookups |
- **Speaker Notes**:
  > *"To ensure authentic algorithmic rigor, we rejected Java Collections. Our `IntArrayList` operates on raw primitive arrays to eliminate boxing overhead; our `CustomHashMap` implements separate chaining with dynamic prime resizing; our `DisjointSetUnion` features path compression achieving inverse Ackermann complexity; and our `CustomPriorityQueue` enforces heap invariants manually."*

---

## Slide 5: Municipal Datasets & Data Governance
- **Slide Title**: Synthetic Metropolitan Datasets (`data/`)
- **Dataset Scale & Schema Compliance**:
  - All 16 datasets in `data/` exceed project requirements and adhere to RFC-4180 standards:
    - **120 Citizen Reports** (`citizen_reports.csv`): Grievance categories, geographic coordinates, priority levels.
    - **80 Service Requests** (`service_requests.csv`): Official departmental work orders and priority scores.
    - **50 Emergency Incidents** (`emergency_reports.csv`): HazMat, structural fires, gas leaks, and casualty weights.
    - **60 Infrastructure Blueprints** (`infrastructure_documents.jsonl`): Engineering standards, safety specs.
    - **25 Traffic Junctions & 50 Arterials** (`traffic_nodes.csv`, `traffic_edges.csv`): Road network with hourly vehicle capacities.
    - **40 Utility Assets & 35 Demands** (`water_resources.csv`, `energy_resources.csv`, `resource_demands.csv`).
    - **15 Emergency Squads & 24 Requirements** (`emergency_teams.csv`, `assignment_requirements.csv`).
    - **24 Civil Projects & 30 Precedences** (`project_tasks.csv`, `project_dependencies.csv`).
    - **22 Defense Nodes & 35 Links** (`infrastructure_vertices.csv`, `infrastructure_edges.csv`).
    - **1,200 IoT Observations** (`iot_sensor_readings.csv`): Temperature, power, vibration, noise streams.
- **Data Ingestion Guarantee**:
  - Ingested via streaming `CSVParser` supporting embedded quotes, commas, and multi-line fields in $\approx 42\text{ ms}$.
- **Speaker Notes**:
  > *"Our platform includes 16 realistic municipal datasets exceeding all schema requirements. We have 120 citizen complaints, a 25-junction road network, 40 water and energy utilities, and 1,200 continuous sensor readings. The data is parsed into memory in 42 milliseconds with complete RFC-4180 compliance."*

---

## Slide 6: Module 1 (CO1) — String Algorithms: KMP & Z-Algorithm
- **Slide Title**: M1 (CO1): Deterministic String Matching & Duplicate Detection
- **1. Knuth-Morris-Pratt (KMP)**:
  - **Mathematical Basis**: Computes the longest proper prefix that is also a suffix:
    $$\pi[i] = \max \{ k : P[0..k-1] \text{ is a suffix of } P[0..i] \}$$
  - **Complexity**: $O(M)$ preprocessing, $O(N)$ text scanning with strictly zero backtracking.
  - **Municipal Role**: Searches citizen grievance records, departmental work orders, and infrastructure codes (`INFRA-NODE-02`).
- **2. Z-Algorithm**:
  - **Mathematical Basis**: Computes $Z[i] = \text{LCP}(S, S[i..|S|-1])$ using a sliding window box $[L, R]$.
  - **Complexity**: Strict $O(N)$ linear time.
  - **Municipal Role**: Detects duplicated complaint patterns and identical grievance spam across city zones.
- **Empirical CLI Evidence**:
  - Matches 80 service work orders in $18.6\text{ ms}$; tracks exact 0-indexed character offsets.
- **Speaker Notes**:
  > *"In Module 1, we implemented KMP and the Z-Algorithm. KMP uses the prefix function to achieve linear O(N) searching across citizen reports and infrastructure codes without backtracking. Meanwhile, the Z-Algorithm detects duplicate citizen complaints by computing the Z-box prefix array in linear time."*

---

## Slide 7: Module 1 (CO1) — String Algorithms: Rabin-Karp & Aho-Corasick
- **Slide Title**: M1 (CO1): Rolling Hashes & Multi-Keyword Threat Scanning
- **1. Rabin-Karp Polynomial Rolling Hash**:
  - **Mathematical Formula**: Uses base $B = 257$ and prime modulus $M = 10^9 + 7$:
    $$H(S[i..i+m-1]) = \left( \sum_{j=0}^{m-1} S[i+j] \cdot B^{m-1-j} \right) \bmod M$$
  - **Sliding Update**: $O(1)$ window transition:
    $$H_{new} = \left( (H_{old} - S[i] \cdot B^{m-1}) \cdot B + S[i+m] \right) \bmod M$$
  - **Municipal Role**: Traces municipal identifiers (`SRQ-5001`, `CR-1004`) with full rolling-hash telemetry and collision verification.
- **2. Aho-Corasick Multi-Pattern Automaton**:
  - **Data Structure**: Trie augmented with BFS failure links and dictionary output links.
  - **Complexity**: $O(N + \sum M_i)$ processing time independent of pattern count $k$.
  - **Municipal Role**: Rapid emergency keyword triage across citizen reports (scanning simultaneously for `"burst"`, `"leak"`, `"dark"`, `"pothole"`, `"surge"`).
- **Speaker Notes**:
  > *"Rabin-Karp enables fast identifier lookups using a polynomial rolling hash with prime modulo 10^9+7, verifying character equality upon hash matches. Aho-Corasick builds a trie with failure transitions to scan citizen reports for dozens of emergency threat keywords in a single pass."*

---

## Slide 8: Module 2 (CO2) — Suffix Structures: Prefix Doubling vs. SA-IS
- **Slide Title**: M2 (CO2): Large-Scale Document Indexing (Prefix Doubling vs. Linear SA-IS)
- **The Challenge**:
  - Indexing large-scale civil engineering documents, environmental regulations, and building codes for sub-millisecond query response.
- **1. Prefix Doubling ($O(N \log^2 N)$)**:
  - Iteratively sorts suffixes by $2^k$-length prefixes using custom MergeSort and rank tuples.
- **2. SA-IS Algorithm ($O(N)$ Linear Time)**:
  - Nong–Zhang–Chan Induced Sorting algorithm.
  - Classifies suffixes into **S-type** and **L-type** ($S[i] < S[i+1]$).
  - Identifies **Leftmost S-type (LMS)** suffixes, sorts LMS substrings via bucket placement, constructs reduced string, and induces full suffix order in $O(N)$ work and space.
- **Head-to-Head Empirical Benchmark (`Menu 2 -> Option 5`)**:
  ```text
   Construction Algorithm │ Suffixes │ Time (ns)   │ Time (ms)  │ Complexity
  ────────────────────────┼──────────┼─────────────┼────────────┼───────────────────
   Prefix Doubling Merge  │      628 │   1,641,750 │     1.6418 │ O(N log^2 N)
   SA-IS Induced Sorting  │      628 │     358,250 │     0.3583 │ O(N) LINEAR
  ────────────────────────┴──────────┴─────────────┴────────────┴───────────────────
   Empirical Speedup Factor: 4.58x faster | Verification: 100% IDENTICAL SUFFIX ORDER
  ```
- **Speaker Notes**:
  > *"For Module 2 document indexing, we implemented both classic Prefix Doubling and the advanced SA-IS induced sorting algorithm. By classifying suffixes into S and L types, SA-IS guarantees O(N) linear-time construction. In our live CLI benchmark on civil documents, SA-IS achieves a 4.58x empirical speedup over prefix doubling while maintaining 100% identical suffix order."*

---

## Slide 9: Module 2 (CO2) — Suffix Structures: Kasai LCP & Suffix Automaton
- **Slide Title**: M2 (CO2): Kasai LCP Array & Suffix Automaton (DAWG)
- **1. Kasai's LCP Array Algorithm**:
  - **Theory**: Exploits the theorem that $LCP[\text{rank}[i]] \ge LCP[\text{rank}[i-1]] - 1$, allowing construction of the full LCP array in $O(N)$ linear time.
  - **Municipal Role**: Identifies recurring service descriptions across work orders. By concatenating work orders with sentinel `#` and analyzing high-LCP adjacent suffixes from distinct documents, recurring city problems are extracted automatically.
- **2. Suffix Automaton (DAWG)**:
  - **Theory**: Directed Acyclic Word Graph representing all suffixes in minimal states ($|\text{states}| \le 2N - 1$, $|\text{transitions}| \le 3N - 4$).
  - **Query Complexity**: $O(M)$ substring verification (independent of document length $N$) and $O(1)$ transition steps.
  - **Municipal Role**: Instantaneous verification of technical phrases in 60 civil building code documents.
- **Speaker Notes**:
  > *"Kasai's algorithm computes the Longest Common Prefix array in linear time. We use it to identify common municipal service descriptions across disparate work orders. Complementing this, our Suffix Automaton provides optimal O(M) substring verification across 60 civil blueprints."*

---

## Slide 10: Module 3 (CO3) — Advanced DP: String Distances & Sequence Alignment
- **Slide Title**: M3 (CO3): Typo-Tolerant Query Correction & Global Alignment
- **1. Levenshtein & Damerau-Levenshtein (OSA)**:
  - **Recurrence**:
    $$D[i, j] = \min \begin{cases} D[i-1, j] + 1 & \text{(Deletion)} \\ D[i, j-1] + 1 & \text{(Insertion)} \\ D[i-1, j-1] + \text{cost} & \text{(Substitution)} \\ D[i-2, j-2] + 1 & \text{(Transposition, if } S_1[i]=S_2[j-1] \land S_1[i-1]=S_2[j] \text{)} \end{cases}$$
  - **Municipal Role**: Powers citizen query auto-correction in `CitizenReportService`. When a user types `"pipline"`, the engine computes edit distances and outputs: *`"Did you mean: pipeline?"`*
- **2. Needleman-Wunsch Global Sequence Alignment**:
  - **Matrix Dynamic Programming**: Score matrix ($+2$ match, $-1$ mismatch, $-2$ gap) with complete backtrack traceback.
  - **Municipal Role**: Compares contractual and regulatory clauses between public utility agencies to identify modified provisions.
- **Speaker Notes**:
  > *"Module 3 covers dynamic programming. We implemented Levenshtein and Damerau-Levenshtein distance to correct citizen queries with typo tolerance, accounting for character transpositions. Additionally, Needleman-Wunsch global alignment performs deep similarity matching between municipal contracts."*

---

## Slide 11: Module 3 (CO3) — Advanced DP: Matrix Chain & Bitmask DP
- **Slide Title**: M3 (CO3): Matrix Chain Multiplication & Bitmask DP
- **1. Matrix Chain Multiplication (MCM)**:
  - **Recurrence**: $m[i, j] = \min_{i \le k < j} \{ m[i, k] + m[k+1, j] + p_{i-1} p_k p_j \}$ in $O(N^3)$ time and $O(N^2)$ space.
  - **Municipal Role**: Optimizes high-dimensional multi-stage sensor correlation pipelines (Traffic $\times$ Air Quality $\times$ Weather $\times$ Energy covariance matrices).
  - **Output**: Optimal parenthesization tree cutting scalar operations by over $60\%$.
- **2. Bitmask DP (Set Cover & TSP)**:
  - **State Representation**: $dp[\text{mask}][u]$ representing minimum cost visiting subset of vertices in $\text{mask}$ ending at $u$.
  - **Complexity**: $O(N^2 \cdot 2^N)$ time vs $O(N!)$ brute force.
  - **Municipal Role**: Optimal procurement of supplier utility packages (Set Cover) and inspection route planning for municipal engineers.
- **Speaker Notes**:
  > *"To optimize analytical operations, Matrix Chain Multiplication determines the optimal multiplication order for multi-sensor matrices in O(N^3). For utility resource packages and inspection routes, our Bitmask DP algorithms solve Set Cover and Travelling Salesperson problems across city districts in O(N^2 * 2^N)."*

---

## Slide 12: Module 3 (CO3) — Advanced DP: Tree DP & Sum Over Subsets (SOS DP)
- **Slide Title**: M3 (CO3): Tree DP & Sum Over Subsets (SOS DP)
- **1. Sum Over Subsets (SOS DP)**:
  - **The Math**: Given array $A$ of size $2^N$, compute:
    $$F[\text{mask}] = \sum_{\text{submask} \subseteq \text{mask}} A[\text{submask}]$$
  - **Algorithmic Leap**: Naive submask enumeration requires $O(3^N)$. SOS DP decomposes transitions along individual bit dimensions in $O(N \cdot 2^N)$.
  - **Municipal Role (`Menu 4 -> Option 5`)**: Evaluates cumulative synergy scores across all combinations of 4 municipal utility districts (Hospital, Water, Transit, Emergency) in $64$ operations vs $81$ brute force.
- **2. Tree Dynamic Programming (Diameter & Rerooting)**:
  - **Technique**: Two-pass DFS on acyclic networks computing max path lengths in $O(V)$ linear time.
  - **Municipal Role**: Optimizes municipal fiber-optic distribution backbones and minimizes transmission delays.
- **Speaker Notes**:
  > *"SOS DP is a prime highlight: instead of checking submasks in O(3^N) time, we iterate bit by bit in O(N * 2^N), enabling real-time synergy scoring for multi-district utility deployments. Additionally, Tree DP computes the diameter and optimal root of the city's fiber backbone in linear O(V) time."*

---

## Slide 13: Module 4 (CO4) — Network Flow: Maximum Flow Formulations
- **Slide Title**: M4 (CO4): Metropolitan Flow Optimization (Dinic vs. Edmonds-Karp)
- **Flow Invariants Enforced**:
  1. **Capacity Constraint**: $0 \le f(u, v) \le c(u, v)$ for all edges.
  2. **Skew Symmetry**: $f(u, v) = -f(v, u)$.
  3. **Flow Conservation**: $\sum_{v} f(u, v) = 0$ for all $u \notin \{s, t\}$.
- **Three Max-Flow Engines**:
  - **Ford-Fulkerson**: DFS augmenting paths, $O(E \cdot \text{MaxFlow})$.
  - **Edmonds-Karp**: BFS shortest paths in residual network, $O(V \cdot E^2)$.
  - **Dinic's Algorithm**: Constructs level graph via BFS; pushes blocking flow via DFS with dynamic head pointers in $O(V^2 \cdot E)$.
- **Municipal Water Distribution (`Menu 4 -> Option 2`)**:
  - Directs reservoir output through 4 regional distribution hubs to consumption districts. Dinic computes maximum throughput of **$1,425\text{ Megaliters/day}$** in $< 1\text{ ms}$.
- **Speaker Notes**:
  > *"In Module 4, we model physical municipal distribution networks. We implemented Ford-Fulkerson, Edmonds-Karp, and Dinic's algorithm. Dinic constructs BFS level graphs and extracts blocking flows in O(V^2 * E). In our water distribution network, it balances reservoir outputs to deliver 1,425 Megaliters per day."*

---

## Slide 14: Module 4 (CO4) — Network Flow: Min-Cut Duality & Bipartite Matching
- **Slide Title**: M4 (CO4): Bottleneck Detection & Emergency Team Allocation
- **1. Max-Flow / Min-Cut Duality Theorem**:
  - **Theorem**: The maximum value of an $s-t$ flow equals the minimum capacity of an $s-t$ cut:
    $$\text{MaxFlow} = \min_{S, T} C(S, T)$$
  - **Implementation**: Runs BFS on residual graph after max-flow saturation to identify reachable set $S$; cut edges cross from $S$ to $T$.
  - **Municipal Role (`Menu 3 -> Option 3`)**: Identifies vulnerable road links in the 25-junction traffic grid whose failure severs transit between city zones.
- **2. Maximum Bipartite Matching**:
  - **Reduction**: Unit-capacity bipartite network flow solved via Dinic / Hopcroft-Karp in $O(E \sqrt{V})$.
  - **Municipal Role (`Menu 5 -> Option 2`)**: Dispatches 15 specialized emergency squads (HazMat, EMT, Fire) to 24 active incident requirements.
- **Speaker Notes**:
  > *"By the Max-Flow Min-Cut theorem, the maximum flow value exactly equals the minimum cut capacity. Our Min-Cut analyzer identifies critical bottleneck road segments. Furthermore, our Bipartite Matching engine solves the emergency team dispatch problem, pairing specialized squads with urgent incident requirements in O(E * sqrt(V))."*

---

## Slide 15: Module 4 (CO4) — Project Scheduling: Critical Path Method (CPM)
- **Slide Title**: M4 (CO4): Capital Infrastructure Project Scheduling (CPM)
- **The Municipal Challenge**:
  - Scheduling 24 civil maintenance projects across 30 precedence constraints (e.g., road excavation $\to$ pipe laying $\to$ repaving).
- **Algorithmic Workflow**:
  ```
  [ Directed Acyclic Graph (DAG) ] ──> [ Topological Sort (Kahn's Algorithm) ]
                                                        │
                                                        ▼
  [ Forward Pass: Compute Earliest Start (ES) and Earliest Finish (EF) ]
                                                        │
                                                        ▼
  [ Backward Pass: Compute Latest Finish (LF) and Latest Start (LS) ]
                                                        │
                                                        ▼
  [ Slack Identification: Slack = LS - ES (Zero Slack = Critical Path Tasks) ]
  ```
- **Project Selection via Min-Cut Closure**:
  - Solves maximum profit project selection with dependency constraints by computing min-cut closure in residual networks.
- **Speaker Notes**:
  > *"To schedule civil infrastructure projects, we implemented the Critical Path Method on Directed Acyclic Graphs. Topological sorting followed by forward and backward DP passes identifies zero-slack tasks that dictate the project makespan. We also implemented project selection closure using min-cut flows."*

---

## Slide 16: Module 5 (CO5) — NP-Completeness & Formal Karp Reductions
- **Slide Title**: M5 (CO5): Formal Polynomial-Time Reductions (Karp Reduction Zoo)
- **The Concept**:
  - Proving computational hardness and solving complex constraints by reducing between NP-complete problem instances in polynomial time.
- **The Live Reduction Pipeline (`Menu 11 -> Option 2`)**:
  ```
  [ 3-SAT Formula (City Zoning Constraints) ]
                  │
                  ▼ Polynomial Reduction (Clause Triples Gadgets)
  [ CLIQUE Instance (Target size k = 3) ]
                  │
                  ▼ Invert Edges (Complement Graph G')
  [ INDEPENDENT-SET Instance (Target size k = 3) ]
                  │
                  ▼ Duality Theorem (V \ S)
  [ VERTEX-COVER Instance (Target size |V| - k = 6) ]
                  │
                  ▼ Maximal Matching Edge Selection
  [ 2-Approximation Vertex Cover Solution ]
  ```
- **Rigorous Proof**:
  - $S$ is an independent set in $G \iff V \setminus S$ is a vertex cover in $G$.
  - Executed end-to-end directly in the CLI during project review.
- **Speaker Notes**:
  > *"Module 5 addresses NP-Completeness. We built a formal Karp Reduction Zoo. In our live CLI pipeline, we represent zoning constraints as a 3-SAT formula, reduce it to a CLIQUE gadget graph, invert it to an INDEPENDENT-SET on the complement graph, and apply the V \ S duality theorem to obtain a VERTEX-COVER, which is then solved via approximation."*

---

## Slide 17: Module 5 (CO5) — Approximation Algorithms & FPT
- **Slide Title**: M5 (CO5): Approximation Schemes & Fixed-Parameter Tractability
- **1. Vertex Cover 2-Approximation**:
  - **Algorithm**: Greedily extracts a maximal matching $M$ of disjoint edges and adds both endpoints to $C$.
  - **Guaranteed Ratio**: $|C| = 2|M| \le 2 \cdot \text{OPT}$.
  - **Municipal Role**: Emergency camera and sensor post coverage across 22 civil facilities and 35 links.
- **2. Parameterized Vertex Cover (FPT Search Tree)**:
  - **Theory**: Exact minimum vertex cover in $O(2^k \cdot (V + E))$. For edge $(u, v)$, branches on $u \in C$ or $v \in C$ decreasing budget parameter $k$.
  - **Municipal Role**: Guarantees the absolute optimal minimal defense post count when budget $k$ is small.
- **3. Knapsack FPTAS**:
  - **Approximation Bound**: $(1 - \varepsilon) \cdot \text{OPT}$ in $O(n^3 / \varepsilon)$ time via dynamic programming value scaling.
- **Speaker Notes**:
  > *"When exact solutions take exponential time, we use approximation and parameterized algorithms. Our Vertex Cover 2-approximation guarantees a solution within twice the optimum for facility defense. For exact minimal installations, our FPT search tree branches in O(2^k * (V+E)) time. We also provide a Knapsack FPTAS guaranteeing (1 - epsilon) optimality."*

---

## Slide 18: Module 6 (CO6) — Randomized Algorithms
- **Slide Title**: M6 (CO6): Randomized Algorithms & Probabilistic Primality
- **1. Randomized QuickSort (Dutch National Flag)**:
  - **Theory**: Uniform random pivot selection guarantees expected $O(N \log N)$ comparisons with probability $1 - 1/N^c$, defeating malicious quadratic worst-cases.
  - **Municipal Role**: Ranks incoming work orders by severity score in $< 1\text{ ms}$.
- **2. Reservoir Sampling (Algorithm R)**:
  - **Theory**: Processes an unbounded streaming sequence with memory $O(k)$. The $i$-th element is included with probability $k / i$. Every item maintains an exact selection probability of $k / N$.
  - **Municipal Role**: Unbiased uniform sampling of $k = 10$ readings from the continuous 1,200-reading IoT sensor stream.
- **3. Miller-Rabin Probabilistic Primality Test**:
  - **Theory**: Deterministic 64-bit witness basis $\{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37\}$. Resilient against pseudoprimes and Carmichael numbers (verified against 561, 1729).
  - **Municipal Role**: Cryptographic verification of smart meter telemetry tokens.
- **Speaker Notes**:
  > *"In Module 6, randomized algorithms provide efficiency and impartiality. Randomized QuickSort with Dutch National Flag partitioning sorts service requests in O(N log N) expected time. Reservoir Sampling draws unbiased uniform samples from streaming IoT sensors. Miller-Rabin validates cryptographic tokens using a deterministic 12-witness basis."*

---

## Slide 19: Module 6 (CO6) — Parallel Algorithms & Brent's Theorem
- **Slide Title**: M6 (CO6): Parallel Prefix Scans & Work/Span Complexity
- **1. Blelloch Parallel Prefix Scan**:
  - **Algorithm**: Work-efficient parallel scan using an implicit binary tree:
    - **Up-Sweep (Reduce)**: Computes partial sums in $O(N)$ operations.
    - **Down-Sweep**: Propagates running sums in $O(N)$ total work and $O(\log N)$ span/depth.
  - **Municipal Role (`Menu 6 -> Option 3`)**: Computes running cumulative power consumption across smart grid meters in parallel.
- **2. ForkJoin Parallel Reduce & Merge Sort**:
  - **Theory**: Recursive divide-and-conquer exploiting multi-core processors for associative reductions (SUM, MIN, MAX).
- **3. Brent's Scheduling Theorem**:
  - **Theoretical Upper Bound**: $T_P \le \frac{W - S}{P} + S$, where $W$ is total work, $S$ is critical-path span, and $P$ is core count.
- **Speaker Notes**:
  > *"To handle citywide telemetry data, we implemented Blelloch's parallel prefix scan, which achieves work-efficient O(N) work and O(log N) span through up-sweep and down-sweep tree passes. We also implemented ForkJoin parallel reduction and evaluate parallel speedup bounds using Brent's Scheduling Theorem."*

---

## Slide 20: Performance Evaluation, Empirical Benchmarks & Conclusion
- **Slide Title**: Evaluation, Quality Audit & Project Conclusion
- **Empirical Benchmarks Summary**:
  | Algorithmic Domain | Competitor Implementations | Winner & Verified Metric |
  |:---|:---|:---|
  | **String Matching** | Naive vs. KMP vs. Z vs. Rabin-Karp | KMP & Z linear ($100\%$ match across all 4) |
  | **Suffix Indexing** | Prefix Doubling vs. Linear SA-IS | **SA-IS achieves 4.58x speedup** ($O(N)$) |
  | **Network Flow** | Ford-Fulkerson vs. Edmonds-Karp vs. Dinic | **Dinic operates in $< 1\text{ ms}$** ($100\%$ agreement) |
  | **Sensor Sorting** | Dual-Pivot vs. Randomized QuickSort vs. Parallel | Parallel Sort on multi-core ForkJoin |
- **Code Quality & Testing Metrics**:
  - **54 Automated JUnit 5 Tests**: Zero failures across data structures and workflows.
  - **Zero Compiler Warnings**: Verified via `javac -Xlint:all`.
  - **Production-Ready Artifact**: Packaged as executable JAR `target/smartcity.jar`.
- **Concluding Statement**:
  - SmartCityX proves that computer science discrete algorithms directly solve the operational and infrastructural bottlenecks of 21st-century metropolitan cities.
- **Speaker Notes**:
  > *"In conclusion, SmartCityX integrates all six DSA curriculum modules into an enterprise-grade municipal platform. We have rigorously verified our implementations through 54 automated tests, empirical benchmarks, and a zero-warning compiler audit. Thank you professors, we are now ready for your questions and live demonstration."*
