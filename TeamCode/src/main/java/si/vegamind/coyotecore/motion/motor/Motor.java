package si.vegamind.coyotecore.motion.motor;

import lombok.Getter;
import si.vegamind.coyotecore.motion.MotionStatus;


public abstract class Motor {
    @Getter
    private final String name;
    MotionStatus status;

    public Motor(String name) {
        this.name = name;
    }

    public MotionStatus getStatus() {
        return status;
    }

    public abstract void run(double power);
}
