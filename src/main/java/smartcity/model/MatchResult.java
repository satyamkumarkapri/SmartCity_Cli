package smartcity.model;

/**
 * Represents an individual string match inside a document.
 */
public class MatchResult {
    private final int documentId;
    private final String documentTitle;
    private final int position;
    private final String pattern;
    private final String snippet;

    public MatchResult(int documentId, String documentTitle, int position, String pattern, String snippet) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.position = position;
        this.pattern = pattern;
        this.snippet = snippet;
    }

    public int getDocumentId() { return documentId; }
    public String getDocumentTitle() { return documentTitle; }
    public int getPosition() { return position; }
    public String getPattern() { return pattern; }
    public String getSnippet() { return snippet; }

    @Override
    public String toString() {
        return String.format("Doc #%d ('%s') at pos %d: \"...%s...\"", documentId, documentTitle, position, snippet);
    }
}
