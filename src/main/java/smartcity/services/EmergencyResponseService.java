package smartcity.services;

import smartcity.algorithms.complexity.ParameterizedVertexCover;
import smartcity.algorithms.complexity.VertexCoverApproximation;
import smartcity.algorithms.graph.BipartiteMatching;
import smartcity.algorithms.strings.KMP;
import smartcity.model.AssignmentRequirement;
import smartcity.model.EmergencyReport;
import smartcity.model.EmergencyTeam;
import smartcity.model.InfrastructureVertex;
import smartcity.repository.EmergencyReportRepository;
import smartcity.repository.EmergencyTeamRepository;
import smartcity.repository.InfrastructureRepository;
import smartcity.structures.DynamicArray;
import smartcity.structures.IntArrayList;
import smartcity.util.ConsoleUtil;

/**
 * Service orchestrating Emergency Response, Team Dispatch, and Critical Asset Protection
 * using M4 (Bipartite Matching) and M5 (Vertex Cover Approximation & FPT).
 */
public class EmergencyResponseService {
    private final EmergencyReportRepository emergencyRepo;
    private final EmergencyTeamRepository teamRepo;
    private final InfrastructureRepository infraRepo;

    public static class AssignmentResult {
        public final int matchedCount;
        public final DynamicArray<BipartiteMatching.MatchingPair> pairs;

        public AssignmentResult(int matchedCount, DynamicArray<BipartiteMatching.MatchingPair> pairs) {
            this.matchedCount = matchedCount;
            this.pairs = pairs != null ? pairs : new DynamicArray<>();
        }
    }

    public static class CoverResult {
        public final IntArrayList coverVertices;
        public final boolean achievable;

        public CoverResult(IntArrayList coverVertices, boolean achievable) {
            this.coverVertices = coverVertices != null ? coverVertices : new IntArrayList();
            this.achievable = achievable;
        }
    }

    public EmergencyResponseService(EmergencyReportRepository emergencyRepo,
                                    EmergencyTeamRepository teamRepo,
                                    InfrastructureRepository infraRepo) {
        this.emergencyRepo = emergencyRepo;
        this.teamRepo = teamRepo;
        this.infraRepo = infraRepo;
    }

    public void listEmergenciesAndTeams() {
        ConsoleUtil.printSection("Active Emergency Incidents (" + emergencyRepo.getActiveEmergencies().size() + " Active)");
        int eShow = Math.min(8, emergencyRepo.size());
        for (int i = 0; i < eShow; i++) {
            EmergencyReport em = emergencyRepo.get(i);
            System.out.println("  " + em.toString());
        }

        ConsoleUtil.printSection("Emergency Response Squads (" + teamRepo.getTeamCount() + " Total)");
        int tShow = Math.min(8, teamRepo.getTeamCount());
        for (int i = 0; i < tShow; i++) {
            EmergencyTeam team = teamRepo.getTeam(i);
            System.out.println("  " + team.toString());
        }
    }

    public AssignmentResult assignTeamsToEmergencies() {
        boolean[][] graph = teamRepo.buildBipartiteGraph();
        BipartiteMatching.MatchingResult result = BipartiteMatching.findMatching(graph);
        return new AssignmentResult(result.matchCount, result.pairs);
    }

    public CoverResult findCameraCoverage2Approx() {
        boolean[][] adj = infraRepo.buildAdjacencyMatrix();
        VertexCoverApproximation.ApproxVCResult approx = VertexCoverApproximation.findApproximateVertexCover(adj);
        return new CoverResult(approx.vertexCover, true);
    }

    public CoverResult findOptimalCameraCoverage(int k) {
        boolean[][] adj = infraRepo.buildAdjacencyMatrix();
        ParameterizedVertexCover.FPTResult fpt = ParameterizedVertexCover.solve(adj, k);
        return new CoverResult(fpt.vertexCover, fpt.found);
    }

    /**
     * Solves the emergency squad-to-incident allocation problem using Maximum Bipartite Matching.
     */
    public void assignEmergencyTeams() {
        ConsoleUtil.printSection("Emergency Squad Optimal Allocation (Maximum Bipartite Matching)");
        int tCount = teamRepo.getTeamCount();
        int rCount = teamRepo.getRequirementCount();

        if (tCount == 0 || rCount == 0) {
            ConsoleUtil.printWarning("Insufficient teams or incident requirements loaded.");
            return;
        }

        boolean[][] graph = teamRepo.buildBipartiteGraph();
        long start = System.nanoTime();
        BipartiteMatching.MatchingResult result = BipartiteMatching.findMatching(graph);
        long elapsed = System.nanoTime() - start;

        System.out.printf("  Available Emergency Squads : %d units%n", tCount);
        System.out.printf("  Active Incident Requirements: %d requirements%n", rCount);
        System.out.printf("  Maximum Matched Incidents   : %s%d successful dispatches%s%n%n",
                ConsoleUtil.BRIGHT_GREEN, result.matchCount, ConsoleUtil.RESET);

        System.out.println("Dispatched Squad Assignments:");
        for (int i = 0; i < result.pairs.size(); i++) {
            BipartiteMatching.MatchingPair pair = result.pairs.get(i);
            EmergencyTeam team = teamRepo.getTeam(pair.left);
            AssignmentRequirement req = teamRepo.getRequirement(pair.right);
            System.out.printf("  ✔ Dispatch #%02d: %s%s%s --> [Req #%d @ %s: %s]%n",
                    i + 1, ConsoleUtil.CYAN, team.getTeamName(), ConsoleUtil.RESET,
                    req.getReqId(), req.getLocation(), req.getRequiredSpecialty());
        }
        System.out.println();
        ConsoleUtil.printSuccess(String.format("Bipartite matching computed in %,d ns (%.4f ms).",
                elapsed, elapsed / 1_000_000.0));
    }

