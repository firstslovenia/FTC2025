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

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(20, 61.5, Math.toRadians(270)))
                        .splineToConstantHeading(new Vector2d(0, 36), Math.toRadians(270))
                        .addDisplacementMarker(() -> {
                            System.out.println("Hanging block");
                        })
                        .lineToConstantHeading(new Vector2d(25, 36))
                        .splineToConstantHeading(new Vector2d(40, 40), Math.toRadians(0))
                        .lineToSplineHeading(new Pose2d(48.5, 40, Math.toRadians(270)))
                        //.splineToSplineHeading(new Pose2d(48.5, 40, Math.toRadians(270)), Math.toRadians(0))

                        //.lineToSplineHeading(new Pose2d(48.5, 41, Math.toRadians(270))) // Can be skipped but it looks like it could knock down a game element (CCA. +0.15s)
                        .splineToSplineHeading(new Pose2d(55, 55, Math.toRadians(45)), Math.toRadians(45))
                        //.splineToSplineHeading(new Pose2d(48.5, 55), Math.toRadians(90))

                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}