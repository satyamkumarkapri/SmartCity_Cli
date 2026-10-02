package smartcityx.services;

import smartcityx.algorithms.strings.*;
import smartcityx.algorithms.parallel.RandomizedQuickSort;
import smartcityx.model.CitizenReport;
import smartcityx.model.EmergencyReport;
import smartcityx.model.ServiceRequest;
import smartcityx.repository.ReportRepository;
import smartcityx.util.ComplexityUtil;
import smartcityx.util.ConsoleUtil;
import java.util.List;
import java.util.Map;

public class ReportService {
    private ReportRepository repo;

    public ReportService(ReportRepository repo) {
        this.repo = repo;
    }

    public void addCitizenReport(CitizenReport report) {
        repo.addCitizenReport(report);
        ConsoleUtil.printSuccess("Citizen report added successfully!");
    }

    public void viewAllReports() {
        List<CitizenReport> reports = repo.getAllCitizenReports();
        if (reports.isEmpty()) {
            System.out.println("No citizen reports available.");
            return;
        }
        for (CitizenReport r : reports) {
            System.out.println(r);
        }
    }

    public void viewEmergencyReports() {
        List<EmergencyReport> reports = repo.getAllEmergencyReports();
        if (reports.isEmpty()) {
            System.out.println("No emergency reports available.");
            return;
        }
        for (EmergencyReport r : reports) {
            System.out.println(r);
        }
    }

    public void sortRequestsByPriority() {
        List<ServiceRequest> reqs = repo.getAllServiceRequests();
        if (reqs.isEmpty()) {
            System.out.println("No service requests to sort.");
            return;
        }
        
        ComplexityUtil.display("Randomized QuickSort", "O(N log N)", "O(N log N)", "O(N^2)", "O(log N)");

        long start = System.nanoTime();
        RandomizedQuickSort.sortRequests(reqs, 0, reqs.size() - 1);
        long end = System.nanoTime();

        ConsoleUtil.printSuccess("Sorted using Randomized QuickSort in " + (end - start) + " ns");
        for (ServiceRequest r : reqs) {
            System.out.println(r);
        }
    }

    public void searchReports(String keyword, int algoChoice) {
        List<CitizenReport> reports = repo.getAllCitizenReports();
        boolean found = false;
        long totalTime = 0;

        if (algoChoice == 1) {
            ComplexityUtil.display("KMP (Knuth-Morris-Pratt)", "O(N)", "O(N)", "O(N)", "O(M)");
        } else if (algoChoice == 2) {
            ComplexityUtil.display("Z-Algorithm", "O(N + M)", "O(N + M)", "O(N + M)", "O(N + M)");
        } else if (algoChoice == 3) {
            ComplexityUtil.display("Rabin-Karp", "O(N + M)", "O(N + M)", "O(N * M)", "O(1)");
        }

        for (CitizenReport r : reports) {
            String text = r.getDescription().toLowerCase();
            String pattern = keyword.toLowerCase();
            long start = 0, end = 0;
            List<Integer> matches = null;

            if (algoChoice == 1) {
                start = System.nanoTime();
                matches = KMP.search(text, pattern);
                end = System.nanoTime();
            } else if (algoChoice == 2) {
                start = System.nanoTime();
                matches = ZAlgorithm.search(text, pattern);
                end = System.nanoTime();
            } else if (algoChoice == 3) {
                start = System.nanoTime();
                matches = RabinKarp.search(text, pattern);
                end = System.nanoTime();
            }

            if (matches != null && !matches.isEmpty()) {
                System.out.println("Found match in Report #" + r.getId() + " at index: " + matches);
                System.out.println("Report: " + r);
                found = true;
                totalTime += (end - start);
            }
        }
        if (!found) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Total Search Time: " + totalTime + " ns");
        }
    }

    public void searchMultipleKeywords(String[] keywords) {
        ComplexityUtil.display("Aho-Corasick", "O(N + M + Z)", "O(N + M + Z)", "O(N + M + Z)", "O(M * Alphabet)");
        
        AhoCorasick ac = new AhoCorasick();
        for (String kw : keywords) {
            ac.insert(kw.toLowerCase());
        }
        ac.buildFailureLinks();

        List<CitizenReport> reports = repo.getAllCitizenReports();
        long start = System.nanoTime();
        boolean found = false;

        for (CitizenReport r : reports) {
            Map<String, List<Integer>> results = ac.search(r.getDescription().toLowerCase());
            if (!results.isEmpty()) {
                System.out.println("Found matches in Report #" + r.getId() + " for keywords: " + results.keySet());
                System.out.println("Report: " + r);
                found = true;
            }
        }
        long end = System.nanoTime();
        if (!found) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Total Execution Time: " + (end - start) + " ns");
        }
    }
}
