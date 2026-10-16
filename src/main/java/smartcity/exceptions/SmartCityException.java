package smartcity.exceptions;

public class SmartCityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SmartCityException(String message) {
        super(message);
    }

    public SmartCityException(String message, Throwable cause) {
        super(message, cause);
    }
}
