package smartcity.model;

/**
 * Represents a water treatment plant, reservoir, or pumping facility.
 */
public class WaterResource {
    private int id;
    private String facilityName;
    private String location;
    private int capacityMld; // Megaliters / day
    private int currentLevelPct;
    private String status;
    private double dailyOutputMld;

    public WaterResource(int id, String facilityName, String location, int capacityMld, int currentLevelPct, String status, double dailyOutputMld) {
        this.id = id;
        this.facilityName = facilityName;
        this.location = location;
        this.capacityMld = capacityMld;
        this.currentLevelPct = currentLevelPct;
        this.status = status;
        this.dailyOutputMld = dailyOutputMld;
    }

    public int getId() { return id; }
    public String getFacilityName() { return facilityName; }
    public String getLocation() { return location; }
    public int getCapacityMld() { return capacityMld; }
    public int getCurrentLevelPct() { return currentLevelPct; }
    public String getStatus() { return status; }
    public double getDailyOutputMld() { return dailyOutputMld; }

    @Override
    public String toString() {
        return String.format("[WTR-%02d] %-30s | %-16s | Cap: %3d MLD | Level: %2d%% | Out: %.1f MLD | %s",
                id, facilityName, location, capacityMld, currentLevelPct, dailyOutputMld, status);
    }
}
