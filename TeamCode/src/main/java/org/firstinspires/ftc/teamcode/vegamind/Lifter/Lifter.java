package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import androidx.core.app.NotificationCompat;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.AutoTransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.Sequence;
import org.firstinspires.ftc.teamcode.vegamind.Vtils;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMap;

import lombok.Getter;
import lombok.Setter;


public abstract class Lifter {

    @Getter
    protected DcMotor liftLeft;

    @Getter
    protected DcMotor liftRight;

    @Getter
    protected TouchSensor sensorLeft;

    @Getter
    protected TouchSensor sensorRight;

    protected double MAX_HEIGHT = 3500;

    @Setter
    @Getter
    protected boolean homingSequenceActive = false;

    protected ElapsedTime accelerationTimer;

    @Setter
    protected int autoHomePos;

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

    public void reset_motors(boolean setPowerZero) {
        if(setPowerZero){
            liftLeft.setPower(0.0f);
            liftRight.setPower(0.0f);
        }

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void homingSequence() {
        if (!homingSequenceActive) {
            return;
        }

        if (sensorRight.getValue() != 0 || sensorLeft.getValue() != 0) {
            homingSequenceActive = false;
            reset_motors(true);

            return;
        }

        liftLeft.setPower(-0.3);
        liftRight.setPower(-0.3);
    }

    public boolean homeToPosAuto(int pos) {

        if (Math.abs(liftLeft.getCurrentPosition() - pos) < 40) {
            liftRight.setPower(0.0f);
            liftRight.setPower(0.0f);
            return true;
        }

        if (liftLeft.getCurrentPosition() > pos) {
            liftLeft.setPower(-1.0f);
            liftRight.setPower(-1.0f);
        } else {
            liftLeft.setPower(1.0);
            liftRight.setPower(1.0);
        }

        return false;
    }

    public void autoUpdate(AutoTransferSequence sequence) {
        if (autoHomePos == -1 || sequence.isRunning()) {
            return;
        }

       if (homeToPosAuto(autoHomePos)) {
           autoHomePos = -1;
           return;
       }
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

    public abstract void run(InputMap map, boolean sequenceActive);
}