package org.firstinspires.ftc.teamcode.vegamind.drivetrain;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;

public class FieldCentricDrivetrain extends Drivetrain {
    public FieldCentricDrivetrain(HardwareMap hardwareMap, IMU imu) {
        super(hardwareMap, imu);
    }

    public void run(double inputX, double inputY, double inputRot) {
        double robotDirection = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = inputX * Math.cos(-robotDirection) - inputY * Math.sin(-robotDirection);
        double rotY = inputX * Math.sin(-robotDirection) + inputY * Math.cos(-robotDirection);

        Pose2d move = new Pose2d(
                rotX,
                rotY,
                inputRot
        );

        drive(move);
    }

    public void run(PrimaryInputMap map) {
        //if (map.isImuReset()) imu.resetYaw();
        run(map.getDriveX(), map.getDriveY(), map.getRotation());
    }

    public void drive(Pose2d pose2d) {
        this.setDrivePower(pose2d);
    }
}
