package smartcity.model;

/**
 * Represents an emergency incident requirement to be assigned via Bipartite Matching.
 */
public class AssignmentRequirement {
    private int reqId;
    private int incidentId;
    private String location;
    private String requiredSpecialty;
    private String priority;
    private int[] eligibleTeamIds;

    public AssignmentRequirement(int reqId, int incidentId, String location, String requiredSpecialty, String priority, int[] eligibleTeamIds) {
        this.reqId = reqId;
        this.incidentId = incidentId;
        this.location = location;
        this.requiredSpecialty = requiredSpecialty;
        this.priority = priority;
        this.eligibleTeamIds = eligibleTeamIds;
    }

    public int getReqId() { return reqId; }
    public int getIncidentId() { return incidentId; }
    public String getLocation() { return location; }
    public String getRequiredSpecialty() { return requiredSpecialty; }
    public String getPriority() { return priority; }
    public int[] getEligibleTeamIds() { return eligibleTeamIds; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[Req #%02d] Incident #%d @ %-16s | Need: %-18s | Pri: %-8s | Teams: ",
                reqId, incidentId, location, requiredSpecialty, priority));
        for (int i = 0; i < eligibleTeamIds.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(eligibleTeamIds[i]);
        }
        return sb.toString();
    }
}
