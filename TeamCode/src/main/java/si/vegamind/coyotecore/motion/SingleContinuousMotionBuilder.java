package si.vegamind.coyotecore.motion;

public class SingleContinuousMotionBuilder extends SingleMotionBuilder<SingleContinuousMotion> {
    public SingleContinuousMotionBuilder(String name) {
        super(name);
        this.name = name;
    }

    public SingleContinuousMotionBuilder addMotorMove(MotorMove motorMove) {
        motorMoves.add(motorMove);
        return this;
    }

    public SingleContinuousMotion build() {
        return new SingleContinuousMotion(this);
    }
}
