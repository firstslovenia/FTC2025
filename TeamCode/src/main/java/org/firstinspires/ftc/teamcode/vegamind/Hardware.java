package org.firstinspires.ftc.teamcode.vegamind;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.LOGO_FACING_DIR;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.USB_FACING_DIR;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
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
    private static DcMotor verticalLiftLeftMotor;
    @Getter
    private static DcMotor verticalLiftRightMotor;
    @Getter
    private static TouchSensor verticalLiftSensorLeft;
    @Getter
    private static TouchSensor verticalLiftSensorRight;
    @Getter
    private static Servo verticalLiftClaw;
    @Getter
    private static Servo verticalLifterSwivel;
    @Getter
    private static DcMotor horizontalLiftLeftMotor;
    @Getter
    private static DcMotor horizontalLiftRightMotor;
    @Getter
    private static TouchSensor horizontalLiftSensorLeft;
    @Getter
    private static TouchSensor horizontalLiftSensorRight;
    @Getter
    private static Servo horizontalLiftClaw;
    @Getter
    private static Servo horizontalSwivelLeft;
    @Getter
    private static Servo horizontalSwivelRight;
    @Getter
    private static Servo horizontalClawSwivel;


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
        verticalLiftLeftMotor = hardwareMap.dcMotor.get("verticalLiftLeft");
        verticalLiftRightMotor = hardwareMap.dcMotor.get("verticalLiftRight");

        verticalLiftLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        verticalLiftRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        verticalLiftLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalLiftRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        verticalLiftLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        verticalLiftRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        verticalLiftSensorRight = hardwareMap.get(TouchSensor.class, "verticalSensorRight");
        verticalLiftSensorLeft = hardwareMap.get(TouchSensor.class, "verticalSensorLeft");

        verticalLiftClaw = hardwareMap.get(Servo.class, "verticalLiftClaw");

        verticalLifterSwivel = hardwareMap.get(Servo.class, "verticalLiftSwivel");

        //Horizontal Lifter Motors

        horizontalLiftLeftMotor = hardwareMap.dcMotor.get("horizontalLiftLeft");
        horizontalLiftRightMotor = hardwareMap.dcMotor.get("horizontalLiftRight");

        horizontalLiftLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizontalLiftRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horizontalLiftLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontalLiftRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        horizontalLiftLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        horizontalLiftSensorLeft = hardwareMap.get(TouchSensor.class, "horizontalSensorLeft");
        horizontalLiftSensorRight = hardwareMap.get(TouchSensor.class, "horizontalSensorRight");

        horizontalLiftClaw = hardwareMap.get(Servo.class, "horizontalLiftClaw");

        horizontalSwivelRight = hardwareMap.get(Servo.class, "swivelRight");
        horizontalSwivelRight.setDirection(Servo.Direction.REVERSE);

        horizontalSwivelLeft = hardwareMap.get(Servo.class, "swivelLeft");

        horizontalClawSwivel = hardwareMap.get(Servo.class, "horizontalClawSwivel");

    }
}
