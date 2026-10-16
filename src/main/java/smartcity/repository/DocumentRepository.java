package smartcity.repository;

import smartcity.model.Document;
import smartcity.structures.CustomHashMap;
import smartcity.structures.DynamicArray;

import java.io.*;

/**
 * Thread-safe, persistent in-memory repository for imported English Wikipedia documents.
 * Backed by custom DynamicArray and CustomHashMap without java.util collections.
 */
public class DocumentRepository {
    private final CustomHashMap<Integer, Document> docById = new CustomHashMap<>();
    private final DynamicArray<Document> docs = new DynamicArray<>();
    private String datasetName = "No dataset loaded";
    private String datasetSource = "None";
    private String lastLoadedTimestamp = "Never";

    public synchronized boolean add(Document doc) {
        if (doc == null) return false;
        if (docById.containsKey(doc.getId())) {
            // Duplicate detected; update reference
            for (int i = 0; i < docs.size(); i++) {
                if (docs.get(i).getId() == doc.getId()) {
                    docs.set(i, doc);
                    docById.put(doc.getId(), doc);
                    return false;
                }
            }
        }
        docs.add(doc);
        docById.put(doc.getId(), doc);
        return true;
    }

    public synchronized Document get(int id) {
        return docById.get(id);
    }

    public synchronized DynamicArray<Document> getAll() {
        return docs;
    }

    public synchronized int size() {
        return docs.size();
    }

    public synchronized void clear() {
        docs.clear();
        docById.clear();
        datasetName = "Empty";
        datasetSource = "None";
    }

    public synchronized String getDatasetName() { return datasetName; }
    public synchronized void setDatasetName(String name) { this.datasetName = name; }
    public synchronized String getDatasetSource() { return datasetSource; }
    public synchronized void setDatasetSource(String source) { this.datasetSource = source; }
    public synchronized String getLastLoadedTimestamp() { return lastLoadedTimestamp; }
    public synchronized void setLastLoadedTimestamp(String ts) { this.lastLoadedTimestamp = ts; }

    /**
     * Persists imported corpus to a local binary file.
     */
    public synchronized void saveToFile(File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            dos.writeUTF(datasetName != null ? datasetName : "");
            dos.writeUTF(datasetSource != null ? datasetSource : "");
            dos.writeUTF(lastLoadedTimestamp != null ? lastLoadedTimestamp : "");
            dos.writeInt(docs.size());
            for (Document d : docs) {
                dos.writeInt(d.getId());
                dos.writeUTF(d.getTitle());
                dos.writeUTF(d.getSource());
                dos.writeUTF(d.getTimestamp());
                byte[] contentBytes = d.getContent().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                dos.writeInt(contentBytes.length);
                dos.write(contentBytes);
            }
        }
    }

    /**
     * Reloads corpus from local binary file.
     */
    public synchronized boolean loadFromFile(File file) throws IOException {
        if (!file.exists() || file.length() == 0) return false;
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            clear();
            datasetName = dis.readUTF();
            datasetSource = dis.readUTF();
            lastLoadedTimestamp = dis.readUTF();
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                int id = dis.readInt();
                String title = dis.readUTF();
                String source = dis.readUTF();
                String ts = dis.readUTF();
                int len = dis.readInt();
                byte[] contentBytes = new byte[len];
                dis.readFully(contentBytes);
                String content = new String(contentBytes, java.nio.charset.StandardCharsets.UTF_8);
                add(new Document(id, title, content, source, ts));
            }
            return true;
        }
    }
}
