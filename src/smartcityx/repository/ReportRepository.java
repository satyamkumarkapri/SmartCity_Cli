package smartcityx.repository;

import smartcityx.model.CitizenReport;
import smartcityx.model.EmergencyReport;
import smartcityx.model.ServiceRequest;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {
    private List<CitizenReport> citizenReports = new ArrayList<>();
    private List<ServiceRequest> serviceRequests = new ArrayList<>();
    private List<EmergencyReport> emergencyReports = new ArrayList<>();

    public void addCitizenReport(CitizenReport report) {
        citizenReports.add(report);
    }

    public List<CitizenReport> getAllCitizenReports() {
        return new ArrayList<>(citizenReports);
    }

    public void addServiceRequest(ServiceRequest request) {
        serviceRequests.add(request);
    }

    public List<ServiceRequest> getAllServiceRequests() {
        return new ArrayList<>(serviceRequests);
    }

    public void addEmergencyReport(EmergencyReport report) {
        emergencyReports.add(report);
    }

    public List<EmergencyReport> getAllEmergencyReports() {
        return new ArrayList<>(emergencyReports);
    }
}
