package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import android.renderscript.ScriptGroup;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class VerticalLifter extends Lifter {

    boolean transfer_sequence = false;

    Servo swivel;

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

        claw = new Claw(Hardware.getVerticalLiftClaw());

    }

    @Override
    public void run() {
        if (transferState == TransferState.HORIZONTAL_LIFTER_CLAW_TO_240) {
            transfer_sequence = true;
            homingSequenceActive = true;

            swivel.setPosition(0);

            claw.run(true);

            return;
        } else if (transferState == TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW) {
            claw.run(true);

            transferState = TransferState.RAISE_VERTICAL_LIFTER;

            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftLeft.setTargetPosition((int)MAX_HEIGHT);
            liftRight.setTargetPosition((int)MAX_HEIGHT);

        } else if (transferState == TransferState.RAISE_VERTICAL_LIFTER) {
            if (liftRight.getCurrentPosition() >= (int)MAX_HEIGHT - 100
            ||  liftLeft.getCurrentPosition() >= (int)MAX_HEIGHT - 100) {

                liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                transferState = TransferState.SWING_VERTICAL_LIFTER_ARM;
            }
        } else if (transferState == TransferState.SWING_VERTICAL_LIFTER_ARM) {
            swivel.setPosition(1);
            swingTimer = new ElapsedTime();
            transferState = TransferState.RELEASE_VERTICAL_LIFTER_CLAW;

        } else if (transferState == TransferState.RELEASE_VERTICAL_LIFTER_CLAW) {
            if(swingTimer.milliseconds() > 1000) {
                claw.run(true);
            }
        }

        if(transferState != TransferState.NONE) {
            return;
        }

        run_motors(InputMapper.getVerticalLifterY());
        claw.run(InputMapper.getVerticalLifterClaw());
    }
}
