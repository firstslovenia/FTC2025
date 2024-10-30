package si.vegamind.coyotecore.motion;

public class MotorMoveBuilder extends MotionBuilder<MotorMove> {
    public final float motorPower;
    public final String motorName;

    public MotorMoveBuilder(String name, String motorName, float motorPower) {
        this.name = name;
        this.motorName = motorName;
        this.motorPower = motorPower;
    }

    public MotorMove build() {
        return new MotorMove(this);
    }
}
