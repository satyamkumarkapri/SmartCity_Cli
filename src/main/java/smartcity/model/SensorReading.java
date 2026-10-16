package smartcity.model;

/**
 * Represents a discrete environmental or structural IoT telemetry reading.
 */
public class SensorReading {
    private int readingId;
    private String sensorId;
    private String sensorType;
    private double value;
    private String unit;
    private String location;
    private String timestamp;
    private String status;

    public SensorReading(int readingId, String sensorId, String sensorType, double value, String unit,
                         String location, String timestamp, String status) {
        this.readingId = readingId;
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.value = value;
        this.unit = unit;
        this.location = location;
        this.timestamp = timestamp;
        this.status = status;
    }

    public int getReadingId() { return readingId; }
    public String getSensorId() { return sensorId; }
    public String getSensorType() { return sensorType; }
    public double getValue() { return value; }
    public String getUnit() { return unit; }
    public String getLocation() { return location; }
    public String getTimestamp() { return timestamp; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("[#%04d] %-15s | %-20s : %7.2f %-7s | %-16s | %-7s | %s",
                readingId, sensorId, sensorType, value, unit, location, status, timestamp);
    }
}
