package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.Sequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.SpecimenSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.TransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.RobotCentricDrivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Main OpMode")
public class TeleOp extends OpMode {
    // === MECHANISMS ==============================================================================
    private RobotCentricDrivetrain drivetrain;
    private VerticalLifter verticalLifter;
    private HorizontalLifter horizontalLifter;

    // === INPUT SCHEMES ===========================================================================

    private PrimaryInputMap primaryInputMap;
    private SecondaryInputMap secondaryInputMap;

    TransferSequence transferSequence;
    SpecimenSequence specimenSequence;

//    Sequence currentSequence;

    @Override
    public void init() {
        Hardware.init(hardwareMap);
        BetterTelemetry.init(telemetry);

        horizontalLifter = new HorizontalLifter();
        verticalLifter = new VerticalLifter();

        drivetrain = new RobotCentricDrivetrain(hardwareMap, Hardware.getImu());

        primaryInputMap = new PrimaryInputMap(gamepad1);
        secondaryInputMap = new SecondaryInputMap(gamepad2);

        transferSequence = new TransferSequence(horizontalLifter, verticalLifter, secondaryInputMap);
        specimenSequence = new SpecimenSequence(secondaryInputMap, verticalLifter);

        horizontalLifter.setHomingSequenceActive(true);
        verticalLifter.setHomingSequenceActive(true);
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
    //    drivetrain.run(primaryInputMap);
        if (secondaryInputMap.getSpecimenPickupTrigger() && !specimenSequence.isRunning()) {
            specimenSequence.start();
        }

        if (secondaryInputMap.getInitTransfer() && !transferSequence.isRunning()) {
            transferSequence.start();
        }

        transferSequence.run();
        specimenSequence.run();

        horizontalLifter.run(secondaryInputMap, transferSequence.isRunning() || specimenSequence.isRunning());
        verticalLifter.run(secondaryInputMap, transferSequence.isRunning() || specimenSequence.isRunning());

        drivetrain.run(primaryInputMap);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}