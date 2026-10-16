package smartcity.model;

/**
 * Represents a node in the municipal civil infrastructure dependency graph.
 */
public class InfrastructureVertex {
    private int vertexId;
    private String name;
    private String facilityType;
    private int criticalityScore; // 40 to 99

    public InfrastructureVertex(int vertexId, String name, String facilityType, int criticalityScore) {
        this.vertexId = vertexId;
        this.name = name;
        this.facilityType = facilityType;
        this.criticalityScore = criticalityScore;
    }

    public int getVertexId() { return vertexId; }
    public String getName() { return name; }
    public String getFacilityType() { return facilityType; }
    public int getCriticalityScore() { return criticalityScore; }

    @Override
    public String toString() {
        return String.format("[InfraNode %2d] %-30s (%-18s) | Criticality: %2d",
                vertexId, name, facilityType, criticalityScore);
    }
}
