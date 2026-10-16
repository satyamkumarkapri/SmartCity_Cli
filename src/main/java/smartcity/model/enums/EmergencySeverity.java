package smartcity.model.enums;

public enum EmergencySeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static EmergencySeverity fromString(String val) {
        if (val == null) return MEDIUM;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEDIUM;
        }
    }
}
