# SmartCityX — Integrated Smart-City Analytics Platform 🏙️⚡
**Enterprise-Grade CLI Analytics Engine for Municipal Infrastructure, Resource Optimization, and Advanced Discrete Algorithms**

SmartCityX is a production-grade, CLI-based smart-city analytics and decision-support application built in pure **Java 17**. It bridges advanced computer science theory and mission-critical municipal workflows by executing genuine algorithms across transportation networks, utility grids, emergency dispatch systems, civil infrastructure blueprints, and high-frequency IoT telemetry streams.

---

## 🌟 Key Highlights

- **Smart-City Management Workflows**: Real-time municipal intelligence spanning Citizen Reports, Infrastructure Document Search, Traffic Network Flow, Water & Energy Resource Optimization, Emergency Team Dispatch, and IoT Telemetry Analytics.
- **Strict Custom Data Structures**: Within the core algorithm engine, **no Java Collections Framework implementations are substituted** for foundational algorithmic requirements (`ArrayList`, `HashMap`, `HashSet`, `PriorityQueue`, `Stack`, `Queue`, `Deque`, `DisjointSetUnion`, and sorting are strictly implemented under `smartcity.structures.*`).
- **All 6 Master Algorithm Modules Fully Integrated**:
  1. **M1 — String Algorithms (CO1)**: Naive Pattern Search, Knuth-Morris-Pratt (KMP), Z-Algorithm, Rabin-Karp rolling-hash matching, and Aho-Corasick multi-pattern automaton.
  2. **M2 — Suffix Structures (CO2)**: Suffix Array construction, Kasai's LCP Array, Suffix Automaton (DAWG), and Longest Common Substring analysis.
  3. **M3 — Advanced Dynamic Programming (CO3)**: Levenshtein distance, Damerau-Levenshtein distance, Needleman-Wunsch & Smith-Waterman sequence alignment, Matrix Chain Multiplication, Bitmask DP (Travelling Salesperson Problem & Hamiltonian Path), Tree DP (Diameter & Rerooting), and Sum Over Subsets (SOS DP).
  4. **M4 — Network Flow & Scheduling (CO4)**: Ford-Fulkerson, Edmonds-Karp, Dinic's blocking-flow algorithm, Max-Flow/Min-Cut duality bottleneck analysis, Hopcroft-Karp Maximum Bipartite Matching, Critical Path Method (CPM), and Project Selection flow reduction.
  5. **M5 — NP-Completeness & Approximation (CO5)**: 3-SAT to CLIQUE polynomial-time reduction, Karp reduction zoo (CLIQUE $\to$ INDEPENDENT-SET $\to$ VERTEX-COVER), Vertex Cover 2-Approximation, Knapsack FPTAS, and Parameterized Vertex Cover ($O(2^k \cdot (V + E))$ FPT branching).
  6. **M6 — Randomized & Parallel Algorithms (CO6)**: Randomized QuickSort (3-way partitioning), Miller-Rabin probabilistic primality testing (deterministic 64-bit witness basis), Universal Randomized Hashing, Reservoir Sampling, Blelloch work-efficient exclusive prefix scan, Parallel Reduction, Parallel Merge Sort, and Work/Span analysis via Brent's Theorem.
- **Comprehensive Synthetic Municipal Datasets**: Deterministic, validated offline datasets in `data/` including 120 citizen reports, 80 service requests, 50 emergency reports, 60 infrastructure documents, 25 traffic nodes & 50 edges, 40 utility resources & 35 demands, 15 emergency squads & 24 incident tasks, 24 project tasks & 30 dependencies, 22 civil structures & 35 edges, and 1,200 IoT sensor readings.
- **100% Automated Test Suite**: 53 JUnit 5 unit and end-to-end integration tests passing with zero failures.

---

## 🛠️ Prerequisites

- **Java Development Kit (JDK)**: Java 17 or higher (`javac -version` and `java -version`).
- **Build Tool**: Apache Maven 3.8+ (`mvn -version`).
- **Operating System**: macOS, Linux, or Windows.

---

## 🚀 Quick Start (Build & Run)

### 1. Build Executable JAR
```bash
mvn package -DskipTests
```

### 2. Run Automated Test Suite
```bash
mvn test
# Or using the test script:
./scripts/run-tests.sh
```

### 3. Launch the Interactive CLI
```bash
java -jar target/smartcity.jar
# Or using Maven:
mvn compile exec:java -Dexec.mainClass="smartcity.Main"
# Or using the launcher script:
./scripts/run.sh
```

---

## 🖥️ Interactive CLI Navigation (17 Menu Options)

