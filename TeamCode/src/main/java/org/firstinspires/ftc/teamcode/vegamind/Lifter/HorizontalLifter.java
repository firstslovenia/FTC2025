package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class HorizontalLifter extends Lifter{

    Servo swivelServoRight;
    Servo swivelServoLeft;

    ElapsedTime servoToTransferTimer;

    boolean transfer_sequence = false;

    public HorizontalLifter(Telemetry telemetry) {
        super();

        this.liftRight = Hardware.getHorizontalLiftRightMotor();
        this.liftLeft = Hardware.getHorizontalLiftLeftMotor();

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.sensorLeft = Hardware.getHorizontalLiftSensorLeft();
        this.sensorRight = Hardware.getHorizontalLiftSensorRight();

        this.claw = new Claw(Hardware.getVerticalLiftClaw());

        swivelServoLeft = Hardware.getHorizontalSwivelLeft();
        //swivelServoLeft.setPosition(30);

        swivelServoRight = Hardware.getHorizontalSwivelRight();
        swivelServoRight.setPosition(degToServoPos(30));

        telemetry.addData("zt", degToServoPos(30));

    }

    private double degToServoPos(double deg) {
        return deg / 270.0f;
    }

    private void run_swivel() {
        if(transfer_sequence) {
            if(servoToTransferTimer == null) {
                servoToTransferTimer = new ElapsedTime();
            }
            return;
        }

        if (InputMapper.getHorizontalSwivelPrime()) {
            swivelServoLeft.setPosition(degToServoPos(0));
            swivelServoRight.setPosition(degToServoPos(0));
        } else {
            swivelServoLeft.setPosition(degToServoPos(30));
            swivelServoRight.setPosition(degToServoPos(30));
        }
    }

    @Override
    public void run() {
        if (InputMapper.getTransferSequenceInit()) {
            transfer_sequence = true;
        }

        if (sensorLeft.getValue() != 0 || sensorRight.getValue() != 0) {
            swivelServoRight.setPosition(degToServoPos(270));
            swivelServoLeft.setPosition(degToServoPos(270));

            servoToTransferTimer = null;
            transfer_sequence = false;
        }

        claw.run(InputMapper.getHorizontalLifterClaw());
        run_swivel();

        if (InputMapper.getHorizontalLifterX() != 0 || servoToTransferTimer == null) {
            run_motors(InputMapper.getHorizontalLifterX());
            return;
        }

        if (servoToTransferTimer.milliseconds() >= 1000) {
            liftRight.setPower(-1);
            liftLeft.setPower(-1);
        }
    }
}
