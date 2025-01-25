package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Gestell {
    private HardwareMap hardwareMap;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    private float frontRightPow;
    private float backRightPow;
    private float frontLeftPow;
    private float backLeftPow;
    private float maxPow;


    public Gestell(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        backLeft = hardwareMap.get(DcMotor.class, "leftRear");
        backRight = hardwareMap.get(DcMotor.class, "rightRear");
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");

        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update() {

        frontLeftPow = 0;
        backLeftPow = 0;
        frontRightPow = 0;
        backRightPow = 0;

        if(Math.abs(gamepad.left_stick_y) > 0.2) {
            frontLeftPow += gamepad.left_stick_y;
            backLeftPow += gamepad.left_stick_y;
            frontRightPow += gamepad.left_stick_y;
            backRightPow += gamepad.left_stick_y;
        }

        if(Math.abs(gamepad.left_stick_x) > 0.2) {
            frontLeftPow -= gamepad.left_stick_x;
            backLeftPow += gamepad.left_stick_x;
            frontRightPow += gamepad.left_stick_x;
            backRightPow -= gamepad.left_stick_x;
        }

        if(Math.abs(gamepad.right_stick_x) > 0.2) {
            frontLeftPow -= gamepad.right_stick_x;
            backLeftPow -= gamepad.right_stick_x;
            frontRightPow += gamepad.right_stick_x;
            backRightPow += gamepad.right_stick_x;
        }

        maxPow = Float.max(Float.max(Math.abs(frontLeftPow), Math.abs(frontLeftPow)),Float.max(Math.abs(frontLeftPow),Math.abs(frontLeftPow)));
        if(maxPow > 1){
            frontLeftPow /= maxPow;
            backLeftPow /= maxPow;
            frontRightPow /= maxPow;
            backRightPow /= maxPow;
        }


        frontLeft.setPower(frontLeftPow);
        backLeft.setPower(backLeftPow);
        frontRight.setPower(frontRightPow);
        backRight.setPower(backRightPow);
    }
}
