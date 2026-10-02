package smartcityx.repository;

import smartcityx.model.CityDocument;
import java.util.ArrayList;
import java.util.List;

public class DocumentRepository {
    private List<CityDocument> documents = new ArrayList<>();

    public void addDocument(CityDocument doc) {
        documents.add(doc);
    }

    public List<CityDocument> getAllDocuments() {
        return new ArrayList<>(documents);
    }
}
