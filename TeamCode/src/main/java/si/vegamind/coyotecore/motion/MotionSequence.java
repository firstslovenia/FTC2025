package si.vegamind.coyotecore.motion;

import java.util.LinkedList;
import java.util.ListIterator;

public class MotionSequence extends Motion {
    private final LinkedList<Motion> motions;
    ListIterator<Motion> listIterator;

    public MotionSequence(MotionSequenceBuilder builder) {
        super(builder);
        this.motions = builder.motions;
    }

    /**
     * Updates current Motion in this MotionSequence.
     */
    public void motionSpecificUpdate() {
        // If MotionSequence is started
        if (status == MotionStatus.IDLE || status == null) {
            listIterator = motions.listIterator();
        }
        // This should never happen
        else if (listIterator == null) {
            // TODO: ERROR HANDLER WARNING
            System.err.println("ERROR: Something went seriously wrong!");
            return;
        }

        // If the sequence was completed
        if (!listIterator.hasNext()) {
            System.out.println("MotionSequence " + name + " was complete");

            if (onComplete != null) {
                onComplete.run();
            }

            status = MotionStatus.IDLE;
            listIterator = motions.listIterator();
            return;
        }

        Motion motion = listIterator.next();

        // If motion is locked stop the update
        if (motion.getStatus() == MotionStatus.LOCKED) {
            status = MotionStatus.LOCKED;
            return;
        }

        System.out.println("MotionSequence " + name + " updated motion " + motion.getName());

        status = MotionStatus.RUNNING;
        motion.motionSpecificUpdate();
    }
}
