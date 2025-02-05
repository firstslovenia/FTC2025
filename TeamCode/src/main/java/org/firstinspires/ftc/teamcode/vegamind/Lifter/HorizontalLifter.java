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

    static final double SWIVEL_TO_TRESHOLD_270 = 1500; // TODO test

    Servo swivelServoRight;
    Servo swivelServoLeft;

    public HorizontalLifter() {
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

    }

    private double degToServoPos(double deg) {
        return deg / 270.0f;
    }

    private void run_swivel() {
        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_240) {
            swivelServoLeft.setPosition(degToServoPos(240));
            swivelServoRight.setPosition(degToServoPos(240));

            transferState = TransferState.RETRACT_HORIZONTAL_LIFT;

            return;

        } else if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_270) {
            swivelServoLeft.setPosition(degToServoPos(270));
            swivelServoRight.setPosition(degToServoPos(270));

            return;
        }

        if (InputMapper.getHorizontalSwivelPrime()) {
            swivelServoLeft.setPosition(degToServoPos(0));
            swivelServoRight.setPosition(degToServoPos(0));
            return;
        }

        swivelServoLeft.setPosition(degToServoPos(30));
        swivelServoRight.setPosition(degToServoPos(30));
    }

    @Override
    public void run() {
        if (InputMapper.getTransferSequenceInit()) {
            transferState = TransferState.HORIZONTAL_LIFTER_CLAW_TO_240;
        }

        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_240 &&
                liftLeft.getCurrentPosition() < SWIVEL_TO_TRESHOLD_270){
            transferState = TransferState.HORIZONTAL_LIFTER_CLAW_TO_270;
        }

      /*  if (sensorLeft.getValue() != 0 || sensorRight.getValue() != 0) {
            swivelServoRight.setPosition(degToServoPos(270));
            swivelServoLeft.setPosition(degToServoPos(270));

            clawToClawTransferTimer = new ElapsedTime();

            servoToTransferTimer = null;
            transfer_sequence = false;

        }

        if(clawToClawTransferTimer == null) {
            claw.run(InputMapper.getHorizontalLifterClaw());
        } else if (clawToClawTransferTimer.milliseconds() > 500) {
            clawToClawTransferTimer = null;
            claw.run(true);

            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            liftLeft.setTargetPosition(500);
            liftRight.setTargetPosition(500);
        }*/

        run_swivel();

        if (InputMapper.getHorizontalLifterX() != 0 || servoToTransferTimer == null) {
            run_motors(InputMapper.getHorizontalLifterX());
            return;
        }

        if (transferState.ordinal() >= TransferState.RETRACT_HORIZONTAL_LIFT.ordinal()
        &&  sensorRight.getValue() == 0) {
            liftRight.setPower(-1);
            liftLeft.setPower(-1);
        }

        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_270 && sensorRight.getValue() != 0){
            transferState = TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW;
            claw.run(true);
        }
    }
}
