package smartcity.model;

import java.io.Serializable;

/**
 * Represents a smart city infrastructure or regulatory document.
 */
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String title;
    private final String content;
    private final String source;
    private final String timestamp;

    public Document(int id, String title, String content, String source, String timestamp) {
        this.id = id;
        this.title = title != null ? title.trim() : "";
        this.content = content != null ? content : "";
        this.source = source != null ? source : "SmartCityX Municipal Archive";
        this.timestamp = timestamp != null ? timestamp : java.time.Instant.now().toString();
    }

    public Document(int id, String title, String content) {
        this(id, title, content, "SmartCityX Civil Infrastructure Standards", java.time.Instant.now().toString());
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSource() { return source; }
    public String getTimestamp() { return timestamp; }

    public int getLength() {
        return content.length();
    }

    public String getSnippet(int startPos, int length) {
        if (content.isEmpty()) return "";
        int start = Math.max(0, startPos);
        int end = Math.min(content.length(), startPos + length);
        return content.substring(start, end).replaceAll("\\s+", " ");
    }

    @Override
    public String toString() {
        return String.format("[Doc #%d] %s (%d chars, source: %s)", id, title, content.length(), source);
    }
}
