package smartcity.dataset;

import smartcity.model.Document;
import smartcity.repository.DocumentRepository;
import smartcity.repository.IndexRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Manages corpus loading, incremental Wikipedia imports (JSONL/TSV),
 * provenance tracking, and index synchronization.
 */
public class DatasetManager {
    public static final String DEFAULT_PROCESSED_CORPUS = "data/processed/corpus.dat";
    public static final String DEFAULT_INDEX_FILE = "data/indexes/inverted_index.dat";
    public static final String BUNDLED_SAMPLE_PATH = "data/infrastructure_documents.jsonl";

    public static class ImportStats {
        public int processedCount = 0;
        public int importedCount = 0;
        public int duplicateCount = 0;
        public int malformedCount = 0;
        public int skippedSizeLimitCount = 0;
        public long elapsedNs = 0;
        public String sourcePath = "";
    }

    /**
     * Loads the bundled offline demonstration corpus.
     */
    public static ImportStats loadBundledDemonstrationCorpus(DocumentRepository docRepo, IndexRepository indexRepo) {
        ImportStats stats = new ImportStats();
        long start = System.nanoTime();
        docRepo.clear();

        // 1. Try classpath resource
        InputStream is = DatasetManager.class.getResourceAsStream("/sample_corpus.jsonl");
        if (is == null) {
            // 2. Try file path
            File file = new File(BUNDLED_SAMPLE_PATH);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException ignored) {}
            }
        }

        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                importFromBufferedReader(reader, docRepo, stats, 1000, 10_000_000);
                docRepo.setDatasetName("SmartCity Infrastructure Technical Specifications (60 Documents)");
                docRepo.setDatasetSource("SmartCityX Municipal Civil Infrastructure Archive");
                docRepo.setLastLoadedTimestamp(java.time.Instant.now().toString());
            } catch (IOException e) {
                System.err.println("Error reading bundled sample corpus: " + e.getMessage());
            }
        }

        // Persist processed corpus
        try {
            docRepo.saveToFile(new File(DEFAULT_PROCESSED_CORPUS));
        } catch (Exception ignored) {}

        if (indexRepo != null) {
            indexRepo.buildIndex(docRepo);
            try {
                indexRepo.saveToFile(new File(DEFAULT_INDEX_FILE));
            } catch (Exception ignored) {}
        }

        stats.elapsedNs = System.nanoTime() - start;
        return stats;
    }

    /**
     * Imports an external English Wikipedia JSONL or TSV corpus incrementally.
     */
    public static ImportStats importCorpusFromFile(File file, DocumentRepository docRepo, IndexRepository indexRepo,
                                                  int maxArticles, int maxDocSizeBytes) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Corpus file not found: " + file.getAbsolutePath());
        }

        ImportStats stats = new ImportStats();
        stats.sourcePath = file.getAbsolutePath();
        long start = System.nanoTime();

        docRepo.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".tsv") || fileName.endsWith(".tab")) {
                importFromTsv(reader, docRepo, stats, maxArticles, maxDocSizeBytes);
            } else {
                importFromBufferedReader(reader, docRepo, stats, maxArticles, maxDocSizeBytes);
            }
        }

        docRepo.setDatasetName("Imported Corpus: " + file.getName());
        docRepo.setDatasetSource("Local Import: " + file.getAbsolutePath() + " (CC BY-SA 4.0 / GFDL)");
        docRepo.setLastLoadedTimestamp(java.time.Instant.now().toString());

        // Persist processed corpus
        try {
            docRepo.saveToFile(new File(DEFAULT_PROCESSED_CORPUS));
        } catch (Exception e) {
            System.err.println("Warning: could not persist corpus to " + DEFAULT_PROCESSED_CORPUS + ": " + e.getMessage());
        }

        if (indexRepo != null) {
            indexRepo.buildIndex(docRepo);
            try {
                indexRepo.saveToFile(new File(DEFAULT_INDEX_FILE));
            } catch (Exception ignored) {}
        }

        stats.elapsedNs = System.nanoTime() - start;
        return stats;
    }

    private static void importFromBufferedReader(BufferedReader reader, DocumentRepository docRepo,
                                                 ImportStats stats, int maxArticles, int maxDocSizeBytes) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            stats.processedCount++;

            Document doc = parseJsonlLine(line);
            if (doc == null) {
                stats.malformedCount++;
                continue;
            }

            if (doc.getContent().length() > maxDocSizeBytes) {
                stats.skippedSizeLimitCount++;
                continue;
            }

            boolean added = docRepo.add(doc);
            if (added) {
                stats.importedCount++;
            } else {
                stats.duplicateCount++;
            }

            if (stats.importedCount >= maxArticles) {
                break;
            }
        }
    }

    private static void importFromTsv(BufferedReader reader, DocumentRepository docRepo,
                                     ImportStats stats, int maxArticles, int maxDocSizeBytes) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            stats.processedCount++;
            String[] parts = line.split("\t", -1);
            if (parts.length < 3) {
                stats.malformedCount++;
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                String text = parts[2].trim();
                String source = parts.length > 3 ? parts[3].trim() : "English Wikipedia Dump";
                if (text.length() > maxDocSizeBytes) {
                    stats.skippedSizeLimitCount++;
                    continue;
                }
                Document doc = new Document(id, title, text, source, java.time.Instant.now().toString());
                if (docRepo.add(doc)) {
                    stats.importedCount++;
                } else {
                    stats.duplicateCount++;
                }
                if (stats.importedCount >= maxArticles) break;
            } catch (Exception e) {
                stats.malformedCount++;
            }
        }
    }

    /**
     * Robust, lightweight JSONL single-line parser without third-party dependencies.
     * Extracts "id", "title", "text", "source".
     */
    public static Document parseJsonlLine(String line) {
        try {
            int id = extractIntField(line, "id");
            String title = extractStringField(line, "title");
            String text = extractStringField(line, "content");
            if (text.isEmpty()) text = extractStringField(line, "text");
            String source = extractStringField(line, "department");
            if (source.isEmpty()) source = extractStringField(line, "source");
            if (source.isEmpty()) source = "SmartCityX Municipal Archive";
            if (title.isEmpty() && text.isEmpty()) return null;
            return new Document(id, title, text, source, java.time.Instant.now().toString());
        } catch (Exception e) {
            return null;
        }
    }

    private static int extractIntField(String json, String field) {
        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return (int) (System.nanoTime() & 0x7FFFFFFF);
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon == -1) return 0;
        int start = colon + 1;
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        if (start == end) return (int) (System.nanoTime() & 0x7FFFFFFF);
        return Integer.parseInt(json.substring(start, end));
    }

    private static String extractStringField(String json, String field) {
        String pattern = "\"" + field + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return "";
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon == -1) return "";
        int firstQuote = json.indexOf('\"', colon + 1);
        if (firstQuote == -1) return "";

        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = firstQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escape) {
                if (c == 'n') sb.append('\n');
                else if (c == 'r') sb.append('\r');
                else if (c == 't') sb.append('\t');
                else if (c == '\\') sb.append('\\');
                else if (c == '\"') sb.append('\"');
                else if (c == 'u' && i + 4 < json.length()) {
                    try {
                        int code = Integer.parseInt(json.substring(i + 1, i + 5), 16);
                        sb.append((char) code);
                        i += 4;
                    } catch (Exception e) {
                        sb.append(c);
                    }
                } else {
                    sb.append(c);
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '\"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
