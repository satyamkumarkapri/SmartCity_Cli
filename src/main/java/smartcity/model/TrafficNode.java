package smartcity.model;

/**
 * Represents a traffic intersection or junction node.
 */
public class TrafficNode {
    private int nodeId;
    private String name;
    private String zone;
    private double latitude;
    private double longitude;

    public TrafficNode(int nodeId, String name, String zone, double latitude, double longitude) {
        this.nodeId = nodeId;
        this.name = name;
        this.zone = zone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getNodeId() { return nodeId; }
    public String getName() { return name; }
    public String getZone() { return zone; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return String.format("Junction [%2d]: %-28s (%s) @ (%.4f, %.4f)", nodeId, name, zone, latitude, longitude);
    }
}
