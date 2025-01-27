package org.firstinspires.ftc.teamcode.vegamind.input;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.Gamepad;

public class PrimaryInputMap implements InputMap {

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
    public double readLifterY() {
        return gamepad2.right_stick_y;
    }
}
