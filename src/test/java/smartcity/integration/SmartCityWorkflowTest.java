package smartcity.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smartcity.dataset.SmartCityDatasetLoader;
import smartcity.model.*;
import smartcity.model.enums.*;
import smartcity.repository.*;
import smartcity.services.*;
import smartcity.structures.DynamicArray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end integration tests for SmartCityX analytics workflows and dataset verification.
 */
public class SmartCityWorkflowTest {

    private static CitizenReportRepository citizenRepo;
    private static ServiceRequestRepository serviceRepo;
    private static EmergencyReportRepository emergencyRepo;
    private static DocumentRepository docRepo;
    private static IndexRepository indexRepo;
    private static TrafficRepository trafficRepo;
    private static ResourceRepository resourceRepo;
    private static EmergencyTeamRepository teamRepo;
    private static ProjectRepository projectRepo;
    private static InfrastructureRepository infraRepo;
    private static SensorRepository sensorRepo;

    private static CitizenReportService citizenService;
    private static DocumentSearchService documentService;
    private static TrafficAnalyticsService trafficService;
    private static ResourceManagementService resourceService;
    private static EmergencyResponseService emergencyService;
    private static IoTSensorService sensorService;

    @BeforeAll
    static void setUpAll() {
        citizenRepo = new CitizenReportRepository();
        serviceRepo = new ServiceRequestRepository();
        emergencyRepo = new EmergencyReportRepository();
        docRepo = new DocumentRepository();
        indexRepo = new IndexRepository();
        trafficRepo = new TrafficRepository();
        resourceRepo = new ResourceRepository();
        teamRepo = new EmergencyTeamRepository();
        projectRepo = new ProjectRepository();
        infraRepo = new InfrastructureRepository();
        sensorRepo = new SensorRepository();

        SmartCityDatasetLoader.LoadStats stats = SmartCityDatasetLoader.loadAll(
                citizenRepo, serviceRepo, emergencyRepo, docRepo, indexRepo,
                trafficRepo, resourceRepo, teamRepo, projectRepo, infraRepo, sensorRepo
        );

        citizenService = new CitizenReportService(citizenRepo, serviceRepo);
        documentService = new DocumentSearchService(docRepo);
        trafficService = new TrafficAnalyticsService(trafficRepo, projectRepo);
        resourceService = new ResourceManagementService(resourceRepo);
        emergencyService = new EmergencyResponseService(emergencyRepo, teamRepo, infraRepo);
        sensorService = new IoTSensorService(sensorRepo);
    }

    @Test
    @DisplayName("Verify SmartCityX synthetic dataset counts meet all specifications")
    void testDatasetCounts() {
        assertTrue(citizenRepo.size() >= 100, "Citizen reports must be >= 100, actual: " + citizenRepo.size());
        assertTrue(serviceRepo.size() >= 60, "Service requests must be >= 60, actual: " + serviceRepo.size());
        assertTrue(emergencyRepo.size() >= 40, "Emergency reports must be >= 40, actual: " + emergencyRepo.size());
        assertTrue(docRepo.size() >= 50, "Infrastructure documents must be >= 50, actual: " + docRepo.size());
        assertTrue(trafficRepo.getNodes().size() >= 20, "Traffic nodes must be >= 20, actual: " + trafficRepo.getNodes().size());
        assertTrue(trafficRepo.getEdges().size() >= 40, "Traffic edges must be >= 40, actual: " + trafficRepo.getEdges().size());
        assertTrue(resourceRepo.getWaterResources().size() >= 15, "Water resources must be >= 15, actual: " + resourceRepo.getWaterResources().size());
        assertTrue(resourceRepo.getEnergyResources().size() >= 15, "Energy resources must be >= 15, actual: " + resourceRepo.getEnergyResources().size());
        assertTrue(resourceRepo.getDemands().size() >= 30, "Resource demands must be >= 30, actual: " + resourceRepo.getDemands().size());
        assertTrue(teamRepo.getTeams().size() >= 12, "Emergency teams must be >= 12, actual: " + teamRepo.getTeams().size());
        assertTrue(teamRepo.getRequirements().size() >= 20, "Assignment requirements must be >= 20, actual: " + teamRepo.getRequirements().size());
        assertTrue(projectRepo.getTasks().size() >= 20, "Project tasks must be >= 20, actual: " + projectRepo.getTasks().size());
        assertTrue(projectRepo.getDependencies().size() >= 25, "Project dependencies must be >= 25, actual: " + projectRepo.getDependencies().size());
        assertTrue(infraRepo.getVertices().size() >= 20, "Infrastructure vertices must be >= 20, actual: " + infraRepo.getVertices().size());
        assertTrue(infraRepo.getEdges().size() >= 30, "Infrastructure edges must be >= 30, actual: " + infraRepo.getEdges().size());
        assertTrue(sensorRepo.size() >= 1000, "IoT sensor readings must be >= 1000, actual: " + sensorRepo.size());
    }

