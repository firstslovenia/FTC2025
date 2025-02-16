package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Vtils;


public abstract class Lifter {
    protected DcMotor liftLeft;
    protected DcMotor liftRight;

    protected TouchSensor sensorLeft;
    protected TouchSensor sensorRight;

    protected double MAX_HEIGHT = 3500;

    protected boolean homingSequenceActive = true;

    protected ElapsedTime accelerationTimer;

     static TransferState transferState = TransferState.NONE;

    protected Lifter() {
        accelerationTimer = new ElapsedTime();
        homingSequenceActive = true;
        transferState = TransferState.NONE;
    }

    protected double calculatePower(double motor1, double motor2, double inputY) {
        if (inputY == 0 && accelerationTimer != null) {
            accelerationTimer = new ElapsedTime();
        }

        double dist = (motor1 / MAX_HEIGHT - motor2 / MAX_HEIGHT);
        double power = (0.9 + Vtils.clamp(-0.1 * dist * 100, -0.1, 0.1)) * inputY;
        if ((power > 0 && motor1 >= MAX_HEIGHT) || (power < 0 && motor1 < 0)) return 0;

        power *= Math.min(accelerationTimer.milliseconds() / 250, 1);

        return inputY; //TODO reimplement; use power
    }

    protected void reset_motors(boolean setPowerZero) {
        if(setPowerZero){
            liftLeft.setPower(0.0f);
            liftRight.setPower(0.0f);
        }

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void homingSequence() {
        if (sensorRight.getValue() != 0 || sensorLeft.getValue() != 0) {
            homingSequenceActive = false;
            reset_motors(true);

            return;
        }

        liftLeft.setPower(-0.3f);
        liftRight.setPower(-0.3f);
    }


    protected void run_motors(double inputY) {
        if (homingSequenceActive) {
            homingSequence();
            return;
        }

        double posLeft = liftLeft.getCurrentPosition();
        double posRight = liftRight.getCurrentPosition();

        if ((sensorLeft.getValue() != 0) || (sensorRight.getValue() != 0)) {
            reset_motors(false);
        }

        liftLeft.setPower(calculatePower(posLeft, posRight, inputY));
        liftRight.setPower(calculatePower(posRight, posLeft, inputY));
    }

    public abstract void run();
}