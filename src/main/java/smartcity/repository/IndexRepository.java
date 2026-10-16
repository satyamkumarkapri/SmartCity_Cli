package smartcity.repository;

import smartcity.model.Document;
import smartcity.structures.CustomHashMap;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;

import java.io.*;

/**
 * Custom Inverted Index mapping normalized terms to posting lists of document IDs.
 * Implemented using custom CustomHashMap and IntArrayList (no java.util collections).
 */
public class IndexRepository {
    private final CustomHashMap<String, IntArrayList> index = new CustomHashMap<>(1024);
    private boolean isIndexed = false;
    private long buildTimeNs = 0;
    private int totalIndexedTokens = 0;

    public synchronized void buildIndex(DocumentRepository docRepo) {
        long start = System.nanoTime();
        index.clear();
        totalIndexedTokens = 0;

        DynamicArray<Document> docs = docRepo.getAll();
        for (Document doc : docs) {
            String text = doc.getContent();
            int docId = doc.getId();
            // Tokenize word-by-word
            int len = text.length();
            int i = 0;
            while (i < len) {
                while (i < len && !Character.isLetterOrDigit(text.charAt(i))) {
                    i++;
                }
                if (i >= len) break;
                int startToken = i;
                while (i < len && Character.isLetterOrDigit(text.charAt(i))) {
                    i++;
                }
                String token = text.substring(startToken, i).toLowerCase();
                if (token.length() >= 2) {
                    IntArrayList postings = index.get(token);
                    if (postings == null) {
                        postings = new IntArrayList();
                        index.put(token, postings);
                    }
                    if (postings.isEmpty() || postings.get(postings.size() - 1) != docId) {
                        postings.add(docId);
                    }
                    totalIndexedTokens++;
                }
            }
        }
        buildTimeNs = System.nanoTime() - start;
        isIndexed = true;
    }

    public synchronized IntArrayList getPostings(String term) {
        if (term == null) return new IntArrayList();
        IntArrayList postings = index.get(term.toLowerCase().trim());
        return postings != null ? postings : new IntArrayList();
    }

    public synchronized boolean isIndexed() {
        return isIndexed;
    }

    public synchronized long getBuildTimeNs() {
        return buildTimeNs;
    }

    public synchronized int getVocabularySize() {
        return index.size();
    }

    public synchronized int getTotalIndexedTokens() {
        return totalIndexedTokens;
    }

    public synchronized void clear() {
        index.clear();
        isIndexed = false;
        buildTimeNs = 0;
        totalIndexedTokens = 0;
    }

    public synchronized void saveToFile(File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            dos.writeBoolean(isIndexed);
            dos.writeLong(buildTimeNs);
            dos.writeInt(totalIndexedTokens);
            dos.writeInt(index.size());
            DynamicArray<String> keys = index.keys();
            for (String key : keys) {
                dos.writeUTF(key);
                IntArrayList postings = index.get(key);
                dos.writeInt(postings.size());
                for (int i = 0; i < postings.size(); i++) {
                    dos.writeInt(postings.get(i));
                }
            }
        }
    }

    public synchronized boolean loadFromFile(File file) throws IOException {
        if (!file.exists() || file.length() == 0) return false;
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            isIndexed = dis.readBoolean();
            buildTimeNs = dis.readLong();
            totalIndexedTokens = dis.readInt();
            int vocabSize = dis.readInt();
            index.clear();
            for (int i = 0; i < vocabSize; i++) {
                String term = dis.readUTF();
                int count = dis.readInt();
                IntArrayList list = new IntArrayList(count);
                for (int j = 0; j < count; j++) {
                    list.add(dis.readInt());
                }
                index.put(term, list);
            }
            return true;
        }
    }
}
