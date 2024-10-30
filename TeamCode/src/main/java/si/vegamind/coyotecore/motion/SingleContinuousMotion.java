package si.vegamind.coyotecore.motion;

public class SingleContinuousMotion extends SingleMotion {
    public SingleContinuousMotion(SingleContinuousMotionBuilder builder) {
        super(builder);
    }

    public void motionSpecificUpdate() {
        if (getStatus() == MotionStatus.LOCKED) {
            return;
        }

        // If MotionSequence is started
        if (status == MotionStatus.IDLE || status == null) {
            listIterator = motorMoves.listIterator();
        }
        // This should never happen
        else if (listIterator == null) {
            // TODO: ERROR HANDLER WARNING
            System.err.println("ERROR: Something went seriously wrong!");
            return;
        }

        if (!listIterator.hasNext()) {
            System.out.println("ContinuousMotion " + name + " was complete");

            if (onComplete != null) {
                onComplete.run();
            }

            status = MotionStatus.IDLE;
            listIterator = motorMoves.listIterator();
            return;
        }

        MotorMove motorMove = listIterator.next();

        if(motorMove.getStatus() == MotionStatus.LOCKED){
            status = MotionStatus.LOCKED;
            return;
        }

        System.out.println("ContinuousMotion " + name + " updated motor move " + motorMove.getName());

        status = MotionStatus.RUNNING;
        motorMove.motionSpecificUpdate();

        System.out.println("\tUpdated " + name + " successfully");
    }
}
