package si.vegamind.coyotecore.motion;

/**
 * <pre>
 * IDLE - Process is not updating
 * RUNNING - Process is updating
 * LOCKED - Process cant be updated (waiting for needed components to be free to use)
 * </pre>
 */
public enum MotionStatus {
    IDLE,
    RUNNING,
    LOCKED;
}