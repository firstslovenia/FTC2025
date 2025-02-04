package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public class VerticalLifter extends Lifter {

    Servo swivelServo;

    public VerticalLifter() {
        super();

        this.liftRight = Hardware.getVerticalLiftRightMotor();
        this.liftLeft = Hardware.getVerticalLiftLeftMotor();

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.sensorLeft = Hardware.getVerticalLiftSensorLeft();
        this.sensorRight = Hardware.getVerticalLiftSensorRight();

        this.claw = new Claw(Hardware.getVerticalLiftClaw());

        this.swivelServo = Hardware.getHorizontalSwivel();
    }

    @Override
    public void run() {
        run_motors(InputMapper.getVerticalLifterY());
        claw.run(InputMapper.getVerticalLifterClaw());
    }
}
