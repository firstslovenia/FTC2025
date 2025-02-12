package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class HorizontalLifter extends Lifter{

    static final double SWIVEL_TO_TRESHOLD_270 = 400; // TODO test

    Servo swivelServoRight;
    Servo swivelServoLeft;

    Servo clawSwivel;

    boolean last_claw_swivel_change = false;

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

        claw = new Claw(Hardware.getVerticalLiftClaw());

        swivelServoLeft = Hardware.getHorizontalSwivelLeft();
        swivelServoLeft.setPosition(degToServoPos(140));

        swivelServoRight = Hardware.getHorizontalSwivelRight();
        swivelServoRight.setPosition(degToServoPos(140));

        clawSwivel = Hardware.getHorizontalClawSwivel();
        clawSwivel.setPosition(0.5);

    }

    private double degToServoPos(double deg) {
        return deg / 270.0f / 2.0f;
    }

    private void run_swivel() {
        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_240) {
            swivelServoLeft.setPosition(degToServoPos(240));
            swivelServoRight.setPosition(degToServoPos(240));

            transferState = TransferState.RETRACT_HORIZONTAL_LIFT;

            return;

        } else if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_270) { // TODO maybe a delay?
            swivelServoLeft.setPosition(degToServoPos(270));
            swivelServoRight.setPosition(degToServoPos(270));

            return;
        }

        if (InputMapper.getHorizontalSwivelPrime()) {
            swivelServoLeft.setPosition(degToServoPos(0));
            swivelServoRight.setPosition(degToServoPos(0));
            return;
        }

        if(transferState != TransferState.NONE) {
            return;
        }

        swivelServoLeft.setPosition(degToServoPos(30));
        swivelServoRight.setPosition(degToServoPos(30));
    }

    private void run_claw_swivel() {
        if (InputMapper.getClawSwivel() != 0 && !last_claw_swivel_change) { //TODO I don't think this is working afaik
            clawSwivel.setPosition(
                    clawSwivel.getPosition() + (InputMapper.getClawSwivel() / 2)
            );
        }

        last_claw_swivel_change = InputMapper.getClawSwivel() != 0;
    }


    private void handle_transfer() {
        if (InputMapper.getTransferSequenceInit()) {

            transferState = TransferState.HORIZONTAL_LIFTER_CLAW_TO_240;

        } else if (transferState == TransferState.RETRACT_HORIZONTAL_LIFT &&
                liftLeft.getCurrentPosition() < SWIVEL_TO_TRESHOLD_270){ // TODO

            transferState = TransferState.HORIZONTAL_LIFTER_CLAW_TO_270;

        } else if (transferState == TransferState.RETRACT_HORIZONTAL_LIFT) {
            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            liftLeft.setTargetPosition(0);
            liftRight.setTargetPosition(0);

            liftLeft.setPower(1.0f);
            liftRight.setPower(1.0f);


        } else if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_270 && sensorRight.getValue() != 0){

            transferState = TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW;

            reset_motors();


            claw.run(true);
        }
    }

    @Override
    public void run() {
        BetterTelemetry.print("state", transferState.name());
        BetterTelemetry.print("wefn", InputMapper.getHorizontalLifterX());
        BetterTelemetry.print("x", liftRight.getCurrentPosition());
        BetterTelemetry.print("home", homingSequenceActive);
        BetterTelemetry.print("sensro", sensorLeft.getValue());
        if (transferState == TransferState.NONE || homingSequenceActive) {
            run_motors(InputMapper.getHorizontalLifterX());
        }

        BetterTelemetry.update();

        handle_transfer();
        run_swivel();
        run_claw_swivel();
    }
}
