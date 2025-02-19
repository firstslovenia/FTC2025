package org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.TransferState;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;

import java.util.Arrays;

public class TransferSequence extends Sequence{

    ElapsedTime verticalLiftGrace;
    ElapsedTime extendLifterTimer;
    ElapsedTime closeClawTime;
    ElapsedTime swingTimer;
    ElapsedTime clawReleaseTimer;

    public TransferSequence(HorizontalLifter horizontalLift, VerticalLifter verticalLift) {
        super();

        Step primeLiftClaw = () -> {
            horizontalLift.getSwivelServoLeft()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(230));
            horizontalLift.getSwivelServoRight()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(230));

            horizontalLift.getLiftLeft().setTargetPosition(0);
            horizontalLift.getLiftRight().setTargetPosition(0);

            horizontalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            horizontalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

            horizontalLift.getLiftLeft().setPower(1.0f);
            horizontalLift.getLiftRight().setPower(1.0f);


            verticalLift.setHomingSequenceActive(true);

            verticalLift.getSwivel().setPosition(1);
            verticalLift.getClaw().setPosition(1);

            return true;
        };

        Step retractHorizontalLift = () -> {
            if (horizontalLift.getLiftRight().getCurrentPosition() > 150) {
                return false;
            }

            horizontalLift.getLiftRight().setPower(0.0f);
            horizontalLift.getLiftLeft().setPower(0.0f);

            return true;
        };

        Step transferToVerticalLiftClaw = () -> {
            if (verticalLift.isHomingSequenceActive()) {
                return false;
            }

            verticalLift.getClaw().setPosition(0);

            return true;
        };

        Step ramHorizontalLift = () -> {
            horizontalLift.getSwivelServoLeft()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(270));
            horizontalLift.getSwivelServoRight()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(270));

            horizontalLift.getLiftLeft().setTargetPosition(-200);
            horizontalLift.getLiftRight().setTargetPosition(-200);

            horizontalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            horizontalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

            horizontalLift.getLiftLeft().setPower(1.0f);
            horizontalLift.getLiftRight().setPower(1.0f);

            if(horizontalLift.getSensorRight().getValue() != 0){
                horizontalLift.reset_motors(true);
                return true;
            }

            return false;
        };

        Step extendHorizontalLifter = () -> {
            if (verticalLiftGrace == null) {
                verticalLiftGrace = new ElapsedTime();
            }

            if(verticalLiftGrace.milliseconds() < 1000) {
                extendLifterTimer = new ElapsedTime();
                return false;
            }


            horizontalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            horizontalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            horizontalLift.getLiftLeft().setPower(1.0);
            horizontalLift.getLiftRight().setPower(1.0);

            horizontalLift.getClaw().setPosition(0.5);

            if (extendLifterTimer.milliseconds() < 1000) {
                return false;
            }

            horizontalLift.getLiftLeft().setPower(0);
            horizontalLift.getLiftRight().setPower(0);

            extendLifterTimer = null;
            verticalLiftGrace = null;


            if (closeClawTime == null) {
                closeClawTime = new ElapsedTime();
            }

            verticalLift.getClaw().setPosition(1);

            return true;
        };

        Step raiseVerticalLifter = () -> {
            if (closeClawTime.milliseconds() < 1000) {
                verticalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                verticalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                return false;
            }


            verticalLift.getLiftLeft().setPower(1.0f);
            verticalLift.getLiftRight().setPower(1.0f);

            if (verticalLift.getLiftRight().getCurrentPosition() < 3500 - 100
                    &&  verticalLift.getLiftLeft().getCurrentPosition() < 3500 - 100) {
                return false;
            }
            verticalLift.getLiftLeft().setPower(0);
            verticalLift.getLiftRight().setPower(0);

            closeClawTime = null;

            return true;
        };

        Step swingVerticalLifterArm = () -> {
            verticalLift.getSwivel().setPosition(0);
            swingTimer = new ElapsedTime();

            return true;
        };

        Step releaseVerticalLifterArm = () -> {
            if (swingTimer.milliseconds() < 1000) {
                return false;
            }

            verticalLift.getClaw().setPosition(0);

            clawReleaseTimer = new ElapsedTime();
            return true;
        };

        Step reset = () -> {
            horizontalLift.setHomingSequenceActive(true);

            if(clawReleaseTimer.milliseconds() < 500) {
                verticalLift.setHomingSequenceActive(true);
                return false;
            }

            verticalLift.getClaw().setPosition(1);
            verticalLift.getSwivel().setPosition(1);

            if /*fuck you I'm not simplifying this*/ (!verticalLift.isHomingSequenceActive()) {
                return true;
            }

            return false;
        };

        steps = Arrays.asList(new Step[]{primeLiftClaw, retractHorizontalLift,
                transferToVerticalLiftClaw, ramHorizontalLift, extendHorizontalLifter,
                raiseVerticalLifter, swingVerticalLifterArm,
                raiseVerticalLifter, releaseVerticalLifterArm, reset});
    }

    TransferState getState() {
        if (!isRunning) {
            return TransferState.NONE;
        }

        switch (currentStep) {
            case 0:
                return TransferState.PRIME_LIFT_CLAW;
            case 1:
                return TransferState.RETRACT_HORIZONTAL_LIFT;
            case 2:
                return TransferState.RAM_HORIZONTAL_LIFT;
            case 3:
                return TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW;
            case 4:
                return TransferState.EXTEND_HORIZONTAL_LIFTER;
            case 5:
                return TransferState.RAISE_VERTICAL_LIFTER;
            case 6:
                return TransferState.SWING_VERTICAL_LIFTER_ARM;
            case 7:
                return TransferState.RELEASE_VERTICAL_LIFTER_CLAW;
            case 8:
                return TransferState.RESET;
        }

        throw new RuntimeException("Again what the hell did you do future me");
    }


}
