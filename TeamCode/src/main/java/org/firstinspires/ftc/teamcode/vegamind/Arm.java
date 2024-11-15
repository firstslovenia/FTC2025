package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import lombok.Getter;

public class Arm {
    // PID
    private final double kP = 1;
    private final double kD = 0.1;
    private double eD = 0;

    double previousError = 0.0;
    private long lastTime = System.nanoTime();

    // Params
    private final double controlMultiplayer = 0.005;
    private final double minPos = 0.1;
    private final double maxPos = 0.9;
    @Getter
    private double target = 0.5;
    @Getter
    private double pos = 0;

    // Robot
    private DcMotor motor;
    private AnalogInput potentiometer;

    public Arm(DcMotor motor, AnalogInput potentiometer) {
        this.motor = motor;
        this.potentiometer = potentiometer;
    }

    public void run(double input, boolean eBreak) {
        if (eBreak) return;
        run(input);
    }

    public void run(double input) {
        target = VUtils.clamp(target + input * controlMultiplayer, minPos, maxPos);
        pos = potentiometer.getVoltage() / potentiometer.getMaxVoltage();
        motor.setPower(calculatePID());
    }

    private double calculatePID() {
        double error = target - pos;

        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1e9;
        lastTime = currentTime;

        if (deltaTime > 0) {
            eD = (error - previousError) / deltaTime;
        }

        double eP = kP * error;

        previousError = error;
        return (kD * eD) + eP;
    }
}
