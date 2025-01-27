package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.core.colorscheme.ColorScheme;
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

public class MeepMeepTesting {
    // Bot Params
    private static final boolean isRed = true;
    private static final boolean isBasketBot = false;

    // Flip angle if red
    public static double getRad(double deg) {
        return Math.toRadians(deg + (isRed ? 180 : 0));
    }

    // Flip pose2d if red
    public static Pose2d getPose2d(Pose2d pose2d) {
        if (!isRed) return pose2d;
        return pose2d.plus(new Pose2d(
                -2 * pose2d.getX(),
                -2 * pose2d.getY() + 2
        ));
    }

    // Flip vector2d if red
    public static Vector2d getVector2d(Vector2d vector2d) {
        if (!isRed) return vector2d;
        return vector2d.plus(new Vector2d(
                -2 * vector2d.getX(),
                -2 * vector2d.getY() + 2
        ));
    }

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        // Positions
        final Pose2d blockDropPose2d = new Pose2d(52, 52, getRad(225));
        final Pose2d blockPickupPose2d = new Pose2d(37, 40, getRad(315));

        // =========================================================================================
        // Bot for block duping to baskets =========================================================
        RoadRunnerBotEntity myBotBasket = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, Math.toRadians(188.22825), Math.toRadians(188.22825), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getPose2d(new Pose2d(20, 61.5, getRad(90))))
                        // Block on bar ------------------------------------------------------------
                        .lineToSplineHeading(getPose2d(new Pose2d(0, 40, getRad(90))))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Bar");
                        })

                        // Block No. 1 -------------------------------------------------------------
                        .lineToSplineHeading(getPose2d(blockPickupPose2d))
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            System.out.println("Pickup");
                        })

                        .setReversed(true)
                        .lineToSplineHeading(getPose2d(blockDropPose2d))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Box");
                        })

                        // Block No. 2 -------------------------------------------------------------
                        .setReversed(true)
                        .lineToSplineHeading(getPose2d(blockPickupPose2d.plus(new Pose2d(10, 0))))
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            System.out.println("Pickup");
                        })

                        .setReversed(true)
                        .lineToSplineHeading(getPose2d(blockDropPose2d))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Box");
                        })

                        // Block No. 3 -------------------------------------------------------------
                        .lineToSplineHeading(getPose2d(new Pose2d(52, 40, getRad(180))))
                        .splineToConstantHeading(getVector2d(new Vector2d(60.5, 15)), getRad(90))
                        .splineToConstantHeading(getVector2d(new Vector2d(60.5, 52)), getRad(90))

                        .build());

        // =========================================================================================
        // Bot for block pushing to zones ==========================================================
        RoadRunnerBotEntity myBotZone = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, Math.toRadians(188.22825), Math.toRadians(188.22825), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(getPose2d(new Pose2d(-20, 61.5, getRad(90))))
                        // Go to Blocks and push ---------------------------------------------------
                        .lineToSplineHeading(getPose2d(new Pose2d(-37, 30, getRad(90))))
                        .splineToConstantHeading(getVector2d(new Vector2d(-54, 30)), getRad(180))
                        .setReversed(true)
                        .lineToConstantHeading(getVector2d(new Vector2d(-54, 20)))
                        .splineToSplineHeading(getPose2d(new Pose2d(-60.5, 10)), getRad(180))
                        .lineToConstantHeading(getVector2d(new Vector2d(-60.5, 52)))

                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(isBasketBot ? myBotBasket : myBotZone)
                .start();
    }
}