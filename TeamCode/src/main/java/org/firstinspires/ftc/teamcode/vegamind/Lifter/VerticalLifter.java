package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class VerticalLifter extends Lifter {

    boolean transfer_sequence = false;

    ElapsedTime closeClawTime;
    ElapsedTime swingTimer;
    ElapsedTime clawReleaseTime;

    Servo swivel;

    Servo specimenClaw;
    Servo claw;


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

        claw = Hardware.getVerticalLiftClaw();
        claw.setPosition(1);

        specimenClaw = Hardware.getSpecimenClaw();

    }

    @Override
    public void run() {
        run_motors(InputMapper.getVerticalLifterY());
        switch (transferState) {
            case NONE:
                claw.setPosition(InputMapper.getVerticalLifterClaw() ? 0 : 1); // idk if this is right lol

                break;

            case PRIME_LIFT_CLAW:
                transfer_sequence = true;
                homingSequenceActive = true;

                swivel.setPosition(1);

                claw.setPosition(1);

                break;

            case TRANSFER_TO_VERTICAL_LIFTER_CLAW:
                if (homingSequenceActive) {
                    break;
                }

                claw.setPosition(0);

                transferState = TransferState.RAM_HORIZONTAL_LIFT;

                break;

            case EXTEND_HORIZONTAL_LIFTER:
                if(closeClawTime == null) {
                    closeClawTime = new ElapsedTime();
                }
                claw.setPosition(1);

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
                if(swingTimer.milliseconds() < 1000) {
                    break;
                }

                claw.setPosition(0);
                transferState = TransferState.RESET;

                break;

            case RESET:
                if (clawReleaseTime == null) {
                    clawReleaseTime = new ElapsedTime();
                }

                if(clawReleaseTime.milliseconds() < 1000) {
                    homingSequenceActive = true;
                    break;
                }

                swivel.setPosition(1);

                if (!homingSequenceActive) {
                    transferState = TransferState.NONE;
                    break;
                }
        }
    }
}
