package si.vegamind.coyotecore.motion;

import java.util.LinkedList;

public class MotionSequenceBuilder extends MotionBuilder<MotionSequence> {
    public LinkedList<Motion> motions = new LinkedList<>();

    public MotionSequenceBuilder(String name) {
        this.name = name;
    }

    /**
     * Add a Motion to MotionSequence.
     *
     * @param motion Motion to add
     */
    public MotionSequenceBuilder addMotion(Motion motion) {
        motions.add(motion);
        return this;
    }

    /**
     * Build the MotionSequence using MAGIC!
     */
    public MotionSequence build() {
        return new MotionSequence(this);
    }
}
