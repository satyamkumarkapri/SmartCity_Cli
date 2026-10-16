package smartcity.model;

import smartcity.model.enums.EmergencySeverity;

/**
 * Represents an active critical city incident requiring rapid dispatch.
 */
public class EmergencyReport {
    private int id;
    private String emergencyType;
    private String location;
    private EmergencySeverity severity;
    private String description;
    private int teamsRequired;
    private String reportedTimestamp;
    private boolean active;

    public EmergencyReport(int id, String emergencyType, String location, EmergencySeverity severity,
                           String description, int teamsRequired, String reportedTimestamp, boolean active) {
        this.id = id;
        this.emergencyType = emergencyType;
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.teamsRequired = teamsRequired;
        this.reportedTimestamp = reportedTimestamp;
        this.active = active;
    }

    public int getId() { return id; }
    public String getEmergencyType() { return emergencyType; }
    public String getLocation() { return location; }
    public EmergencySeverity getSeverity() { return severity; }
    public String getDescription() { return description; }
    public int getTeamsRequired() { return teamsRequired; }
    public String getReportedTimestamp() { return reportedTimestamp; }
    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("[EMR-%03d] %-22s | %-16s | %-8s | Teams Needed: %d | Active: %-5s | %s",
                id, emergencyType, location, severity, teamsRequired, active, description);
    }
}
