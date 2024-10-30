package si.vegamind.coyotecore.motion;

import java.util.HashMap;
import java.util.HashSet;

public class MotionManager {
    private static final HashMap<String, Motion> motions = new HashMap<>();
    private static final HashSet<Motion> queue = new HashSet<>();
    private static final HashSet<Motion> nextQueue = new HashSet<>();

    public static void addMotion(Motion motion) {
        motions.put(motion.getName(), motion);
    }

    public static Motion getMotion(String name) {
        return motions.get(name);
    }

    public static Motion startMotion(String name) {
        if (!motions.containsKey(name)) {
            // TODO: ERROR HANDLER
            System.err.println("ERROR: " + name + " motion was not found!");
            return null;
        }

        return startMotion(motions.get(name));
    }

    public static Motion startMotion(Motion motion) {
        if (motion == null) {
            // TODO: ERROR HANDLER
            System.err.println("ERROR: Motion null!");
            return null;
        }

        queue.add(motion);
        return motion;
    }

    public static Motion removeMotion(String name) {
        if (!queue.contains(motions.get(name))) {
            System.err.println("ERROR: " + name + " motion was not found!");
            return null;
        }

        queue.remove(motions.get(name));
        return motions.get(name);
    }

    public static Motion removeMotion(Motion motion) {
        if (motion == null) {
            System.err.println("ERROR: Motion null!");
        }

        queue.remove(motion);
        return motion;
    }

    public static void update() {
        while (!queue.isEmpty()) {
            Motion motion = queue.iterator().next();
            motion.update();
            queue.remove(motion);
            if (motion.getStatus() == MotionStatus.RUNNING) {
                nextQueue.add(motion);
            }
        }

        queue.addAll(nextQueue);
        nextQueue.clear();
    }
}