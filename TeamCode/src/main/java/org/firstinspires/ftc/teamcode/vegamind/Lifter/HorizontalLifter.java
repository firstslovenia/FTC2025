package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class HorizontalLifter extends Lifter{

    static final double SWIVEL_TO_TRESHOLD_270 = 150; // TODO test

    Servo swivelServoRight;
    Servo swivelServoLeft;

    Servo clawSwivel;

    ElapsedTime extendLifterTimer;
    ElapsedTime verticalLiftGrace;

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

        claw = new Claw(Hardware.getHorizontalLiftClaw(), true);
        claw.setPos(1);

        swivelServoLeft = Hardware.getHorizontalSwivelLeft();

        swivelServoLeft.setPosition(degToServoPosSwivel(230));

        swivelServoRight = Hardware.getHorizontalSwivelRight();
        swivelServoRight.setPosition(degToServoPosSwivel(230));

        clawSwivel = Hardware.getHorizontalClawSwivel();
        clawSwivel.scaleRange(0.3333, 1);

        clawSwivel.setPosition(degToServoPosClawSwivel(0));

    }

    private double degToServoPosSwivel(double deg) {
        return (270 - deg) / 600 + 0.66666666667;
    }
    private double degToServoPosClawSwivel(double deg) {
        return (deg) / 180;
    }

    private void run_swivel() {
        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_240) {
            swivelServoLeft.setPosition(degToServoPosSwivel(230));
            swivelServoRight.setPosition(degToServoPosSwivel(230));

            liftLeft.setTargetPosition(-200);
            liftRight.setTargetPosition(-200);

            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            liftRight.setPower(1.0f);
            liftRight.setPower(1.0f);

            transferState = TransferState.RETRACT_HORIZONTAL_LIFT;

            return;

        } else if (transferState == TransferState.RAM_HORIZONTAL_LIFT) { // TODO maybe a delay?

            return;
        }

        if (InputMapper.getHorizontalSwivelPrime()) {
            swivelServoLeft.setPosition(degToServoPosSwivel(0));
            swivelServoRight.setPosition(degToServoPosSwivel(0));
            return;
        }

        if(transferState != TransferState.NONE) {
            return;
        }

        //swivelServoLeft.setPosition(degToServoPosSwivel(240));
        //swivelServoRight.setPosition(degToServoPosSwivel(240));
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
        BetterTelemetry.print("retracty", liftRight.getCurrentPosition());
        if (InputMapper.getTransferSequenceInit()) {

            transferState = TransferState.HORIZONTAL_LIFTER_CLAW_TO_240;


        } else if (transferState == TransferState.RETRACT_HORIZONTAL_LIFT &&
                liftRight.getCurrentPosition() < SWIVEL_TO_TRESHOLD_270){ // TODO



                liftRight.setPower(0.0f);
                liftLeft.setPower(0.0f);

                transferState = TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW;
        } else if (transferState == TransferState.RAM_HORIZONTAL_LIFT) {
            swivelServoLeft.setPosition(degToServoPosSwivel(270));
            swivelServoRight.setPosition(degToServoPosSwivel(270));

            liftLeft.setTargetPosition(-200);
            liftRight.setTargetPosition(-200);

            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            liftLeft.setPower(1.0f);
            liftRight.setPower(1.0f);



            if(sensorRight.getValue() != 0){
                reset_motors(true);
                transferState = TransferState.EXTEND_HORIZONTAL_LIFTER;
            }

        }

        else if (transferState == TransferState.EXTEND_HORIZONTAL_LIFTER) {
            if (verticalLiftGrace == null) {
                verticalLiftGrace = new ElapsedTime();
            }

            if(verticalLiftGrace.milliseconds() < 1000) {
                extendLifterTimer = new ElapsedTime();
                return;
            }


            liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            liftRight.setPower(1.0);
            liftLeft.setPower(1.0);
            claw.setPos(0.5);

            if (extendLifterTimer.milliseconds() > 1000) {
                liftLeft.setPower(0);
                liftRight.setPower(0);
                extendLifterTimer = null;
                verticalLiftGrace = null;
                transferState = TransferState.RAISE_VERTICAL_LIFTER;
            }
        }
    }

    @Override
    public void run() {
        BetterTelemetry.print("state", transferState.name());
        BetterTelemetry.print("wefn", InputMapper.getHorizontalLifterX());
        BetterTelemetry.print("x", liftRight.getCurrentPosition());
        BetterTelemetry.print("sensro", sensorLeft.getValue());
        if (transferState == TransferState.NONE || homingSequenceActive) {
            run_motors(InputMapper.getHorizontalLifterX());
        }

        BetterTelemetry.update();

        run_swivel();
        handle_transfer();
        run_claw_swivel();
    }
}
