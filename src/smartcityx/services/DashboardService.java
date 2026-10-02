package smartcityx.services;

import smartcityx.repository.*;
import smartcityx.util.ConsoleUtil;

public class DashboardService {
    private ReportRepository reportRepo;
    private DocumentRepository docRepo;
    private ResourceRepository resourceRepo;
    private SensorRepository sensorRepo;

    public DashboardService(ReportRepository reportRepo, DocumentRepository docRepo, ResourceRepository resourceRepo, SensorRepository sensorRepo) {
        this.reportRepo = reportRepo;
        this.docRepo = docRepo;
        this.resourceRepo = resourceRepo;
        this.sensorRepo = sensorRepo;
    }

    public void showDashboard() {
        ConsoleUtil.printHeader("SMARTCITYX DASHBOARD");
        System.out.println("Citizen Reports       : " + reportRepo.getAllCitizenReports().size());
        System.out.println("Emergency Reports     : " + reportRepo.getAllEmergencyReports().size());
        System.out.println("Service Requests      : " + reportRepo.getAllServiceRequests().size());
        System.out.println("City Documents        : " + docRepo.getAllDocuments().size());
        System.out.println("Resources             : " + resourceRepo.getAllResources().size());
        System.out.println("Infrastructure        : " + resourceRepo.getAllInfrastructures().size());
        System.out.println("IoT Readings          : " + sensorRepo.getAllReadings().size());

        ConsoleUtil.printSection("CITY STATUS");
        System.out.println("Traffic               : MODERATE");
        System.out.println("Water Resources       : NORMAL");
        System.out.println("Energy Usage          : HIGH");
        System.out.println("Emergency Alerts      : " + reportRepo.getAllEmergencyReports().size());
        
        int issues = 0;
        for (var infra : resourceRepo.getAllInfrastructures()) {
            if (infra.getStatus() == smartcityx.model.enums.InfrastructureStatus.DAMAGED || 
                infra.getStatus() == smartcityx.model.enums.InfrastructureStatus.OFFLINE) {
                issues++;
            }
        }
        System.out.println("Infrastructure Issues : " + issues);
        System.out.println("==================================================");
    }
}
