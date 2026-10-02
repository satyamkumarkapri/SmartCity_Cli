package smartcityx.util;

import smartcityx.model.*;
import smartcityx.model.enums.*;
import smartcityx.repository.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    private static final String[] LOCATIONS = {
        "MG Road", "Benz Circle", "Governorpet", "Gandhi Nagar", "Patamata",
        "Ramavarappadu", "Auto Nagar", "Bhavanipuram", "One Town", "Kanuru"
    };

    public static void loadSampleData(ReportRepository reportRepo, DocumentRepository docRepo, 
                                      ResourceRepository resourceRepo, SensorRepository sensorRepo) {
        Random rand = new Random();

        for (int i = 1; i <= 20; i++) {
            reportRepo.addCitizenReport(new CitizenReport(
                i, "Citizen " + i, LOCATIONS[rand.nextInt(LOCATIONS.length)],
                ReportCategory.values()[rand.nextInt(ReportCategory.values().length)],
                "Issue reported at location. Water pipeline leakage or similar.",
                ReportPriority.values()[rand.nextInt(ReportPriority.values().length)],
                ReportStatus.OPEN, LocalDateTime.now().minusHours(rand.nextInt(100))
            ));
        }

        for (int i = 1; i <= 10; i++) {
            reportRepo.addServiceRequest(new ServiceRequest(
                i, "Requester " + i, "Maintenance", LOCATIONS[rand.nextInt(LOCATIONS.length)],
                "Need maintenance service for local facilities.",
                ReportPriority.values()[rand.nextInt(ReportPriority.values().length)],
                ReportStatus.IN_PROGRESS
            ));
        }

        for (int i = 1; i <= 8; i++) {
            reportRepo.addEmergencyReport(new EmergencyReport(
                i, "Fire/Accident", LOCATIONS[rand.nextInt(LOCATIONS.length)],
                EmergencySeverity.values()[rand.nextInt(EmergencySeverity.values().length)],
                "Urgent emergency response required.",
                LocalDateTime.now().minusMinutes(rand.nextInt(60))
            ));
        }

        String[] docContents = {
            "Water management policy document for 2024.",
            "Traffic regulations and infrastructure guidelines.",
            "Emergency response protocol for fire and accidents.",
            "Smart city grid energy optimization plan.",
            "Waste management and recycling procedures.",
            "Public transport schedules and maintenance logs.",
            "Healthcare facilities distribution report.",
            "Urban planning and green space development.",
            "IoT sensor network deployment strategy.",
            "Citizen feedback summary and action plan."
        };
        for (int i = 1; i <= 10; i++) {
            docRepo.addDocument(new CityDocument(
                i, "Document " + i, "Dept " + (i % 3 + 1),
                docContents[i - 1], "Policy"
            ));
        }

        for (int i = 1; i <= 15; i++) {
            resourceRepo.addResource(new Resource(
                i, "Resource " + i, ResourceType.values()[rand.nextInt(ResourceType.values().length)],
                rand.nextInt(50) + 1, rand.nextInt(50000) + 1000,
                LOCATIONS[rand.nextInt(LOCATIONS.length)], true
            ));
        }
        
        for (int i = 1; i <= 30; i++) {
            resourceRepo.addInfrastructure(new Infrastructure(
                i, "Infra " + i, "Type " + (i % 5 + 1),
                LOCATIONS[rand.nextInt(LOCATIONS.length)],
                InfrastructureStatus.values()[rand.nextInt(InfrastructureStatus.values().length)],
                rand.nextDouble() * 1000
            ));
        }

        List<SensorReading> readings = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            readings.add(new SensorReading(
                i, "SENS-" + (i % 10 + 1), LOCATIONS[rand.nextInt(LOCATIONS.length)],
                SensorType.values()[rand.nextInt(SensorType.values().length)],
                20.0 + rand.nextDouble() * 30.0, LocalDateTime.now().minusMinutes(i)
            ));
        }
        sensorRepo.addAll(readings);
    }
}
