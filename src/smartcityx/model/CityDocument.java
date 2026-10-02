package smartcityx.model;

public class CityDocument {
    private int id;
    private String title;
    private String department;
    private String content;
    private String documentType;

    public CityDocument(int id, String title, String department, String content, String documentType) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.content = content;
        this.documentType = documentType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    @Override
    public String toString() {
        return "Document #" + id + " | " + title + " (" + documentType + ") - " + department;
    }
}
