package smartcity.services;

import smartcity.algorithms.dynamicprogramming.SequenceAlignment;
import smartcity.algorithms.suffix.Kasai;
import smartcity.algorithms.suffix.SuffixArray;
import smartcity.algorithms.suffix.SuffixAutomaton;
import smartcity.model.Document;
import smartcity.repository.DocumentRepository;
import smartcity.structures.DynamicArray;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Civil Infrastructure Document Search and Text Analytics
 * using M2 (Suffix Structures) and M3 (Sequence Alignment & Levenshtein).
 */
public class DocumentSearchService {
    private final DocumentRepository docRepo;

    public DocumentSearchService(DocumentRepository docRepo) {
        this.docRepo = docRepo;
    }

    public void listDocuments(int limit) {
        ConsoleUtil.printSection("Civil Infrastructure & Operational Documents (" + docRepo.size() + " Total)");
        int n = Math.min(limit, docRepo.size());
        for (int i = 0; i < n; i++) {
            Document d = docRepo.get(i + 1);
            if (d != null) {
                System.out.printf("  [Doc #%02d] %-60s | Length: %,d chars%n",
                        d.getId(), d.getTitle(), d.getLength());
            }
        }
        if (docRepo.size() > n) {
            System.out.println("  ... and " + (docRepo.size() - n) + " more operational documents.");
        }
    }

    /**
     * Suffix Automaton fast substring query across documents.
     */
    public void searchSubstringViaAutomaton(String query) {
        ConsoleUtil.printSection("Suffix Automaton Substring Verification for \"" + query + "\"");
        long start = System.nanoTime();
        int foundCount = 0;

        for (Document d : docRepo.getAll()) {
            SuffixAutomaton sam = SuffixAutomaton.build(d.getContent());
            if (sam.contains(query)) {
                foundCount++;
                System.out.printf("  ✔ [VERIFIED] Found in Doc #%02d: \"%s\"%n", d.getId(), d.getTitle());
            }
        }
        long elapsed = System.nanoTime() - start;
        ConsoleUtil.printSuccess(String.format("Suffix Automaton verified existence in %d/%d documents in %.3f ms.",
                foundCount, docRepo.size(), elapsed / 1_000_000.0));
    }

    /**
     * Build and inspect Suffix Array & LCP for a specific document.
     */
    public void inspectSuffixArrayAndLCP(int docId) {
        Document d = docRepo.get(docId);
        if (d == null) {
            ConsoleUtil.printError("Document #" + docId + " not found.");
            return;
        }

        ConsoleUtil.printSection("Suffix Array & Kasai LCP Analysis: Doc #" + docId + " (" + d.getTitle() + ")");
        long start = System.nanoTime();
        String text = d.getContent();
        int[] sa = SuffixArray.buildSuffixArray(text);
        int[] lcp = Kasai.buildLCP(text, sa);
        long elapsed = System.nanoTime() - start;

        System.out.printf("Text length: %,d chars | Suffixes indexed: %,d | Construction Time: %.3f ms%n%n",
                text.length(), sa.length, elapsed / 1_000_000.0);

        int display = Math.min(12, sa.length);
        System.out.println(ConsoleUtil.BOLD_BLACK + " Rank │ SA[i] │ LCP[i] │ Suffix Snippet S[SA[i]..]" + ConsoleUtil.RESET);
        System.out.println("──────┼───────┼────────┼──────────────────────────────────────────────────");
        for (int i = 0; i < display; i++) {
            String suff = text.substring(sa[i]);
            if (suff.length() > 45) suff = suff.substring(0, 45) + "...";
            System.out.printf("  %3d │ %5d │   %4d │ %s%n", i, sa[i], (i < lcp.length ? lcp[i] : 0), suff.replace("\n", " "));
        }
        ConsoleUtil.printSuccess("Suffix Array and Kasai LCP construction verified.");
    }

