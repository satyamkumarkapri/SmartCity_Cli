package smartcityx.util;

public class ValidationUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static void requireNonEmpty(String str, String message) {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }
}
