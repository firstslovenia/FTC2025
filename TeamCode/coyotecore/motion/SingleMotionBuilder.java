package si.vegamind.coyotecore.motion;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class SingleMotionBuilder<T extends SingleMotion> extends MotionBuilder<Motion> {
    public LinkedList<MotorMove> motorMoves = new LinkedList<>();

    public SingleMotionBuilder(String name) {
        this.name = name;
    }

    public SingleMotionBuilder<T> addMotorMove(MotorMove motorMove) {
        motorMoves.add(motorMove);
        return this;
    }
}
