package org.firstinspires.ftc.teamcode.vegamind;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.LOGO_FACING_DIR;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.USB_FACING_DIR;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.TouchSensor;

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
    private static TouchSensor lifterSensorLeft;
    @Getter
    private static TouchSensor lifterSensorRight;

    @Getter
    private static IMU.Parameters imuParameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            LOGO_FACING_DIR,
            USB_FACING_DIR));

    public static void init(HardwareMap hardwareMap) {
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

        // Lifter Motors
        lifterLeftMotor = hardwareMap.dcMotor.get("liftLeft");
        lifterRightMotor = hardwareMap.dcMotor.get("liftRight");

        lifterLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lifterRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lifterLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lifterRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lifterRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        lifterSensorRight = hardwareMap.get(TouchSensor.class, "sensorRight");
        lifterSensorLeft = hardwareMap.get(TouchSensor.class, "sensorLeft");

    }
}
