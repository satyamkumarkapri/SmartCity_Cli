package smartcityx.services;

import smartcityx.algorithms.suffix.*;
import smartcityx.algorithms.dp.Levenshtein;
import smartcityx.model.CityDocument;
import smartcityx.repository.DocumentRepository;
import smartcityx.util.ComplexityUtil;
import smartcityx.util.ConsoleUtil;
import java.util.List;

public class DocumentService {
    private DocumentRepository repo;

    public DocumentService(DocumentRepository repo) {
        this.repo = repo;
    }

    public void listDocuments() {
        List<CityDocument> docs = repo.getAllDocuments();
        if (docs.isEmpty()) {
            System.out.println("No documents available.");
            return;
        }
        for (CityDocument doc : docs) {
            System.out.println(doc);
        }
    }

    public void compareDocuments(int docId1, int docId2) {
        CityDocument d1 = null, d2 = null;
        for (CityDocument doc : repo.getAllDocuments()) {
            if (doc.getId() == docId1) d1 = doc;
            if (doc.getId() == docId2) d2 = doc;
        }

        if (d1 == null || d2 == null) {
            ConsoleUtil.printError("One or both documents not found.");
            return;
        }

        ComplexityUtil.display("Levenshtein Distance", "O(N*M)", "O(N*M)", "O(N*M)", "O(N*M)");

        String content1 = d1.getContent();
        String content2 = d2.getContent();

        long start = System.nanoTime();
        int dist = Levenshtein.calculateDistance(content1, content2);
        long end = System.nanoTime();

        int maxLen = Math.max(content1.length(), content2.length());
        double similarity = 100.0 * (maxLen - dist) / maxLen;

        System.out.println("Levenshtein Distance: " + dist);
        System.out.println(String.format("Similarity Percentage: %.2f%%", similarity));
        System.out.println("Execution Time: " + (end - start) + " ns");
    }

    public void buildSuffixArrayAndLCP(int docId) {
        CityDocument d = null;
        for (CityDocument doc : repo.getAllDocuments()) {
            if (doc.getId() == docId) d = doc;
        }
        if (d == null) {
            ConsoleUtil.printError("Document not found.");
            return;
        }
        
        ComplexityUtil.display("Suffix Array (Sorting) & LCP (Kasai)", "O(N log N)", "O(N log^2 N)", "O(N^2 log N)", "O(N)");

        String text = d.getContent();
        long start = System.nanoTime();
        int[] sa = SuffixArray.buildSuffixArray(text);
        int[] lcp = LCP.buildLCP(text, sa);
        long end = System.nanoTime();

        System.out.println("Suffix Array & LCP built in " + (end - start) + " ns");
        System.out.println("Displaying first 10 suffixes:");
        for (int i = 0; i < Math.min(10, sa.length); i++) {
            System.out.println("SA[" + i + "] = " + sa[i] + "\tLCP = " + lcp[i] + "\tSuffix: " + 
                text.substring(sa[i]).substring(0, Math.min(20, text.length() - sa[i])) + "...");
        }
    }

    public void searchSubstringSuffixAutomaton(int docId, String pattern) {
        CityDocument d = null;
        for (CityDocument doc : repo.getAllDocuments()) {
            if (doc.getId() == docId) d = doc;
        }
        if (d == null) {
            ConsoleUtil.printError("Document not found.");
            return;
        }

        ComplexityUtil.display("Suffix Automaton", "O(N)", "O(N)", "O(N)", "O(N)");

        String text = d.getContent();
        long startBuild = System.nanoTime();
        SuffixAutomaton sa = new SuffixAutomaton(text);
        long endBuild = System.nanoTime();

        long startSearch = System.nanoTime();
        boolean found = sa.searchSubstring(pattern);
        long endSearch = System.nanoTime();

        System.out.println("Automaton Build Time: " + (endBuild - startBuild) + " ns");
        System.out.println("Pattern search result: " + (found ? "Found" : "Not Found"));
        System.out.println("Search Execution Time: " + (endSearch - startSearch) + " ns");
    }
}
