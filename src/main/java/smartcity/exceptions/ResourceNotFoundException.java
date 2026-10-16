package smartcity.exceptions;

public class ResourceNotFoundException extends SmartCityException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
