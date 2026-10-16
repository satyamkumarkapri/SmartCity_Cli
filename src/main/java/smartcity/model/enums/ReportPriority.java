package smartcity.model.enums;

public enum ReportPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT;

    public static ReportPriority fromString(String val) {
        if (val == null) return MEDIUM;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEDIUM;
        }
    }
}
