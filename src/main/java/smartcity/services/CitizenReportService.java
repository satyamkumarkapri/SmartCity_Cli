package smartcity.services;

import smartcity.algorithms.dynamicprogramming.DamerauLevenshtein;
import smartcity.algorithms.dynamicprogramming.Levenshtein;
import smartcity.algorithms.randomized.RandomizedQuickSort;
import smartcity.algorithms.strings.AhoCorasick;
import smartcity.algorithms.strings.KMP;
import smartcity.algorithms.strings.RabinKarp;
import smartcity.algorithms.strings.ZAlgorithm;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.model.CitizenReport;
import smartcity.model.ServiceRequest;
import smartcity.repository.CitizenReportRepository;
import smartcity.repository.ServiceRequestRepository;
import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Citizen Reports and Service Requests using M1 (Strings),
 * M3 (Dynamic Programming), and M6 (Randomized QuickSort).
 */
public class CitizenReportService {
    private final CitizenReportRepository citizenRepo;
    private final ServiceRequestRepository serviceRepo;

    public CitizenReportService(CitizenReportRepository citizenRepo, ServiceRequestRepository serviceRepo) {
        this.citizenRepo = citizenRepo;
        this.serviceRepo = serviceRepo;
    }

    public void listAllReports(int limit) {
        ConsoleUtil.printSection("Recent Citizen Reports (" + citizenRepo.size() + " Total)");
        int n = Math.min(limit, citizenRepo.size());
        for (int i = 0; i < n; i++) {
            System.out.println("  " + citizenRepo.get(i).toString());
        }
        if (citizenRepo.size() > n) {
            System.out.println("  ... and " + (citizenRepo.size() - n) + " more records in database.");
        }
    }

    public void listServiceRequests(int limit) {
        ConsoleUtil.printSection("Municipal Service Work Orders (" + serviceRepo.size() + " Total)");
        int n = Math.min(limit, serviceRepo.size());
        for (int i = 0; i < n; i++) {
            System.out.println("  " + serviceRepo.get(i).toString());
        }
        if (serviceRepo.size() > n) {
            System.out.println("  ... and " + (serviceRepo.size() - n) + " more work orders.");
        }
    }

