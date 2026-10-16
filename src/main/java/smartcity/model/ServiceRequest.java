package smartcity.model;

import smartcity.model.enums.ReportStatus;

/**
 * Represents an official municipal maintenance or service work order.
 */
public class ServiceRequest {
    private int id;
    private String requesterName;
    private String department;
    private String location;
    private String requestDetails;
    private int priorityScore; // 10 to 100
    private ReportStatus status;
    private int estimatedHours;

    public ServiceRequest(int id, String requesterName, String department, String location,
                          String requestDetails, int priorityScore, ReportStatus status, int estimatedHours) {
        this.id = id;
        this.requesterName = requesterName;
        this.department = department;
        this.location = location;
        this.requestDetails = requestDetails;
        this.priorityScore = priorityScore;
        this.status = status;
        this.estimatedHours = estimatedHours;
    }

    public int getId() { return id; }
    public String getRequesterName() { return requesterName; }
    public String getDepartment() { return department; }
    public String getLocation() { return location; }
    public String getRequestDetails() { return requestDetails; }
    public int getPriorityScore() { return priorityScore; }
    public ReportStatus getStatus() { return status; }
    public int getEstimatedHours() { return estimatedHours; }

    public void setStatus(ReportStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[SRQ-%03d] Dept: %-18s | Score: %3d | Est: %2d hrs | Status: %-11s | %s",
                id, department, priorityScore, estimatedHours, status, requestDetails);
    }
}
