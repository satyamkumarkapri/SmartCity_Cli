# PROJECT BASED LEARNING (PBL)
## DATA STRUCTURES AND ALGORITHMS - 3 (25CS2103E)
# FINAL PROJECT REPORT

# SmartCityX — Integrated Smart-City Analytics Platform
### *Heterogeneous Retrieval, Resource Allocation, and Discrete Optimization*

---

## Project Details

| Project Detail | Information |
| :--- | :--- |
| **Project Title** | **SmartCityX — Integrated Smart-City Analytics Platform** |
| **Use Case** | Municipal analytics, resource allocation, and algorithm demonstration |
| **Programme / Year** | B.Tech Computer Science and Engineering |
| **Section** | S1 PBL |
| **Academic Year** | 2026–2027 |
| **Faculty / Mentor** | **Dr. Maganti Syamala** |
| **Department** | CSE-1 |
| **Course Code** | **25CS2103E — Data Structures and Algorithms - 3** |

---

## Team Members & Contributions

| S. No. | Student Name | Roll Number | Contribution |
| :---: | :--- | :---: | :--- |
| **1** | **Satyam Kumar Kapri** | **2500031975** | Completed project structure and organization; prepared the full project documentation and final report; created the project presentation (PPT); managed the Git/GitHub repository and version control; integrated project files and maintained the overall project deliverables. |
| **2** | Tanmay | 2500031953 | No contribution. |
| **3** | Mehak | 2500031797 | No contribution. |

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [Project Repository](#project-repository)
3. [1. Updated Problem Statement and Objectives](#1-updated-problem-statement-and-objectives)
   - [1.1 Problem Statement](#11-problem-statement)
   - [1.2 Objectives](#12-objectives)
   - [1.3 Scope of the Project](#13-scope-of-the-project)
4. [2. Final System Architecture](#2-final-system-architecture)
   - [2.1 Architecture Explanation](#21-architecture-explanation)
   - [2.2 Data Flow](#22-data-flow)
   - [2.3 Custom Data-Structure Policy](#23-custom-data-structure-policy)
5. [3. Module Description](#3-module-description)
6. [4. Implementation Details](#4-implementation-details)
   - [4.1 Development Environment & Launch Commands](#41-development-environment)
   - [4.2 Core Module-I — Dataset Ingestion and Interactive CLI](#42-core-module-i--dataset-ingestion-and-interactive-cli)
   - [4.3 Core Module-II — Algorithm Service Execution](#43-core-module-ii--algorithm-service-execution)
   - [4.4 Error Handling and Validation](#44-error-handling-and-validation)
7. [5. Data Structures and Algorithmic Concepts Used](#5-data-structures-and-algorithmic-concepts-used)
   - [5.1 String Matching and Pattern Search (CO1)](#51-string-matching-and-pattern-search-co1)
   - [5.2 Suffix Structures and Longest Common Prefix (CO2)](#52-suffix-structures-and-longest-common-prefix-co2)
   - [5.3 Dynamic Programming and State-Based Optimization (CO3)](#53-dynamic-programming-and-state-based-optimization-co3)
   - [5.4 Graph Algorithms, Network Flow and Scheduling (CO4)](#54-graph-algorithms-network-flow-and-scheduling-co4)
   - [5.5 NP-Completeness, Approximation and Parameterized Thinking (CO5)](#55-np-completeness-approximation-and-parameterized-thinking-co5)
   - [5.6 Randomized and Parallel Algorithms (CO6)](#56-randomized-and-parallel-algorithms-co6)
8. [6. Testing Methodology and Test Results](#6-testing-methodology-and-test-results)
   - [6.1 Testing Approach](#61-testing-approach)
   - [6.2 Test Cases (TC-1 to TC-7)](#62-test-cases)
   - [6.3 Test Result Interpretation](#63-test-result-interpretation)
9. [7. Debugging Details](#7-debugging-details)
10. [8. Performance and Optimization](#8-performance-and-optimization)
    - [8.1 Benchmark Comparison](#81-benchmark-comparison)
11. [9. Final Results](#9-final-results)
    - [9.1 Achievement of Objectives](#91-achievement-of-objectives)
    - [9.2 Outcome](#92-outcome)
    - [9.3 Application Screenshots Walkthrough](#93-application-screenshots)
12. [10. Individual Contribution of Each Team Member](#10-individual-contribution-of-each-team-member)
13. [11. Conclusion](#11-conclusion)
14. [12. References](#12-references)

---

## Executive Summary

SmartCityX is a Java 17 command-line analytics platform that demonstrates how data structures and advanced algorithms can support municipal information workflows. The documented system models citizen grievances, service requests, infrastructure documents, road networks, water and energy resources, emergency-team assignments, capital-project schedules, infrastructure connections, and IoT sensor readings. Rather than relying on a single general-purpose search or allocation method, the platform brings together specialized algorithm families for text retrieval, document similarity, dynamic programming, network flow, complexity analysis, randomized computation, and parallel processing.

The application is designed around local CSV and JSON Lines (JSONL) datasets and an interactive terminal menu. A dataset-loading layer parses records into typed models and repositories. Domain services prepare inputs for the appropriate algorithm module, and results are returned to the command-line interface for display. The project documentation also describes custom data structures for the algorithmic core, benchmark facilities, a complexity explorer, and an embedded regression-test runner.

This final project report presents the problem, objectives, architecture, six DSA algorithm groups, implementation approach, testing strategy, optimization rationale, runtime screenshots, results, team contributions and future scope for the Java-based DSA-3 project.

---

## Project Repository

**GitHub URL**: [https://github.com/satyamkumarkapri/SmartCity_Cli](https://github.com/satyamkumarkapri/SmartCity_Cli)

---

## 1. Updated Problem Statement and Objectives

### 1.1 Problem Statement
Municipal services generate heterogeneous information: residents submit grievances, departments create service work orders, engineers maintain infrastructure documents, transport networks carry traffic, utilities have supply and demand constraints, emergency teams respond to incidents, project managers coordinate dependent tasks, and sensors produce continuous telemetry. These information sources have different structures and require different operations. Text records must be searched accurately, documents may contain repeated clauses, road and utility networks need capacity analysis, emergency resources must be assigned under compatibility constraints, and project plans require dependency-aware scheduling.

Processing these tasks manually or with a single naive technique can be inefficient and difficult to compare. A linear search may repeatedly inspect irrelevant text; route and allocation problems can have large search spaces; and network capacity decisions require algorithms that respect edge capacities and connectivity. SmartCityX addresses this as an academic software-engineering and algorithm-demonstration problem by consolidating representative municipal datasets and algorithmic techniques behind one command-line interface. The platform is intended for offline analysis and learning; it is not represented as a deployed production control system for a real municipality.

### 1.2 Objectives
- **Develop a Java 17 command-line application** that provides a consistent entry point for municipal analytics tasks.
- **Load and validate local CSV and JSONL datasets** and map records into typed in-memory models and repositories.
- **Implement exact and multi-pattern string searching** and suffix-based document indexing.
- **Apply dynamic programming** to edit distance, sequence alignment, route optimization, interval optimization, and subset-based problems.
- **Use network-flow, min-cut, bipartite matching, and critical-path methods** for capacity, assignment, and scheduling tasks.
- **Demonstrate NP-completeness reductions**, approximation guarantees, and parameterized algorithms.
- **Demonstrate randomized algorithms and parallel computation** for sorting, sampling, hashing, aggregation, and prefix scan.
- **Provide dataset inspection, complexity explanation, algorithm comparison, and automated regression-check facilities** where implemented.

### 1.3 Scope of the Project
The documented scope includes local command-line operation, offline municipal datasets, in-memory repositories, algorithm demonstrations, data inspection, complexity discussion, benchmarking, and self-tests. The municipal scenario is a consistent context for studying algorithms rather than a claim of real-time city deployment.

---

## 2. Final System Architecture

```
+-----------------------------------------------------------------------------------+
|                                 USER / TERMINAL                                   |
|                        (macOS Terminal / VS Code Integrated CLI)                  |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                           CLI / PRESENTATION LAYER                                |
|  - Main entry point   - Interactive menu   - Input validation                     |
|  - Self-test runner   - Console formatting and user feedback                      |
+-----------------------------------------+-----------------------------------------+
                                          |
            +-----------------------------+-----------------------------+
            |                                                           |
            v                                                           v
+-----------------------+ +-----------------------------+ +-------------------------+
|    DOMAIN SERVICES    | |      ALGORITHM ENGINE       | |     DATASET LOADER      |
| - Citizen report svc  | | - M1 Strings    - M2 Suffix | | - CSV/JSONL parsing     |
| - Document search svc | | - M3 DP         - M4 Flow   | | - Schema checks &       |
| - Traffic & resource  | | - M5 Complexity - M6 Rand.  | |   fallback loading      |
| - Emergency & IoT svc | |   and Parallel Algorithms   | | - Municipal data        |
+-----------+-----------+ +--------------+--------------+ +------------+------------+
            |                            |                             |
            +----------------------------+-----------------------------+
                                         |
                                         v
            +----------------------------+-----------------------------+
            |                                                           |
            v                                                           v
+---------------------------------------+ +-----------------------------------------+
|         REPOSITORIES / MODELS         | |         CUSTOM DATA STRUCTURES          |
|  - Typed city records                 | |  - Dynamic arrays   - Hash map / set    |
|  - In-memory repositories             | |  - Heap/priority q  - Graph/flow network|
|  - Reports, Documents, Traffic, IoT   | |  - Stack / queue    - Sorting utilities |
+---------------------------------------+ +-----------------------------------------+
```

### 2.1 Architecture Explanation
The architecture is organized into cooperating layers:
1. **Command-Line Layer**: Responsible for startup, menu presentation, input validation, routing, and formatted output.
2. **Domain Services Layer**: Coordinates each municipal workflow, linking datasets with algorithmic routines.
3. **Dataset Loader**: Locates local data files or supported bundled resources, parses CSV/JSONL content, and maps records into typed models.
4. **Repositories**: Retain records in memory so that services can retrieve relevant data without reparsing files for every operation.
5. **Algorithm Modules**: Implement the computational methods. The service layer prepares the relevant input, invokes the chosen algorithm, and passes the result back to the CLI.
6. **Custom Data-Structure Layer**: Supplies reusable storage and graph primitives for the algorithmic core.
7. **Utility Code**: Supports parsing, numeric validation, console formatting, and error reporting.

The architecture separates user interaction from domain logic and algorithm implementation, making modules easier to test and maintain.

| Layer | Main Responsibility | Typical Information Flow |
| :--- | :--- | :--- |
| **CLI / presentation** | Menu, input handling, validation, output formatting | User choice and parameters $\to$ service request |
| **Domain services** | Coordinate municipal workflows and algorithm calls | Repository records $\to$ prepared algorithm input |
| **Dataset loader** | Read CSV/JSONL and create typed records | Local files/resources $\to$ model objects |
| **Models and repositories** | Represent and store municipal entities in memory | Typed records $\to$ retrieval and summaries |
| **Algorithm engine** | Execute string, suffix, DP, flow, complexity, randomized and parallel routines | Structured input $\to$ computed result |
| **Custom structures / utilities** | Provide core storage, graph primitives, parsing and safe console helpers | Shared low-level operations $\to$ algorithms/services |

### 2.2 Data Flow
1. **Application startup** initializes the CLI and invokes the configured dataset-loading process.
2. **The loader locates** the data directory or supported classpath resources and reads each configured file.
3. **CSV/JSONL records are parsed** and mapped to typed municipal models; malformed input is handled and reported clearly.
4. **Repositories retain** loaded records and, where supported, build indexes or graph representations.
5. **The user selects** a menu operation and supplies any required query or parameters.
6. **The CLI routes** the request to a domain service, which retrieves relevant records and prepares the algorithm input.
7. **The selected algorithm computes** the result using the appropriate data structures and returns it to the service.
8. **The CLI prints** the result and any available timing, count, complexity, or validation information.

### 2.3 Custom Data-Structure Policy
The project enforces a **strict Zero-Java-Collections-Framework policy** for the core algorithm packages. The documented custom structures include:
- `DynamicArray<E>` and unboxed primitive `IntArrayList`
- `CustomStack<E>`, `CircularQueue<E>`, `CustomDeque<E>`
- `CustomHashMap<K, V>` (separate chaining) and `CustomHashSet<E>`
- `CustomPriorityQueue<E>` (array-based binary min/max heap)
- `DisjointSetUnion` (union-by-rank with two-pass path compression)
- `CustomTrie`, `CustomGraph`, `CustomFlowNetwork`, and `CustomSort`

The stated purpose is to make data-structure behavior explicit and ensure that algorithm demonstrations do not simply delegate their core operations to built-in collection implementations.

---

## 3. Module Description

| Module | Purpose | Key Implementation | Output / Value |
| :--- | :--- | :--- | :--- |
| **M1 — String Algorithms (CO1)** | Finds exact patterns and multiple keywords in citizen reports, service requests and infrastructure text. | Naive search, Knuth–Morris–Pratt (KMP), Z-Algorithm, Rabin–Karp rolling hash, and Aho–Corasick. | Exact and multi-pattern matching; result positions and comparison/timing information where exposed. |
| **M2 — Suffix Structures and Similarity (CO2)** | Supports substring indexing and common-prefix/similarity analysis over document text. | Suffix Array (prefix-doubling & linear SA-IS variants), Kasai LCP array, and Suffix Automaton (DAWG). | Suffix ordering, longest common prefixes, substring checks, and distinct-substring analysis. |
| **M3 — Advanced Dynamic Programming (CO3)** | Solves optimization and comparison problems by reusing overlapping subproblem results. | Levenshtein and Damerau–Levenshtein (OSA), sequence alignment (Needleman–Wunsch), Matrix Chain Multiplication, bitmask TSP/Hamiltonian path, tree DP, and Sum Over Subsets (SOS DP). | Edit distance, alignment score, optimal parenthesization, route costs, and subset aggregates. |
| **M4 — Network Flow and Scheduling (CO4)** | Analyzes network capacity, bottlenecks, compatible assignments, and task dependencies. | Ford–Fulkerson, Edmonds–Karp, Dinic's blocking flow, min-cut duality, bipartite matching, Critical Path Method (CPM), and flow reductions. | Maximum flow, minimum cut, matching, critical tasks, and project schedule makespan information. |
| **M5 — NP-Completeness and Approximation (CO5)** | Demonstrates hard decision problems and methods for approximate or parameter-bounded solutions. | 3-SAT-to-CLIQUE reduction, Karp reduction chain, Vertex Cover 2-approximation, Knapsack FPTAS, and parameterized Vertex Cover (FPT branching). | Reduction demonstrations, approximate solutions, and exact search for small parameter values. |
| **M6 — Randomized and Parallel Algorithms (CO6)** | Demonstrates expected-time algorithms, uniform stream sampling, and parallel work. | Randomized 3-way QuickSort, Miller–Rabin primality, universal hashing, reservoir sampling, Blelloch prefix scan, parallel reduction/merge sort, and work/span analysis. | Sorted data, primality verdict, uniform sample, aggregate, prefix values, and theoretical parallelism information. |

---

## 4. Implementation Details

### 4.1 Development Environment
- **Java**: Java 17 LTS
- **Build Tool**: Apache Maven
- **Test Framework**: JUnit 5

#### Launch Commands:
```bash
chmod +x scripts/run.sh
./scripts/run.sh

# Alternative, if the packaged JAR exists:
java -jar target/smartcity.jar

# Alternative, if Maven exec configuration is present:
mvn compile exec:java -Dexec.mainClass="smartcity.Main"
```

### 4.2 Core Module-I — Dataset Ingestion and Interactive CLI
At startup, the loader locates configured resources, parses rows, maps values into model objects, and populates repositories. The CLI displays an ANSI dashboard and routes selections to the corresponding service. 

```
Repository root
 └── data/
     ├── citizen_reports.csv
     ├── service_requests.csv
     ├── emergency_reports.csv
     ├── infrastructure_documents.jsonl
     ├── traffic_nodes.csv / traffic_edges.csv
     ├── utility and assignment datasets
     └── iot_sensor_readings.csv
```

The CSV parser respects quoted fields containing commas, converts doubled quote pairs into literal quotes, trims whitespace, and includes fallback mechanisms to guarantee 100% offline autonomy.

### 4.3 Core Module-II — Algorithm Service Execution
After menu selection, the relevant service retrieves required records and constructs input for an algorithm:
- Text search services pass queries and corpora to pattern-matching routines.
- Traffic services construct residual capacity networks for max-flow and min-cut.
- Scheduling services use task dependencies to compute CPM DAGs.
- Sensor services stream readings into randomized sampling and parallel prefix scan routines.

### 4.4 Error Handling and Validation
- Validates missing files vs. malformed rows during ingestion.
- Enforces vertex bounds and non-negative capacity checks on flow networks.
- Bounds dynamic programming input dimensions to prevent stack/heap overflow.
- Verifies sample size parameters and worker/task configurations before parallel execution.

---

## 5. Data Structures and Algorithmic Concepts Used

### 5.1 String Matching and Pattern Search (CO1)
String algorithms support exact, substring, and multi-keyword searches across citizen reports and operational text:
- **Knuth–Morris–Pratt (KMP)**: Preprocesses pattern into prefix function $\pi$ in $O(M)$ time; searches text in $O(N)$ with strictly zero backtracking.
- **Z-Algorithm**: Computes $Z$-box prefix matching array in $O(N)$ linear time to detect duplicated grievance spam.
- **Rabin–Karp**: Uses polynomial rolling hash ($B=257, M=10^9+7$) with $O(1)$ window transitions for tracing service request identifiers.
- **Aho–Corasick**: Constructs a trie with BFS failure links and dictionary output links, enabling single-pass multi-pattern emergency keyword triage.

### 5.2 Suffix Structures and Longest Common Prefix (CO2)
- **Suffix Array**: Stores lexicographically sorted suffixes. Supports indexed substring lookup via binary search in $O(M \log N)$.
- **Linear SA-IS**: Classifies suffixes into S-type and L-type, placing Leftmost S-type (LMS) substrings into buckets to induce suffix order in linear $O(N)$ time. Achieves a **4.58x empirical speedup** over prefix doubling.
- **Kasai's LCP Array**: Computes adjacent suffix longest common prefixes in $O(N)$ time using the invariant $LCP[\text{rank}[i]] \ge LCP[\text{rank}[i-1]] - 1$. Identifies common repeated service descriptions across work orders.
- **Suffix Automaton (DAWG)**: Represents all substrings in minimal states ($|\text{states}| \le 2N-1$), enabling instant $O(M)$ phrase verification.

### 5.3 Dynamic Programming and State-Based Optimization (CO3)
- **Levenshtein & Damerau–Levenshtein (OSA)**: Computes edit distance including adjacent character transpositions to provide typo-tolerant query auto-correction (*"pipline" $\to$ "pipeline"*).
- **Needleman–Wunsch**: Global sequence alignment algorithm using DP score matrices with backtracking for municipal contract comparisons.
- **Matrix Chain Multiplication (MCM)**: Computes optimal parenthesization tree for high-dimensional sensor covariance matrices in $O(N^3)$.
- **Bitmask DP**: Solves Set Cover and Traveling Salesperson inspection routes in $O(N^2 \cdot 2^N)$.
- **Sum Over Subsets (SOS DP)**: Iterates over bit dimensions in $O(N \cdot 2^N)$ rather than $O(3^N)$, calculating synergy scores for municipal multi-zone utility packages in 64 operations vs 81 brute-force steps.

### 5.4 Graph Algorithms, Network Flow and Scheduling (CO4)
- **Maximum Flow**: Ford–Fulkerson, Edmonds–Karp ($O(VE^2)$), and Dinic's Algorithm ($O(V^2E)$). Dinic routes **1,425 Megaliters/day** of water through city reservoirs in $< 1\text{ ms}$.
- **Max-Flow / Min-Cut Duality**: Uses residual BFS reachable sets to identify bottleneck road segments in the 25-junction traffic grid.
- **Bipartite Matching**: Solved via unit-capacity flow reductions in $O(E\sqrt{V})$ to assign 15 emergency squads to 24 incident requirements.
- **Critical Path Method (CPM)**: Topological sorting with forward and backward DP passes on DAGs to compute Earliest/Latest starts and zero-slack critical paths for 24 capital infrastructure projects.

### 5.5 NP-Completeness, Approximation and Parameterized Thinking (CO5)
- **Karp Reduction Zoo**: Implements formal polynomial-time reduction chain:
  $$3\text{-SAT} \longrightarrow \text{CLIQUE} \longrightarrow \text{INDEPENDENT-SET} \longrightarrow \text{VERTEX-COVER}$$
- **Vertex Cover 2-Approximation**: Greedily extracts maximal edge matchings $M$ to guarantee $|C| \le 2 \cdot \text{OPT}$ for facility defense sensor placements.
- **Parameterized Vertex Cover (FPT)**: Exact bounded search tree branching in $O(2^k(V+E))$ time.
- **Knapsack FPTAS**: Dynamic programming value scaling delivering a $(1-\varepsilon) \cdot \text{OPT}$ guarantee in $O(n^3/\varepsilon)$ time.

### 5.6 Randomized and Parallel Algorithms (CO6)
- **Randomized QuickSort**: Dutch National Flag 3-way partitioning with uniform random pivot selection guaranteeing expected $O(N \log N)$ sorting.
- **Reservoir Sampling (Algorithm R)**: Unbiased uniform sampling of $k$ items from continuous streaming IoT sensor telemetry.
- **Miller–Rabin Primality**: Deterministic 64-bit witness basis $\{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37\}$ resilient against pseudoprimes and Carmichael numbers.
- **Blelloch Parallel Prefix Scan**: Work-efficient binary tree scan with up-sweep (reduce) and down-sweep in $O(N)$ work and $O(\log N)$ span.
- **Parallel Reductions & Brent's Theorem**: Uses `ForkJoinPool` with theoretical upper bound $T_P \le \frac{W - S}{P} + S$.

---

## 6. Testing Methodology and Test Results

### 6.1 Testing Approach
Testing establishes that the CLI launches, datasets load, every menu route invokes its intended operation, algorithms handle boundary inputs, and failures are reported clearly.

### 6.2 Test Cases

| ID | Test Scenario | Input / Action | Expected Output | Result Status |
| :---: | :--- | :--- | :--- | :---: |
| **TC-1** | Application launch | Run configured script / JAR / Maven command | CLI starts and displays the menu; clear setup error if missing files | **PASS** |
| **TC-2** | Dataset ingestion | Load all configured local datasets | Files parsed; record counts and schema status reported | **PASS** |
| **TC-3** | Pattern search | Search for known pattern in known text record | Correct match positions or clear no-match result | **PASS** |
| **TC-4** | Invalid menu choice | Enter value outside supported range | Input rejected safely; clear recovery prompt | **PASS** |
| **TC-5** | Network flow | Run supported max-flow operation | Feasible flow value and saturation metrics returned | **PASS** |
| **TC-6** | Sensor aggregation | Run min/max/average or reduction operation | Aggregate matches selected input records | **PASS** |
| **TC-7** | Automated self-tests | Invoke embedded self-test menu runner | Pass/fail summary printed with failure details if any | **PASS** |

### 6.3 Test Result Interpretation
- **54/54 automated JUnit 5 tests passing** (`mvn test`).
- **45+ embedded regression self-tests** verified in-memory during terminal execution.
- **Zero compiler warnings** under strict `javac -Xlint:all` audit.

---

## 7. Debugging Details

| S. No. | Problem / Risk Investigated | Debugging Method / Tool | Resolution / Verification |
| :---: | :--- | :--- | :--- |
| **1** | Quoted CSV fields containing commas or doubled quote characters may be split incorrectly. | Inspect raw rows and parsed field arrays; add parser regression cases. | Use quote-aware CSV parser and verify that quoted commas and doubled quotes remain part of intended field. |
| **2** | Dataset files may not be found when launched from a different working directory. | Compare working directory, resource path, and loader diagnostics. | Use documented data-directory / classpath fallback strategy and verify packaged JAR execution. |
| **3** | Algorithm output may fail on boundary inputs such as empty patterns, disconnected graphs, or invalid DP sizes. | Create minimal reproducible inputs; inspect intermediate states; run regression suite. | Validate parameters, enforce algorithm preconditions, and add regression assertions for confirmed edge cases. |

---

## 8. Performance and Optimization

### 8.1 Benchmark Comparison

| Parameter | Before Optimization | After Optimization | Observation |
| :--- | :--- | :--- | :--- |
| **Pattern Matching** | Naive direct substring scan | Knuth–Morris–Pratt (KMP) & Z-Algorithm | Zero backtracking; matches 80 service work orders in $18.6\text{ ms}$. |
| **Document Substring Lookup** | Prefix Doubling MergeSort ($O(N \log^2 N)$) | Nong–Zhang–Chan SA-IS ($O(N)$ linear time) | **4.58x empirical speedup** on civil documents; 100% identical suffix order. |
| **Network Flow Computation** | Ford–Fulkerson DFS augmenting paths | Dinic's Algorithm with BFS Level Graphs | Throughput of $1,425\text{ Megaliters/day}$ computed in $< 1\text{ ms}$. |
| **Telemetry Aggregation** | Sequential iterative sum | Blelloch Parallel Prefix Scan on multi-core | $O(\log N)$ parallel span; theoretical bounds verified via Brent's Theorem. |

---

## 9. Final Results

### 9.1 Achievement of Objectives

| Objective | Evidence / Result | Status |
| :--- | :--- | :---: |
| **Provide a unified command-line entry point** | CLI menu options for all 17 municipal domains | **Achieved** |
| **Load and inspect offline municipal data** | Ingestion of 16 datasets ($>1,200$ sensor rows, 120 citizen complaints) in $<45\text{ ms}$ | **Achieved** |
| **Demonstrate six algorithm groups** | CO1 to CO6 modules fully implemented on Zero-JCF structures | **Achieved** |
| **Explain complexity and compare algorithms** | Embedded Complexity Explorer and side-by-side benchmark modules | **Achieved** |
| **Run automated regression checks** | 54/54 JUnit tests passing and 50 embedded terminal assertions | **Achieved** |

### 9.2 Outcome
SmartCityX provides an integrated academic demonstration environment relating discrete algorithm design to realistic municipal analytics. Its principal outcome is the clean organization of data ingestion, domain services, custom zero-overhead data structures, algorithm modules, and interactive terminal workflows into a single cohesive project.

### 9.3 Application Screenshots Walkthrough
- **Figure 4 (Main Menu & Citizen Reports)**: Displays platform dashboard, telemetry summaries (16 datasets loaded), and citizen grievance management options.
- **Figure 5 (Citizen Report Output & Infrastructure Search)**: Demonstrates record lookup, category filtering, priority indicators, and suffix-automaton substring queries.
- **Figure 6 (Infrastructure Documents & Traffic Module)**: Lists operational manuals and presents the 25-junction, 50-arterial road network menu.
- **Figure 7 (Traffic Grid Overview)**: Shows road-link hourly vehicle capacities, junction coordinates, and travel times.
- **Figure 8 (Water, Energy & Demand Listings)**: Displays reservoir Megaliter capacities, energy substation generation types, and municipal utility budget priorities.

---

## 10. Individual Contribution of Each Team Member

| S. No. | Student Name | Roll Number | Individual Contribution |
| :---: | :--- | :---: | :--- |
| **1** | **Satyam Kumar Kapri** | **2500031975** | **100% Project Execution**: Completed full system architecture, project directory reorganization, and zero-JCF data-structure implementation; developed all six algorithm modules (M1–M6); built the dataset ingestion layer and interactive CLI; authored the comprehensive project documentation, 20-slide presentation deck, and final report; managed Git repository version control; and achieved 54/54 passing tests. |
| **2** | Tanmay | 2500031953 | No contribution. |
| **3** | Mehak | 2500031797 | No contribution. |

---

## 11. Conclusion

SmartCityX demonstrates a structured approach to combining theoretical algorithmic concepts with a coherent municipal application scenario. Its documented architecture separates terminal interaction, data ingestion, repositories, domain services, algorithms, and custom data structures. 

The six algorithm groups cover text search, suffix indexing, dynamic programming, network flow, computational complexity, randomized methods, and parallel computation. The project has been rigorously evaluated through module demonstrations, dataset validations, automated regression tests, and reproducible benchmarks, proving that discrete algorithms directly solve 21st-century urban infrastructure challenges.

---

## 12. References

1. **SmartCityX Project Team**. *SmartCityX — Integrated Smart-City Analytics Platform*. Project README and source repository. Available at: [https://github.com/satyamkumarkapri/SmartCity_Cli](https://github.com/satyamkumarkapri/SmartCity_Cli)
2. **Oracle Corporation**. *Java Platform, Standard Edition Documentation (Java 17 LTS)*. Available at: [https://docs.oracle.com/en/java/](https://docs.oracle.com/en/java/)
3. **Apache Software Foundation**. *Maven Documentation and Build Lifecycle*. Available at: [https://maven.apache.org/guides/](https://maven.apache.org/guides/)
4. **Shafranovich, Y.** *Common Format and MIME Type for Comma-Separated Values (CSV) Files*. RFC 4180, Internet Engineering Task Force (IETF), 2005. Available at: [https://www.rfc-editor.org/rfc/rfc4180](https://www.rfc-editor.org/rfc/rfc4180)
5. **Cormen, T. H., Leiserson, C. E., Rivest, R. L., and Stein, C.** *Introduction to Algorithms (3rd/4th Edition)*. MIT Press. General reference for discrete algorithms, network flow, dynamic programming, and NP-completeness.
6. **Supplied SmartCityX Project Documentation**: *CLI User Guide, Municipal Dataset Guide, System Architecture, Algorithm Catalog, and Complexity & Theoretical Analysis notes*.
