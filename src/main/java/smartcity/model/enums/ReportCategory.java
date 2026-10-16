package smartcity.model.enums;

public enum ReportCategory {
    POTHOLE,
    STREETLIGHT,
    WATER_LEAK,
    WASTE,
    TRAFFIC_SIGNAL,
    NOISE,
    INFRASTRUCTURE,
    OTHER;

    public static ReportCategory fromString(String val) {
        if (val == null) return OTHER;
        try {
            return valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
