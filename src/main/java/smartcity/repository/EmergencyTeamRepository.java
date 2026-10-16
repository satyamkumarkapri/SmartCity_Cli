package smartcity.repository;

import smartcity.model.EmergencyTeam;
import smartcity.model.AssignmentRequirement;
import smartcity.structures.DynamicArray;

/**
 * Repository for emergency rescue teams and incident assignment requirements.
 */
public class EmergencyTeamRepository {
    private final DynamicArray<EmergencyTeam> teams = new DynamicArray<>();
    private final DynamicArray<AssignmentRequirement> requirements = new DynamicArray<>();

    public void addTeam(EmergencyTeam team) { if (team != null) teams.add(team); }
    public void addRequirement(AssignmentRequirement req) { if (req != null) requirements.add(req); }

    public void clear() {
        teams.clear();
        requirements.clear();
    }

    public int getTeamCount() { return teams.size(); }
    public int getRequirementCount() { return requirements.size(); }

    public DynamicArray<EmergencyTeam> getTeams() { return teams; }
    public DynamicArray<AssignmentRequirement> getRequirements() { return requirements; }

    public EmergencyTeam getTeam(int index) {
        return (index >= 0 && index < teams.size()) ? teams.get(index) : null;
    }

    public AssignmentRequirement getRequirement(int index) {
        return (index >= 0 && index < requirements.size()) ? requirements.get(index) : null;
    }

    /**
     * Builds bipartite adjacency matrix: graph[u][v] = true if team u is eligible for requirement v.
     */
    public boolean[][] buildBipartiteGraph() {
        int tCount = teams.size();
        int rCount = requirements.size();
        boolean[][] graph = new boolean[tCount][rCount];

        for (int r = 0; r < rCount; r++) {
            AssignmentRequirement req = requirements.get(r);
            int[] eligible = req.getEligibleTeamIds();
            for (int teamId : eligible) {
                // Find 0-indexed team index
                for (int t = 0; t < tCount; t++) {
                    if (teams.get(t).getTeamId() == teamId) {
                        graph[t][r] = true;
                        break;
                    }
                }
            }
        }
        return graph;
    }
}
