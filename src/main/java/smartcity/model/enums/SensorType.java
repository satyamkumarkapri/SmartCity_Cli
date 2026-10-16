package smartcity.model.enums;

public enum SensorType {
    TEMPERATURE,
    HUMIDITY,
    AIR_QUALITY_AQI,
    TRAFFIC_FLOW,
    WATER_PRESSURE,
    ENERGY_GRID_LOAD,
    NOISE_LEVEL,
    STRUCTURAL_VIBRATION,
    OTHER;

    public static SensorType fromString(String val) {
        if (val == null) return OTHER;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
