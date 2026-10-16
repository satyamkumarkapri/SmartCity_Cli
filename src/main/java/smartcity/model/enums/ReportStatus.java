package smartcity.model.enums;

public enum ReportStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    public static ReportStatus fromString(String val) {
        if (val == null) return OPEN;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OPEN;
        }
    }
}
