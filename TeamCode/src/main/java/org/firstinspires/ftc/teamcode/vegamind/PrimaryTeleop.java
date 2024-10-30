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

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.LOGO_FACING_DIR;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.USB_FACING_DIR;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kStatic;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
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
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();

    private IMU imu;
    IMU.Parameters imuParameters = new IMU.Parameters(new RevHubOrientationOnRobot(
            LOGO_FACING_DIR,
            USB_FACING_DIR));

    private TeleopMecanumDrive mecanumDrive;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        mecanumDrive = new TeleopMecanumDrive(hardwareMap);
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(imuParameters);

        // CoyoteCore
        //MotionManager.addMotion(new MotorMoveBuilder("test motor move", "test_motor", 1f)
        //        .setOnComplete(() -> {
        //            telemetry.addData("RUNNING MOTOR", "YES SIR");
        //            MotionManager.startMotion("test motor move");
        //        })
        //        .setExitCondition(() -> {
        //            return gamepad1.cross;
        //        })
        //
        //        .build());
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
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        // Movement
        // Reset field centric rotation
        if (gamepad1.options) {
            imu.resetYaw();
        }

        double rot = -gamepad1.right_stick_x;       // Rotation
        double forward = -gamepad1.left_stick_y;     // Forward power
        double strafe = gamepad1.left_stick_x;     // Strafe power

        double robotDirection = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = strafe * Math.cos(-robotDirection) - forward * Math.sin(-robotDirection);
        double rotY = strafe * Math.sin(-robotDirection) + forward * Math.cos(-robotDirection);

        Pose2d move = new Pose2d(
                rot,
                rotY,
                rotX
        );
        mecanumDrive.drive(move);

        // Telemetry
        telemetry.addLine("TeleOP");
        telemetry.addData("Robot Direction", robotDirection);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