    /**
     * Search citizen reports using KMP or Rabin-Karp exact pattern matching.
     */
    public void searchReports(String query, String algo) {
        ConsoleUtil.printSection("Search Citizen Reports [" + algo + "] Query: \"" + query + "\"");
        if (query == null || query.trim().isEmpty()) {
            ConsoleUtil.printWarning("Query cannot be empty.");
            return;
        }

        long start = System.nanoTime();
        int matchCount = 0;
        int docCount = citizenRepo.size();

        for (int i = 0; i < docCount; i++) {
            CitizenReport r = citizenRepo.get(i);
            String text = r.getDescription() + " " + r.getLocation() + " " + r.getCitizenName();

            smartcity.structures.IntArrayList matches;
            if ("RABIN_KARP".equalsIgnoreCase(algo)) {
                matches = RabinKarp.search(text, query);
            } else {
                matches = KMP.search(text, query);
            }

            if (!matches.isEmpty()) {
                matchCount += matches.size();
                System.out.printf("  %s%s%s (Matched %d time(s) at offsets: %s)%n",
                        ConsoleUtil.BRIGHT_GREEN, r.toString(), ConsoleUtil.RESET,
                        matches.size(), matches.toString());
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.println();
        ConsoleUtil.printSuccess(String.format("Found %,d occurrences across %,d reports in %.3f ms (%s)",
                matchCount, docCount, elapsed / 1_000_000.0, algo));
    }

    /**
     * Searches municipal service requests using KMP pattern matching.
     * Implements Image 2 requirement: "KMP to search citizen reports, service requests, and infrastructure codes."
     */
    public void searchServiceRequests(String query, String algo) {
        ConsoleUtil.printSection("Search Service Requests [" + algo + "] Query: \"" + query + "\"");
        if (query == null || query.trim().isEmpty()) {
            ConsoleUtil.printWarning("Query cannot be empty.");
            return;
        }

        long start = System.nanoTime();
        int matchCount = 0;
        int reqCount = serviceRepo.size();

        for (int i = 0; i < reqCount; i++) {
            ServiceRequest req = serviceRepo.get(i);
            String text = req.getRequestDetails() + " " + req.getDepartment() + " " + req.getLocation();

            smartcity.structures.IntArrayList matches;
            if ("RABIN_KARP".equalsIgnoreCase(algo)) {
                matches = RabinKarp.search(text, query);
            } else {
                matches = KMP.search(text, query);
            }

            if (!matches.isEmpty()) {
                matchCount += matches.size();
                System.out.printf("  %s%s%s (Matched %d time(s) at offsets: %s)%n",
                        ConsoleUtil.BRIGHT_GREEN, req.toString(), ConsoleUtil.RESET,
                        matches.size(), matches.toString());
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.println();
        ConsoleUtil.printSuccess(String.format("Found %,d occurrences across %,d service work orders in %.3f ms (%s)",
                matchCount, reqCount, elapsed / 1_000_000.0, algo));
    }

    /**
     * Searches for service and infrastructure identifiers using Rabin-Karp rolling hash with computational trace.
     * Implements Image 2 requirement: "Rabin-Karp to search service and infrastructure identifiers."
     */
    public void searchIdentifiersWithRollingHash(String identifier) {
        ConsoleUtil.printSection("Rabin-Karp Rolling Hash Identifier Search: \"" + identifier + "\"");
        if (identifier == null || identifier.trim().isEmpty()) {
            ConsoleUtil.printWarning("Identifier cannot be empty.");
            return;
        }

        String target = identifier.trim();
        long base = 257;
        long mod = 1_000_000_007L;

        // Compute hash of pattern
        long targetHash = 0;
        for (int i = 0; i < target.length(); i++) {
            targetHash = (targetHash * base + target.charAt(i)) % mod;
        }

        System.out.printf("Rolling Hash Parameters: Base = %d | Modulo = %,d%n", base, mod);
        System.out.printf("Target Identifier Hash  : %s%d%s (length %d)%n%n",
                ConsoleUtil.BRIGHT_YELLOW, targetHash, ConsoleUtil.RESET, target.length());

        long start = System.nanoTime();
        int foundInReports = 0;
        int foundInRequests = 0;

        // 1. Search in Citizen Reports
        for (int i = 0; i < citizenRepo.size(); i++) {
            CitizenReport r = citizenRepo.get(i);
            String text = r.getDescription();
            smartcity.structures.IntArrayList matches = RabinKarp.search(text, target);
            if (!matches.isEmpty()) {
                foundInReports++;
                System.out.printf("  ✔ [Citizen Report #%03d] Matched at offset %s | %s%n",
                        r.getId(), matches.toString(), r.getDescription());
            }
        }

        // 2. Search in Service Requests
        for (int i = 0; i < serviceRepo.size(); i++) {
            ServiceRequest req = serviceRepo.get(i);
            String text = req.getRequestDetails();
            smartcity.structures.IntArrayList matches = RabinKarp.search(text, target);
            if (!matches.isEmpty()) {
                foundInRequests++;
                System.out.printf("  ✔ [Work Order #%03d] Matched at offset %s | %s%n",
                        req.getId(), matches.toString(), req.getRequestDetails());
            }
        }

        long elapsed = System.nanoTime() - start;
        System.out.println();
        ConsoleUtil.printSuccess(String.format("Rabin-Karp found %d report(s) and %d service request(s) matching \"%s\" in %.3f ms.",
                foundInReports, foundInRequests, target, elapsed / 1_000_000.0));
    }

    /**
     * Identifies common repeating service descriptions across municipal work orders using Suffix Array and Kasai LCP.
     * Implements Image 2 requirement: "LCP/Kasai to identify common service descriptions."
     */
    public void identifyCommonServiceDescriptions(int minLength) {
        ConsoleUtil.printSection("LCP/Kasai Common Service Description Identification (Min Length: " + minLength + ")");
        int count = serviceRepo.size();
        if (count == 0) {
            ConsoleUtil.printWarning("No service requests loaded.");
            return;
        }

        // Concatenate descriptions with a sentinel character '#'
        StringBuilder sb = new StringBuilder();
        int[] docIndices = new int[count];
        for (int i = 0; i < count; i++) {
            docIndices[i] = sb.length();
            sb.append(serviceRepo.get(i).getRequestDetails().trim()).append(" # ");
        }
        String combined = sb.toString();

        long start = System.nanoTime();
        int[] sa = SuffixArray.buildSAIS(combined);
        int[] lcp = Kasai.buildLCP(combined, sa);
        long elapsed = System.nanoTime() - start;

        // Find longest common substrings across DIFFERENT service requests
        DynamicArray<String> commonPhrases = new DynamicArray<>();
        smartcity.structures.CustomHashSet<String> seen = new smartcity.structures.CustomHashSet<>();

        for (int i = 1; i < lcp.length; i++) {
            if (lcp[i] >= minLength) {
                int pos1 = sa[i - 1];
                int pos2 = sa[i];

                // Ensure they come from different service requests
                int doc1 = findDocIndex(pos1, docIndices);
                int doc2 = findDocIndex(pos2, docIndices);

                if (doc1 != doc2) {
                    int len = Math.min(lcp[i], Math.min(combined.length() - pos1, combined.length() - pos2));
                    if (len <= 0) continue;
                    String phrase = combined.substring(pos1, pos1 + len).trim();
                    int hashIdx = phrase.indexOf('#');
                    if (hashIdx >= 0) phrase = phrase.substring(0, hashIdx).trim();

                    if (phrase.length() >= minLength && !seen.contains(phrase)) {
                        seen.add(phrase);
                        commonPhrases.add(phrase);
                        if (commonPhrases.size() >= 10) break;
                    }
                }
            }
        }

        System.out.printf("Analyzed %,d characters across %,d service work orders in %.3f ms (SA-IS + Kasai LCP)%n%n",
                combined.length(), count, elapsed / 1_000_000.0);

        if (commonPhrases.isEmpty()) {
            System.out.println("No recurring phrase found with minimum length " + minLength + " characters.");
        } else {
            System.out.println(ConsoleUtil.BOLD_BLACK + " Rank │ LCP │ Common Recurring Service Description Phrase" + ConsoleUtil.RESET);
            System.out.println("──────┼─────┼─────────────────────────────────────────────────────────────");
            for (int i = 0; i < commonPhrases.size(); i++) {
                String p = commonPhrases.get(i);
                System.out.printf("  %2d  │ %3d │ %s\"%s\"%s%n",
                        i + 1, p.length(), ConsoleUtil.BRIGHT_CYAN, p, ConsoleUtil.RESET);
            }
            System.out.println("──────┴─────┴─────────────────────────────────────────────────────────────");
            ConsoleUtil.printSuccess(String.format("Identified %d common municipal service patterns via Kasai LCP.", commonPhrases.size()));
        }
    }

    private int findDocIndex(int pos, int[] docIndices) {
        int low = 0, high = docIndices.length - 1;
        int ans = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (docIndices[mid] <= pos) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    /**
     * Scans all citizen reports for multiple emergency/service keywords simultaneously using Aho-Corasick.
     */
    public void scanMultipleKeywords(String[] keywords) {
        ConsoleUtil.printSection("Aho-Corasick Multi-Keyword Emergency Threat Scan");
        long start = System.nanoTime();

        AhoCorasick ac = new AhoCorasick();
        for (String kw : keywords) {
            if (!kw.trim().isEmpty()) ac.addPattern(kw.trim().toLowerCase());
        }
        ac.buildFailureLinks();

        int totalFound = 0;
        int reportsScanned = citizenRepo.size();

        for (int i = 0; i < reportsScanned; i++) {
            CitizenReport r = citizenRepo.get(i);
            String text = (r.getDescription() + " " + r.getLocation()).toLowerCase();
            DynamicArray<AhoCorasick.Match> matches = ac.searchMatches(text);

            if (!matches.isEmpty()) {
                totalFound += matches.size();
                System.out.printf("  %sReport #%03d%s [%s]: Found %d keyword(s): ",
                        ConsoleUtil.CYAN, r.getId(), ConsoleUtil.RESET, r.getLocation(), matches.size());
                for (int m = 0; m < matches.size(); m++) {
                    AhoCorasick.Match match = matches.get(m);
                    System.out.print(ConsoleUtil.BRIGHT_YELLOW + "\"" + match.pattern + "\"@" + match.position + " " + ConsoleUtil.RESET);
                }
                System.out.println("\n       " + r.getDescription());
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.println();
        ConsoleUtil.printSuccess(String.format("Aho-Corasick scan completed: %,d matches found across %,d reports in %.3f ms.",
                totalFound, reportsScanned, elapsed / 1_000_000.0));
    }

    /**
     * Analyzes repeated prefix patterns and complaint duplicates using Z-Algorithm.
     */
    public void analyzeRepeatedPatterns(String patternPrefix) {
        ConsoleUtil.printSection("Z-Algorithm Repeated Complaint Pattern Detection");
        long start = System.nanoTime();
        int matchedReports = 0;

        for (int i = 0; i < citizenRepo.size(); i++) {
            CitizenReport r = citizenRepo.get(i);
            String combined = patternPrefix + "$" + r.getDescription();
            int[] z = ZAlgorithm.computeZArray(combined);

            int pLen = patternPrefix.length();
            int occurrences = 0;
            for (int k = pLen + 1; k < z.length; k++) {
                if (z[k] >= pLen) {
                    occurrences++;
                }
            }
            if (occurrences > 0) {
                matchedReports++;
                System.out.printf("  Report #%03d (%s): %d repeated pattern occurrence(s)%n",
                        r.getId(), r.getLocation(), occurrences);
            }
        }
        long elapsed = System.nanoTime() - start;
        ConsoleUtil.printSuccess(String.format("Z-Algorithm analysis finished in %.3f ms. Found in %d reports.",
                elapsed / 1_000_000.0, matchedReports));
    }

    /**
     * Typo-tolerant search using Levenshtein / Damerau-Levenshtein edit distance.
     */
    /**
     * Typo-tolerant search using Levenshtein / Damerau-Levenshtein edit distance.
     * Implements CO3 requirement: "Levenshtein/Damerau-Levenshtein to correct citizen queries."
     */
    public void fuzzySearchReports(String query, int maxDistance) {
        ConsoleUtil.printSection("Fuzzy Typo-Tolerant Report Search (Max Edit Distance: " + maxDistance + ")");
        if (query == null || query.trim().isEmpty()) {
            ConsoleUtil.printWarning("Query cannot be empty.");
            return;
        }

        // Automatic Query Correction via Levenshtein / Damerau-Levenshtein
        String suggested = suggestQueryCorrection(query.trim());
        if (suggested != null && !suggested.equalsIgnoreCase(query.trim())) {
            int lev = Levenshtein.compute(query.trim().toLowerCase(), suggested.toLowerCase());
            int dam = DamerauLevenshtein.distance(query.trim().toLowerCase(), suggested.toLowerCase());
            System.out.println(ConsoleUtil.BRIGHT_YELLOW + "  [Query Auto-Correction (M3 DP)] Did you mean: \"" + suggested + "\"?" + ConsoleUtil.RESET);
            System.out.printf("   Levenshtein Dist: %d | Damerau-Levenshtein Dist: %d (OSA transposition-aware)%n%n", lev, dam);
        }

        long start = System.nanoTime();
        int matchesFound = 0;

        for (int i = 0; i < citizenRepo.size(); i++) {
            CitizenReport r = citizenRepo.get(i);
            String[] words = r.getDescription().split("\\s+");
            for (String w : words) {
                String cleanWord = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (cleanWord.isEmpty()) continue;
                int distDam = DamerauLevenshtein.distance(query.toLowerCase(), cleanWord);
                int distLev = Levenshtein.compute(query.toLowerCase(), cleanWord);
                int dist = Math.min(distDam, distLev);
                if (dist <= maxDistance) {
                    matchesFound++;
                    System.out.printf("  Report #%03d: Word \"%s%s%s\" ~ \"%s\" (Edit Dist: %d) | %s%n",
                            r.getId(), ConsoleUtil.BRIGHT_YELLOW, w, ConsoleUtil.RESET, query, dist, r.getDescription());
                    break;
                }
            }
        }
        long elapsed = System.nanoTime() - start;
        ConsoleUtil.printSuccess(String.format("Fuzzy search matched %d reports in %.3f ms.",
                matchesFound, elapsed / 1_000_000.0));
    }

    /**
     * Dictionary of municipal terminology for citizen query auto-correction.
     */
    private String suggestQueryCorrection(String term) {
        String[] dictionary = {
            "pothole", "streetlight", "pipeline", "water", "sewage", "drainage",
            "blackout", "transformer", "traffic", "signal", "hazard", "fire",
            "smoke", "accident", "hydrant", "garbage", "waste", "infrastructure",
            "contamination", "rupture", "debris", "flooding", "congestion"
        };
        String best = null;
        int minDistance = Integer.MAX_VALUE;
        String lower = term.toLowerCase();

        for (String word : dictionary) {
            int dist = DamerauLevenshtein.distance(lower, word);
            if (dist < minDistance && dist <= 2) {
                minDistance = dist;
                best = word;
            }
        }
        return best;
    }

    /**
     * Prioritizes and ranks municipal service work orders using Randomized QuickSort.
     * Implements CO6 requirement: "Randomized QuickSort to rank city-service requests."
     */
    public void rankServiceRequestsByPriority() {
        ConsoleUtil.printSection("Service Request Prioritization (Randomized QuickSort)");
        long start = System.nanoTime();

        int n = serviceRepo.size();
        if (n == 0) {
            ConsoleUtil.printWarning("No service requests loaded.");
            return;
        }

        // Copy array for sorting
        ServiceRequest[] arr = new ServiceRequest[n];
        for (int i = 0; i < n; i++) arr[i] = serviceRepo.get(i);

        // Sort descending by priority score using Dutch National Flag randomized partitioning
        java.util.Random rng = new java.util.Random(1337);
        RandomizedQuickSort.SortMetrics metrics = new RandomizedQuickSort.SortMetrics();
        quickSortDescending(arr, 0, n - 1, rng, metrics);

        long elapsed = System.nanoTime() - start;
        System.out.println("Top 15 Highest Priority Dispatches (Ranked):");
        int show = Math.min(15, n);
        for (int i = 0; i < show; i++) {
            System.out.printf("  Rank [%2d]: %s%n", i + 1, arr[i].toString());
        }
        System.out.printf("%n  [Metrics] Randomized Comparisons: %,d | Swaps: %,d%n", metrics.comparisons, metrics.swaps);
        ConsoleUtil.printSuccess(String.format("Successfully sorted %,d service work orders via Randomized QuickSort in %.3f ms.",
                n, elapsed / 1_000_000.0));
    }

    private void quickSortDescending(ServiceRequest[] a, int low, int high, java.util.Random rng, RandomizedQuickSort.SortMetrics metrics) {
        if (low >= high) return;
        int pIdx = low + rng.nextInt(high - low + 1);
        ServiceRequest pivot = a[pIdx];
        a[pIdx] = a[high];
        a[high] = pivot;
        if (metrics != null) metrics.swaps++;

        int i = low;
        for (int j = low; j < high; j++) {
            if (metrics != null) metrics.comparisons++;
            // Descending order: higher priority score comes first
            if (a[j].getPriorityScore() >= pivot.getPriorityScore()) {
                ServiceRequest temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                if (metrics != null) metrics.swaps++;
                i++;
            }
        }
        a[high] = a[i];
        a[i] = pivot;
        if (metrics != null) metrics.swaps++;

        quickSortDescending(a, low, i - 1, rng, metrics);
        quickSortDescending(a, i + 1, high, rng, metrics);
    }

    public DynamicArray<CitizenReport> searchByKeyword(String keyword) {
        DynamicArray<CitizenReport> results = new DynamicArray<>();
        if (keyword == null || keyword.isEmpty()) return results;
        for (int i = 0; i < citizenRepo.size(); i++) {
            CitizenReport r = citizenRepo.get(i);
            String full = (r.getDescription() + " " + r.getLocation() + " " + r.getCategory()).toLowerCase();
            if (full.contains(keyword.toLowerCase())) {
                results.add(r);
            }
        }
        return results;
    }

    public DynamicArray<CitizenReport> filterByPriority(smartcity.model.enums.ReportPriority priority) {
        DynamicArray<CitizenReport> results = new DynamicArray<>();
        for (int i = 0; i < citizenRepo.size(); i++) {
            CitizenReport r = citizenRepo.get(i);
            if (r.getPriority() == priority) {
                results.add(r);
            }
        }
        return results;
    }

    public DynamicArray<CitizenReport> sortByPriority() {
        DynamicArray<CitizenReport> copy = new DynamicArray<>();
        for (int i = 0; i < citizenRepo.size(); i++) {
            copy.add(citizenRepo.get(i));
        }
        for (int i = 1; i < copy.size(); i++) {
            CitizenReport key = copy.get(i);
            int j = i - 1;
            while (j >= 0 && copy.get(j).getPriority().ordinal() < key.getPriority().ordinal()) {
                copy.set(j + 1, copy.get(j));
                j--;
            }
            copy.set(j + 1, key);
        }
        return copy;
    }
}

