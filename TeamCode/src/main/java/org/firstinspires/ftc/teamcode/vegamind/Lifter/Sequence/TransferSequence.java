package org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.TransferState;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;

import java.util.Arrays;

public class TransferSequence extends Sequence{

    ElapsedTime extendLifterTimer;
    ElapsedTime closeClawTime;
    ElapsedTime swingTimer;
    ElapsedTime swivelSwingTimer;
    ElapsedTime horizontalSwingTimer;

    boolean cancelled = true;

    public TransferSequence(HorizontalLifter horizontalLift, VerticalLifter verticalLift, PrimaryInputMap map) {
        super();

        Step primeLiftClaw = () -> {
            cancelled = true;

            if (horizontalSwingTimer == null) {
                horizontalSwingTimer = new ElapsedTime();
            }

            verticalLift.getSwivel().setPosition(1);
            verticalLift.getClaw().setPosition(1);

            if (horizontalSwingTimer.milliseconds() < 300) {
                return false;
            }

            horizontalLift.getSwivelServoLeft()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(230));
            horizontalLift.getSwivelServoRight()
                    .setPosition(HorizontalLifter.degToServoPosSwivel(230));


            horizontalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            horizontalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            horizontalLift.getLiftLeft().setPower(-1.0f);
            horizontalLift.getLiftRight().setPower(-1.0f);


            verticalLift.setHomingSequenceActive(true);

            horizontalLift.getClawSwivel().setPosition(HorizontalLifter.degToServoPosClawSwivel(0));

            horizontalSwingTimer = null;

            return true;
        };

        Step retractHorizontalLift = () -> {
            if (horizontalLift.getLiftRight().getCurrentPosition() > 170) {
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

            verticalLift.getSwivel().setPosition(0.95);

            if(horizontalLift.getSensorRight().getValue() != 0){
                horizontalLift.reset_motors(true);
                return true;
            }

            return false;
        };

        Step prepareClawsForTransfer = () -> {

            verticalLift.getClaw().setPosition(1);
            horizontalLift.getClaw().setPosition(0.5);



            extendLifterTimer = new ElapsedTime();
            closeClawTime = new ElapsedTime();

            return map.getContinueTransferSequence();
        };

        Step extendHorizontalLifter = () -> {


            if (extendLifterTimer.milliseconds() > 500) {
                extendLifterTimer = null;

                horizontalLift.getLiftRight().setPower(0.0f);
                horizontalLift.getLiftLeft().setPower(0.0f);

                return true;
            }

            horizontalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            horizontalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            horizontalLift.getLiftLeft().setPower(1.0);
            horizontalLift.getLiftRight().setPower(1.0);



            return false;
        };

        Step raiseVerticalLifter = () -> {
            if (closeClawTime.milliseconds() < 1000) {
                verticalLift.getLiftLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                verticalLift.getLiftRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                return false;
            }


            verticalLift.getLiftLeft().setPower(1.0f);
            verticalLift.getLiftRight().setPower(1.0f);

            if (verticalLift.getLiftRight().getCurrentPosition() < 3500
                    &&  verticalLift.getLiftLeft().getCurrentPosition() < 3500) {
                return false;
            }
            verticalLift.getLiftLeft().setPower(0);
            verticalLift.getLiftRight().setPower(0);

            closeClawTime = null;

            return true;
        };

        Step swingVerticalLifterArm = () -> {
            verticalLift.getSwivel().setPosition(0.4);
            swingTimer = new ElapsedTime();

            return true;
        };

        Step releaseVerticalLifterArm = () -> {
            if (swingTimer.milliseconds() < 500) {
                return false;
            }

            if (!map.getContinueTransferSequence()) {
                return false;
            }


            swivelSwingTimer = new ElapsedTime();
            verticalLift.getSwivel().setPosition(0);

            cancelled = false;

            return true;
        };

        Step reset = () -> {
            horizontalLift.setHomingSequenceActive(true);

            if(swivelSwingTimer == null) {
                swivelSwingTimer = new ElapsedTime();
            }

            if(swivelSwingTimer.milliseconds() < 500) {
                return false;
            }

            verticalLift.getClaw().setPosition(0);

            if (!map.getContinueTransferSequence() && !cancelled) {
                return false;
            }

            verticalLift.setHomingSequenceActive(true);
            verticalLift.getClaw().setPosition(1);
            verticalLift.getSwivel().setPosition(1);

            horizontalLift.getClaw().setPosition(1);

            swivelSwingTimer = null;

            return true;
        };

        steps = Arrays.asList(new Step[]{primeLiftClaw, retractHorizontalLift,
                transferToVerticalLiftClaw, ramHorizontalLift, prepareClawsForTransfer,
                extendHorizontalLifter, raiseVerticalLifter, swingVerticalLifterArm,
                releaseVerticalLifterArm, reset});
    }

    public TransferState getState() {
        if (!isRunning) {
            return TransferState.NONE;
        }

        switch (currentStep) {
            case 0:
                return TransferState.PRIME_LIFT_CLAW;
            case 1:
                return TransferState.RETRACT_HORIZONTAL_LIFT;
            case 2:
                return TransferState.TRANSFER_TO_VERTICAL_LIFTER_CLAW;
            case 3:
                return TransferState.RAM_HORIZONTAL_LIFT;
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

        return TransferState.NONE;
    }
}
