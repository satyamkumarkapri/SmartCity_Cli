package smartcityx.model;

import smartcityx.model.enums.InfrastructureStatus;

public class Infrastructure {
    private int id;
    private String name;
    private String type;
    private String location;
    private InfrastructureStatus status;
    private double capacity;

    public Infrastructure(int id, String name, String type, String location, InfrastructureStatus status, double capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.status = status;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public InfrastructureStatus getStatus() { return status; }
    public void setStatus(InfrastructureStatus status) { this.status = status; }
    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Infra #" + id + " | " + name + " (" + type + ") @ " + location + " | Status: " + status;
    }
}
