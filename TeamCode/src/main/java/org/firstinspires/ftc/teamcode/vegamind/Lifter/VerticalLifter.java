package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

import lombok.Getter;

public class VerticalLifter extends Lifter {

    boolean transfer_sequence = false;

    ElapsedTime closeClawTime;
    ElapsedTime swingTimer;
    ElapsedTime clawReleaseTime;

    @Getter
    Servo swivel;

    @Getter
    Servo specimenClaw;

    @Getter
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
        specimenClaw.setPosition(1);
    }

    public static double heightToSteps(double height) {
        return (height - 44) / 71 * 3500;
    }

    @Override
    public void run(InputMap map, boolean sequenceActive) {
        homingSequence();

        SecondaryInputMap inputMap = (SecondaryInputMap)map;
    }

}

