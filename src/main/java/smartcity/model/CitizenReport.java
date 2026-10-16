package smartcity.model;

import smartcity.model.enums.ReportCategory;
import smartcity.model.enums.ReportPriority;
import smartcity.model.enums.ReportStatus;

/**
 * Represents a citizen complaint or incident report.
 */
public class CitizenReport {
    private int id;
    private String citizenName;
    private String location;
    private ReportCategory category;
    private String description;
    private ReportPriority priority;
    private ReportStatus status;
    private String reportedTimestamp;

    public CitizenReport(int id, String citizenName, String location, ReportCategory category,
                         String description, ReportPriority priority, ReportStatus status, String reportedTimestamp) {
        this.id = id;
        this.citizenName = citizenName;
        this.location = location;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.reportedTimestamp = reportedTimestamp;
    }

    public int getId() { return id; }
    public String getCitizenName() { return citizenName; }
    public String getLocation() { return location; }
    public ReportCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public ReportPriority getPriority() { return priority; }
    public ReportStatus getStatus() { return status; }
    public String getReportedTimestamp() { return reportedTimestamp; }

    public void setStatus(ReportStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[#%03d] %-18s | %-14s | %-14s | Priority: %-6s | Status: %-11s | %s",
                id, citizenName, location, category, priority, status, description);
    }
}
