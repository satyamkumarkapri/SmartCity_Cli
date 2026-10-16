package smartcity.model;

/**
 * Represents a zone utility demand request for optimization.
 */
public class ResourceDemand {
    private int demandId;
    private String consumerZone;
    private String resourceType;
    private int requiredQuantity;
    private int maxBudgetUsd;
    private String criticality;

    public ResourceDemand(int demandId, String consumerZone, String resourceType, int requiredQuantity, int maxBudgetUsd, String criticality) {
        this.demandId = demandId;
        this.consumerZone = consumerZone;
        this.resourceType = resourceType;
        this.requiredQuantity = requiredQuantity;
        this.maxBudgetUsd = maxBudgetUsd;
        this.criticality = criticality;
    }

    public int getDemandId() { return demandId; }
    public String getConsumerZone() { return consumerZone; }
    public String getResourceType() { return resourceType; }
    public int getRequiredQuantity() { return requiredQuantity; }
    public int getMaxBudgetUsd() { return maxBudgetUsd; }
    public String getCriticality() { return criticality; }

    @Override
    public String toString() {
        return String.format("[DEM-%02d] Zone: %-18s | %-16s | Need: %3d units | Budget: $%,6d | %s",
                demandId, consumerZone, resourceType, requiredQuantity, maxBudgetUsd, criticality);
    }
}
