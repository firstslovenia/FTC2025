package si.vegamind.coyotecore.motion;

public abstract class MotionBuilder<T extends Motion> {
    protected String name;
    protected Runnable onComplete;
    protected Returnable<Boolean> exitCondition;

    public MotionBuilder<T> setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public MotionBuilder<T> setExitCondition(Returnable<Boolean> exitCondition) {
        this.exitCondition = exitCondition;
        return this;
    }

    public abstract T build();
}