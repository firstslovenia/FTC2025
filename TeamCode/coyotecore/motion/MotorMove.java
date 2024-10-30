package si.vegamind.coyotecore.motion;

import lombok.Getter;
import si.vegamind.coyotecore.motion.motor.Motor;

public class MotorMove extends Motion {
    private float power;
    private final Motor motor;

    @Getter
    private final String motorName;

    public MotorMove(MotorMoveBuilder builder) {
        super(builder);
        power = builder.motorPower;
        motorName = builder.motorName;
        motor = MotorManager.getMotor(motorName);
    }

    public void motionSpecificUpdate() {
        motor.run(power);
    }
}
