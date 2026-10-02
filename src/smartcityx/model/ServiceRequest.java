package smartcityx.model;

import smartcityx.model.enums.ReportPriority;
import smartcityx.model.enums.ReportStatus;

public class ServiceRequest {
    private int id;
    private String citizenName;
    private String serviceType;
    private String location;
    private String description;
    private ReportPriority priority;
    private ReportStatus status;

    public ServiceRequest(int id, String citizenName, String serviceType, String location, String description, ReportPriority priority, ReportStatus status) {
        this.id = id;
        this.citizenName = citizenName;
        this.serviceType = serviceType;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ReportPriority getPriority() { return priority; }
    public void setPriority(ReportPriority priority) { this.priority = priority; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Request #" + id + " | " + citizenName + " | " + serviceType + " | " + location + " | Priority: " + priority + " | Status: " + status;
    }
}
