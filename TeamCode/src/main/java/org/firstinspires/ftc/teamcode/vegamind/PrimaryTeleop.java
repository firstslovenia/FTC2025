/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

// import si.vegamind.coyotecore.motion.MotionManager;
// import si.vegamind.coyotecore.motion.MotorMoveBuilder;

/*
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name = "Primary Op Mode", group = "Testing")
// @Disabled
public class PrimaryTeleop extends OpMode {


    //IMU imu;
    /*
     * Code to run ONCE when the driver hits INIT
     */

    private Lifter lifter;
    private Gestell gestell;

    void initIMU() {
        /*IMU.Parameters parameters;

        parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        );

        imu = hardwareMap.get(IMU.class, "imu1");

        imu.initialize(parameters);*/
    }

    @Override
    public void init() {

        lifter = new Lifter(hardwareMap, telemetry, gamepad1);
        gestell = new Gestell(hardwareMap, gamepad1, telemetry);

        initIMU();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        lifter.update();
        gestell.update();
    }


//0 left 1 right
    @Deprecated
    boolean homingSequence(DcMotor targetMotor) {
        telemetry.addLine("Running Homing Sequence for Motor: " + targetMotor.getDeviceName());
        float tolerance = 0.1f;
        float homingPower = 0.2f;

        int prev_position = targetMotor.getCurrentPosition();

        ElapsedTime stopwatch = null;
        ElapsedTime globalSequenceWatch = new ElapsedTime();

        while (true) {
            //TODO: Change the homing allowed time value here to the actual time, this is just a rough estimate
            if (globalSequenceWatch.milliseconds() >= 25000) {
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
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}
