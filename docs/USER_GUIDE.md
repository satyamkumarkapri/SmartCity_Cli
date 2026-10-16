# SmartCityX Interactive CLI User Guide

**SmartCityX — Integrated Smart-City Analytics Platform** is an enterprise-grade command-line analytics engine built in Java 17 for municipal management, resource optimization, emergency dispatch, and advanced discrete algorithms.

---

## 1. Launching the Application

### Option A: Via Run Script
```bash
./scripts/run.sh
```

### Option B: Via Executable JAR
```bash
java -jar target/smartcity.jar
```

### Option C: Via Maven
```bash
mvn compile exec:java -Dexec.mainClass="smartcity.Main"
```

---

## 2. Main Menu Structure

Upon startup, SmartCityX loads all 11+ municipal datasets from `data/` and displays the system dashboard:

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
Enter choice (0-17):
```

---

## 3. Detailed Workflow Descriptions

### [1] Citizen Reports and Service Requests
- **Purpose**: Search municipal reports using exact (KMP / Rabin-Karp) or typo-tolerant fuzzy search (Levenshtein edit distance).
- **Sub-Actions**:
  1. Search reports by keyword (e.g. `pothole`, `leak`, `street light`).
  2. Search by fuzzy match with specified edit-distance tolerance.
  3. View high-priority request dispatch queues sorted by urgency.

### [2] Infrastructure Document Search
- **Purpose**: Multi-pattern and substring indexing across civil blueprints, engineering specs, and municipal codes.
- **Sub-Actions**:
  1. Multi-pattern inspection using the Aho-Corasick automaton across civil safety codes (`HVAC`, `seismic`, `concrete`, `drainage`).
  2. Suffix Automaton fast substring search and distinct factor counting.
  3. Document similarity scoring using Kasai's Longest Common Prefix (LCP) array.

### [3] Traffic and Transportation Analysis
- **Purpose**: Graph-theoretic network optimization for arterial roads, intersections, and municipal transit routes.
- **Sub-Actions**:
  1. Maximum vehicle throughput computation using Dinic's blocking-flow algorithm.
  2. Roadway bottleneck detection via Max-Flow / Min-Cut duality theorem.
  3. Travelling Salesperson Problem (TSP) exact route optimization using $O(N^2 2^N)$ Bitmask DP.
  4. Inspection of traffic intersection topology (25 nodes, 50 edges).

### [4] Water and Energy Resource Management
- **Purpose**: Utility allocation, quota matching, and budget optimization across reservoirs, pumps, and power substations.
- **Sub-Actions**:
  1. Maximum bipartite matching of district quotas to utility plants.
  2. Budget allocation optimization using the $(1 - \varepsilon)$ Knapsack FPTAS.
  3. Direct inspection of water and electrical facility parameters.

### [5] Emergency Response Management
- **Purpose**: Real-time incident triage and emergency unit assignment.
- **Sub-Actions**:
  1. Maximal bipartite matching dispatch between specialized teams (HazMat, EMT, Fire) and active emergency calls.
  2. Urgent incident prioritization using custom min/max heaps.
  3. Reachability and shortest-path calculation across the municipal transport graph.

### [6] IoT Sensor Analytics
- **Purpose**: Continuous telemetry processing from 1,200+ environmental and structural sensors.
- **Sub-Actions**:
  1. Blelloch work-efficient parallel prefix sum (scan) over telemetry sequences.
  2. Multi-threaded parallel reduction for telemetry extremes (MAX, MIN, AVG).
  3. 2-independent universal hashing for stream sampling and deduplication.
  4. Threshold anomaly detection (PM2.5, vibration, temperature excursions).

### [7] String Algorithms — CO1
- **Implementations**: Naive matching, Knuth-Morris-Pratt (KMP), Z-Algorithm, Rabin-Karp rolling hash, and Aho-Corasick.
- **Features**: Exact character comparisons, nanosecond timing, and full corpus hit positions.

### [8] Suffix Structures & Document Similarity — CO2
- **Implementations**: Suffix Array construction, Kasai's algorithm for LCP arrays, Suffix Automaton (DAWG), and longest common substring extraction.

### [9] Advanced Dynamic Programming — CO3
- **Implementations**: Levenshtein, Damerau-Levenshtein (optimal string alignment), Needleman-Wunsch & Smith-Waterman alignment, Matrix-Chain Multiplication, Bitmask TSP & Hamiltonian Path, Tree Diameter & Rerooting, and SOS DP.

### [10] Network Flow & Resource Allocation — CO4
- **Implementations**: Ford-Fulkerson, Edmonds-Karp, Dinic's blocking flow, Max-Flow/Min-Cut dual identification, Bipartite Matching, Critical Path Method (CPM) project scheduling, and Project Selection flow reduction.

### [11] NP-Completeness and Approximation — CO5
- **Implementations**: 3-SAT to CLIQUE reduction, Karp reduction chain (CLIQUE $\to$ INDEPENDENT-SET $\to$ VERTEX-COVER), Vertex Cover 2-approximation, Knapsack FPTAS, and Parameterized Vertex Cover ($O(2^k \cdot (V + E))$ FPT branching).

### [12] Randomized Algorithms — CO6
- **Implementations**: Randomized 3-way QuickSort, Miller-Rabin primality testing with 64-bit deterministic basis, Universal Hashing, and Reservoir Sampling over unbounded data streams.

### [13] Parallel Algorithms — CO6
- **Implementations**: Multi-threaded parallel tree reduction, Blelloch exclusive prefix scan, Parallel Merge Sort, and Work/Span theoretical analysis via Brent's Theorem.

### [14] Dataset Management
- **Features**: Reload all datasets, display schema verification reports, and inspect summary statistics of in-memory municipal repositories.

### [15] Algorithm Comparison & Benchmarking
- **Features**: Side-by-side empirical benchmarking measuring wall-clock nanoseconds, exact operation counts, and Big-O invariants with 100% agreement verification.

### [16] Complexity Explorer
- **Features**: In-depth theoretical taxonomy covering formal problem definitions, invariants, worst/average case bounds, and engineering boundaries for all implemented algorithms.

### [17] Automated Self-Tests
- **Features**: In-memory test runner executing 45+ algorithmic assertions and unit checks across all 6 DSA modules without requiring external build tools.
