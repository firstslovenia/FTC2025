package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.TransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Main OpMode")
public class TeleOp extends OpMode {
    // === MECHANISMS ==============================================================================
    private Drivetrain drivetrain;
    private VerticalLifter verticalLifter;
    private HorizontalLifter horizontalLifter;

    // === INPUT SCHEMES ===========================================================================

    private PrimaryInputMap primaryInputMap;
    private SecondaryInputMap secondaryInputMap;

    TransferSequence transferSequence;

    @Override
    public void init() {
        primaryInputMap = new PrimaryInputMap(gamepad1);
        secondaryInputMap = new SecondaryInputMap(gamepad2);

        transferSequence = new TransferSequence(horizontalLifter, verticalLifter);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
        horizontalLifter.homingSequence();
        verticalLifter.homingSequence();
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
        // Run Mechanisms
        drivetrain.run();
        verticalLifter.run(secondaryInputMap, transferSequence);
        horizontalLifter.run(secondaryInputMap, transferSequence);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

    protected abstract void updateTelemetry();
}