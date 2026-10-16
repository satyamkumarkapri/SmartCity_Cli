package smartcity.model.enums;

public enum InfrastructureStatus {
    OPERATIONAL,
    UNDER_MAINTENANCE,
    DAMAGED,
    CRITICAL;

    public static InfrastructureStatus fromString(String val) {
        if (val == null) return OPERATIONAL;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OPERATIONAL;
        }
    }
}
