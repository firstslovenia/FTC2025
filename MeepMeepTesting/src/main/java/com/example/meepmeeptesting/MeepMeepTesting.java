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
    private static final boolean isBasketBot = true;

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
        if (!isRed) return pose2d;
        return new Pose2d(-pose2d.getX(), -pose2d.getY());
    }

    /**
     * Get pose of robot depending on the color
     * @return Pose2d on the field (flipped around field center)
     */
    public static Pose2d getFieldPose2d(Pose2d pose2d) {
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

    // =============================================================================================
    // MainShit ====================================================================================
    // =============================================================================================
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        // =========================================================================================
        // Basket Side =============================================================================
        // =========================================================================================
        RoadRunnerBotEntity myBotBasket = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, Math.toRadians(188.22825), Math.toRadians(188.22825), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getFieldPose2d(new Pose2d(20, 61.5, getRad(90))))
                        // Block on bar ------------------------------------------------------------
                        .lineToSplineHeading(getRungPos(0))
                        .addDisplacementMarker(MeepMeepTesting::blockRungDrop)

                        // Block No. 1 -------------------------------------------------------------
                        .lineToSplineHeading(getRungPos(0).plus(getAbsPose2d(new Pose2d(10, 5))))
                        .splineToSplineHeading(getBlockOnFloorPos(0), getRad(0))
                        .addDisplacementMarker(MeepMeepTesting::blockPickup)

                        .setReversed(true)
                        .lineToSplineHeading(blockDropPose)
                        .addDisplacementMarker(MeepMeepTesting::blockBasketDrop)

                        // Block No. 2 -------------------------------------------------------------
                        .setReversed(true)
                        .lineToSplineHeading(getBlockOnFloorPos(1))
                        .addDisplacementMarker(MeepMeepTesting::blockPickup)

                        .setReversed(true)
                        .lineToSplineHeading(blockDropPose)
                        .addDisplacementMarker(MeepMeepTesting::blockBasketDrop)

                        // Block No. 3 -------------------------------------------------------------
                        .lineToSplineHeading(getFieldPose2d(new Pose2d(52, 30, getRad(180))))
                        .splineToConstantHeading(getFieldVector2d(new Vector2d(60.5, 15)), getRad(90))
                        .splineToConstantHeading(getFieldVector2d(new Vector2d(60.5, 52)), getRad(90))

                        .build());

        // =========================================================================================
        // Bot for block pushing to zones ==========================================================
        RoadRunnerBotEntity myBotZone = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, Math.toRadians(188.22825), Math.toRadians(188.22825), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getFieldPose2d(new Pose2d(-20, 61.5, getRad(90))))

                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(isBasketBot ? myBotBasket : myBotZone)
                .start();
    }
}