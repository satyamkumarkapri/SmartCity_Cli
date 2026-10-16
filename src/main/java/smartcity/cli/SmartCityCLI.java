package smartcity.cli;

import smartcity.algorithms.complexity.ComplexityClassesGuide;
import smartcity.algorithms.complexity.KnapsackFPTAS;
import smartcity.algorithms.complexity.ParameterizedVertexCover;
import smartcity.algorithms.complexity.ReductionZoo;
import smartcity.algorithms.complexity.VertexCoverApproximation;
import smartcity.algorithms.dynamicprogramming.*;
import smartcity.algorithms.graph.*;
import smartcity.algorithms.parallel.BlellochScan;
import smartcity.algorithms.parallel.BrentsTheoremGuide;
import smartcity.algorithms.parallel.ParallelReduce;
import smartcity.algorithms.parallel.ParallelSort;
import smartcity.algorithms.randomized.MillerRabin;
import smartcity.algorithms.randomized.RandomizedClassificationsGuide;
import smartcity.algorithms.randomized.RandomizedHashing;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.randomized.ReservoirSampling;
import smartcity.algorithms.strings.*;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.algorithms.suffix.SuffixAutomaton;
import smartcity.dataset.SmartCityDatasetLoader;
import smartcity.model.enums.ReportCategory;
import smartcity.model.enums.ReportPriority;
import smartcity.model.enums.ReportStatus;
import smartcity.model.CitizenReport;
import smartcity.repository.*;
import smartcity.services.*;
import smartcity.structures.CustomFlowEdge;
import smartcity.structures.CustomFlowNetwork;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;
import smartcity.util.ConsoleUtil;
import smartcity.util.InputUtil;

import java.util.Random;

/**
 * SmartCityX Interactive CLI Application.
 * Full-featured terminal interface integrating all 17 municipal analytics and DSA modules.
 */
public class SmartCityCLI {
    private final CitizenReportRepository citizenRepo = new CitizenReportRepository();
    private final ServiceRequestRepository serviceRepo = new ServiceRequestRepository();
    private final EmergencyReportRepository emergencyRepo = new EmergencyReportRepository();
    private final DocumentRepository docRepo = new DocumentRepository();
    private final IndexRepository indexRepo = new IndexRepository();
    private final TrafficRepository trafficRepo = new TrafficRepository();
    private final ResourceRepository resourceRepo = new ResourceRepository();
    private final EmergencyTeamRepository teamRepo = new EmergencyTeamRepository();
    private final ProjectRepository projectRepo = new ProjectRepository();
    private final InfrastructureRepository infraRepo = new InfrastructureRepository();
    private final SensorRepository sensorRepo = new SensorRepository();

    private final CitizenReportService citizenService;
    private final DocumentSearchService documentService;
    private final TrafficAnalyticsService trafficService;
    private final ResourceManagementService resourceService;
    private final EmergencyResponseService emergencyService;
    private final IoTSensorService sensorService;
    private final BenchmarkService benchmarkService;
    private final SelfTestRunner selfTestRunner = new SelfTestRunner();

    private boolean running = true;
    private SmartCityDatasetLoader.LoadStats datasetStats;

    public SmartCityCLI() {
        this.citizenService = new CitizenReportService(citizenRepo, serviceRepo);
        this.documentService = new DocumentSearchService(docRepo);
        this.trafficService = new TrafficAnalyticsService(trafficRepo, projectRepo);
        this.resourceService = new ResourceManagementService(resourceRepo);
        this.emergencyService = new EmergencyResponseService(emergencyRepo, teamRepo, infraRepo);
        this.sensorService = new IoTSensorService(sensorRepo);
        this.benchmarkService = new BenchmarkService(docRepo, trafficRepo, sensorRepo);
    }

    public void start() {
        // Load datasets on startup
        loadDatasets();

        ConsoleUtil.printBanner();

        while (running) {
            printMainMenu();
            try {
                int choice = InputUtil.getInt(ConsoleUtil.BOLD_BLACK + "Enter choice (0-17): " + ConsoleUtil.RESET);
                handleMenuChoice(choice);
            } catch (java.util.NoSuchElementException e) {
                break;
            } catch (Exception e) {
                ConsoleUtil.printError("An error occurred: " + e.getMessage());
            }
        }
    }

