package smartcity.model;

/**
 * Represents a directional road or arterial conduit between junctions.
 */
public class TrafficEdge {
    private int source;
    private int target;
    private int capacity; // vehicles / hour
    private int freeFlowTime; // minutes
    private double lengthKm;

    public TrafficEdge(int source, int target, int capacity, int freeFlowTime, double lengthKm) {
        this.source = source;
        this.target = target;
        this.capacity = capacity;
        this.freeFlowTime = freeFlowTime;
        this.lengthKm = lengthKm;
    }

    public int getSource() { return source; }
    public int getTarget() { return target; }
    public int getCapacity() { return capacity; }
    public int getFreeFlowTime() { return freeFlowTime; }
    public double getLengthKm() { return lengthKm; }

    @Override
    public String toString() {
        return String.format("[%2d] -> [%2d] | Cap: %,4d veh/hr | Time: %2d min | Length: %.1f km",
                source, target, capacity, freeFlowTime, lengthKm);
    }
}
