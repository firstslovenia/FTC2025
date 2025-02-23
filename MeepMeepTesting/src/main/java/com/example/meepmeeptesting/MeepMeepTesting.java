package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    // =============================================================================================
    // Constants ===================================================================================
    // =============================================================================================

    // Bot Params ----------------------------------------------------------------------------------
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
    // MainShit ====================================================================================
    // =============================================================================================
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        System.out.println(
                getBlockOnFloorPos(1).getX()
        );

        System.out.println(
                getBlockOnFloorPos(1).getY()
        );

        // =========================================================================================
        // Basket Side =============================================================================
        // =========================================================================================
        RoadRunnerBotEntity myBotBasket = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, 3.82, Math.toRadians(198.135), 13.65)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getFieldPose2d(new Pose2d(20, 61.5, getRad(270))))
                        .setReversed(isRed)

                        // Block on bar ------------------------------------------------------------
                        .lineToConstantHeading(getRungPos(0).vec())
                        .addDisplacementMarker(MeepMeepTesting::blockRungDrop)

                        // Block No. 1 -------------------------------------------------------------
                        .lineToConstantHeading(getRungPos(0).vec().plus(getFieldPose2d(new Pose2d(0, 5)).vec()))
                        .splineToConstantHeading(getBlockOnFloorPos(0).vec(), getRad(0))
                        .addDisplacementMarker(MeepMeepTesting::blockPickup)

                        .turn(Math.toRadians(180))

                        .lineToConstantHeading(blockDropPose.vec())
                        .addDisplacementMarker(MeepMeepTesting::blockBasketDrop)

                        .turn(Math.toRadians(-45))

                        .waitSeconds(1)

                        .turn(Math.toRadians(45))

                        // Block No. 2 -------------------------------------------------------------
                        .lineToConstantHeading(getFieldVector2d(new Vector2d(58, 40)))
                        .addDisplacementMarker(MeepMeepTesting::blockPickup)

                        .lineToConstantHeading(blockDropPose.vec())
                        .addDisplacementMarker(MeepMeepTesting::blockBasketDrop)


                        .waitSeconds(1)
                        .turn(Math.toRadians(-45))

                        // Block No. 3 -------------------------------------------------------------
                        .lineToConstantHeading(getFieldVector2d(new Vector2d(-55, 52)))

                        .build());

        // =========================================================================================
        // Bot for block pushing to zones ==========================================================

        boolean reverseRobot;
        double turnAngle;
        if (!isRed) {
            reverseRobot = true;
            turnAngle = 180;
        } else {
            turnAngle = 0;
            reverseRobot = false;
        }

        RoadRunnerBotEntity myBotZone = new DefaultBotBuilder(meepMeep)
    //            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(39.4224324932042, 39.4224324932042, 3.82, Math.toRadians(198.135), 13.65)
            .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getFieldPose2d(new Pose2d(20, 61.5, getRad(270))))
                    .setReversed(reverseRobot)

                    .lineToConstantHeading(
                            MeepMeepTesting.rungStartingPose.vec()
                    )

                    .waitSeconds(1.5)

//                    // BLOCK #1 ------------------------
//                    .splineToConstantHeading(
//                            getBlockOnFloorPos(0).plus(getFieldPose2d(new Pose2d(0, 0, getRad(90)))).vec(),
//                            getRad(90)
//                    )
//
//                    .turn(getRad(180))
//
//                    .waitSeconds(2)
//
//                    .splineToConstantHeading(
//                            MeepMeepTesting.blockDropPose.plus(getFieldPose2d(new Pose2d(0, 0, getRad(0)))).vec(),
//                            getRad(180)
//                    )
//
//                    .turn(getRad(turnAngle))
//
//                    .waitSeconds(1)
//
//                    .turn(getRad(turnAngle))
//
//                    .lineToConstantHeading(
//                            MeepMeepTesting.blockDropPose.plus(getFieldPose2d(new Pose2d(0, -5, getRad(0)))).vec()
//                    )
//
//                    .waitSeconds(3)
//
//                    // BLOCK #2 ------------------------
///*                    .splineToConstantHeading(
//                            getBlockOnFloorPos(1).plus(getFieldPose2d(new Pose2d(-22.5, 10,getRad(0)))).vec(),
//                            getRad(270)
//                    )
//
//                    .splineToConstantHeading(
//                            getBlockOnFloorPos(1).plus(getFieldPose2d(new Pose2d(-10, -30, 0))).vec(),
//                            getRad(180)
//                    )
//
//                    .splineToConstantHeading(
//                            getBlockOnFloorPos(1).plus(getFieldPose2d(new Pose2d(-12, -30, getRad(0)))).vec(),
//                            getRad(180)
//                    )
//
//                    .splineToConstantHeading(
//                            MeepMeepTesting.blockDropPose.plus(getFieldPose2d(new Pose2d(10, 0, getRad(0)))).vec(),
//                            getRad(180)
//                    )*/
//
//
//                    // RUNG BLOCK #1 ----------
//
//                    .lineToConstantHeading(
//                            MeepMeepTesting.blockDropPose.plus(getFieldPose2d(new Pose2d(8,8, getRad(0)))).vec()
//                    )
//
//                    .lineToConstantHeading(
//                            MeepMeepTesting.blockDropPose.plus(getFieldPose2d(new Pose2d(5,5, getRad(0)))).vec()
//                    )
//
//                    .turn(Math.toRadians(180))
//
//                    .waitSeconds(1)
//
//                    .lineToConstantHeading(
//                            MeepMeepTesting.rungStartingPose.vec()
//                    )
//
//                    .waitSeconds(1)
//
//                    .lineToConstantHeading(blockDropPose.vec())
//
            .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(isBasketBot ? myBotBasket : myBotZone)
                .start();
    }
}