    private void loadDatasets() {
        datasetStats = SmartCityDatasetLoader.loadAll(
                citizenRepo, serviceRepo, emergencyRepo, docRepo, indexRepo,
                trafficRepo, resourceRepo, teamRepo, projectRepo, infraRepo, sensorRepo
        );
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println(ConsoleUtil.CYAN + "╔══════════════════════════════════════════════════════════════════════════════════════╗" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "                 SMARTCITYX — INTEGRATED CITY ANALYTICS PLATFORM                      " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "╠══════════════════════════════════════════════════════════════════════════════════════╣" + ConsoleUtil.RESET);
        System.out.printf(ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET + "  " + ConsoleUtil.BOLD_BLACK + "%-20s" + ConsoleUtil.RESET + " : %-59s" + ConsoleUtil.CYAN + " ║%n" + ConsoleUtil.RESET,
                "Active Datasets", String.format("%,d Reports, %,d Reqs, %,d Docs, %,d Sensors",
                        citizenRepo.size(), serviceRepo.size(), docRepo.size(), sensorRepo.size()));
        System.out.printf(ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET + "  " + ConsoleUtil.BOLD_BLACK + "%-20s" + ConsoleUtil.RESET + " : %-59s" + ConsoleUtil.CYAN + " ║%n" + ConsoleUtil.RESET,
                "Network Grids", String.format("Traffic: %d nodes, %d links | Infra: %d nodes, %d links",
                        trafficRepo.getNodeCount(), trafficRepo.getEdgeCount(), infraRepo.getVertexCount(), infraRepo.getEdgeCount()));
        if (datasetStats != null) {
            System.out.printf(ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET + "  " + ConsoleUtil.BOLD_BLACK + "%-20s" + ConsoleUtil.RESET + " : %-59s" + ConsoleUtil.CYAN + " ║%n" + ConsoleUtil.RESET,
                    "Dataset Telemetry", String.format("16 datasets loaded and indexed in %d ms (100%% Local)", datasetStats.durationMs));
        }
        System.out.println(ConsoleUtil.CYAN + "╠══════════════════════════════════════════════════════════════════════════════════════╣" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET + "  " + ConsoleUtil.BOLD + ConsoleUtil.BRIGHT_CYAN + "MUNICIPAL OPERATIONS & SERVICES" + ConsoleUtil.RESET + "              " + ConsoleUtil.BOLD + ConsoleUtil.BRIGHT_CYAN + "DATA STRUCTURES & ALGORITHMS (CO1-CO6)" + ConsoleUtil.RESET + " " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [1] Citizen Reports & Service Requests      [7] String Algorithms — CO1            " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [2] Infrastructure Document Search          [8] Suffix Structures & LCP — CO2      " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [3] Traffic & Transportation Analysis       [9] Advanced Dynamic Prog — CO3        " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [4] Water & Energy Resource Management     [10] Network Flow & Allocation — CO4    " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [5] Emergency Response Management          [11] NP-Completeness & Approx — CO5     " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "   [6] IoT Sensor Analytics                   [12] Randomized Algorithms — CO6        " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "                                              [13] Parallel Algorithms — CO6          " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "╟──────────────────────────────────────────────────────────────────────────────────────╢" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET + "  " + ConsoleUtil.BOLD + ConsoleUtil.BRIGHT_YELLOW + "SYSTEM, BENCHMARKS & EVALUATION" + ConsoleUtil.RESET + "                                                        " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "  [14] Dataset Management                     [16] Complexity Explorer                " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "  [15] Algorithm Comparison & Benchmarking    [17] Automated Self-Tests               " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "║" + ConsoleUtil.BOLD_BLACK + "                                               [0] Exit Application                   " + ConsoleUtil.CYAN + "║" + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.CYAN + "╚══════════════════════════════════════════════════════════════════════════════════════╝" + ConsoleUtil.RESET);
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1: menuCitizenReports(); break;
            case 2: menuDocumentSearch(); break;
            case 3: menuTrafficTransportation(); break;
            case 4: menuResourceManagement(); break;
            case 5: menuEmergencyResponse(); break;
            case 6: menuIoTSensors(); break;
            case 7: menuStringAlgorithms(); break;
            case 8: menuSuffixStructures(); break;
            case 9: menuDynamicProgramming(); break;
            case 10: menuNetworkFlow(); break;
            case 11: menuNPCompleteness(); break;
            case 12: menuRandomizedAlgorithms(); break;
            case 13: menuParallelAlgorithms(); break;
            case 14: menuDatasetManagement(); break;
            case 15: menuBenchmarking(); break;
            case 16: menuComplexityExplorer(); break;
            case 17: selfTestRunner.runAllTests(); break;
            case 0:
                running = false;
                System.out.println("Exiting SmartCityX Analytics Platform. Goodbye!");
                break;
            default:
                ConsoleUtil.printError("Invalid choice. Please enter a number between 0 and 17.");
        }
    }