    /**
     * Protects critical municipal infrastructure facilities using Vertex Cover 2-Approximation and FPT.
     */
    public void optimizeInfrastructureProtection() {
        ConsoleUtil.printSection("Critical Infrastructure Hazard Coverage (Vertex Cover 2-Approximation & FPT)");
        int vCount = infraRepo.getVertexCount();
        int eCount = infraRepo.getEdgeCount();

        if (vCount == 0 || eCount == 0) {
            ConsoleUtil.printWarning("Infrastructure graph not loaded.");
            return;
        }

        boolean[][] adj = infraRepo.buildAdjacencyMatrix();

        // 1. Run 2-Approximation
        long start2Approx = System.nanoTime();
        VertexCoverApproximation.ApproxVCResult approx = VertexCoverApproximation.findApproximateVertexCover(adj);
        long elapsed2Approx = System.nanoTime() - start2Approx;

        // 2. Run Parameterized FPT
        long startFpt = System.nanoTime();
        ParameterizedVertexCover.FPTResult fpt = ParameterizedVertexCover.solve(adj, approx.vertexCover.size());
        long elapsedFpt = System.nanoTime() - startFpt;

        System.out.printf("  Total Facilities (Vertices) : %d nodes%n", vCount);
        System.out.printf("  Interconnecting Links (Edges): %d links%n%n", eCount);

        System.out.printf("  2-Approximation Cover Size   : %s%d nodes%s (Guaranteed <= 2 * OPT, in %.3f ms)%n",
                ConsoleUtil.BRIGHT_YELLOW, approx.vertexCover.size(), ConsoleUtil.RESET, elapsed2Approx / 1_000_000.0);
        System.out.printf("  Exact Parameterized (FPT)    : %s%d nodes%s (Exact minimum cover, in %.3f ms)%n%n",
                ConsoleUtil.BRIGHT_GREEN, fpt.vertexCover.size(), ConsoleUtil.RESET, elapsedFpt / 1_000_000.0);

        System.out.println("Key Critical Installations Selected for Rapid Sensor / Defense Coverage:");
        for (int i = 0; i < Math.min(8, fpt.vertexCover.size()); i++) {
            int vId = fpt.vertexCover.get(i);
            InfrastructureVertex iv = (vId < infraRepo.getVertices().size()) ? infraRepo.getVertices().get(vId) : null;
            if (iv != null) {
                System.out.println("  ★ " + iv.toString());
            }
        }
        ConsoleUtil.printSuccess("Infrastructure vertex cover calculated. 100% of network edges protected.");
    }

    /**
     * Searches municipal civil infrastructure codes using Knuth-Morris-Pratt (KMP).
     * Implements Image 2 requirement: "KMP to search citizen reports, service requests, and infrastructure codes."
     */
    public void searchInfrastructureCodes(String codePattern) {
        ConsoleUtil.printSection("KMP Infrastructure Code & Facility Search: \"" + codePattern + "\"");
        if (codePattern == null || codePattern.trim().isEmpty()) {
            ConsoleUtil.printWarning("Search pattern cannot be empty.");
            return;
        }

        long start = System.nanoTime();
        int matchedNodes = 0;
        int matchedEdges = 0;

        DynamicArray<InfrastructureVertex> vertices = infraRepo.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            InfrastructureVertex v = vertices.get(i);
            String code = String.format("INFRA-NODE-%02d %s %s", v.getVertexId(), v.getName(), v.getFacilityType());
            IntArrayList matches = KMP.search(code.toLowerCase(), codePattern.toLowerCase());
            if (!matches.isEmpty()) {
                matchedNodes++;
                System.out.printf("  ✔ [Node Match] %s%s%s (Matched at offset %s)%n",
                        ConsoleUtil.BRIGHT_GREEN, v.toString(), ConsoleUtil.RESET, matches.toString());
            }
        }

        DynamicArray<smartcity.model.InfrastructureEdge> edges = infraRepo.getEdges();
        for (int i = 0; i < edges.size(); i++) {
            smartcity.model.InfrastructureEdge e = edges.get(i);
            String edgeText = String.format("LINK-%02d-%02d %s", e.getU(), e.getV(), e.getConnectionType());
            IntArrayList matches = KMP.search(edgeText.toLowerCase(), codePattern.toLowerCase());
            if (!matches.isEmpty()) {
                matchedEdges++;
                System.out.printf("  ✔ [Link Match] %s%s%s (Matched at offset %s)%n",
                        ConsoleUtil.CYAN, e.toString(), ConsoleUtil.RESET, matches.toString());
            }
        }

        long elapsed = System.nanoTime() - start;
        System.out.println();
        ConsoleUtil.printSuccess(String.format("KMP pattern \"%s\" matched %d facility nodes and %d infrastructure links in %.3f ms.",
                codePattern, matchedNodes, matchedEdges, elapsed / 1_000_000.0));
    }
}
