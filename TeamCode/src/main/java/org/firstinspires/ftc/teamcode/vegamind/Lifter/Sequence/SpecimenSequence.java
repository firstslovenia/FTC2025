package org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence;

//range is from 44 to 115 cm .... 71cm
//specimen height is 50cm

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Button;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

import java.util.Arrays;

public class SpecimenSequence extends Sequence {
    boolean hasRaiseLiftBeenPressed;
    boolean canCloseClaw = false;

    boolean liftDir = true;
    boolean lastLiftDirInput = false;

    ElapsedTime closeClawTimer;


    public SpecimenSequence(SecondaryInputMap map, VerticalLifter verticalLift) {
        super();

        Step goToPickup = () -> {

            verticalLift.getLiftLeft().setPower(1.0);
            verticalLift.getLiftRight().setPower(1.0);

            if (verticalLift.getLiftLeft().getCurrentPosition() >= VerticalLifter.heightToSteps(48)
                    || verticalLift.getLiftRight().getCurrentPosition() >= VerticalLifter.heightToSteps(48)) {

                verticalLift.getLiftRight().setPower(0.0f);
                verticalLift.getLiftLeft().setPower(0.0f);

                return true;
            }

            verticalLift.getSpecimenClaw().setPosition(0.5); // open is 1, 0.5 is closed
            return false;
        };

        Step closeClaw = () -> {
            if (!map.getSpecimenPickupTrigger()) {
                canCloseClaw = true;
            }

            if (!canCloseClaw) {
                return false;
            }

            closeClawTimer = new ElapsedTime();

            return map.getSpecimenPickupTrigger();
        };

        Step canRaiseLifter = () -> {
            verticalLift.getSpecimenClaw().setPosition(1);
            if (closeClawTimer.milliseconds() < 1000) {
                return false;
            }

            if(verticalLift.getLiftRight().getCurrentPosition() >= VerticalLifter.heightToSteps(60)
            || verticalLift.getLiftLeft().getCurrentPosition() >= VerticalLifter.heightToSteps(60)) {

                verticalLift.getLiftLeft().setPower(0.0f);
                verticalLift.getLiftRight().setPower(0.0f);

                return true;
            }

            verticalLift.getLiftRight().setPower(1.0f);
            verticalLift.getLiftLeft().setPower(1.0f);

            return false;
        };

        Step toggleLifter = () -> {

            if (map.getSpecimenLift()) {
                hasRaiseLiftBeenPressed = true;
            }

            if (!hasRaiseLiftBeenPressed) {
                return false;
            }

            if (map.getHangSpecimen()) {
                return true;
            }

            if (lastLiftDirInput != map.getSpecimenLift() && lastLiftDirInput) {
                liftDir = !liftDir;
            }


            lastLiftDirInput = map.getSpecimenLift();

            if (liftDir) {

                if (verticalLift.getLiftLeft().getCurrentPosition() >= VerticalLifter.heightToSteps(95)
                        || verticalLift.getLiftRight().getCurrentPosition() >= VerticalLifter.heightToSteps(95)) {

                    verticalLift.getLiftRight().setPower(0.0f);
                    verticalLift.getLiftLeft().setPower(0.0f);

                    return false;
                }

                verticalLift.getLiftLeft().setPower(1.0f);
                verticalLift.getLiftRight().setPower(1.0f);

                return false;
            }


            if (verticalLift.getLiftLeft().getCurrentPosition() <= VerticalLifter.heightToSteps(82)
                    || verticalLift.getLiftRight().getCurrentPosition() <= VerticalLifter.heightToSteps(82)) {

                verticalLift.getLiftRight().setPower(0.0f);
                verticalLift.getLiftLeft().setPower(0.0f);

                return false;
            }

            verticalLift.getLiftLeft().setPower(-1.0f);
            verticalLift.getLiftRight().setPower(-1.0f);

            return false;
        };

        Step releaseClaw = () -> {
            verticalLift.getSpecimenClaw().setPosition(0.5);
            return true;
        };

        Step reset = () -> {
            verticalLift.setHomingSequenceActive(true);

            hasRaiseLiftBeenPressed = false;
            canCloseClaw = false;

            return true;
        };

        steps = Arrays.asList(new Step[]{
                goToPickup, closeClaw, canRaiseLifter, toggleLifter, releaseClaw, reset});
    }
}
