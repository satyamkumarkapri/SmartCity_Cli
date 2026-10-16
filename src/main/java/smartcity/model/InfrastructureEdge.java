package smartcity.model;

/**
 * Represents a physical or operational link between infrastructure assets.
 */
public class InfrastructureEdge {
    private int u;
    private int v;
    private String connectionType;
    private double distanceKm;
    private int bandwidthGbps;

    public InfrastructureEdge(int u, int v, String connectionType, double distanceKm, int bandwidthGbps) {
        this.u = u;
        this.v = v;
        this.connectionType = connectionType;
        this.distanceKm = distanceKm;
        this.bandwidthGbps = bandwidthGbps;
    }

    public int getU() { return u; }
    public int getV() { return v; }
    public String getConnectionType() { return connectionType; }
    public double getDistanceKm() { return distanceKm; }
    public int getBandwidthGbps() { return bandwidthGbps; }

    @Override
    public String toString() {
        return String.format("[%2d] <--> [%2d] | %-22s | %.1f km | %d Gbps",
                u, v, connectionType, distanceKm, bandwidthGbps);
    }
}
