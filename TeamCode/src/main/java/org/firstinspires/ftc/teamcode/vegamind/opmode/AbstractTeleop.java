package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;

public abstract class AbstractTeleop extends OpMode {
    protected final ElapsedTime runtime = new ElapsedTime();

    // === MECHANISMS ==============================================================================
    protected Drivetrain drivetrain;
    protected Lifter lifter;

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
        drivetrain.run(InputMapper.getDriveY(), InputMapper.getDriveX(), InputMapper.getDriveRot());
        lifter.run(InputMapper.getLifterY(), InputMapper.getClaw());

        // Telemetry
        updateTelemetry();
        telemetry.update();
        /*telemetry.addData("Y", InputMapper.getDriveY());
        telemetry.addData("X", InputMapper.getDriveX());
        telemetry.addData("R", InputMapper.getDriveRot());*/
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

    protected abstract void updateTelemetry();
}