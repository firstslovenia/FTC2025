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

     ElapsedTime closeClawTime;

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

        claw = new Claw(Hardware.getVerticalLiftClaw(), false);
        claw.setPos(1);

        specimenClaw = new Claw(Hardware.getSpecimenClaw(), false);
        specimenClaw.setOpen(false);

    }

    @Override
    public void run() {
        BetterTelemetry.print("home", homingSequenceActive);
        BetterTelemetry.print("liftservo", Hardware.getVerticalLiftClaw().getPosition());
        run_motors(InputMapper.getVerticalLifterY());
        switch (transferState) {
            case NONE:
                //claw.run(!InputMapper.getVerticalLifterClaw());

                break;

            case HORIZONTAL_LIFTER_CLAW_TO_240:
                transfer_sequence = true;
                homingSequenceActive = true;

                swivel.setPosition(1);

                claw.setPos(1);

                break;

            case TRANSFER_TO_VERTICAL_LIFTER_CLAW:
                if (homingSequenceActive) {
                    break;
                }

                claw.setPos(0);

                transferState = TransferState.RAM_HORIZONTAL_LIFT;

                break;

            case EXTEND_HORIZONTAL_LIFTER:
                if(closeClawTime == null) {
                    closeClawTime = new ElapsedTime();
                }
                claw.setPos(1);

                break;

            case RAISE_VERTICAL_LIFTER:

                if (closeClawTime.milliseconds() < 1000) {
                    break;
                }

                liftLeft.setPower(1.0f);
                liftRight.setPower(1.0f);

                if (liftRight.getCurrentPosition() >= (int)MAX_HEIGHT - 100
                        ||  liftLeft.getCurrentPosition() >= (int)MAX_HEIGHT - 100) {

                    liftRight.setPower(0);
                    liftLeft.setPower(0);

                    transferState = TransferState.SWING_VERTICAL_LIFTER_ARM;
                    closeClawTime = null;
                }
                break;

            case SWING_VERTICAL_LIFTER_ARM:
                swivel.setPosition(0);
                swingTimer = new ElapsedTime();
                transferState = TransferState.RELEASE_VERTICAL_LIFTER_CLAW;

                break;

            case RELEASE_VERTICAL_LIFTER_CLAW:
                if(swingTimer.milliseconds() > 1000) {
                    claw.setPos(0);
                }

                break;
        }
    }
}
