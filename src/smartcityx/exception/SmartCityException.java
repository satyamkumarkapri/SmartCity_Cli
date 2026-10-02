package smartcityx.exception;

public class SmartCityException extends RuntimeException {
    public SmartCityException(String message) {
        super(message);
    }

    public SmartCityException(String message, Throwable cause) {
        super(message, cause);
    }
}
