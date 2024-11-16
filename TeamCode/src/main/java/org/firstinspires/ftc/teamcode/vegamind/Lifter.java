package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.DcMotor;

import lombok.Getter;
import si.vegamind.coyotecore.motion.motor.Motor;

public class Lifter {
    // Params
    private final double stringWinderR = 17.5;
    private final double maxLength = 5300;

    @Getter
    private double currentLength = 0;
    @Getter
    private double targetLength = 0;

    // Robot
    private DcMotor motor;

    public Lifter(DcMotor motor) {
        this.motor = motor;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void run(double input, boolean eBreak) {
        if (eBreak) return;
        run(input);
    }

    public void run(double input) {
        // NOTE: 1 rotation = 28 ticks then convert to circumference of string winder
        currentLength = motor.getCurrentPosition(); // Calculated for use in telemetry

        targetLength = VUtils.clamp(input + targetLength, 0, maxLength);
        motor.setTargetPosition((int) (targetLength));
        motor.setPower(1); // CHECK: Does actually work instead of velocity?
    }
}
