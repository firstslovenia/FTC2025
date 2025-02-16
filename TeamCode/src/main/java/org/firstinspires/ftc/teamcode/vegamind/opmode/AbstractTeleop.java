package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public abstract class AbstractTeleop extends OpMode {
    protected final ElapsedTime runtime = new ElapsedTime();

    // === MECHANISMS ==============================================================================
    protected Drivetrain drivetrain;
    protected VerticalLifter verticalLifter;
    protected HorizontalLifter horizontalLifter;

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
        InputMapper.update();

        // Imu Reset
        if (InputMapper.isImuReset()) Hardware.getImu().resetYaw();

        // Run Mechanisms
        drivetrain.run();
        verticalLifter.run();
        horizontalLifter.run();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

    protected abstract void updateTelemetry();
}