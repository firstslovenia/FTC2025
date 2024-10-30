package si.vegamind.coyotecore.motion;

import java.util.LinkedList;
import java.util.ListIterator;

public abstract class SingleMotion extends Motion {
    protected final LinkedList<MotorMove> motorMoves;
    protected ListIterator<MotorMove> listIterator;

    public SingleMotion(SingleContinuousMotionBuilder builder) {
        super(builder);
        this.motorMoves = builder.motorMoves;
    }

    /**
     * Returns current MotionStatus. If Motion can't be updated LOCKED status will be returned.
     *
     * @return Status as MotionStatus enum
     */
    @Override public MotionStatus getStatus() {
        // TODO: For testing until operations are implemented
        if (status == MotionStatus.RUNNING){
            status = MotionStatus.IDLE;
        }

        if (status == MotionStatus.LOCKED) {
            // TODO: ERROR HANDLER WARNING
            System.out.println("WARNING: Motion " + name + " is locked");
            return MotionStatus.LOCKED;
        }

        return status;
    }

    /**
     * Update Operations in this Motion
     */
    public abstract void motionSpecificUpdate();
}