The SmartCityX terminal presents an ANSI-formatted interface with 17 functional modules and 0 Exit:

```
========================================================================================
  SMARTCITYX — INTEGRATED SMART-CITY ANALYTICS PLATFORM
========================================================================================
 Citizen Reports : 120 loaded         | Service Requests    : 80 loaded
 Emergency Inc.  : 50 loaded          | Infr. Documents     : 60 loaded
 Traffic Graph   : 25 nodes, 50 edges | Utilities (W/E)     : 20 water, 20 energy
 Emergency Teams : 15 teams, 24 reqs  | Project Schedule    : 24 tasks, 30 deps
 Infrastructure  : 22 verts, 35 edges | IoT Sensor Stream   : 1,200 readings
----------------------------------------------------------------------------------------
 [ 1] Citizen Reports and Service Requests      (Exact/Fuzzy search, Priority Queue)
 [ 2] Infrastructure Document Search            (Aho-Corasick, Suffix Automaton, LCP)
 [ 3] Traffic and Transportation Analysis       (Dinic Max-Flow, Min-Cut, TSP Bitmask)
 [ 4] Water and Energy Resource Management      (Bipartite Matching, Knapsack FPTAS)
 [ 5] Emergency Response Management             (Squad dispatch, Graph reachability)
 [ 6] IoT Sensor Analytics                      (Blelloch Scan, Parallel Reduce, Hashing)
 [ 7] String Algorithms — CO1                   (KMP, Z-Algorithm, Rabin-Karp, Naive)
 [ 8] Suffix Structures & Similarity — CO2      (Suffix Array, Kasai LCP, Automaton)
 [ 9] Advanced Dynamic Programming — CO3        (Levenshtein, Alignment, MCM, Tree DP)
 [10] Network Flow & Resource Alloc — CO4       (Ford-Fulkerson, Dinic, CPM Scheduling)
 [11] NP-Completeness & Approximation — CO5     (Karp Reductions, VC-Approx, FPT)
 [12] Randomized Algorithms — CO6               (QuickSort, Miller-Rabin, Reservoir)
 [13] Parallel Algorithms — CO6                 (Work/Span, Blelloch, Parallel Sort)
 [14] Dataset Management                        (Inspect records, verify schemas)
 [15] Algorithm Comparison & Benchmarking       (Head-to-head empirical benchmarks)
 [16] Complexity Explorer                       (Big-O analysis & theoretical bounds)
 [17] Automated Self-Tests                      (Run 45+ embedded regression checks)
 [ 0] Exit SmartCityX
========================================================================================
```

---

## 📊 Synthetic Municipal Datasets (`data/`)

All datasets reside under `data/` and adhere to strict RFC-4180 CSV / JSONL specifications:

| Dataset File | Records | Required | Description |
|:---|:---:|:---:|:---|
| `citizen_reports.csv` | 120 | $\ge 100$ | Municipal grievances (potholes, streetlights, leaks) with priority levels |
| `service_requests.csv` | 80 | $\ge 60$ | Internal work orders mapped to municipal department queues |
| `emergency_reports.csv` | 50 | $\ge 40$ | High-urgency incidents (structural fires, chemical hazards, gas leaks) |
| `infrastructure_documents.jsonl` | 60 | $\ge 50$ | Municipal building specs, transit plans, and civil engineering records |
| `traffic_nodes.csv` | 25 | $\ge 20$ | Key traffic intersections, highway interchanges, and transit hubs |
| `traffic_edges.csv` | 50 | $\ge 40$ | Arterial roadways with vehicle capacities, speeds, and traversal weights |
| `water_resources.csv` | 20 | $\ge 15$ | Reservoirs, treatment plants, pumping stations, and supply capacities |
| `energy_resources.csv` | 20 | $\ge 15$ | Substation transformers, solar microgrids, and MW output limits |
| `resource_demands.csv` | 35 | $\ge 30$ | District utility quotas and consumption requirements |
| `emergency_teams.csv` | 15 | $\ge 12$ | Specialized response units (HazMat, EMT, Search & Rescue) |
| `assignment_requirements.csv` | 24 | $\ge 20$ | Incident dispatch constraints and squad qualifications |
| `project_tasks.csv` | 24 | $\ge 20$ | Capital improvement tasks with durations and budget estimates |
| `project_dependencies.csv` | 30 | $\ge 25$ | Directed precedence constraints for Critical Path Method (CPM) |
| `infrastructure_vertices.csv` | 22 | $\ge 20$ | Critical civil assets for maintenance network optimization |
| `infrastructure_edges.csv` | 35 | $\ge 30$ | Interconnected infrastructure pipelines and structural connections |
| `iot_sensor_readings.csv` | 1,200 | $\ge 1,000$ | Telemetry observations (PM2.5, noise, temperature, flow rate, vibration) |

