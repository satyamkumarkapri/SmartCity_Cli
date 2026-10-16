package smartcity.model;

/**
 * Represents an electrical grid substation or power plant.
 */
public class EnergyResource {
    private int id;
    private String gridStation;
    private String location;
    private int ratedMva; // Megavolt-amperes
    private int currentLoadPct;
    private String sourceType;
    private double unitCostUsdKwh;

    public EnergyResource(int id, String gridStation, String location, int ratedMva, int currentLoadPct, String sourceType, double unitCostUsdKwh) {
        this.id = id;
        this.gridStation = gridStation;
        this.location = location;
        this.ratedMva = ratedMva;
        this.currentLoadPct = currentLoadPct;
        this.sourceType = sourceType;
        this.unitCostUsdKwh = unitCostUsdKwh;
    }

    public int getId() { return id; }
    public String getGridStation() { return gridStation; }
    public String getLocation() { return location; }
    public int getRatedMva() { return ratedMva; }
    public int getCurrentLoadPct() { return currentLoadPct; }
    public String getSourceType() { return sourceType; }
    public double getUnitCostUsdKwh() { return unitCostUsdKwh; }

    @Override
    public String toString() {
        return String.format("[NRG-%02d] %-26s | %-16s | %4d MVA | Load: %2d%% | Type: %-16s | $%.4f/kWh",
                id, gridStation, location, ratedMva, currentLoadPct, sourceType, unitCostUsdKwh);
    }
}
