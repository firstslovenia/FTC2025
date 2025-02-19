package org.firstinspires.ftc.teamcode.vegamind.drivetrain;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class RobotCentricDrivetrain extends Drivetrain {
    public RobotCentricDrivetrain(HardwareMap hardwareMap, IMU imu) {
        super(hardwareMap, imu);
    }

    public void run() {
        double y = InputMapper.getDriveY();
        double x = -InputMapper.getDriveX() * 1.1;
        double rx = InputMapper.getDriveRot();

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        leftFrontMotor.setPower(frontLeftPower);
        leftRearMotor.setPower(backLeftPower);
        rightFrontMotor.setPower(frontRightPower);
        rightRearMotor.setPower(backRightPower);
    }
}
