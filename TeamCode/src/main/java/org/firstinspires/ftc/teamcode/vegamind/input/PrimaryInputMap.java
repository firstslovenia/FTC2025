package org.firstinspires.ftc.teamcode.vegamind.input;

import com.qualcomm.robotcore.hardware.Gamepad;

public class PrimaryInputMap extends InputMap{
    public PrimaryInputMap(Gamepad gamepad) {
        super(InputMapType.PRIMARY, gamepad);
    }

    @Override
    public boolean getOverride() {
        return gamepad.circle && gamepad.share;
    }

    @Override
    public double getDriveX() {
        return gamepad.left_stick_x;
    }

    @Override
    public double getDriveY() {
        return gamepad.left_stick_y;
    }

    public boolean getSlowdown() {
        return gamepad.right_bumper;
    }

    public boolean getContinueTransferSequence() {
        return gamepad.triangle;
    }

    public double getRotation() {
       return gamepad.left_trigger - gamepad.right_trigger;
    }

    public boolean getVibrate() {
        return gamepad.square;
    }

    public boolean getIMUReset() { return gamepad.left_stick_button; }


    public boolean getCancelSequences() {
        return gamepad.left_bumper;
    }
}
