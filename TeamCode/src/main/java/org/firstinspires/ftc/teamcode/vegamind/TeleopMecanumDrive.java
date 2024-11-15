package org.firstinspires.ftc.teamcode.vegamind;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class TeleopMecanumDrive extends SampleMecanumDrive {
    private IMU imu;

    public TeleopMecanumDrive(HardwareMap hardwareMap, IMU imu) {
        super(hardwareMap);
        this.imu = imu;
    }

    public void run(double x, double y, double rotation) {
        double robotDirection = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-robotDirection) - y * Math.sin(-robotDirection);
        double rotY = x * Math.sin(-robotDirection) + y * Math.cos(-robotDirection);

        Pose2d move = new Pose2d(
                rotation,
                rotY,
                rotX
        );

        drive(move);
    }

    public void drive(Pose2d pose2d) {
        this.setDrivePower(pose2d);
    }
}
