package si.vegamind.coyotecore.motion;

import lombok.Getter;

public abstract class Motion {
    @Getter
    protected final String name;
    protected final Runnable onComplete;
    protected final Returnable<Boolean> exitCondition;

    @Getter
    protected MotionStatus status;

    public void update() {
        if (status == MotionStatus.LOCKED) return;

        if (exitCondition != null && exitCondition.run()) {
            status = MotionStatus.IDLE;
            onComplete.run();
            return;
        }

        status = MotionStatus.RUNNING;
        motionSpecificUpdate();
    }

    protected abstract void motionSpecificUpdate();

    public Motion(MotionBuilder<?> builder) {
        this.name = builder.name;
        this.onComplete = builder.onComplete;
        this.exitCondition = builder.exitCondition;
    }
}