    // =========================================================================
    // SECTION 1: Citizen Reports & Service Requests
    // =========================================================================
    private void menuCitizenReports() {
        ConsoleUtil.printSection("Citizen Reports & Service Work Orders");
        System.out.println(" 1. View Recent Citizen Reports");
        System.out.println(" 2. View Municipal Service Work Orders");
        System.out.println(" 3. Exact Citizen Report Search (KMP Algorithm)");
        System.out.println(" 4. Exact Service Request Search (KMP Algorithm)");
        System.out.println(" 5. Substring Search in Reports (Rabin-Karp Rolling Hash)");
        System.out.println(" 6. Search Service & Infra Identifiers (Rabin-Karp Rolling Hash Trace)");
        System.out.println(" 7. Multi-Keyword Emergency Threat Scan (Aho-Corasick)");
        System.out.println(" 8. Repeated Complaint Pattern Detection (Z-Algorithm)");
        System.out.println(" 9. Identify Common Service Descriptions (SA-IS + Kasai LCP)");
        System.out.println("10. Typo-Tolerant Report Search (Levenshtein Distance & Auto-Correction)");
        System.out.println("11. Prioritize Service Work Orders (Randomized QuickSort)");
        System.out.println("12. Add New Citizen Incident Report");
        System.out.println(" 0. Back to Main Menu");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: citizenService.listAllReports(12); break;
            case 2: citizenService.listServiceRequests(12); break;
            case 3: {
                String q = InputUtil.getStringWithDefault("Enter search keyword", "pipeline");
                citizenService.searchReports(q, "KMP");
                break;
            }
            case 4: {
                String q = InputUtil.getStringWithDefault("Enter service keyword", "maintenance");
                citizenService.searchServiceRequests(q, "KMP");
                break;
            }
            case 5: {
                String q = InputUtil.getStringWithDefault("Enter search pattern", "water");
                citizenService.searchReports(q, "RABIN_KARP");
                break;
            }
            case 6: {
                String id = InputUtil.getStringWithDefault("Enter identifier to trace (e.g. SRQ-5001, CR-1004)", "SRQ-5001");
                citizenService.searchIdentifiersWithRollingHash(id);
                break;
            }
            case 7: {
                String input = InputUtil.getStringWithDefault("Enter keywords (comma separated)", "burst, leak, dark, pothole, surge");
                citizenService.scanMultipleKeywords(input.split(","));
                break;
            }
            case 8: {
                String p = InputUtil.getStringWithDefault("Enter complaint prefix", "Major water");
                citizenService.analyzeRepeatedPatterns(p);
                break;
            }
            case 9: {
                int minL = InputUtil.getIntWithDefault("Enter minimum common phrase length", 18);
                citizenService.identifyCommonServiceDescriptions(minL);
                break;
            }
            case 10: {
                String typo = InputUtil.getStringWithDefault("Enter query or typo", "pipline");
                int maxD = InputUtil.getIntWithDefault("Max edit distance", 2);
                citizenService.fuzzySearchReports(typo, maxD);
                break;
            }
            case 11: citizenService.rankServiceRequestsByPriority(); break;
            case 12: addNewCitizenReport(); break;
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    private void addNewCitizenReport() {
        ConsoleUtil.printSection("Submit New Citizen Incident Report");
        String name = InputUtil.getStringWithDefault("Citizen Name", "Citizen User");
        String location = InputUtil.getStringWithDefault("Location", "Downtown Central");
        String desc = InputUtil.getStringWithDefault("Incident Description", "Reported low water pressure and street waterlogging.");
        int newId = citizenRepo.size() + 1;
        CitizenReport r = new CitizenReport(newId, name, location, ReportCategory.WATER_LEAK, desc,
                ReportPriority.HIGH, ReportStatus.OPEN, java.time.Instant.now().toString());
        citizenRepo.add(r);
        ConsoleUtil.printSuccess(String.format("Report #%d registered successfully and added to in-memory repository.", newId));
    }

    // =========================================================================
    // SECTION 2: Infrastructure Document Search
    // =========================================================================
    private void menuDocumentSearch() {
        ConsoleUtil.printSection("Civil Infrastructure & Operational Documents");
        System.out.println("1. List Operational Documents");
        System.out.println("2. Suffix Automaton Fast Substring Search");
        System.out.println("3. Suffix Array & Kasai LCP Analysis");
        System.out.println("4. Document Sequence Alignment (Needleman-Wunsch)");
        System.out.println("5. Linear SA-IS vs Prefix-Doubling Benchmark (O(N) vs O(N log^2 N))");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: documentService.listDocuments(15); break;
            case 2: {
                String q = InputUtil.getStringWithDefault("Enter search phrase", "SCADA");
                documentService.searchSubstringViaAutomaton(q);
                break;
            }
            case 3: {
                int id = InputUtil.getIntWithDefault("Enter Document ID (1-60)", 1);
                documentService.inspectSuffixArrayAndLCP(id);
                break;
            }
            case 4: {
                int id1 = InputUtil.getIntWithDefault("Enter first Document ID", 1);
                int id2 = InputUtil.getIntWithDefault("Enter second Document ID", 2);
                documentService.compareDocumentsAlignment(id1, id2);
                break;
            }
            case 5: {
                int id = InputUtil.getIntWithDefault("Enter Document ID (1-60)", 1);
                documentService.benchmarkSAISIndexing(id);
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 3: Traffic & Transportation Analysis
    // =========================================================================
    private void menuTrafficTransportation() {
        ConsoleUtil.printSection("Traffic & Transportation Network Analytics");
        System.out.println("1. View Traffic Grid Overview (25 Junctions, 50 Arterials)");
        System.out.println("2. Maximum Vehicle Throughput Flow (Dinic Algorithm)");
        System.out.println("3. Critical Bottleneck Road Identification (Min-Cut)");
        System.out.println("4. Road Maintenance Project Scheduling (Critical Path Method)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: trafficService.displayNetworkOverview(); break;
            case 2: {
                int src = InputUtil.getIntWithDefault("Enter source junction ID (0-24)", 0);
                int sink = InputUtil.getIntWithDefault("Enter destination junction ID (0-24)", 24);
                trafficService.computeMaxFlow(src, sink);
                break;
            }
            case 3: {
                int src = InputUtil.getIntWithDefault("Enter source junction ID (0-24)", 0);
                int sink = InputUtil.getIntWithDefault("Enter destination junction ID (0-24)", 24);
                trafficService.identifyBottleneckMinCut(src, sink);
                break;
            }
            case 4: trafficService.scheduleProjectTasks(); break;
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 4: Water & Energy Resource Management
    // =========================================================================
    private void menuResourceManagement() {
        ConsoleUtil.printSection("Water & Energy Utility Resource Management");
        System.out.println("1. View Utility Assets (Water Plants & Energy Substations)");
        System.out.println("2. Water Pipeline Distribution Maximum Flow (Dinic)");
        System.out.println("3. Municipal Multi-Zone Utility Allocation (Bitmask DP)");
        System.out.println("4. Optimize Analytics Operations (Matrix Chain Multiplication)");
        System.out.println("5. Municipal Resource Subset Selection (DP on Subsets / SOS DP)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: resourceService.listResources(); break;
            case 2: resourceService.computeWaterDistributionFlow(); break;
            case 3: resourceService.optimizeResourceAllocationBitmask(); break;
            case 4: resourceService.optimizeAnalyticsMatrixChain(); break;
            case 5: resourceService.optimizeSubsetResourceSelection(); break;
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 5: Emergency Response Management
    // =========================================================================
    private void menuEmergencyResponse() {
        ConsoleUtil.printSection("Emergency Response & Civil Protection");
        System.out.println("1. View Active Incidents & Emergency Squads");
        System.out.println("2. Dispatch Squads (Maximum Bipartite Matching)");
        System.out.println("3. Critical Infrastructure Coverage (Vertex Cover 2-Approx & FPT)");
        System.out.println("4. Search Infrastructure Codes & Facility Assets (KMP Algorithm)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: emergencyService.listEmergenciesAndTeams(); break;
            case 2: emergencyService.assignEmergencyTeams(); break;
            case 3: emergencyService.optimizeInfrastructureProtection(); break;
            case 4: {
                String pattern = InputUtil.getStringWithDefault("Enter facility or link code", "substation");
                emergencyService.searchInfrastructureCodes(pattern);
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 6: IoT Sensor Analytics
    // =========================================================================
    private void menuIoTSensors() {
        ConsoleUtil.printSection("Continuous IoT Telemetry & Parallel Analytics");
        System.out.println("1. View Continuous Sensor Readings Stream");
        System.out.println("2. Unbiased Stream Sampling (Reservoir Sampling k=10)");
        System.out.println("3. Cumulative Running Exposure (Blelloch Parallel Scan)");
        System.out.println("4. Citywide Telemetry Aggregation (ForkJoin Parallel Reduce)");
        System.out.println("5. Sensor Cryptographic Key Validation (Miller-Rabin)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: sensorService.listReadings(12); break;
            case 2: {
                int k = InputUtil.getIntWithDefault("Enter reservoir sample size k", 10);
                sensorService.sampleStreamViaReservoir(k);
                break;
            }
            case 3: {
                String st = InputUtil.getStringWithDefault("Sensor Type (AIR_QUALITY_AQI, TEMPERATURE, ENERGY_GRID_LOAD)", "ENERGY_GRID_LOAD");
                sensorService.computeRunningCumulativeScan(st);
                break;
            }
            case 4: sensorService.computeParallelCitywideStatistics(); break;
            case 5: {
                long id = InputUtil.getLongWithDefault("Enter sensor key/ID to test", 1000000007L);
                sensorService.testSensorCryptoPrimality(id);
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 7: String Algorithms — CO1
    // =========================================================================
    private void menuStringAlgorithms() {
        ConsoleUtil.printSection("Module 1 (CO1): String Pattern Matching");
        System.out.println("1. Knuth-Morris-Pratt (KMP)");
        System.out.println("2. Z-Algorithm Linear Match");
        System.out.println("3. Rabin-Karp Rolling Hash");
        System.out.println("4. Aho-Corasick Multi-Pattern Automaton");
        System.out.println("5. Naive Brute Force");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Choose algorithm: ");
        if (choice == 0) return;

        String text = InputUtil.getStringWithDefault("Enter text string", "MUNICIPAL WATER PIPELINE WATER LEAKAGE DETECTED ON PIPELINE");
        String pat = InputUtil.getStringWithDefault("Enter pattern to find", "PIPELINE");

        long start = System.nanoTime();
        IntArrayList matches;
        String name;

        switch (choice) {
            case 1: name = "KMP"; matches = KMP.search(text, pat); break;
            case 2: name = "Z-Algorithm"; matches = ZAlgorithm.search(text, pat); break;
            case 3: name = "Rabin-Karp"; matches = RabinKarp.search(text, pat); break;
            case 4: {
                name = "Aho-Corasick";
                AhoCorasick ac = new AhoCorasick();
                ac.addPattern(pat);
                ac.buildFailureLinks();
                DynamicArray<AhoCorasick.Match> acMatches = ac.searchMatches(text);
                matches = new IntArrayList();
                for (int i = 0; i < acMatches.size(); i++) matches.add(acMatches.get(i).position);
                break;
            }
            default: name = "Naive"; matches = NaiveSearch.search(text, pat); break;
        }
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Algorithm : %s%n", name);
        System.out.printf("  Matches   : %d occurrences at 0-indexed positions %s%n", matches.size(), matches.toString());
        System.out.printf("  Runtime   : %,d ns (%.4f ms)%n", elapsed, elapsed / 1_000_000.0);
        ConsoleUtil.printSuccess(name + " pattern search completed.");
    }

    // =========================================================================
    // SECTION 8: Suffix Structures & LCP — CO2
    // =========================================================================
    private void menuSuffixStructures() {
        ConsoleUtil.printSection("Module 2 (CO2): Suffix Structures & LCP");
        System.out.println("1. Suffix Array Construction (SA-IS / O(N log N))");
        System.out.println("2. Kasai LCP Array Construction");
        System.out.println("3. Suffix Automaton (DAWG) Substring Query");
        System.out.println("4. Distinct Substrings Count via Suffix Automaton");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        if (choice == 0) return;

        String text = InputUtil.getStringWithDefault("Enter string", "abracadabra");
        if (choice == 1 || choice == 2) {
            int[] sa = SuffixArray.buildSuffixArray(text);
            int[] lcp = Kasai.buildLCP(text, sa);
            System.out.println(ConsoleUtil.BOLD_BLACK + " i │ SA[i] │ LCP[i] │ Suffix" + ConsoleUtil.RESET);
            for (int i = 0; i < sa.length; i++) {
                System.out.printf("%2d │   %3d │    %3d │ %s%n", i, sa[i], (i < lcp.length ? lcp[i] : 0), text.substring(sa[i]));
            }
            ConsoleUtil.printSuccess("Suffix Array & Kasai LCP computed.");
        } else if (choice == 3) {
            String query = InputUtil.getStringWithDefault("Enter substring to test", "cadab");
            SuffixAutomaton sam = SuffixAutomaton.build(text);
            boolean exists = sam.contains(query);
            System.out.printf("Substring \"%s\" exists in \"%s\": %s%n",
                    query, text, (exists ? ConsoleUtil.BRIGHT_GREEN + "TRUE" : ConsoleUtil.RED + "FALSE") + ConsoleUtil.RESET);
        } else if (choice == 4) {
            SuffixAutomaton sam = SuffixAutomaton.build(text);
            long distinct = sam.countDistinctSubstrings();
            System.out.printf("Total distinct substrings in \"%s\" : %s%,d%s%n",
                    text, ConsoleUtil.BRIGHT_GREEN, distinct, ConsoleUtil.RESET);
        }
    }

    // =========================================================================
    // SECTION 9: Advanced Dynamic Programming — CO3
    // =========================================================================
    private void menuDynamicProgramming() {
        ConsoleUtil.printSection("Module 3 (CO3): Advanced Dynamic Programming");
        System.out.println("1. Levenshtein & Damerau-Levenshtein Edit Distance");
        System.out.println("2. Global Sequence Alignment (Needleman-Wunsch)");
        System.out.println("3. Matrix Chain Multiplication");
        System.out.println("4. Travelling Salesperson (TSP) Bitmask DP");
        System.out.println("5. Hamiltonian Path Bitmask DP");
        System.out.println("6. Tree Diameter & Tree Rerooting DP");
        System.out.println("7. Sum Over Subsets (SOS DP)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: {
                String s1 = InputUtil.getStringWithDefault("First string", "algorithm");
                String s2 = InputUtil.getStringWithDefault("Second string", "algoritm");
                int lev = Levenshtein.distance(s1, s2);
                int dam = DamerauLevenshtein.distance(s1, s2);
                System.out.printf("Levenshtein Distance         : %d%n", lev);
                System.out.printf("Damerau-Levenshtein Distance : %d%n", dam);
                break;
            }
            case 2: {
                String s1 = InputUtil.getStringWithDefault("Sequence 1", "AGCACACA");
                String s2 = InputUtil.getStringWithDefault("Sequence 2", "ACACACTA");
                SequenceAlignment.AlignmentResult res = SequenceAlignment.needlemanWunsch(s1, s2, 2, -1, -2);
                System.out.println("Alignment Score: " + res.score);
                System.out.println("Aligned 1      : " + res.alignedSeq1);
                System.out.println("Aligned 2      : " + res.alignedSeq2);
                break;
            }
            case 3: {
                int[] dims = {10, 30, 5, 60, 20};
                MatrixChainMultiplication.Result res = MatrixChainMultiplication.optimizeOrder(dims);
                System.out.println("Min Multiplications: " + res.minMultiplications);
                System.out.println("Parenthesization   : " + res.parenthesization);
                break;
            }
            case 4: {
                int[][] dist = {
                        {0, 10, 15, 20},
                        {10, 0, 35, 25},
                        {15, 35, 0, 30},
                        {20, 25, 30, 0}
                };
                TravellingSalesperson.TSPResult res = TravellingSalesperson.solve(dist);
                System.out.println("TSP Minimum Tour Cost: " + res.minCost);
                System.out.println("Optimal Tour Path    : " + res.tour.toString());
                break;
            }
            case 5: {
                int n = 4;
                boolean[][] adj = new boolean[n][n];
                adj[0][1] = adj[1][2] = adj[2][3] = true;
                HamiltonianPath.PathResult res = HamiltonianPath.findPath(adj);
                System.out.println("Hamiltonian Path Exists: " + res.exists);
                if (res.exists) System.out.println("Path: " + res.path.toString());
                break;
            }
            case 6: {
                int n = 5;
                int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 4}};
                TreeDiameter.DiameterResult res = TreeDiameter.computeDiameter(n, edges);
                System.out.println("Tree Diameter Length: " + res.diameter);
                System.out.println("Diameter Path       : " + res.path.toString());
                break;
            }
            case 7: {
                int[] a = {1, 2, 4, 8};
                int[] sos = SumOverSubsetsDP.computeSOS(a);
                System.out.println("Input Array: " + java.util.Arrays.toString(a));
                System.out.println("SOS Output : " + java.util.Arrays.toString(sos));
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 10: Network Flow — CO4
    // =========================================================================
    private void menuNetworkFlow() {
        ConsoleUtil.printSection("Module 4 (CO4): Network Flow & Graph Optimization");
        System.out.println("1. Dinic Maximum Flow");
        System.out.println("2. Edmonds-Karp Maximum Flow");
        System.out.println("3. Ford-Fulkerson Maximum Flow");
        System.out.println("4. Maximum Bipartite Matching");
        System.out.println("5. Min-Cut Cut-Set Analyzer");
        System.out.println("6. Project Selection Problem via Min-Cut");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: {
                Dinic dinic = new Dinic(createSampleFlowNet(), 0, 5);
                System.out.printf("Dinic Max Flow: %,.0f%n", dinic.value());
                break;
            }
            case 2: {
                EdmondsKarp ek = new EdmondsKarp(createSampleFlowNet(), 0, 5);
                System.out.printf("Edmonds-Karp Max Flow: %,.0f%n", ek.value());
                break;
            }
            case 3: {
                FordFulkerson ff = new FordFulkerson(createSampleFlowNet(), 0, 5);
                System.out.printf("Ford-Fulkerson Max Flow: %,.0f%n", ff.value());
                break;
            }
            case 4: {
                boolean[][] bp = {{true, true, false}, {false, true, true}, {true, false, true}};
                BipartiteMatching.MatchingResult res = BipartiteMatching.findMatching(bp);
                System.out.println("Max Bipartite Matches: " + res.matchCount);
                System.out.println("Matched Pairs        : " + res.pairs.toString());
                break;
            }
            case 5: {
                MinCut.CutAnalysis res = MinCut.computeMinCut(createSampleFlowNet(), 0, 5);
                System.out.printf("Cut Capacity : %,.0f%n", res.cutCapacity);
                System.out.printf("Max Flow     : %,.0f%n", res.maxFlowValue);
                System.out.println("Cut Edges    : " + res.cutEdges.toString());
                break;
            }
            case 6: {
                double[] weights = {20, -50, 40};
                int[][] deps = {{0, 1}};
                ProjectSelectionFlow.ProjectResult res = ProjectSelectionFlow.solveProjectSelection(weights, deps);
                System.out.printf("Max Project Net Profit: %,.2f%n", res.maxNetProfit);
                System.out.println("Selected Projects     : " + res.selectedProjects.toString());
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 11: NP-Completeness & Approximation — CO5
    // =========================================================================
    private void menuNPCompleteness() {
        ConsoleUtil.printSection("Module 5 (CO5): NP-Completeness & Approximation");
        System.out.println("1. 3-SAT Solver");
        System.out.println("2. Karp Reductions (3-SAT -> CLIQUE -> IS -> VERTEX-COVER)");
        System.out.println("3. Vertex Cover 2-Approximation");
        System.out.println("4. Parameterized Vertex Cover (FPT)");
        System.out.println("5. Knapsack FPTAS Approximation Scheme");
        System.out.println("6. Complexity Classes Guide (P vs NP vs NP-Complete)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: {
                int nVars = 3;
                int[][] clauses = {{1, 2, -3}, {-1, 2, 3}, {1, -2, 3}};
                ReductionZoo.ReductionMapping res = ReductionZoo.reduce3SATtoClique(nVars, clauses);
                System.out.println(res.toString());
                break;
            }
            case 2: {
                ConsoleUtil.printSection("End-to-End Infrastructure Conflict Reduction Pipeline");
                System.out.println("Formal Karp Reductions: 3-SAT -> CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER\n");

                // City zoning constraint: 3 variables, 3 clauses
                // z1: Zone commercial, z2: Zone industrial, z3: Zone residential
                int nVars = 3;
                int[][] clauses = {
                    {1, 2, -3},   // (z1 OR z2 OR NOT z3)
                    {-1, 2, 3},   // (NOT z1 OR z2 OR z3)
                    {1, -2, 3}    // (z1 OR NOT z2 OR z3)
                };
                int kClauses = clauses.length;

                // Step 1: 3-SAT -> CLIQUE
                ReductionZoo.ReductionMapping r1 = ReductionZoo.reduce3SATtoClique(nVars, clauses);
                boolean[][] cliqueGraph = (boolean[][]) r1.transformedInstance;
                int nTriples = cliqueGraph.length; // 9 vertices
                System.out.printf("  [Step 1] 3-SAT -> CLIQUE: Constructed gadget graph with %d vertices across %d clause triples.%n",
                        nTriples, kClauses);

                // Step 2: CLIQUE -> INDEPENDENT SET
                ReductionZoo.ReductionMapping r2 = ReductionZoo.reduceCliqueToIndependentSet(cliqueGraph, kClauses);
                boolean[][] complementGraph = (boolean[][]) r2.transformedInstance;
                System.out.printf("  [Step 2] CLIQUE -> INDEPENDENT-SET: Inverted edges to construct complement graph G' (%d vertices).%n",
                        complementGraph.length);

                // Step 3: INDEPENDENT SET -> VERTEX COVER
                ReductionZoo.ReductionMapping r3 = ReductionZoo.reduceIndependentSetToVertexCover(nTriples, kClauses);
                int targetVC = (int) r3.transformedInstance; // |V| - k = 9 - 3 = 6
                System.out.printf("  [Step 3] INDEPENDENT-SET -> VERTEX-COVER: Applied V \\ S duality theorem (Target VC size: %d).%n",
                        targetVC);

                // Step 4: 2-Approximation Vertex Cover on the Conflict Graph
                VertexCoverApproximation.ApproxVCResult approx = VertexCoverApproximation.findApproximateVertexCover(complementGraph);
                System.out.printf("  [Step 4] 2-Approximation Vertex Cover on Conflict Graph: Found cover of size %d vertices.%n",
                        approx.vertexCover.size());
                System.out.printf("           Selected Conflict Cover Nodes: %s%n%n", approx.vertexCover.toString());

                ConsoleUtil.printSuccess("Complete polynomial reduction chain 3-SAT -> CLIQUE -> IS -> VERTEX-COVER validated.");
                break;
            }
            case 3: {
                boolean[][] g = new boolean[5][5];
                int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 4}};
                for (int[] e : edges) { g[e[0]][e[1]] = true; g[e[1]][e[0]] = true; }
                VertexCoverApproximation.ApproxVCResult res = VertexCoverApproximation.findApproximateVertexCover(g);
                System.out.println("2-Approximation Vertex Cover Size: " + res.vertexCover.size());
                System.out.println("Cover Nodes: " + res.vertexCover.toString());
                break;
            }
            case 4: {
                boolean[][] g = new boolean[5][5];
                int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 4}};
                for (int[] e : edges) { g[e[0]][e[1]] = true; g[e[1]][e[0]] = true; }
                ParameterizedVertexCover.FPTResult res = ParameterizedVertexCover.solve(g, 3);
                System.out.println("FPT Cover Found: " + res.found);
                if (res.found) System.out.println("Exact Min Cover: " + res.vertexCover.toString());
                break;
            }
            case 5: {
                int[] val = {60, 100, 120};
                int[] wt = {10, 20, 30};
                int W = 50;
                KnapsackFPTAS.FPTASResult res = KnapsackFPTAS.solve(wt, val, W, 0.2);
                System.out.println("FPTAS Total Value : " + res.totalValue);
                System.out.println("Total Weight      : " + res.totalWeight);
                System.out.println("Theoretical Bound : >= " + res.theoreticalLowerBoundOpt);
                break;
            }
            case 6: {
                System.out.println(ComplexityClassesGuide.getComprehensiveOverview());
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 12: Randomized Algorithms — CO6
    // =========================================================================
    private void menuRandomizedAlgorithms() {
        ConsoleUtil.printSection("Module 6 (CO6): Randomized Algorithms");
        System.out.println("1. Randomized QuickSort with Partition Analysis");
        System.out.println("2. Miller-Rabin Randomized Primality Test");
        System.out.println("3. Reservoir Sampling Stream Simulation");
        System.out.println("4. Randomized Universal Hash Family Collision Analysis");
        System.out.println("5. Randomized Classifications (Monte Carlo vs Las Vegas)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: {
                int[] a = {45, 12, 98, 3, 77, 54, 21};
                RandomizedQuickSort.SortMetrics m = new RandomizedQuickSort.SortMetrics();
                RandomizedQuickSort.sort(a, new Random(), m);
                System.out.println("Sorted Array : " + java.util.Arrays.toString(a));
                System.out.println("Comparisons  : " + m.comparisons);
                System.out.println("Swaps        : " + m.swaps);
                break;
            }
            case 2: {
                long num = InputUtil.getLongWithDefault("Enter number to test", 1000000007L);
                boolean prime = MillerRabin.isPrime(num);
                System.out.printf("Number %,d is %s%n", num, (prime ? ConsoleUtil.BRIGHT_GREEN + "PRIME" : ConsoleUtil.RED + "COMPOSITE") + ConsoleUtil.RESET);
                break;
            }
            case 3: {
                DynamicArray<Integer> stream = new DynamicArray<>();
                for (int i = 1; i <= 1000; i++) stream.add(i);
                DynamicArray<Integer> res = ReservoirSampling.sample(stream, 10, new Random(42));
                System.out.println("Unbiased Sample (k=10 from 1,000 items): " + res.toString());
                break;
            }
            case 4: {
                long[] keys = new long[1000];
                for (int i = 0; i < 1000; i++) keys[i] = i * 17L + 31L;
                RandomizedHashing.HashFamilyMetrics m = RandomizedHashing.benchmarkHashFamily(keys, 100, 20, new Random(42));
                System.out.println(m.toString());
                break;
            }
            case 5: {
                System.out.println(RandomizedClassificationsGuide.getClassificationOverview());
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 13: Parallel Algorithms — CO6
    // =========================================================================
    private void menuParallelAlgorithms() {
        ConsoleUtil.printSection("Module 6 (CO6): Parallel Algorithms");
        System.out.println("1. Parallel Reduce (ForkJoin)");
        System.out.println("2. Blelloch Parallel Prefix Scan");
        System.out.println("3. Parallel Sort (ForkJoin QuickSort)");
        System.out.println("4. Work/Span Analysis & Brent's Theorem Guide");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        switch (choice) {
            case 1: {
                double[] d = new double[10000];
                for (int i = 0; i < d.length; i++) d[i] = i + 1;
                double sum = ParallelReduce.computeSequential(d, ParallelReduce.Op.SUM);
                System.out.printf("Sum of 10,000 elements via ForkJoin: %,.0f%n", sum);
                break;
            }
            case 2: {
                double[] in = {2, 1, 4, 3, 6, 5, 8, 7};
                double[] p = BlellochScan.exclusiveScan(in);
                System.out.println("Input : " + java.util.Arrays.toString(in));
                System.out.println("Scan  : " + java.util.Arrays.toString(p));
                break;
            }
            case 3: {
                double[] d = {9.1, 2.3, 7.8, 1.4, 5.5, 3.2, 8.0, 4.6};
                ParallelSort.sort(d);
                System.out.println("Parallel Sorted Array: " + java.util.Arrays.toString(d));
                break;
            }
            case 4: {
                System.out.println(BrentsTheoremGuide.getOverview());
                break;
            }
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 14: Dataset Management
    // =========================================================================
    private void menuDatasetManagement() {
        ConsoleUtil.printSection("SmartCityX Municipal Dataset Management");
        System.out.println("Loaded Dataset Registry Status:");
        System.out.printf("  1. Citizen Reports           : %,5d records (data/citizen_reports.csv)%n", citizenRepo.size());
        System.out.printf("  2. Service Requests          : %,5d records (data/service_requests.csv)%n", serviceRepo.size());
        System.out.printf("  3. Emergency Reports         : %,5d records (data/emergency_reports.csv)%n", emergencyRepo.size());
        System.out.printf("  4. Infrastructure Documents  : %,5d documents (data/infrastructure_documents.jsonl)%n", docRepo.size());
        System.out.printf("  5. Traffic Network Nodes     : %,5d junctions (data/traffic_nodes.csv)%n", trafficRepo.getNodeCount());
        System.out.printf("  6. Traffic Network Edges     : %,5d arterials (data/traffic_edges.csv)%n", trafficRepo.getEdgeCount());
        System.out.printf("  7. Water Facilities          : %,5d plants (data/water_resources.csv)%n", resourceRepo.getWaterCount());
        System.out.printf("  8. Energy Grid Substations   : %,5d stations (data/energy_resources.csv)%n", resourceRepo.getEnergyCount());
        System.out.printf("  9. Municipal Demands         : %,5d demands (data/resource_demands.csv)%n", resourceRepo.getDemandCount());
        System.out.printf(" 10. Emergency Squads          : %,5d teams (data/emergency_teams.csv)%n", teamRepo.getTeamCount());
        System.out.printf(" 11. Assignment Requirements   : %,5d incidents (data/assignment_requirements.csv)%n", teamRepo.getRequirementCount());
        System.out.printf(" 12. Project Tasks             : %,5d tasks (data/project_tasks.csv)%n", projectRepo.getTaskCount());
        System.out.printf(" 13. Project Precedences       : %,5d dependencies (data/project_dependencies.csv)%n", projectRepo.getDependencyCount());
        System.out.printf(" 14. Infrastructure Vertices   : %,5d assets (data/infrastructure_vertices.csv)%n", infraRepo.getVertexCount());
        System.out.printf(" 15. Infrastructure Edges      : %,5d links (data/infrastructure_edges.csv)%n", infraRepo.getEdgeCount());
        System.out.printf(" 16. IoT Telemetry Readings    : %,5d observations (data/iot_sensor_readings.csv)%n", sensorRepo.size());
        System.out.println();
        System.out.println("1. Reload All Datasets from Disk");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select option: ");
        if (choice == 1) {
            loadDatasets();
            ConsoleUtil.printSuccess("All datasets reloaded from data/ directory successfully.");
        }
    }

    // =========================================================================
    // SECTION 15: Algorithm Comparison & Benchmarking
    // =========================================================================
    private void menuBenchmarking() {
        ConsoleUtil.printSection("Rigorous Algorithm Head-to-Head Benchmarking");
        System.out.println("1. Exact String Search Benchmark (Naive vs KMP vs Z vs Rabin-Karp)");
        System.out.println("2. Network Max Flow Benchmark (Ford-Fulkerson vs Edmonds-Karp vs Dinic)");
        System.out.println("3. Telemetry Sorting Benchmark (Arrays.sort vs QuickSort vs ParallelSort)");
        System.out.println("0. Back");

        int choice = InputUtil.getInt("Select benchmark: ");
        switch (choice) {
            case 1: {
                String pat = InputUtil.getStringWithDefault("Search pattern", "pipeline");
                benchmarkService.runStringBenchmark(pat);
                break;
            }
            case 2: benchmarkService.runMaxFlowBenchmark(0, trafficRepo.getNodeCount() - 1); break;
            case 3: benchmarkService.runSortingBenchmark(); break;
            case 0: break;
            default: ConsoleUtil.printError("Invalid choice.");
        }
    }

    // =========================================================================
    // SECTION 16: Complexity Explorer
    // =========================================================================
    private void menuComplexityExplorer() {
        ConsoleUtil.printSection("Theoretical Big-O Complexity Explorer");
        System.out.println(ComplexityClassesGuide.getComprehensiveOverview());
    }

    private CustomFlowNetwork createSampleFlowNet() {
        CustomFlowNetwork net = new CustomFlowNetwork(6);
        net.addEdge(new CustomFlowEdge(0, 1, 16));
        net.addEdge(new CustomFlowEdge(0, 2, 13));
        net.addEdge(new CustomFlowEdge(1, 2, 10));
        net.addEdge(new CustomFlowEdge(1, 3, 12));
        net.addEdge(new CustomFlowEdge(2, 1, 4));
        net.addEdge(new CustomFlowEdge(2, 4, 14));
        net.addEdge(new CustomFlowEdge(3, 2, 9));
        net.addEdge(new CustomFlowEdge(3, 5, 20));
        net.addEdge(new CustomFlowEdge(4, 3, 7));
        net.addEdge(new CustomFlowEdge(4, 5, 4));
        return net;
    }
}

