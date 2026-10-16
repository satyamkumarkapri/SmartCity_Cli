# SmartCityX Automated Testing & Quality Assurance Report

SmartCityX enforces a rigorous automated testing discipline using JUnit 5 (Jupiter), Maven Surefire, and an embedded in-memory test runner (`SelfTestRunner`).

---

## 1. Test Execution Commands

### Run Full Test Suite via Maven
```bash
mvn clean test
```

### Run Full Test Suite via Shell Script
```bash
./scripts/run-tests.sh
```

### Package Executable JAR
```bash
mvn package -DskipTests
```

---

## 2. JUnit 5 Test Suite Results

The automated Maven test run compiles all production source files and executes **53 comprehensive unit and integration tests** across 8 test classes.

**Overall Test Suite Status: 100% PASSING (53/53 Passed, 0 Failures, 0 Errors, 0 Skipped)**

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running smartcity.structures.DataStructuresTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.algorithms.StringAlgorithmsTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.algorithms.DynamicProgrammingTest
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.algorithms.NetworkFlowAndGraphTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.algorithms.ComplexityAndApproximationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.algorithms.RandomizedAndParallelTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.dataset.DatasetIntegrationTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
Running smartcity.integration.SmartCityWorkflowTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0

Results:
Tests run: 53, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 3. Test Coverage Breakdown by Category

### Category 1: Foundational Data Structures (`DataStructuresTest`)
- `testDynamicArray`: Growth, indexed element access, bounds enforcement, and element removal.
- `testIntArrayList`: Primitive int expansion and array conversions.
- `testCustomStack`: LIFO push/pop order, peeking, and empty-stack exception handling.
- `testCircularQueue`: FIFO enqueue/dequeue behavior and circular wrap-around.
- `testCustomDeque`: Double-ended operations (addFirst, addLast, pollFirst, pollLast).
- `testCustomHashMap`: Key insertion, retrieval, hash collision resolution via separate chaining, key overwrites, and size tracking.
- `testCustomHashSet`: Set uniqueness and membership queries.
- `testCustomPriorityQueue`: Min-heap property preservation across insertions and polls.
- `testDisjointSetUnion`: Union-by-rank and path compression equivalence checks.

### Category 2: String & Suffix Algorithms (`StringAlgorithmsTest`)
- `testExactPatternMatchingAlgorithms`: Validates agreement across Naive, KMP, Z-Algorithm, and Rabin-Karp on municipal grievance records and civil documents.
- `testRabinKarpCollisions`: Validates hash calculation and verification safeguards against false positive collisions.
- `testAhoCorasick`: Multi-keyword search across infrastructure specifications.
- `testSuffixArray`: Validates lexicographical sorting of suffixes and binary search pattern queries.
- `testKasaiLCP`: Validates $O(N)$ Longest Common Prefix values against brute-force comparisons.
- `testSuffixAutomaton`: Verifies online state transitions, distinct substring counts, and pattern queries.
- `testComparisonEngineAgreement`: Verifies 100% agreement across all 4 pattern matching algorithms.

### Category 3: Dynamic Programming (`DynamicProgrammingTest`)
- `testLevenshteinDistance`: Distance values and operation traceback on identical, replaced, inserted, and deleted strings.
- `testDamerauLevenshtein`: Distinguishes Optimal String Alignment (OSA) from unrestricted Damerau-Levenshtein distances.
- `testSequenceAlignment`: Validates Needleman-Wunsch global alignment and Smith-Waterman local alignment.
- `testMatrixChainMultiplication`: Verifies optimal scalar multiplications and parenthesization strings.
- `testOptimalBST`: Computes expected search cost and reconstructed BST root structure.
- `testTravellingSalesperson`: Exact minimum tour cost and path reconstruction.
- `testHamiltonianPath`: Validates path existence and cycle detection.
- `testTreeDiameter`: Computes longest tree path via two-pass BFS/DFS.
- `testTreeRerooting`: Validates all-node distance sum computation.
- `testSumOverSubsets`: Validates fast SOS DP against brute-force submask sums.

