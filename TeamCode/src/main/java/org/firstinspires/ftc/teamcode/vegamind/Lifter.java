package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Lifter {
    private final DcMotor lifterLeft;
    private final DcMotor lifterRight;

    private final TouchSensor sensorLeft;
    private final TouchSensor sensorRight;

    private final Servo liftClaw;

    private final double MAX_HEIGHT = 3500;

    private boolean last_reset_left = false;
    private boolean last_reset_right = false;

    private boolean homingSequenceActive = true;

    private ElapsedTime accelerationTimer;

    public Lifter() {
        lifterLeft = Hardware.getLifterLeftMotor();
        lifterRight = Hardware.getLifterRightMotor();
        lifterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftClaw = Hardware.getLiftClaw();

        sensorLeft = Hardware.getLifterSensorLeft();
        sensorRight = Hardware.getLifterSensorRight();

        accelerationTimer = new ElapsedTime();
    }

    private double calculatePower(double motor1, double motor2, double inputY) {
        if (inputY == 0 && accelerationTimer != null) {
            accelerationTimer = new ElapsedTime();
        }

        double dist = (motor1 / MAX_HEIGHT - motor2 / MAX_HEIGHT);
        double power = (0.9 + Vtils.clamp(-0.1 * dist * 100, -0.1, 0.1)) * inputY;
        if ((power > 0 && motor1 >= MAX_HEIGHT) || (power < 0 && motor1 < 0)) return 0;

        power *= Math.min(accelerationTimer.milliseconds() / 250, 1);

        return power;
    }

    private void reset_motors() {
        lifterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lifterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void homingSequence() {
        if (sensorRight.getValue() != 0 || sensorLeft.getValue() != 0) {
            homingSequenceActive = false;
            reset_motors();

            return;
        }

        lifterLeft.setPower(-0.2);
        lifterRight.setPower(-0.2);
    }


    private void run_motors(double inputY) {
        if (homingSequenceActive) {
            homingSequence();
            return;
        }

        double posLeft = lifterLeft.getCurrentPosition();
        double posRight = lifterRight.getCurrentPosition();

        if ((sensorLeft.getValue() != -1 && !last_reset_left) || (sensorRight.getValue() != 0 && !last_reset_right)) {
            reset_motors();
        }

        lifterLeft.setPower(calculatePower(posLeft, posRight, inputY));
        lifterRight.setPower(calculatePower(posRight, posLeft, inputY));

        last_reset_left = sensorLeft.getValue() != -1;
        last_reset_right = sensorRight.getValue() != -1;
    }

    private void run_claw(boolean claw, double x) {
        /*if (claw) {
            liftClaw.setPosition(0.0);
            return;
        }*/
        liftClaw.setPosition(x);
    }

    public void run(double inputY, boolean claw) {
        run_motors(inputY);
        //run_claw(claw, inputY);
    }
}