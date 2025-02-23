package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.AutoTransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.SpecimenSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.TransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

import java.util.Queue;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(name = "FTCAuto", group = "drive")
public class FTCAuto extends LinearOpMode {

    private static final boolean isRed = false;
    private static final boolean isBasketBot = false;

    // Positions -----------------------------------------------------------------------------------
    private static final Pose2d blockDropPose = getFieldPose2d(new Pose2d(52, 52, getRad(225)));
    private static final Pose2d blockPickupPose = getFieldPose2d(new Pose2d(48, 42, getRad(270)));
    private static final Pose2d rungStartingPose = getFieldPose2d(new Pose2d(10, 35, getRad(90)));

    // Offsets -------------------------------------------------------------------------------------
    private static final double blockOnFloorOffset = 10.5;
    private static final double rungPosOffsetX = -4; // TODO: Check me!

    // =============================================================================================
    // Color Conversion ============================================================================
    // =============================================================================================

    /**
     * Get radians depending on the robot color
     * @param deg
     * @return Radians (flipped by 180 if RED)
     */
    public static double getRad(double deg) {
        return Math.toRadians(deg + (isRed ? 180 : 0));
    }

    /**
     * Get local flipped pose
     * @return Locally flipped Pose2d
     */
    public static Pose2d getAbsPose2d(Pose2d pose2d) {
        if (isRed) return pose2d;
        return new Pose2d(-pose2d.getX(), -pose2d.getY(), pose2d.getHeading());
    }

    /**
     * Get pose of robot depending on the color
     * @return Pose2d on the field (flipped around field center)
     */
    public static Pose2d getFieldPose2d(Pose2d pose2d) {
        if (!isBasketBot) {
            pose2d = new Pose2d(-pose2d.getX(), pose2d.getY(), pose2d.getHeading());
        }

        if (!isRed) return pose2d;
        return pose2d.plus(new Pose2d(
                -2 * pose2d.getX(),
                -2 * pose2d.getY() // + 2
        ));
    }

    /**
     * Get vector of robot depending on the color
     * @return Vector2d on the field (flipped around field center)
     */
    public static Vector2d getFieldVector2d(Vector2d vector2d) {
        if (!isBasketBot) {
            vector2d = new Vector2d(-vector2d.getX(), vector2d.getY());
        }

        if (!isRed) return vector2d;
        return vector2d.plus(new Vector2d(
                -2 * vector2d.getX(),
                -2 * vector2d.getY() // + 2
        ));
    }

    // =============================================================================================
    // Robot =======================================================================================
    // =============================================================================================
    public static void blockRungDrop() {
        System.out.println("Block Lift");
    }

    public static void blockBasketDrop() {
        System.out.println("Block Lift");
    }

    public static void blockPickup() {
        System.out.println("Block Pickup");
    }

    // =============================================================================================
    // MeepMeep ====================================================================================
    // =============================================================================================

    private static Pose2d getRungPos(int rungOffsetIdx) {
        return new Pose2d(getAbsPose2d(new Pose2d(rungPosOffsetX)).getX() * rungOffsetIdx).plus(rungStartingPose);
    }

    private static Pose2d getBlockOnFloorPos(int blockIdx) {
        return new Pose2d(getAbsPose2d(new Pose2d(blockOnFloorOffset)).getX() * blockIdx).plus(blockPickupPose);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        boolean reverseRobot;
        double turnAngle;

        Hardware.init(hardwareMap, false);

        VerticalLifter verticalLift = new VerticalLifter();
        HorizontalLifter horizontalLift = new HorizontalLifter();

        AutoTransferSequence autoTransferSequence = new AutoTransferSequence(horizontalLift, verticalLift);

        if (!isRed) {
            reverseRobot = true;
            turnAngle = 180;
        } else {
            turnAngle = 0;
            reverseRobot = false;
        }


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        TrajectorySequence myBotBasket = drive.trajectorySequenceBuilder(new Pose2d(20, 61.5, getRad(0)))
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                // Block on bar ------------------------------------------------------------

                .addDisplacementMarker(() -> {
                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(95));
                })

                .lineToConstantHeading(getRungPos(0).vec())