### Category 4: Network Flow & Scheduling (`NetworkFlowAndGraphTest`)
- `testMaxFlowAlgorithms`: Validates Ford-Fulkerson, Edmonds-Karp, and Dinic on municipal flow networks.
- `testMinCutDuality`: Strictly verifies Max-Flow Min-Cut duality ($\text{max\_flow} = \text{min\_cut\_capacity}$).
- `testBipartiteMatching`: Computes maximum cardinality bipartite matching for emergency dispatch.
- `testMinCostMaxFlow`: Computes minimum total cost for maximum flow.
- `testCriticalPathMethod`: Validates early/late schedules and critical path identification on municipal engineering projects.
- `testFlowReductions`: Tests Project Selection and Image Segmentation flow models.

### Category 5: Complexity & Approximation (`ComplexityAndApproximationTest`)
- `testVertexCoverApproximation`: Validates 2-approximation factor against exact minimum covers.
- `testKnapsackFPTAS`: Validates $(1 - \varepsilon)$ approximation guarantee.
- `testParameterizedVertexCover`: Validates bounded search tree solver for parameter budgets $k$.
- `testReductionZoo`: Validates polynomial transformation mappings (3-SAT $\to$ CLIQUE $\to$ INDEPENDENT-SET $\to$ VERTEX-COVER).

### Category 6: Randomized & Parallel Algorithms (`RandomizedAndParallelTest`)
- `testRandomizedQuickSort`: Verifies sorting stability on sorted, reverse, random, and duplicate-dense arrays.
- `testMillerRabin`: Validates known primes, composites, and Carmichael pseudoprimes ($561$, $1729$).
- `testReservoirSampling`: Validates uniform extraction over stream inputs.
- `testParallelReduce`: Compares multi-threaded fork-join reduction with sequential loops.
- `testBlellochScan`: Validates parallel exclusive prefix scan against sequential reference.

### Category 7: End-to-End SmartCity Integration (`SmartCityWorkflowTest`)
- `testDatasetLoading`: Validates complete loading of all 11+ municipal datasets.
- `testCitizenReportSearch`: Tests KMP and Levenshtein search against `citizen_reports.csv`.
- `testTrafficMaxFlowDinic`: Executes Dinic's algorithm on the 25-node, 50-edge traffic network.
- `testEmergencyDispatchBipartiteMatching`: Matches emergency squads to active incident requirements.
- `testIoTSensorParallelScan`: Tests Blelloch scan and parallel reduction on 1,200 IoT sensor readings.
- `testCriticalPathScheduling`: Verifies CPM calculations on capital improvement project dependencies.
- `testAhoCorasickInfrastructureSearch`: Searches infrastructure blueprints for safety keywords.

---

## 4. Performance Benchmarking Methodology

- **Time Measurement**: Standardized on `System.nanoTime()` for sub-microsecond resolution.
- **Comparison Counters**: Algorithmic comparison loops explicitly increment counters on character comparisons to record work independent of JVM JIT variations.
- **Collision Counters**: Hash tables and rolling hashes track slot collisions to measure hash dispersion efficiency.

---

## 5. Input Limits & Safety Boundaries

To prevent `OutOfMemoryError` or terminal freezing, SmartCityX enforces defensive safety guards:
- **Bitmask Algorithms** (TSP, Hamiltonian Path, Set Cover): Hard input limit of $N \le 20$ vertices ($2^{20} \approx 10^6$ states).
- **Matrix Chain Multiplication & Optimal BST**: Maximum $N \le 200$ keys to bound $O(N^3)$ cubic time.
- **Miller-Rabin Primality**: Supports full 64-bit unsigned integers via deterministic 12-witness basis; arbitrary integers supported via BigInteger modular exponentiation.
- **RFC-4180 Ingestion**: Safely handles multiline and quoted CSV fields without naive split crashes.
