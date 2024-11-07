package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        Pose2d blockDropPose2d = new Pose2d(55, 55, Math.toRadians(45));
        Vector2d blockPickupVector2d = new Vector2d(36, 27);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(39.4224324932042, 39.4224324932042, Math.toRadians(188.22825), Math.toRadians(188.22825), 12)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(20, 61.5, Math.toRadians(270)))
                        .splineToConstantHeading(new Vector2d(0, 36), Math.toRadians(270))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Hung block");
                        })

                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(6, 42), Math.toRadians(0))
                        .lineToSplineHeading(new Pose2d(26, 42, Math.toRadians(0)))
                        .splineToConstantHeading(blockPickupVector2d, Math.toRadians(270))
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            System.out.println("Pickup");
                        })

                        .lineToConstantHeading(new Vector2d(36, 30))
                        .splineToSplineHeading(blockDropPose2d, Math.toRadians(45))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Added block to box");
                        })

                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(45, 27, Math.toRadians(0)), Math.toRadians(270))
                        .waitSeconds(2)
                        .addDisplacementMarker(() -> {
                            System.out.println("Pickup");
                        })

                        .lineToConstantHeading(new Vector2d(45, 30))
                        .splineToSplineHeading(blockDropPose2d, Math.toRadians(45))
                        .waitSeconds(3)
                        .addDisplacementMarker(() -> {
                            System.out.println("Added block to box");
                        })

                        .lineToSplineHeading(new Pose2d(55, 15, Math.toRadians(90)))
                        .splineToConstantHeading(new Vector2d(61, 15), Math.toRadians(90))
                        .lineToConstantHeading(new Vector2d(61, 55))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}