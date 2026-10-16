# SmartCityX System Architecture

`SmartCityX — Integrated Smart-City Analytics Platform` is an enterprise-grade, production CLI laboratory and municipal decision-support system designed for exploring advanced data structures and discrete algorithms across civil infrastructure, transportation networks, utility grids, emergency dispatch, and continuous IoT telemetry.

---

## 1. High-Level Architectural Diagram

```
+-----------------------------------------------------------------------------------+
|                                 USER / TERMINAL                                   |
|                        (macOS Terminal / VS Code Integrated CLI)                  |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                             smartcity.cli (Presentation)                          |
|  - SmartCityCLI (17-Menu Interactive Shell & Sub-Routers)                         |
|  - SelfTestRunner (45+ assertion automated regression test battery)               |
|  - InputUtil & ConsoleUtil (ANSI styling, bold dark text, robust EOF recovery)    |
+-----------------------------------------+-----------------------------------------+
                                          |
                                          | QueryRequest / Direct Service Calls
                                          v
+-----------------------------------------------------------------------------------+
|                       smartcity.service (Municipal Domain Services)               |
|  - CitizenReportService       - DocumentSearchService   - TrafficAnalyticsService |
|  - ResourceManagementService  - EmergencyResponseService- IoTSensorService        |
|  - BenchmarkService           - SmartCityEngine / QueryEngine                     |
+-------------------+---------------------+--------------------+--------------------+
                    |                     |                    |
                    v                     v                    v
+-----------------------+ +-------------------------+ +-----------------------------+
| smartcity.repository  | | smartcity.dataset       | | smartcity.algorithms.*      |
| - CitizenRepository   | | - SmartCityDatasetLoader| | - strings.* (CO1)           |
| - DocumentRepository  | |   (RFC-4180 CSV & JSONL | | - suffix.* (CO2)            |
| - TrafficRepository   | |    parser with fallback)| | - dp.* (CO3)                |
| - ResourceRepository  | | - 11+ municipal datasets| | - graph.* & flow.* (CO4)    |
| - EmergencyRepository | |   in data/ directory    | | - complexity.* (CO5)        |
| - IoTSensorRepository | |                         | | - randomized.* (CO6)        |
| - ProjectRepository   | |                         | | - parallel.* (CO6)          |
+-----------------------+ +-------------------------+ +-----------------------------+
                    ^                     ^                    |
                    |                     |                    |
                    +---------------------+--------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                             smartcity.structures                                  |
|  (Core Data-Structure Foundation: NO java.util Collections inside algorithms)     |
|  - DynamicArray<E>, IntArrayList, CustomStack<E>, CircularQueue<E>, CustomDeque<E>|
|  - CustomHashMap<K, V>, CustomHashSet<E>, CustomPriorityQueue<E> (Min/Max Heap)   |
|  - DisjointSetUnion (DSU), CustomTrie, CustomGraph, CustomFlowNetwork, CustomSort |
+-----------------------------------------------------------------------------------+
```

---

## 2. Package Responsibilities

| Package | Responsibility |
| :--- | :--- |
| `smartcity` | Root package containing `Main.java` (entry point and CLI bootstrapper). |
| `smartcity.cli` | Terminal user interface, 17-item menu renderers, ANSI console colorizers, and the embedded `SelfTestRunner`. |
| `smartcity.services` | Domain services wiring municipal datasets directly to algorithmic engines (`CitizenReportService`, `TrafficAnalyticsService`, etc.). |
| `smartcity.core` | Public query interface (`QueryEngine`), implementation (`SmartCityEngine`), and the comprehensive `AlgorithmRegistry`. |
| `smartcity.model` | Strongly typed immutable request and municipal domain records (`CitizenReport`, `ServiceRequest`, `EmergencyReport`, `TrafficNode`, `IoTReading`, etc.). |
| `smartcity.repository`| In-memory data repositories holding typed entities and inverted keyword indexes for instantaneous lookup. |
| `smartcity.dataset` | RFC-4180 compliant CSV / JSONL loader with fallback mechanisms to ensure 100% offline autonomy. |
| `smartcity.structures` | Strict custom implementations of all foundational data structures. Enforces zero `java.util` collections within core algorithms. |
| `smartcity.algorithms.strings` | Exact pattern matching (`KMP`, `ZAlgorithm`, `RabinKarp`, `NaiveSearch`, `AhoCorasick`) and string benchmark comparison. |
| `smartcity.algorithms.suffix` | Lexicographical suffix sorting (`SuffixArray`), Kasai's `LCP`, and Directed Acyclic Word Graphs (`SuffixAutomaton`). |
| `smartcity.algorithms.dynamicprogramming` | String distances (`Levenshtein`, `DamerauLevenshtein`), alignments (`NeedlemanWunsch`, `SmithWaterman`), tree DPs, bitmasks (`TSP`, `HamiltonianPath`), interval DPs (`MCM`, `OptimalBST`), and fast Walsh-Hadamard transform (`SumOverSubsetsDP`). |
| `smartcity.algorithms.graph` | Network flow algorithms (`Dinic`, `EdmondsKarp`, `FordFulkerson`), Min-Cut duality (`MinCut`), Min-Cost Max-Flow, Bipartite Matching, Critical Path Method (`CPM`), and flow reductions (`ProjectSelectionFlow`, `ImageSegmentationFlow`). |
| `smartcity.algorithms.complexity` | Complexity class taxonomies, Karp reduction zoo demonstrations (3-SAT $\to$ CLIQUE $\to$ INDEPENDENT-SET $\to$ VERTEX-COVER), 2-approximation for Vertex Cover, Knapsack FPTAS, and parameterized bounded branching solver (`ParameterizedVertexCover`). |
| `smartcity.algorithms.randomized`| 3-way randomized quicksort, Miller-Rabin primality testing with deterministic 64-bit witness basis, universal 2-independent hashing, and streaming reservoir sampling. |
| `smartcity.algorithms.parallel` | Work/Span analysis, Brent's Theorem guide, parallel divide-and-conquer reductions (`ParallelReduce`), Blelloch work-efficient exclusive prefix scan, and parallel merge-sort. |
| `smartcity.util` | Safe input parsing, numeric validation with EOF safety, and bold ANSI terminal formatting helpers. |

