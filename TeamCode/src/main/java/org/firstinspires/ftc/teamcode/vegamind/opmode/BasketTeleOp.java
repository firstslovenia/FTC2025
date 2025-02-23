package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.SpecimenSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.TransferSequence;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.TransferState;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.FieldCentricDrivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "BASKET Main OpMode")
public class BasketTeleOp extends OpMode {
    // === MECHANISMS ==============================================================================
    private FieldCentricDrivetrain drivetrain;
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
        Hardware.init(hardwareMap, false);
        BetterTelemetry.init(telemetry);

        horizontalLifter = new HorizontalLifter();
        verticalLifter = new VerticalLifter();

        drivetrain = new FieldCentricDrivetrain(hardwareMap, Hardware.getImu());

        primaryInputMap = new PrimaryInputMap(gamepad1);
        secondaryInputMap = new SecondaryInputMap(gamepad2);

        transferSequence = new TransferSequence(horizontalLifter, verticalLifter, primaryInputMap);
        specimenSequence = new SpecimenSequence(secondaryInputMap, verticalLifter);

        horizontalLifter.setHomingSequenceActive(true);
        verticalLifter.setHomingSequenceActive(true);
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
        // Run Mechanisms
        //    drivetrain.run(primaryInputMap);

        if (primaryInputMap.getIMUReset()) {
            Hardware.getImu().resetYaw();
        }

        if (secondaryInputMap.getSpecimenPickupTrigger() && !specimenSequence.isRunning()) {
            specimenSequence.start();
        }

        if (secondaryInputMap.getInitTransfer() && !transferSequence.isRunning() && horizontalLifter.getClaw().getPosition() == 1.0) {
            transferSequence.start();
        }

        if (primaryInputMap.getVibrate()) {
            gamepad2.rumble(500);
        }

        if (secondaryInputMap.getVibrate()) {
            gamepad1.rumble(500);
        }

        if (secondaryInputMap.getCancelSequences() || primaryInputMap.getCancelSequences()) {
            if (transferSequence.isRunning()) {
                transferSequence.reset();
            }

            if (specimenSequence.isRunning()) {
                specimenSequence.reset();
            }
        }

        transferSequence.run();
        specimenSequence.run();

        horizontalLifter.run(secondaryInputMap, (transferSequence.isRunning() && transferSequence.getState() != TransferState.RESET) || specimenSequence.isRunning());
        verticalLifter.run(secondaryInputMap, (transferSequence.isRunning() && transferSequence.getState() != TransferState.RESET) || specimenSequence.isRunning());

        drivetrain.run(primaryInputMap, secondaryInputMap, false);

        BetterTelemetry.print(" transfer sequence state", transferSequence.getState()); //TODO find out why reset isn't working properly
        BetterTelemetry.print("pos", horizontalLifter.getLiftRight().getCurrentPosition());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
}