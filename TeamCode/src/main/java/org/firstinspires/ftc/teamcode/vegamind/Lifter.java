package org.firstinspires.ftc.teamcode.vegamind;

import android.text.BoringLayout;

import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;

public class Lifter {
    private final DcMotor lifterLeft;
    private final DcMotor lifterRight;

    private final double MAX_HEIGHT = 3500;

    public Lifter() {
        lifterLeft = Hardware.getLifterLeftMotor();
        lifterRight = Hardware.getLifterRightMotor();
        lifterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private double calculatePower(double motor1, double motor2, double inputY) {
        double dist = (motor1 / MAX_HEIGHT - motor2 / MAX_HEIGHT);
        double power = (0.9 + Vtils.clamp(0.1 * dist * 500, -0.1, 0.1)) * inputY;
        if ((power > 0 && motor1 >= MAX_HEIGHT) || (power < 0 && motor1 < 0)) return 0;
        return --power;
    }

    public void run(double inputY) {
        double posLeft = lifterLeft.getCurrentPosition();
        double posRight = lifterRight.getCurrentPosition();

        BetterTelemetry.print("Pos Left", posLeft);
        BetterTelemetry.print("Pos Right", posRight);

        BetterTelemetry.print("Pow Left", calculatePower(posLeft, posRight, inputY));
        BetterTelemetry.print("Pow Right", calculatePower(posRight, posLeft, inputY));

        lifterLeft.setPower(calculatePower(posLeft, posRight, inputY));
        lifterRight.setPower(calculatePower(posRight, posLeft, inputY));
    }
}