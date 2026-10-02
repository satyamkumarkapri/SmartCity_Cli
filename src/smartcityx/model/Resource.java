package smartcityx.model;

import smartcityx.model.enums.ResourceType;

public class Resource {
    private int id;
    private String name;
    private ResourceType type;
    private double quantity;
    private double cost;
    private String location;
    private boolean available;

    public Resource(int id, String name, ResourceType type, double quantity, double cost, String location, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.cost = cost;
        this.location = location;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ResourceType getType() { return type; }
    public void setType(ResourceType type) { this.type = type; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Resource #" + id + " | " + name + " | Type: " + type + " | Qty: " + quantity + " | Cost: ₹" + cost + " | Avail: " + available;
    }
}
