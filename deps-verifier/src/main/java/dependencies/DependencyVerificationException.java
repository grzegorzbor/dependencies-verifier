package dependencies;

/**
 * Thrown when any violation of dependencies has been discovered
 */
public class DependencyVerificationException extends RuntimeException {

    public DependencyVerificationException(String message) {
        super(message);
    }
}
