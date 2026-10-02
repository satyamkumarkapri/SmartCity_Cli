package smartcityx;

import smartcityx.repository.*;
import smartcityx.services.*;
import smartcityx.util.*;
import smartcityx.exception.*;

public class Main {
    private static ReportRepository reportRepo = new ReportRepository();
    private static DocumentRepository docRepo = new DocumentRepository();
    private static ResourceRepository resourceRepo = new ResourceRepository();
    private static SensorRepository sensorRepo = new SensorRepository();

    private static ReportService reportService = new ReportService(reportRepo);
    private static DocumentService documentService = new DocumentService(docRepo);
    private static ResourceService resourceService = new ResourceService(resourceRepo);
    private static NetworkService networkService = new NetworkService();
    private static ConstraintService constraintService = new ConstraintService();
    private static IoTService iotService = new IoTService(sensorRepo);
    private static UniversalSearchService universalSearchService = new UniversalSearchService(reportRepo, docRepo, resourceRepo);
    private static DashboardService dashboardService = new DashboardService(reportRepo, docRepo, resourceRepo, sensorRepo);

    public static void main(String[] args) {
        ConsoleUtil.printHeader("Welcome to SmartCityX!");
        if (InputUtil.getYesNo("Load sample data?")) {
            DataGenerator.loadSampleData(reportRepo, docRepo, resourceRepo, sensorRepo);
            ConsoleUtil.printSuccess("Sample data loaded successfully.");
        }

        InputUtil.getString("Press Enter to continue...");
        dashboardService.showDashboard();

        boolean running = true;
        while (running) {
            ConsoleUtil.printHeader("SMARTCITYX MAIN MENU");
            System.out.println("1. Citizen & Service Reports");
            System.out.println("2. Document Search & Similarity");
            System.out.println("3. City Resource Optimization");
            System.out.println("4. Network Flow & Resource Allocation");
            System.out.println("5. NP-Completeness & Approximation");
            System.out.println("6. IoT & Parallel Data Analytics");
            System.out.println("7. Universal Search (All Data)");
            System.out.println("8. View City Dashboard");
            System.out.println("9. Load Sample Data");
            System.out.println("10. Exit");

            try {
                int choice = InputUtil.getInt("Enter your choice: ");
                switch (choice) {
                    case 1: reportMenu(); break;
                    case 2: documentMenu(); break;
                    case 3: resourceMenu(); break;
                    case 4: networkMenu(); break;
                    case 5: constraintMenu(); break;
                    case 6: iotMenu(); break;
                    case 7:
                        String keyword = InputUtil.getString("Enter keyword for universal search: ");
                        universalSearchService.search(keyword);
                        break;
                    case 8: dashboardService.showDashboard(); break;
                    case 9: 
                        DataGenerator.loadSampleData(reportRepo, docRepo, resourceRepo, sensorRepo);
                        ConsoleUtil.printSuccess("Sample data loaded.");
                        break;
                    case 10:
                        running = false;
                        System.out.println("Exiting SmartCityX. Goodbye!");
                        break;
                    default:
                        ConsoleUtil.printError("Invalid choice. Please select 1-10.");
                }
            } catch (Exception e) {
                ConsoleUtil.printError("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void reportMenu() {
        ConsoleUtil.printSection("Citizen & Service Reports");
        System.out.println("1. View All Citizen Reports");
        System.out.println("2. View Emergency Reports");
        System.out.println("3. Sort Service Requests by Priority (Randomized QuickSort)");
        System.out.println("4. Search Reports (KMP)");
        System.out.println("5. Search Reports (Z-Algorithm)");
        System.out.println("6. Search Reports (Rabin-Karp)");
        System.out.println("7. Search Multiple Keywords (Aho-Corasick)");
        System.out.println("8. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) reportService.viewAllReports();
        else if (choice == 2) reportService.viewEmergencyReports();
        else if (choice == 3) reportService.sortRequestsByPriority();
        else if (choice >= 4 && choice <= 6) {
            String kw = InputUtil.getString("Enter keyword to search: ");
            reportService.searchReports(kw, choice - 3);
        } else if (choice == 7) {
            String keywords = InputUtil.getString("Enter keywords separated by comma: ");
            reportService.searchMultipleKeywords(keywords.split(","));
        }
    }

    private static void documentMenu() {
        ConsoleUtil.printSection("Document Search & Similarity");
        System.out.println("1. List Documents");
        System.out.println("2. Document Similarity (Levenshtein)");
        System.out.println("3. Build Suffix Array & LCP for Document");
        System.out.println("4. Suffix Automaton Substring Search");
        System.out.println("5. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) documentService.listDocuments();
        else if (choice == 2) {
            int d1 = InputUtil.getInt("Enter first document ID: ");
            int d2 = InputUtil.getInt("Enter second document ID: ");
            documentService.compareDocuments(d1, d2);
        } else if (choice == 3) {
            int dId = InputUtil.getInt("Enter document ID: ");
            documentService.buildSuffixArrayAndLCP(dId);
        } else if (choice == 4) {
            int dId = InputUtil.getInt("Enter document ID: ");
            String pattern = InputUtil.getString("Enter pattern to search: ");
            documentService.searchSubstringSuffixAutomaton(dId, pattern);
        }
    }

    private static void resourceMenu() {
        ConsoleUtil.printSection("City Resource Optimization");
        System.out.println("1. View Resources");
        System.out.println("2. Optimize Resource Selection (Bitmask DP)");
        System.out.println("3. Exact Budget Matching (Subset DP)");
        System.out.println("4. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) resourceService.viewResources();
        else if (choice == 2) {
            double budget = InputUtil.getDouble("Enter available budget: ");
            resourceService.optimizeResourceSelection(budget);
        } else if (choice == 3) {
            int budget = InputUtil.getInt("Enter exact budget to match: ");
            resourceService.subsetSumOptimization(budget);
        }
    }

    private static void networkMenu() {
        ConsoleUtil.printSection("Network Flow & Resource Allocation");
        System.out.println("1. Run Network Flow (Water Distribution)");
        System.out.println("2. Run Bipartite Matching (Emergency Allocation)");
        System.out.println("3. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) networkService.runNetworkFlow();
        else if (choice == 2) networkService.runBipartiteMatching();
    }

    private static void constraintMenu() {
        ConsoleUtil.printSection("NP-Completeness & Approximation");
        System.out.println("1. Solve sample 3-SAT problem");
        System.out.println("2. Graph Constraint Problems (Clique, IS, Vertex Cover)");
        System.out.println("3. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) constraintService.solveSATProblem();
        else if (choice == 2) constraintService.solveGraphProblems();
    }

    private static void iotMenu() {
        ConsoleUtil.printSection("IoT & Parallel Data Analytics");
        System.out.println("1. Analyze IoT Data Stream");
        System.out.println("2. Check Sensor ID Primality (Miller-Rabin)");
        System.out.println("3. Back");

        int choice = InputUtil.getInt("Enter choice: ");
        if (choice == 1) iotService.analyzeIoTData();
        else if (choice == 2) {
            long id = InputUtil.getLong("Enter Sensor ID number: ");
            iotService.checkPrimeSensorId(id);
        }
    }
}