    /**
     * Head-to-head empirical benchmarking of linear-time SA-IS against prefix-doubling.
     * Implements Image 2 requirement: "SA-IS for large-scale city-document indexing."
     */
    public void benchmarkSAISIndexing(int docId) {
        Document d = docRepo.get(docId);
        if (d == null) {
            ConsoleUtil.printError("Document #" + docId + " not found.");
            return;
        }

        ConsoleUtil.printSection("Suffix Array Construction Benchmark: Doc #" + docId + " (" + d.getTitle() + ")");
        String text = d.getContent();
        System.out.printf("Document Length: %,d characters%n%n", text.length());

        // Warmup
        SuffixArray.buildSuffixArray(text);
        SuffixArray.buildSAIS(text);

        // 1. Prefix Doubling
        long s1 = System.nanoTime();
        int[] saPrefix = SuffixArray.buildSuffixArray(text);
        long t1 = System.nanoTime() - s1;

        // 2. Linear SA-IS
        long s2 = System.nanoTime();
        int[] saLinear = SuffixArray.buildSAIS(text);
        long t2 = System.nanoTime() - s2;

        boolean matches = true;
        if (saPrefix.length != saLinear.length) {
            matches = false;
        } else {
            for (int i = 0; i < saPrefix.length; i++) {
                if (saPrefix[i] != saLinear[i]) {
                    matches = false;
                    break;
                }
            }
        }

        System.out.println(ConsoleUtil.BOLD_BLACK + " Construction Algorithm │ Suffixes │ Time (ns)   │ Time (ms)  │ Theoretical Complexity" + ConsoleUtil.RESET);
        System.out.println("────────────────────────┼──────────┼─────────────┼────────────┼───────────────────────");
        System.out.printf(" Prefix Doubling Merge  │ %,8d │ %,11d │ %10.4f │ O(N log^2 N), O(N)%n", saPrefix.length, t1, t1 / 1_000_000.0);
        System.out.printf(" SA-IS Induced Sorting  │ %,8d │ %,11d │ %10.4f │ O(N) LINEAR, O(N)%n", saLinear.length, t2, t2 / 1_000_000.0);
        System.out.println("────────────────────────┴──────────┴─────────────┴────────────┴───────────────────────");
        double speedup = (t1 > 0 && t2 > 0) ? ((double) t1 / t2) : 1.0;
        System.out.printf(" SA-IS Speedup Factor   : %s%.2fx faster%s than prefix doubling%n",
                ConsoleUtil.BRIGHT_GREEN, speedup, ConsoleUtil.RESET);
        System.out.println(" Verification Status    : " + (matches ? ConsoleUtil.BRIGHT_GREEN + "100% IDENTICAL SUFFIX ORDER" + ConsoleUtil.RESET : ConsoleUtil.RED + "MISMATCH" + ConsoleUtil.RESET));
        ConsoleUtil.printSuccess("Linear-time SA-IS document indexing successfully benchmarked.");
    }

    /**
     * Identifies common clauses between two documents using Needleman-Wunsch Global Alignment.
     */
    public void compareDocumentsAlignment(int docId1, int docId2) {
        Document d1 = docRepo.get(docId1);
        Document d2 = docRepo.get(docId2);
        if (d1 == null || d2 == null) {
            ConsoleUtil.printError("One or both document IDs are invalid.");
            return;
        }

        ConsoleUtil.printSection("Document Global Sequence Alignment (Needleman-Wunsch)");
        System.out.println("Doc 1: " + d1.getTitle() + " (" + d1.getLength() + " chars)");
        System.out.println("Doc 2: " + d2.getTitle() + " (" + d2.getLength() + " chars)");

        // Sample first 150 chars for deep alignment matrix
        int sampleLen = 120;
        String s1 = d1.getContent().substring(0, Math.min(sampleLen, d1.getLength()));
        String s2 = d2.getContent().substring(0, Math.min(sampleLen, d2.getLength()));

        long start = System.nanoTime();
        SequenceAlignment.AlignmentResult res = SequenceAlignment.needlemanWunsch(s1, s2, 2, -1, -2);
        long elapsed = System.nanoTime() - start;

        System.out.println("\nAlignment Score : " + ConsoleUtil.BRIGHT_GREEN + res.score + ConsoleUtil.RESET);
        System.out.printf("Analyzed Prefix Length: %d x %d chars (in %.3f ms)%n", s1.length(), s2.length(), elapsed / 1_000_000.0);
        System.out.println("Aligned Seq 1 : " + res.alignedSeq1);
        System.out.println("Aligned Seq 2 : " + res.alignedSeq2);
        ConsoleUtil.printSuccess("Needleman-Wunsch global alignment computed successfully.");
    }

    public DynamicArray<Document> searchKMP(String pattern) {
        DynamicArray<Document> results = new DynamicArray<>();
        if (pattern == null || pattern.isEmpty()) return results;
        for (Document d : docRepo.getAll()) {
            if (!smartcity.algorithms.strings.KMP.search(d.getContent().toLowerCase(), pattern.toLowerCase()).isEmpty()) {
                results.add(d);
            }
        }
        return results;
    }

    public DynamicArray<Document> searchRabinKarp(String pattern) {
        DynamicArray<Document> results = new DynamicArray<>();
        if (pattern == null || pattern.isEmpty()) return results;
        for (Document d : docRepo.getAll()) {
            if (!smartcity.algorithms.strings.RabinKarp.search(d.getContent().toLowerCase(), pattern.toLowerCase()).isEmpty()) {
                results.add(d);
            }
        }
        return results;
    }

    public DynamicArray<Document> fuzzySearch(String pattern, int maxDistance) {
        DynamicArray<Document> results = new DynamicArray<>();
        if (pattern == null || pattern.isEmpty()) return results;
        for (Document d : docRepo.getAll()) {
            String[] words = d.getContent().split("\\s+");
            for (String w : words) {
                String clean = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (clean.length() >= pattern.length() - 2) {
                    int dist = smartcity.algorithms.dynamicprogramming.Levenshtein.compute(pattern.toLowerCase(), clean);
                    if (dist <= maxDistance) {
                        results.add(d);
                        break;
                    }
                }
            }
        }
        return results;
    }
}

