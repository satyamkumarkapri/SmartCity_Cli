package smartcityx.model;

import smartcityx.model.enums.ReportCategory;
import smartcityx.model.enums.ReportPriority;
import smartcityx.model.enums.ReportStatus;

import java.time.LocalDateTime;

public class CitizenReport {
    private int id;
    private String citizenName;
    private String location;
    private ReportCategory category;
    private String description;
    private ReportPriority priority;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public CitizenReport(int id, String citizenName, String location, ReportCategory category, String description, ReportPriority priority, ReportStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.citizenName = citizenName;
        this.location = location;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public ReportCategory getCategory() { return category; }
    public void setCategory(ReportCategory category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ReportPriority getPriority() { return priority; }
    public void setPriority(ReportPriority priority) { this.priority = priority; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Report #" + id + " | " + citizenName + " | " + location + " | " + category + " | Priority: " + priority + " | Status: " + status;
    }
}