---

## 3. Data Flow Architecture

The data pipeline guarantees strict data consistency and fast query execution:

1. **Bootstrap Phase**:
   - `SmartCityDatasetLoader.loadAll()` is invoked from `Main.java`.
   - Locates `data/` directory or classpath resource fallbacks.
   - Parses RFC-4180 CSV records (handling commas within quotes, escaped characters).
   - Populates typed repositories (`CitizenRepository`, `TrafficRepository`, `ResourceRepository`, `IoTSensorRepository`, etc.).
   - Builds inverted indexes and suffix structures over infrastructure documents and citizen grievances.

2. **Execution Phase**:
   - User enters selection in `SmartCityCLI`.
   - Action delegates to corresponding service layer (`TrafficAnalyticsService`, `EmergencyResponseService`, etc.).
   - Service extracts problem instance from repositories and constructs algorithmic models (`CustomFlowNetwork`, `CustomGraph`, `int[]` stream).
   - Algorithmic engine executes using custom data structures, capturing nanosecond runtimes and comparison counts.
   - Formatted results render to terminal with Big-O complexity cards.

---

## 4. Core Data-Structure Restriction Enforcement

To preserve academic and software engineering rigor, all algorithm packages (`smartcity.algorithms.*`) **strictly prohibit** Java Collections Framework implementations:
- **Prohibited**: `java.util.ArrayList`, `LinkedList`, `HashMap`, `HashSet`, `TreeMap`, `TreeSet`, `PriorityQueue`, `Stack`, `Queue`, `Arrays.sort`, `Collections.sort`.
- **Allowed & Enforced**: Custom data structures implemented under `smartcity.structures.*` (`DynamicArray`, `IntArrayList`, `CustomHashMap`, `CustomHashSet`, `CustomStack`, `CircularQueue`, `CustomDeque`, `CustomPriorityQueue`, `DisjointSetUnion`, `CustomTrie`, `CustomGraph`, `CustomFlowNetwork`, `CustomSort`).
- **Primitives Permitted**: Basic Java arrays (`int[]`, `char[]`, `double[]`), `java.math.BigInteger` (for overflow-safe modular exponentiation in Miller-Rabin), standard Java I/O streams in dataset/cli layers, and concurrency primitives (`ForkJoinPool`, `RecursiveTask`) in parallel algorithm wrappers.

---

## 5. Related Academic Documentation

- 📄 [PBL Final Project Report](FINAL_PROJECT_REPORT.md) — Complete 19-page formal report for Course 25CS2103E.
- 📊 [20-Slide Presentation Deck](PRESENTATION_SLIDES_20.md) — Comprehensive defense deck with theoretical formulations and speaker notes.
- 📚 [Algorithm Catalog](ALGORITHM_CATALOG.md) — Detailed mapping of all Modules 1–6 (CO1–CO6) algorithms.
- 📐 [Complexity & Theoretical Analysis](COMPLEXITY_ANALYSIS.md) — Mathematical complexity proofs and asymptotic bounds.
- 🧪 [Testing Methodology & Test Cases](TESTING.md) — Verification suite breakdown and JUnit 5 regression matrix.

