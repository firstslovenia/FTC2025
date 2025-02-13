package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import android.renderscript.ScriptGroup;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class VerticalLifter extends Lifter {

    boolean transfer_sequence = false;

     ElapsedTime transferToLifterGrace;

    Servo swivel;

    Claw specimenClaw;

    ElapsedTime swingTimer;

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

        swivel = Hardware.getVerticalLifterSwivel();
        swivel.setPosition(1);

        claw = new Claw(Hardware.getVerticalLiftClaw());
        claw.setOpen(false);

        specimenClaw = new Claw(Hardware.getSpecimenClaw());
        specimenClaw.setOpen(false);

    }

    @Override
    public void run() {
        switch (transferState) {
            case NONE:
                run_motors(InputMapper.getVerticalLifterY());
                claw.run(InputMapper.getVerticalLifterClaw());

                break;

            case HORIZONTAL_LIFTER_CLAW_TO_240:
                transfer_sequence = true;
                homingSequenceActive = true;

                swivel.setPosition(0);

                claw.setOpen(false);

                break;

            case TRANSFER_TO_VERTICAL_LIFTER_CLAW:

                claw.setOpen(true);

                if (transferToLifterGrace == null) {
                    transferToLifterGrace = new ElapsedTime();
                    break;
                }

                if (transferToLifterGrace.milliseconds() > 500) {
                    transferState = TransferState.RAISE_VERTICAL_LIFTER;
                    transferToLifterGrace = null;
                }

                break;

            case RAISE_VERTICAL_LIFTER:
                claw.setOpen(false);

                liftLeft.setTargetPosition((int)MAX_HEIGHT);
                liftRight.setTargetPosition((int)MAX_HEIGHT);

                liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                liftLeft.setPower(1.0f);
                liftRight.setPower(1.0f);

                if (liftRight.getCurrentPosition() >= (int)MAX_HEIGHT - 100
                        ||  liftLeft.getCurrentPosition() >= (int)MAX_HEIGHT - 100) {

                    reset_motors(true);

                    transferState = TransferState.SWING_VERTICAL_LIFTER_ARM;
                }
                break;

            case SWING_VERTICAL_LIFTER_ARM:
                swivel.setPosition(0);
                swingTimer = new ElapsedTime();
                transferState = TransferState.RELEASE_VERTICAL_LIFTER_CLAW;

                break;

            case RELEASE_VERTICAL_LIFTER_CLAW:
                if(swingTimer.milliseconds() > 1000) {
                    claw.setOpen(true);
                }

                break;
        }
    }
}
