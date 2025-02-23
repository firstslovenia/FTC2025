package org.firstinspires.ftc.teamcode.vegamind.drivetrain;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

public class FieldCentricDrivetrain extends Drivetrain {
    public FieldCentricDrivetrain(HardwareMap hardwareMap, IMU imu) {
        super(hardwareMap, imu);
    }

    @Override
    public void run(PrimaryInputMap primaryInputMap, SecondaryInputMap secondaryInputMap) {

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

    public void run(PrimaryInputMap primaryInputMap, SecondaryInputMap secondaryInputMap, boolean flipped) {
        //if (map.isImuReset()) imu.resetYaw();

        double x = primaryInputMap.getDriveY();
        double y = primaryInputMap.getDriveX();
        double rot = primaryInputMap.getRotation();

        if (x == 0 && y == 0 && rot == 0) {
            y = secondaryInputMap.getDriveX();
            x = secondaryInputMap.getDriveY();
        }

        if (flipped) {
            y *= -1;
            x *= -1;
        }

        run(-x, -y, rot);
    }

    public void drive(Pose2d pose2d) {
        this.setDrivePower(pose2d);
    }
}