                .addDisplacementMarker(() -> {
                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(82));
                    verticalLift.getSpecimenClaw().setPosition(0.5); //TODO a delay???
                })

                .waitSeconds(1)

                // Block No. 1 -------------------------------------------------------------

                .addDisplacementMarker(() -> {
                    horizontalLift.setAutoHomePos((int)HorizontalLifter.cmToSteps(20 /*TODO tune*/));
                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(40));
                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(40));
                })

                .lineToConstantHeading(getRungPos(0).vec().plus(getFieldPose2d(new Pose2d(0, 5)).vec()))
                .splineToConstantHeading(getBlockOnFloorPos(0).vec(), getRad(0))


                .turn(Math.toRadians(180))

                .addDisplacementMarker(() -> {
                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(0));
                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(0));
                    horizontalLift.getClaw().setPosition(0);
                })


                .waitSeconds(0.5)

                .addDisplacementMarker(() -> {
                    autoTransferSequence.start();
                })

                .lineToConstantHeading(blockDropPose.vec())

                .turn(Math.toRadians(-45))


                .waitSeconds(5)

                .waitSeconds(1)

                .turn(Math.toRadians(45))

                // Block No. 2 -------------------------------------------------------------

                .addDisplacementMarker(() -> {
                    horizontalLift.setAutoHomePos((int)HorizontalLifter.cmToSteps(20 /*TODO tune*/));
                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(40));
                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(40));
                })

                .lineToConstantHeading(getFieldVector2d(new Vector2d(58, 40)))

                .addDisplacementMarker(() -> {
                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(0));
                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(0));
                    horizontalLift.getClaw().setPosition(0);
                })

                .waitSeconds(0.5)

                .addDisplacementMarker(() -> {
                    autoTransferSequence.start();
                })

                .lineToConstantHeading(blockDropPose.vec())

                .waitSeconds(5)

                .turn(Math.toRadians(-45))

                .addDisplacementMarker(() -> {
                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(130));
                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(130));
                    horizontalLift.setHomingSequenceActive(true);
                    verticalLift.setHomingSequenceActive(true);
                })

                // Block No. 3 -------------------------------------------------------------
                .lineToConstantHeading(getFieldVector2d(new Vector2d(-55, 52)))

                .build();

        // =========================================================================================
        // Bot for block pushing to zones ==========================================================


        TrajectorySequence myBotZone = drive.trajectorySequenceBuilder(new Pose2d(20, 61.5, getRad(0)))
                //            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setReversed(reverseRobot)

                .forward(100)

//                .back(100)
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(95));
//                })
//
//            //    .lineToConstantHeading(getRungPos(0).vec())
//
//                .waitSeconds(6)
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(44));
//                    verticalLift.getSpecimenClaw().setPosition(0.5); //TODO a delay???
//                })
//
//                .waitSeconds(1.5)

                // BLOCK #1 ------------------------

//                .addDisplacementMarker(() -> {
//                    horizontalLift.setAutoHomePos((int)HorizontalLifter.cmToSteps(20 /*TODO tune*/));
//                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(40));
//                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(40));
//                    horizontalLift.getClaw().setPosition(0);
//                })
//
//                .splineToConstantHeading(
//                        getBlockOnFloorPos(0).plus(getFieldPose2d(new Pose2d(0, 0, getRad(90)))).vec(),
//                        getRad(90)
//                )
//
//                .turn(getRad(180))
//
//                .addDisplacementMarker(() -> {
//                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(0));
//                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(0));
//                })
//
//                .waitSeconds(0.3)
//
//                .addDisplacementMarker(() -> {
//                    horizontalLift.getSwivelServoLeft().setPosition(HorizontalLifter.degToServoPosSwivel(40));
//                    horizontalLift.getSwivelServoRight().setPosition(HorizontalLifter.degToServoPosSwivel(40));
//                })
//
//                .splineToConstantHeading(
//                        FTCAuto.blockDropPose.plus(getFieldPose2d(new Pose2d(0, 0, getRad(0)))).vec(),
//                        getRad(180)
//                )
//
//                .turn(getRad(turnAngle))
//
//
//                .waitSeconds(0.3)
//
//                .addDisplacementMarker(() -> {
//                    horizontalLift.getClaw().setPosition(1);
//                })
//
//                .waitSeconds(0.5)
//
//                .turn(getRad(turnAngle))
//
//                .lineToConstantHeading(
//                        FTCAuto.blockDropPose.plus(getFieldPose2d(new Pose2d(0, -5, getRad(0)))).vec()
//                )
//
//                .waitSeconds(3)
//
//
//                // RUNG BLOCK #1 ----------
//
//                .lineToConstantHeading(
//                        FTCAuto.blockDropPose.plus(getFieldPose2d(new Pose2d(8, 8, getRad(0)))).vec()
//                )
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.getSpecimenClaw().setPosition(0.5);
//                })
//
//                .waitSeconds(1)
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.getSpecimenClaw().setPosition(1);
//                })
//
//                .lineToConstantHeading(
//                        FTCAuto.rungStartingPose.vec()
//                )
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(95));
//                })
//
//                .turn(Math.toRadians(180))
//
//                .addDisplacementMarker(() -> {
//                    verticalLift.setAutoHomePos((int)VerticalLifter.heightToSteps(44));
//                    verticalLift.getSpecimenClaw().setPosition(0.5); //TODO a delay???
//                })
//
//                .waitSeconds(1)
//
//
//
//                .lineToConstantHeading(blockDropPose.vec())
//
                .build();

                waitForStart();

                while (!isStopRequested()) {
                    drive.followTrajectorySequence(isBasketBot ? myBotBasket : myBotZone);


                    telemetry.addData("efwef", verticalLift.getLiftLeft().getCurrentPosition());
                    telemetry.update();
                }
    }
}