    @Test
    @DisplayName("Verify CitizenReportService string search and filtering")
    void testCitizenReportsWorkflow() {
        DynamicArray<CitizenReport> waterReports = citizenService.searchByKeyword("water");
        assertNotNull(waterReports);
        assertFalse(waterReports.isEmpty(), "Keyword search for 'water' should find matches");

        DynamicArray<CitizenReport> urgentReports = citizenService.filterByPriority(ReportPriority.URGENT);
        assertNotNull(urgentReports);
        for (int i = 0; i < urgentReports.size(); i++) {
            assertEquals(ReportPriority.URGENT, urgentReports.get(i).getPriority());
        }

        DynamicArray<CitizenReport> sortedByPri = citizenService.sortByPriority();
        assertNotNull(sortedByPri);
        assertEquals(citizenRepo.size(), sortedByPri.size());
    }

    @Test
    @DisplayName("Verify DocumentSearchService exact, multi-pattern, and fuzzy search")
    void testDocumentSearchWorkflow() {
        DynamicArray<Document> kmpResults = documentService.searchKMP("inspection");
        assertNotNull(kmpResults);
        assertFalse(kmpResults.isEmpty(), "Documents should contain 'inspection'");

        DynamicArray<Document> rabinResults = documentService.searchRabinKarp("safety");
        assertNotNull(rabinResults);

        DynamicArray<Document> fuzzyResults = documentService.fuzzySearch("saftey", 2);
        assertNotNull(fuzzyResults);
        assertFalse(fuzzyResults.isEmpty(), "Fuzzy search should identify safety-related docs with typo");
    }

    @Test
    @DisplayName("Verify TrafficAnalyticsService Dinic Max Flow and CPM Scheduling")
    void testTrafficAnalyticsWorkflow() {
        TrafficAnalyticsService.MaxFlowResult flow = trafficService.computeMaxThroughput("TNODE-001", "TNODE-010");
        assertNotNull(flow);
        assertTrue(flow.maxFlow > 0, "Max traffic flow should be positive");
        assertNotNull(flow.bottleneckEdges);

        TrafficAnalyticsService.ScheduleResult sched = trafficService.computeCriticalPathSchedule();
        assertNotNull(sched);
        assertTrue(sched.makespan > 0, "Project makespan should be positive");
        assertNotNull(sched.criticalPath);
        assertFalse(sched.criticalPath.isEmpty(), "Critical path should contain tasks");
    }

    @Test
    @DisplayName("Verify ResourceManagementService Bipartite Matching and Knapsack FPTAS")
    void testResourceManagementWorkflow() {
        ResourceManagementService.AllocationResult alloc = resourceService.allocateResourcesBipartite();
        assertNotNull(alloc);
        assertTrue(alloc.matchedCount > 0, "Resource bipartite matching should match demands to resources");

        ResourceManagementService.KnapsackResult dispatch = resourceService.optimizeEnergyDispatch(500.0, 0.1);
        assertNotNull(dispatch);
        assertTrue(dispatch.totalBenefit > 0, "Knapsack dispatch should produce positive benefit");
        assertTrue(dispatch.totalWeight <= 500.0, "Total dispatched capacity must not exceed 500.0");
    }

    @Test
    @DisplayName("Verify EmergencyResponseService Team Matching and Vertex Cover")
    void testEmergencyResponseWorkflow() {
        EmergencyResponseService.AssignmentResult result = emergencyService.assignTeamsToEmergencies();
        assertNotNull(result);
        assertTrue(result.matchedCount > 0, "Should assign emergency teams to incidents");

        EmergencyResponseService.CoverResult cover2 = emergencyService.findCameraCoverage2Approx();
        assertNotNull(cover2);
        assertTrue(cover2.coverVertices.size() > 0, "2-Approx vertex cover should select nodes");

        EmergencyResponseService.CoverResult paramCover = emergencyService.findOptimalCameraCoverage(15);
        assertNotNull(paramCover);
        assertTrue(paramCover.achievable, "Optimal parameter cover should find valid cover");
    }

    @Test
    @DisplayName("Verify IoTSensorService Blelloch Scan, Parallel Reduce, and Reservoir Sampling")
    void testIoTSensorWorkflow() {
        IoTSensorService.Aggregates agg = sensorService.computeAggregates();
        assertNotNull(agg);
        assertTrue(agg.count >= 1000, "Sensor aggregates count must be >= 1000");
        assertTrue(agg.minTemp <= agg.maxTemp, "Min temp must be <= max temp");
        assertTrue(agg.totalPower >= 0, "Total power consumption should be non-negative");

        double[] prefixPower = sensorService.computeCumulativePowerPrefixSum();
        assertNotNull(prefixPower);
        assertEquals(sensorRepo.size(), prefixPower.length);

        DynamicArray<SensorReading> auditSample = sensorService.sampleReadings(20);
        assertNotNull(auditSample);
        assertEquals(20, auditSample.size(), "Reservoir sampling should return exactly 20 items");
    }
}
