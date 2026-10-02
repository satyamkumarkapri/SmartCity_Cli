package smartcityx.services;

import smartcityx.repository.*;
import smartcityx.algorithms.strings.KMP;
import smartcityx.util.ConsoleUtil;
import smartcityx.util.ComplexityUtil;
import smartcityx.model.*;

public class UniversalSearchService {
    private ReportRepository reportRepo;
    private DocumentRepository docRepo;
    private ResourceRepository resourceRepo;

    public UniversalSearchService(ReportRepository reportRepo, DocumentRepository docRepo, ResourceRepository resourceRepo) {
        this.reportRepo = reportRepo;
        this.docRepo = docRepo;
        this.resourceRepo = resourceRepo;
    }

    public void search(String keyword) {
        ConsoleUtil.printSection("UNIVERSAL SEARCH RESULTS FOR: '" + keyword + "'");
        ComplexityUtil.display("KMP Algorithm (Used for Universal Search)", "O(N)", "O(N)", "O(N)", "O(M)");
        
        String pattern = keyword.toLowerCase();
        boolean found = false;

        System.out.println("\n--- Citizen & Emergency Reports ---");
        for (CitizenReport r : reportRepo.getAllCitizenReports()) {
            if (!KMP.search(r.toString().toLowerCase() + " " + r.getDescription().toLowerCase(), pattern).isEmpty()) {
                System.out.println(r); found = true;
            }
        }
        for (EmergencyReport r : reportRepo.getAllEmergencyReports()) {
            if (!KMP.search(r.toString().toLowerCase() + " " + r.getDescription().toLowerCase(), pattern).isEmpty()) {
                System.out.println(r); found = true;
            }
        }

        System.out.println("\n--- City Documents ---");
        for (CityDocument d : docRepo.getAllDocuments()) {
            if (!KMP.search(d.toString().toLowerCase() + " " + d.getContent().toLowerCase(), pattern).isEmpty()) {
                System.out.println(d); found = true;
            }
        }

        System.out.println("\n--- Resources & Infrastructure ---");
        for (Resource r : resourceRepo.getAllResources()) {
            if (!KMP.search(r.toString().toLowerCase(), pattern).isEmpty()) {
                System.out.println(r); found = true;
            }
        }
        for (Infrastructure inf : resourceRepo.getAllInfrastructures()) {
            if (!KMP.search(inf.toString().toLowerCase(), pattern).isEmpty()) {
                System.out.println(inf); found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo results found in any department.");
        }
    }
}
