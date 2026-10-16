package smartcity.repository;

import smartcity.model.WaterResource;
import smartcity.model.EnergyResource;
import smartcity.model.ResourceDemand;
import smartcity.structures.DynamicArray;

/**
 * Repository for municipal utilities: water plants, energy substations, and demands.
 */
public class ResourceRepository {
    private final DynamicArray<WaterResource> waterResources = new DynamicArray<>();
    private final DynamicArray<EnergyResource> energyResources = new DynamicArray<>();
    private final DynamicArray<ResourceDemand> demands = new DynamicArray<>();

    public void addWater(WaterResource w) { if (w != null) waterResources.add(w); }
    public void addEnergy(EnergyResource e) { if (e != null) energyResources.add(e); }
    public void addDemand(ResourceDemand d) { if (d != null) demands.add(d); }

    public void clear() {
        waterResources.clear();
        energyResources.clear();
        demands.clear();
    }

    public int getWaterCount() { return waterResources.size(); }
    public int getEnergyCount() { return energyResources.size(); }
    public int getDemandCount() { return demands.size(); }

    public DynamicArray<WaterResource> getWaterResources() { return waterResources; }
    public DynamicArray<EnergyResource> getEnergyResources() { return energyResources; }
    public DynamicArray<ResourceDemand> getDemands() { return demands; }
}
