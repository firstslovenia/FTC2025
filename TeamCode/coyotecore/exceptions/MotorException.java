package si.vegamind.coyotecore.exceptions;

public class MotorException extends RuntimeException {
    public MotorException() {
        super("Motor Error");
    }

    public MotorException(String message) {
        super(message);
    }
}
