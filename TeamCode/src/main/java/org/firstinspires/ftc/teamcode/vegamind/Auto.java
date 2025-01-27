package org.firstinspires.ftc.teamcode.vegamind;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class Auto extends LinearOpMode {
    private static final Pose2d endPose = new Pose2d(58, 60, Math.toRadians(0));

    // Bot Params
    private static final boolean isRed = true;
    private static final boolean isBasketBot = true;

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

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        TrajectorySequence trajectory;

        trajectory = drive.trajectorySequenceBuilder(getPose2d(new Pose2d(-20, 61.5, getRad(90))))
                .lineToSplineHeading(getPose2d(new Pose2d(-37, 30, getRad(90))))
                .splineToConstantHeading(getVector2d(new Vector2d(-54, 30)), getRad(180))
                .setReversed(true)
                .lineToConstantHeading(getVector2d(new Vector2d(-54, 20)))
                .splineToSplineHeading(getPose2d(new Pose2d(-60.5, 10)), getRad(180))
                .lineToConstantHeading(getVector2d(new Vector2d(-60.5, 52)))

                .build();


        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectorySequence(trajectory);
    }
}
