package smartcity.model;

/**
 * Represents a specialized emergency response squad or rescue crew.
 */
public class EmergencyTeam {
    private int teamId;
    private String teamName;
    private String baseStation;
    private String specialty;
    private int crewCount;
    private String status;

    public EmergencyTeam(int teamId, String teamName, String baseStation, String specialty, int crewCount, String status) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.baseStation = baseStation;
        this.specialty = specialty;
        this.crewCount = crewCount;
        this.status = status;
    }

    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public String getBaseStation() { return baseStation; }
    public String getSpecialty() { return specialty; }
    public int getCrewCount() { return crewCount; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[Squad %02d] %-32s | Station: %-16s | Crew: %2d | %s",
                teamId, teamName, baseStation, crewCount, status);
    }
}
