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

    ElapsedTime blockTimer;
    int prevPosL, prevPosR;

    public Lifter(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        //this.blinker = blinker;

        lifterRight = hardwareMap.get(DcMotor.class, "liftRight");
        lifterLeft = hardwareMap.get(DcMotor.class, "liftLeft");

        touchLeft = hardwareMap.get(TouchSensor.class, "touchLeft");
        touchRight = hardwareMap.get(TouchSensor.class, "touchRight");

        lifterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        prevPosL = lifterLeft.getCurrentPosition();
        prevPosR = lifterRight.getCurrentPosition();

        blockTimer = new ElapsedTime();

        //lifterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //lifterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

    /*
    public boolean doHomingSequence() {
        DcMotor targetMotorL = lifterLeft;
        DcMotor targetMotorR = lifterRight;
        telemetry.addLine("Running Homing Sequence for Motor: " + targetMotorL.getDeviceName());
        float tolerance = 0.1f;
        float homingPower = -0.4f;

        int prev_positionL = targetMotorL.getCurrentPosition();
        int prev_positionR = targetMotorR.getCurrentPosition();

        ElapsedTime stopwatchL = null;
        ElapsedTime globalSequenceWatchL = new ElapsedTime();
        ElapsedTime stopwatchR = null;
        ElapsedTime globalSequenceWatchR = new ElapsedTime();

        while (true) {
            //TODO: Change the homing allowed time value here to the actual time, this is just a rough estimate
            if (globalSequenceWatchL.milliseconds() >= 10000) {
                //Homing failed / Took too long
                telemetry.addLine("Homing sequence failed for Motor: " + targetMotorL.getDeviceName());
                return false;
            }
            if (globalSequenceWatchR.milliseconds() >= 10000) {
                //Homing failed / Took too long
                telemetry.addLine("Homing sequence failed for Motor: " + targetMotorR.getDeviceName());
                return false;
            }

            //Move Motor down
            targetMotorL.setPower(homingPower);

            if(targetMotorL.getCurrentPosition() == prev_positionL){
                if (stopwatchL == null) {
                    stopwatchL = new ElapsedTime();
                } else {
                    if (stopwatchL.milliseconds() > 100) {
                        targetMotorL.setPower(0);
                        break;
                    }
                }
            } else {
                stopwatchL = null;
            }

            if(targetMotorR.getCurrentPosition() == prev_positionR){
                if (stopwatchR == null) {
                    stopwatchR = new ElapsedTime();
                } else {
                    if (stopwatchR.milliseconds() > 100) {
                        targetMotorR.setPower(0);
                        break;
                    }
                }
            } else {
                stopwatchR = null;
            }
            prev_positionR = targetMotorR.getCurrentPosition();
        }

        telemetry.addLine("Homing sequence successful for Motor: " + targetMotorL.getDeviceName());
        return true;
    }
     */

    public void update() {
        float lifterDir = gamepad.right_trigger - gamepad.left_trigger;
        telemetry.addLine("Current trigger value: " + Float.toString(lifterDir));
        telemetry.addLine("Is Homing: " + Boolean.toString(isHoming));
        float lifterMultiplier = 1.0f;
        /*int topLimit = 3800;

        float bottomDist = Math.abs(lifterLeft.getCurrentPosition());
        float topDist = Math.abs(lifterLeft.getCurrentPosition() - topLimit);

        if (topDist < brakeDist && lifterDir > 0) {
            lifterMultiplier = (float) topDist / brakeDist;
        } else if(bottomDist < brakeDist && lifterDir < 0){
            lifterMultiplier = (float) bottomDist / brakeDist;
        }*/

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
        if (blockTimer.milliseconds() > 100) {

            /*if (lifterLeft.getCurrentPosition() == prevPosL && lifterDir != 0) {
                if (blockTimer == null) {
                    blockTimer = new ElapsedTime();
                } else {
                    if (blockTimer.milliseconds() > 100) {
                        isMotorLBlocked = true;
                    }
                }
            } else if (lifterRight.getCurrentPosition() == prevPosR && lifterDir != 0) {
                if (blockTimer == null) {
                    blockTimer = new ElapsedTime();
                } else {
                    if (blockTimer.milliseconds() > 100) {
                        isMotorRBlocked = true;
                    }
                }
            } else {
                blockTimer = null;
                isMotorRBlocked = false;
                isMotorLBlocked = false;
            }*/

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
        }

        float tolerance = 0.1f;
        if (lifterDir != 0 && !isHoming && (isMotorLBlocked || isMotorRBlocked)) {
            //At least one motor is completely blocked from movement

            //Stop wasting power on blocked motors
            lifterLeft.setPower(0);
            lifterRight.setPower(0);

            //Inform the operator
            if (isMotorLBlocked)
                telemetry.addLine("\nWARNING: Your LEFT LIFTER is COMPLETELY BLOCKED from movement. Your motor input has automatically been terminated and you should move your robot to a SAFER LOCATION.");
            if (isMotorRBlocked)
                telemetry.addLine("\nWARNING: Your RIGHT LIFTER is COMPLETELY BLOCKED from movement. Your motor input has automatically been terminated and you should move your robot to a SAFER LOCATION.");
        }

        if (isHoming) {
            telemetry.addLine("Running Homing Sequence");
        }

        prevPosR = lifterRight.getCurrentPosition();
        prevPosL = lifterLeft.getCurrentPosition();

        /*if (lifterDir != 0 && !isHoming){
            //Block from moving during homing

            float pow = lifterDir * lifterMultiplier;

            if (pow < 0) {
                if (!touchLeft.isPressed())
                    lifterLeft.setPower(lifterDir * lifterMultiplier);
                else{
                    lifterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    lifterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }

                if (!touchRight.isPressed())
                    lifterRight.setPower(lifterDir * lifterMultiplier);
                else {
                    lifterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    lifterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                return;
            }

            lifterRight.setPower(lifterDir * lifterMultiplier);
            lifterLeft.setPower(lifterDir * lifterMultiplier);

            telemetry.addData("left", lifterLeft.getCurrentPosition());
            telemetry.addData("right", lifterRight.getCurrentPosition());

            return;
        }*/

        //lifterRight.setPower(-gamepad.right_stick_y);
        //lifterLeft.setPower(-gamepad.left_stick_y);
    }
}
