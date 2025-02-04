package org.firstinspires.ftc.teamcode.vegamind.input;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.Gamepad;

public class PrimaryInputMap extends InputMap {
    public PrimaryInputMap(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    @Override
    public double readDriveX() {
        return gamepad1.left_stick_x;
    }

    @Override
    public double readDriveY() {
        return gamepad1.left_stick_y;
    }

    @Override
    public double readDriveRot() {
        return gamepad1.right_stick_x;
    }

    @Override
    public boolean readImuReset() {
        return gamepad1.options;
    }

    @Override
    public double readVerticalLifterY() {
        return gamepad2.right_stick_y;
    }

    @Override
    public boolean readVerticalLifterClaw() {
        return gamepad2.right_stick_button;
    }
}
