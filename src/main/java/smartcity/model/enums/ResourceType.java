package smartcity.model.enums;

public enum ResourceType {
    WATER_MLD,
    ENERGY_MVA,
    EMERGENCY_CREW,
    DESALINATION_SUPPLY,
    ROAD_EQUIPMENT,
    OTHER;

    public static ResourceType fromString(String val) {
        if (val == null) return OTHER;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
