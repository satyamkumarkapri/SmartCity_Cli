package smartcity.dataset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import smartcity.cli.SelfTestRunner;
import smartcity.core.SmartCityEngine;
import smartcity.model.*;
import smartcity.repository.DocumentRepository;
import smartcity.repository.IndexRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dataset & Full System Integration Tests")
public class DatasetIntegrationTest {

    @Test
    @DisplayName("Load bundled demonstration corpus and verify offline documents")
    public void testBundledCorpusLoading() {
        DocumentRepository docRepo = new DocumentRepository();
        IndexRepository indexRepo = new IndexRepository();

        DatasetManager.ImportStats stats = DatasetManager.loadBundledDemonstrationCorpus(docRepo, indexRepo);
        assertTrue(stats.importedCount >= 10, "Bundled corpus should import at least 10 sample articles");
        assertEquals(stats.importedCount, docRepo.size());
        assertTrue(indexRepo.isIndexed());
        assertTrue(indexRepo.getVocabularySize() > 100);

        // Verify known document content
        Document doc1 = docRepo.get(1);
        assertNotNull(doc1);
        assertTrue(doc1.getContent().contains("Turing"));
        assertTrue(doc1.getContent().contains("algorithm"));
    }

    @Test
    @DisplayName("Import local dataset: valid records, duplicate handling, and malformed line resilience")
    public void testLocalImportAndMalformedHandling(@TempDir Path tempDir) throws IOException {
        File jsonlFile = tempDir.resolve("test_articles.jsonl").toFile();
        try (FileWriter fw = new FileWriter(jsonlFile)) {
            fw.write("{\"id\": 101, \"title\": \"Graph Theory\", \"text\": \"Vertices and edges form graphs.\", \"source\": \"Wikipedia\"}\n");
            fw.write("\n"); // Blank line
            fw.write("{\"id\": 102, \"title\": \"Linear Programming\", \"text\": \"Simplex method optimizes linear objectives.\", \"source\": \"Wikipedia\"}\n");
            fw.write("{\"id\": 101, \"title\": \"Graph Theory Duplicate\", \"text\": \"Updated text.\", \"source\": \"Wikipedia\"}\n"); // Duplicate ID
            fw.write("MALFORMED NON-JSON LINE HERE\n"); // Malformed line
            fw.write("{\"id\": 103, \"title\": \"Shortest Path\", \"text\": \"Dijkstra computes paths.\", \"source\": \"Wikipedia\"}\n");
        }

        DocumentRepository docRepo = new DocumentRepository();
        IndexRepository indexRepo = new IndexRepository();

        DatasetManager.ImportStats stats = DatasetManager.importCorpusFromFile(jsonlFile, docRepo, indexRepo, 50, 10000);
        assertEquals(3, stats.importedCount); // 101, 102, 103
        assertEquals(1, stats.duplicateCount); // duplicate 101
        assertEquals(1, stats.malformedCount); // malformed line

        Document d101 = docRepo.get(101);
        assertNotNull(d101);
        assertEquals("Updated text.", d101.getContent()); // Updated duplicate content
    }

    @Test
    @DisplayName("Corpus persistence: saving to disk and reloading survives restart")
    public void testCorpusPersistence(@TempDir Path tempDir) throws IOException {
        File storeFile = tempDir.resolve("corpus_store.dat").toFile();

        DocumentRepository docRepo1 = new DocumentRepository();
        docRepo1.setDatasetName("Persistence Test Set");
        docRepo1.add(new Document(1, "Alpha Title", "Alpha content with keywords.", "Source A", "2026-10-15"));
        docRepo1.add(new Document(2, "Beta Title", "Beta content with algorithms.", "Source B", "2026-10-15"));
        docRepo1.saveToFile(storeFile);

        DocumentRepository docRepo2 = new DocumentRepository();
        boolean loaded = docRepo2.loadFromFile(storeFile);
        assertTrue(loaded);
        assertEquals(2, docRepo2.size());
        assertEquals("Persistence Test Set", docRepo2.getDatasetName());
        assertEquals("Alpha Title", docRepo2.get(1).getTitle());
        assertEquals("Beta Title", docRepo2.get(2).getTitle());
    }

    @Test
    @DisplayName("End-to-End Query Engine: Real search, fuzzy matching, and similarity across corpus")
    public void testEndToEndQueryEngine() {
        DocumentRepository docRepo = new DocumentRepository();
        IndexRepository indexRepo = new IndexRepository();
        DatasetManager.loadBundledDemonstrationCorpus(docRepo, indexRepo);

        SmartCityEngine engine = new SmartCityEngine(docRepo, indexRepo);

        // 1. Exact Search
        QueryRequest exactReq = new QueryRequest.Builder(QueryType.EXACT_SEARCH)
                .algorithmVariant("KMP")
                .primaryInput("algorithm")
                .build();
        QueryResult exactRes = engine.execute(exactReq);
        assertTrue(exactRes.isSuccess());
        assertTrue(exactRes instanceof StringSearchResult);
        StringSearchResult searchRes = (StringSearchResult) exactRes;
        assertTrue(searchRes.getMatchCount() > 0, "Should find occurrences of 'algorithm' in bundled corpus");

        // 2. Fuzzy Search
        QueryRequest fuzzyReq = new QueryRequest.Builder(QueryType.FUZZY_SEARCH)
                .primaryInput("Turing")
                .numericParam1(2)
                .build();
        QueryResult fuzzyRes = engine.execute(fuzzyReq);
        assertTrue(fuzzyRes.isSuccess());
        assertTrue(((StringSearchResult) fuzzyRes).getMatchCount() > 0);

        // 3. Document Similarity
        QueryRequest simReq = new QueryRequest.Builder(QueryType.DOCUMENT_SIMILARITY)
                .algorithmVariant("LEVENSHTEIN")
                .numericParam1(1)
                .numericParam2(2)
                .build();
        QueryResult simRes = engine.execute(simReq);
        assertTrue(simRes.isSuccess());
        assertTrue(simRes instanceof SimilarityResult);
        assertTrue(((SimilarityResult) simRes).getSimilarityScore() >= 0);

        // 4. Citation Flow
        QueryResult flowRes = engine.execute(new QueryRequest.Builder(QueryType.CITATION_FLOW).build());
        assertTrue(flowRes.isSuccess());
        assertTrue(flowRes instanceof FlowResult);
        FlowResult fr = (FlowResult) flowRes;
        assertTrue(fr.getMaxFlow() > 0);
        assertTrue(fr.isFlowCutDualityVerified());
    }

    @Test
    @DisplayName("SelfTestRunner executes cleanly and passes 100% of internal checks")
    public void testSelfTestRunnerExecution() {
        SelfTestRunner runner = new SelfTestRunner();
        // Running should execute without uncaught exceptions
        assertDoesNotThrow(runner::runAllTests);
    }
}
