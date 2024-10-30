package si.vegamind.coyotecore;

import org.slf4j.Logger;
import si.vegamind.coyotecore.logging.CoyoteLoggerFactory;
import si.vegamind.coyotecore.motion.*;
import si.vegamind.coyotecore.motion.motor.LogMotor;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final Logger logger = CoyoteLoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        MotorManager.addMotor(new LogMotor("drive-motor-left"));
        MotorManager.addMotor(new LogMotor("drive-motor-right"));
        MotorManager.addMotor(new LogMotor("lifter-motor"));

        MotionManager.addMotion(
                new SingleContinuousMotionBuilder("drive forward motion")
                        .addMotorMove(new MotorMoveBuilder("left-motor-forward", "drive-motor-left", 1).build())
                        .addMotorMove(new MotorMoveBuilder("right-motor-forward", "drive-motor-right", 1).build())
                        .setOnComplete(() -> {
                        })
                        .build()
        );

        AtomicBoolean button = new AtomicBoolean(false);

        MotionManager.addMotion(
                new MotorMoveBuilder("lifter up", "lifter-motor", 1)
                        .setOnComplete(() -> {
                            MotionManager.startMotion("lifter up");
                            MotionManager.startMotion("drive forward motion");
                        })
                        .setExitCondition(() -> {
                            System.out.println("Button status: " + button.get());
                            return button.get();
                        })
                        .build()
        );

        MotionManager.startMotion("lifter up");

        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                button.set(true);
            }
            System.out.println("\n------------- UPDATE -------------");
            MotionManager.update();
        }
    }
}