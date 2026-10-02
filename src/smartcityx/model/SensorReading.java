package smartcityx.model;

import smartcityx.model.enums.SensorType;
import java.time.LocalDateTime;

public class SensorReading {
    private long id;
    private String sensorId;
    private String location;
    private SensorType type;
    private double value;
    private LocalDateTime timestamp;

    public SensorReading(long id, String sensorId, String location, SensorType type, double value, LocalDateTime timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.location = location;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public SensorType getType() { return type; }
    public void setType(SensorType type) { this.type = type; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Reading [" + timestamp + "] " + sensorId + " (" + type + ") @ " + location + " = " + String.format("%.2f", value);
    }
}
