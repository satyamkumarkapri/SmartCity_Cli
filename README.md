# SmartCityX 🏙️
**Integrated Smart-City Analytics Platform**

SmartCityX is a comprehensive command-line interface (CLI) application built in pure **Java 17**. It serves as an integrated smart-city analytics platform that manages and analyzes transportation, water/energy resources, public service requests, emergency reports, city infrastructure, and continuous IoT sensor observations.

This project was built to demonstrate the practical application of **Advanced Data Structures and Algorithms** in solving complex real-world urban management problems.

---

## 🚀 Features

- **Citizen & Service Reports**: Manage and search through citizen-submitted issues and emergency reports.
- **Document Search & Similarity**: Index, search, and compare city infrastructure and operational documents.
- **City Resource Optimization**: Allocate budgets and resources efficiently using dynamic programming.
- **Network Flow & Resource Allocation**: Analyze water/electricity distribution and emergency team assignments.
- **Constraint Analysis (NP-Completeness)**: Solve complex city constraints and bottlenecks using graph theory and boolean satisfiability.
- **IoT & Parallel Data Analytics**: Process high-velocity simulated IoT sensor streams (temperature, traffic, air quality) in parallel.
- **Universal Search**: Instantly query across all departments and objects.
- **Algorithm Complexity Display**: View the theoretical Big-O Time & Space complexities of every algorithm as they execute.

---

## 🧠 Implemented Algorithms

The platform is powered by over **20+ advanced algorithms** grouped into 6 core modules:

### 1. String Algorithms (M1)
Used for fast information retrieval in citizen reports and service requests.
* **KMP (Knuth-Morris-Pratt)**: Substring search.
* **Z-Algorithm**: Linear time pattern matching.
* **Rabin-Karp**: Rolling hash-based search.
* **Aho-Corasick**: Simultaneous multi-keyword search.

### 2. Suffix Structures (M2)
Used for document indexing, similarity, and deep substring searches.
* **Suffix Array & SA-IS**: Lexicographical suffix sorting.
* **LCP (Longest Common Prefix) & Kasai's Algorithm**: Finding common substrings.
* **Suffix Tree**: Fast substring operations.
* **Suffix Automaton**: Directed acyclic word graph for pattern matching.

### 3. Advanced Dynamic Programming (M3)
Used for resource selection, budget optimization, and typo-correction.
* **Levenshtein Distance**: Query correction and document similarity.
* **Damerau-Levenshtein**: Edit distance handling transpositions.
* **Bitmask DP**: Optimal city resource combinations.
* **Subset DP (Subset Sum)**: Exact budget matching.
* **Matrix Chain Multiplication**: Operations optimization.

### 4. Network Flow (M4)
Used for water distribution, traffic analysis, and emergency resource allocation.
* **Ford-Fulkerson**: Max-flow using DFS.
* **Edmonds-Karp**: Max-flow using BFS.
* **Dinic’s Algorithm**: Max-flow using level graphs.
* **Bipartite Matching**: Assigning emergency teams to incidents.

### 5. NP-Completeness & Approximation (M5)
Used for solving hard constraint scenarios in urban planning.
* **SAT & 3-SAT**: Boolean satisfiability for city constraints.
* **Clique & Independent Set**: Graph-based structural analysis.
* **Vertex Cover (Exact & 2-Approximation)**: Infrastructure monitoring and bottleneck detection.

### 6. Randomized & Parallel Algorithms (M6)
Used for real-time IoT processing and priority sorting.
* **Randomized QuickSort**: Fast sorting of service requests by priority.
* **Reservoir Sampling**: Processing infinite IoT sensor streams.
* **Miller-Rabin**: Probabilistic primality testing (e.g., for secure sensor IDs).
* **Blelloch Scan**: Exclusive prefix sum for cumulative measurements.
* **Parallel Reduce**: Multi-threaded calculation of Sum, Min, and Max using Java `ForkJoinPool`.
* **Brent's Theorem**: Parallel execution bound estimation.

---

## 🏗️ Architecture

The project follows a clean Object-Oriented Programming (OOP) design:
- **Models**: `CitizenReport`, `CityDocument`, `EmergencyReport`, `Infrastructure`, `Resource`, `SensorReading`, `CityNode`.
- **Enums**: Strongly typed statuses and categories (`ReportPriority`, `EmergencySeverity`, etc.)
- **Repositories**: In-memory data structures simulating database storage.
- **Services**: Business logic separating algorithms from the CLI routing.
- **Utils**: Clean console formatting, input validation, algorithm complexity display, and realistic sample data generation.
- **Exceptions**: Custom `SmartCityException`, `InvalidInputException`, etc.

*Note: No external libraries (like Spring Boot, Maven, or Gradle) are used. Everything is built using standard `java.util` and `java.math` libraries.*

---

## 💻 How to Run (Eclipse IDE)

1. Open **Eclipse**.
2. Go to **File** → **Import** → **Existing Projects into Workspace**.
3. Select the `SmartCityCLI` directory and click **Finish**.
4. In the **Package Explorer** panel on the left, expand `SmartCityCLI` → `src` → `smartcityx`.
5. Right-click on **`Main.java`**.
6. Select **Run As** → **Java Application**.
7. In the Eclipse Console, interact with the system (type `Y` to load sample data!).

---
*Built for DSA-3 Group Project.*
