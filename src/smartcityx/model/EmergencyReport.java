package smartcityx.model;

import smartcityx.model.enums.EmergencySeverity;
import java.time.LocalDateTime;

public class EmergencyReport {
    private int id;
    private String type;
    private String location;
    private EmergencySeverity severity;
    private String description;
    private LocalDateTime reportedAt;

    public EmergencyReport(int id, String type, String location, EmergencySeverity severity, String description, LocalDateTime reportedAt) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.reportedAt = reportedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public EmergencySeverity getSeverity() { return severity; }
    public void setSeverity(EmergencySeverity severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    @Override
    public String toString() {
        return "Emergency #" + id + " | " + type + " | " + location + " | Severity: " + severity + " | Time: " + reportedAt;
    }
}
