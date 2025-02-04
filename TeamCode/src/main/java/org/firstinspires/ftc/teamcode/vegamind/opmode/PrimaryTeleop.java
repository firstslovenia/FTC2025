package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.HorizontalLifter;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.VerticalLifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.FieldCentricDrivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;

//@Disabled
@TeleOp(name = "Primary Op Mode", group = "Testing")
public class PrimaryTeleop extends AbstractTeleop {
    @Override
    public void init() {
        // General setup
        Hardware.init(hardwareMap);
        BetterTelemetry.init(telemetry);
        InputMapper.init(new PrimaryInputMap(gamepad1, gamepad2));

        // Mechanisms
        drivetrain = new FieldCentricDrivetrain(hardwareMap, Hardware.getImu());
        verticalLifter = new VerticalLifter();
        horizontalLifter = new HorizontalLifter();
    }

    public void updateTelemetry() {

    }
}