---

## 🏛️ System Architecture

```
SmartCityCLI/
├── pom.xml                                <-- Maven POM (Java 17, JUnit 5.10.2)
├── README.md                              <-- Master project documentation
├── LICENSE                                <-- MIT License
├── data/                                  <-- Municipal CSV/JSONL datasets
│   ├── citizen_reports.csv
│   ├── service_requests.csv
│   ├── emergency_reports.csv
│   ├── infrastructure_documents.jsonl
│   ├── traffic_nodes.csv & traffic_edges.csv
│   ├── water_resources.csv & energy_resources.csv
│   ├── resource_demands.csv
│   ├── emergency_teams.csv & assignment_requirements.csv
│   ├── project_tasks.csv & project_dependencies.csv
│   ├── infrastructure_vertices.csv & infrastructure_edges.csv
│   ├── iot_sensor_readings.csv
│   └── README.md                          <-- Complete dataset schema documentation
├── docs/                                  <-- Theoretical & design documentation
│   ├── ARCHITECTURE.md
│   ├── ALGORITHM_CATALOG.md
│   ├── COMPLEXITY_ANALYSIS.md
│   ├── DATASET_GUIDE.md
│   ├── USER_GUIDE.md
│   └── TESTING.md
├── scripts/
│   ├── run.sh                             <-- Executable launcher
│   └── run-tests.sh                       <-- Test runner script
└── src/
    ├── main/
    │   ├── java/smartcity/
    │   │   ├── Main.java                  <-- Application entry point
    │   │   ├── cli/                       <-- SmartCityCLI interactive shell & SelfTestRunner
    │   │   ├── core/                      <-- QueryEngine, SmartCityEngine, Registry
    │   │   ├── model/                     <-- Municipal entity records & typed query results
    │   │   ├── repository/                <-- In-memory repositories & inverted indexes
    │   │   ├── dataset/                   <-- SmartCityDatasetLoader (RFC-4180 CSV / JSONL)
    │   │   ├── services/                  <-- Municipal domain service orchestrators
    │   │   ├── structures/                <-- Custom zero-JCF foundational data structures
    │   │   ├── algorithms/
    │   │   │   ├── strings/               <-- KMP, Z-Algo, Rabin-Karp, Aho-Corasick
    │   │   │   ├── suffix/                <-- SuffixArray, Kasai LCP, SuffixAutomaton
    │   │   │   ├── dynamicprogramming/    <-- Levenshtein, Alignment, MCM, TSP, Tree DP, SOS
    │   │   │   ├── graph/                 <-- Dinic, Ford-Fulkerson, MinCut, CPM, Bipartite
    │   │   │   ├── complexity/            <-- VC-Approximation, FPTAS, Reductions, FPT
    │   │   │   ├── randomized/            <-- Miller-Rabin, QuickSort, Reservoir, Hashing
    │   │   │   └── parallel/              <-- Blelloch Scan, Parallel Reduce, Parallel Sort
    │   │   └── util/                      <-- Input parsing & ANSI styling helpers
    │   └── resources/
    │       └── sample_corpus.jsonl        <-- Fallback bundled municipal blueprint corpus
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

## 🧪 Testing and Verification

Run the full automated test suite:
```bash
mvn clean test
```

### Test Suite Summary:
- **53 Automated Tests Passing** (0 Failures, 0 Errors, 0 Skipped).
- **Embedded Self-Tests**: 45+ internal assertions executable directly from Menu Option 17.
- **Workflow Verification**: `SmartCityWorkflowTest` validates end-to-end dataset loading, KMP query search, Dinic traffic network max flow, bipartite squad dispatch, and Blelloch telemetry prefix scans.

---

## 🔒 Safety Invariants & Boundary Guards

1. **Bitmask DP Safety Caps**: TSP and Hamiltonian Path bitmask DP operate in $O(N^2 \cdot 2^N)$. Graph sizes are defensively capped at $N \le 20$ vertices to prevent JVM OutOfMemoryError.
2. **Matrix Chain Multiplication**: Restricted to $N \le 200$ matrices to enforce responsive $O(N^3)$ terminal response.
3. **RFC-4180 Compliant CSV Ingestion**: Handles quoted values containing commas, escaped quotation marks, and arbitrary whitespace safely.
4. **Offline Autonomy**: Operates 100% locally with zero internet dependencies and zero external cloud API connections.
