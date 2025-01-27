package org.firstinspires.ftc.teamcode.vegamind;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.LOGO_FACING_DIR;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.USB_FACING_DIR;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import lombok.Getter;

public class Hardware {
    @Getter
    private static IMU imu;

    @Getter
    private static DcMotor leftRearMotor;
    @Getter
    private static DcMotor rightRearMotor;
    @Getter
    private static DcMotor leftFrontMotor;
    @Getter
    private static DcMotor rightFrontMotor;
    @Getter
    private static DcMotor lifterLeftMotor;
    @Getter
    private static DcMotor lifterRightMotor;

    @Getter
    private static IMU.Parameters imuParameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            LOGO_FACING_DIR,
            USB_FACING_DIR));

    public static void init() {
        // Imu
        imu = hardwareMap.get(IMU .class, "imu");
        imu.initialize(imuParameters);

        // Drive Motors
        leftRearMotor = hardwareMap.dcMotor.get("leftRear");
        leftFrontMotor = hardwareMap.dcMotor.get("leftFront");
        rightRearMotor = hardwareMap.dcMotor.get("rightRear");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFront");
        rightRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        lifterLeftMotor = hardwareMap.dcMotor.get("liftLeft");
        lifterRightMotor = hardwareMap.dcMotor.get("liftRight");
        lifterRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
