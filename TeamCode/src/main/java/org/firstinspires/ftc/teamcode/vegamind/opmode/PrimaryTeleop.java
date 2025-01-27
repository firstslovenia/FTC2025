package org.firstinspires.ftc.teamcode.vegamind.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter;
import org.firstinspires.ftc.teamcode.vegamind.drivetrain.FieldCentricDrivetrain;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapper;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;

//@Disabled
@TeleOp(name = "Primary Op Mode", group = "Testing")
public class PrimaryTeleop extends AbstractTeleop {
    @Override
    public void init() {
        // General setup
        InputMapper.init(new PrimaryInputMap());
        Hardware.init();

        // Mechanisms
        drivetrain = new FieldCentricDrivetrain(hardwareMap, Hardware.getImu());
        lifter = new Lifter(hardwareMap, telemetry, gamepad2);
    }

    public void updateTelemetry() {

    }
}
