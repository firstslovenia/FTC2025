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

    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    //private Blinker blinker;

    private DcMotor lifterLeft;
    private DcMotor lifterRight;
    private TouchSensor touchLeft;
    private TouchSensor touchRight;
    private Gamepad gamepad;

    private int topLimit;
    private int brakeDist;

    boolean isHoming = false;
    boolean isMotorLBlocked = false;
    boolean isMotorRBlocked = false;

    private final int LIFTER_HEIGHT = 3500;

    ElapsedTime blockTimer;
    int prevPosL, prevPosR;

    public Lifter(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        //this.blinker = blinker;

        lifterLeft = Hardware.getLifterLeftMotor();
        lifterRight = Hardware.getLifterLeftMotor();

        //touchLeft = hardwareMap.get(TouchSensor.class, "touchLeft");
        //touchRight = hardwareMap.get(TouchSensor.class, "touchRight");

        lifterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        prevPosL = lifterLeft.getCurrentPosition();
        prevPosR = lifterRight.getCurrentPosition();

        blockTimer = new ElapsedTime();

        //lifterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //lifterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void calculateMotorPower() {
        int leftPos = lifterLeft.getCurrentPosition();
        int rightPos = lifterRight.getCurrentPosition();

        int diff = leftPos - rightPos; //NEG DIFF = right faster, POS DIFF = left faster

        if (diff < 0.0d) {
            diff *= -1; //Make it positive
            double ratio = diff / LIFTER_HEIGHT;
            double newPower = 0.9d + (0.1d * Vtils.clamp(ratio, -200, 200));
            lifterLeft.setPower(newPower);
        } else if (diff > 0.0d) {
            double ratio = diff / LIFTER_HEIGHT;
            double newPower = 0.9d + (0.1d * Vtils.clamp(ratio, -200, 200));
            lifterRight.setPower(newPower);
        }
    }

    public boolean doHomingSequence(DcMotor targetMotor) {
        telemetry.addLine("Running Homing Sequence for Motor: " + targetMotor.getDeviceName());
        float tolerance = 0.1f;
        float homingPower = -0.4f;

        int prev_position = targetMotor.getCurrentPosition();

        ElapsedTime stopwatch = null;
        ElapsedTime globalSequenceWatch = new ElapsedTime();

        while (true) {
            //TODO: Change the homing allowed time value here to the actual time, this is just a rough estimate
            if (globalSequenceWatch.milliseconds() >= 10000) {
                //Homing failed / Took too long
                telemetry.addLine("Homing sequence failed for Motor: " + targetMotor.getDeviceName());
                return false;
            }

            //Move Motor down
            targetMotor.setPower(homingPower);

            if(targetMotor.getCurrentPosition() == prev_position){
                if (stopwatch == null) {
                    stopwatch = new ElapsedTime();
                } else {
                    if (stopwatch.milliseconds() > 100) {
                        targetMotor.setPower(0);
                        //targetMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        break;
                    }
                }
            } else {
                stopwatch = null;
            }
            prev_position = targetMotor.getCurrentPosition();
        }

        telemetry.addLine("Homing sequence successful for Motor: " + targetMotor.getDeviceName());
        return true;
    }

    public void run() {
        float lifterDir = gamepad.right_trigger - gamepad.left_trigger;
        telemetry.addLine("Current trigger value: " + Float.toString(lifterDir));
        telemetry.addLine("Is Homing: " + Boolean.toString(isHoming));
        float lifterMultiplier = 1.0f;

        if (gamepad.triangle && !isHoming && !isMotorLBlocked && !isMotorRBlocked) {
            //Start homing sequence
            isHoming = true;
            lifterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            lifterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            new Thread(() -> {
                boolean resLeft = doHomingSequence(lifterLeft);
            }).start();

            new Thread(() -> {
                boolean resRight = doHomingSequence(lifterRight);
            }).start();
            isHoming = false;
        }

        if (lifterDir != 0 && !isHoming && !isMotorLBlocked && !isMotorRBlocked) {
            //Block lifter from manual movement during the homing sequence
            float pow = lifterDir * lifterMultiplier;
            telemetry.addLine("Lifter power: " + Float.toString(pow));
            //lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lifterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            lifterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            lifterRight.setPower(lifterDir * lifterMultiplier);
            lifterLeft.setPower(lifterDir * lifterMultiplier);

            telemetry.addData("left", lifterLeft.getCurrentPosition());
            telemetry.addData("right", lifterRight.getCurrentPosition());
        }

        if (lifterDir == 0 && !isHoming && !isMotorLBlocked && !isMotorRBlocked) {
            lifterRight.setPower(0);
            lifterLeft.setPower(0);
            lifterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lifterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        //Check for motor blockages
        /*if (blockTimer.milliseconds() > 100) {

            //Check for block every 100 milliseconds
            if ((lifterLeft.getCurrentPosition() == prevPosL) && lifterDir != 0) {
                isMotorLBlocked = true;
                //NOTE: Actual handling of real blockages is done in a different section of the Lifter code
            } else {
                isMotorLBlocked = false;
            }

            if ((lifterRight.getCurrentPosition() == prevPosR) && lifterDir != 0) {
                isMotorRBlocked = true;
            } else {
                isMotorRBlocked = false;
            }
            blockTimer = new ElapsedTime();
        }*/

        float tolerance = 0.1f;
        /*if (lifterDir != 0 && !isHoming && (isMotorLBlocked || isMotorRBlocked)) {
            //At least one motor is completely blocked from movement

            //Stop wasting power on blocked motors
            lifterLeft.setPower(0);
            lifterRight.setPower(0);

            //Inform the operator
            if (isMotorLBlocked)
                telemetry.addLine("\nWARNING: Your LEFT LIFTER is COMPLETELY BLOCKED from movement. Your motor input has automatically been terminated and you should move your robot to a SAFER LOCATION.");
            if (isMotorRBlocked)
                telemetry.addLine("\nWARNING: Your RIGHT LIFTER is COMPLETELY BLOCKED from movement. Your motor input has automatically been terminated and you should move your robot to a SAFER LOCATION.");
        }*/

        if (isHoming) {
            telemetry.addLine("Running Homing Sequence");
        }

        calculateMotorPower();

        prevPosR = lifterRight.getCurrentPosition();
        prevPosL = lifterLeft.getCurrentPosition();
    }
}