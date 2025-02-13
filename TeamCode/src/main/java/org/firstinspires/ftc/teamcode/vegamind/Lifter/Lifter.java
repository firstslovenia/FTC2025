package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Claw;
import org.firstinspires.ftc.teamcode.vegamind.Vtils;


public abstract class Lifter {
    protected DcMotor liftLeft;
    protected DcMotor liftRight;

    protected TouchSensor sensorLeft;
    protected TouchSensor sensorRight;

    protected double MAX_HEIGHT = 3500;

    protected boolean lastResetLeft = false;
    protected boolean lastResetRight = false;

    protected boolean homingSequenceActive = true;

    protected ElapsedTime accelerationTimer;

    protected Claw claw;

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

        return power; //TODO reimplement; use power
    }

    protected void reset_motors(boolean setPoewrZero) {
        if(setPoewrZero){
            liftLeft.setPower(0.0f);
            liftRight.setPower(0.0f);
        }

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void homingSequence() {
        // TODO Runout one side, conitnue moving the other side until sensor detects end position - make it paralel => then reset encoders
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

        //! JAKA JE ČARU IN JE DAL SENSOR RIGHT NA -1, BIL JE 0
        // TODO Dodaj možnost za reset s pritisom na gumb na kontrolerju - lahko sta 2 gumba just in case
        if ((sensorLeft.getValue() != 0) || (sensorRight.getValue() != 0)) { // TODO if this is intetnional ( why is one sensor set to not be -1 and the other 0) add a comment why
            reset_motors(false);
        }

        BetterTelemetry.print("left", calculatePower(posLeft, posRight, inputY));
        BetterTelemetry.print("right", calculatePower(posRight, posLeft, inputY));
        BetterTelemetry.print("encoderLeft", liftLeft.getCurrentPosition());
        BetterTelemetry.print("encoderRight", liftRight.getCurrentPosition());
        BetterTelemetry.print("encoderDifference", (liftLeft.getCurrentPosition() - liftRight.getCurrentPosition()));
        BetterTelemetry.print("sensorLeft", sensorLeft.getValue());
        BetterTelemetry.print("sensorRight", sensorRight.getValue());

        liftLeft.setPower(calculatePower(posLeft, posRight, inputY));
        liftRight.setPower(calculatePower(posRight, posLeft, inputY));

        //! JAKA JE MESOU S TEM 0 JE BLA -1
        // lastResetLeft = sensorLeft.getValue() != 0;
        // lastResetRight = sensorRight.getValue() != 0;
    }

    public abstract void run();
}