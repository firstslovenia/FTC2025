package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class VerticalLifter extends Lifter {

    Servo swivelServo;

    ElapsedTime servoToTransferTimer;

    boolean transfer_sequence = false;

    public VerticalLifter() {
        super();

        liftRight = Hardware.getVerticalLiftRightMotor();
        liftLeft = Hardware.getVerticalLiftLeftMotor();

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sensorLeft = Hardware.getVerticalLiftSensorLeft();
        sensorRight = Hardware.getVerticalLiftSensorRight();

        claw = new Claw(Hardware.getVerticalLiftClaw());

        swivelServo = Hardware.getHorizontalSwivel();
        swivelServo.setPosition(30);
    }

    private double degToServoPos(double deg) {
        return (deg / 270) / 2; //270 because that's the full range of motion and divided by two to account for the gear ratio
    }

    private void run_swivel() {
        if(transfer_sequence) {
            if(servoToTransferTimer == null) {
                servoToTransferTimer = new ElapsedTime();
            }
            return;
        }

        if (InputMapper.getHorizontalSwivelPrime()) {
            swivelServo.setPosition(degToServoPos(0));
        } else {
            swivelServo.setPosition(degToServoPos(30));
        }
    }

    @Override
    public void run() {
        if (InputMapper.getTransferSequenceInit()) {
            transfer_sequence = true;
        }

        if (sensorLeft.getValue() != 0 || sensorRight.getValue() != 0) {
            swivelServo.setPosition(degToServoPos(270));
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
