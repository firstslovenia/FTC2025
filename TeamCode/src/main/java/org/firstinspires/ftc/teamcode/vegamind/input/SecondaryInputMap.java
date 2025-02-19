package org.firstinspires.ftc.teamcode.vegamind.input;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SecondaryInputMap extends InputMap{

    public SecondaryInputMap(Gamepad gamepad) {
        super(InputMapType.SECONDARY, gamepad);
    }

    @Override
    public boolean getOverride() {
        return gamepad.left_trigger == 1 && gamepad.right_trigger == 1;
    }

    @Override
    public double getDriveX() {
        return gamepad.left_stick_x;
    }

    @Override
    public double getDriveY() {
        return gamepad.left_stick_y;
    }

    public double getHorizontalLift() {
        return gamepad.right_stick_x;
    }

    public boolean getSpecimenLift() {
        return gamepad.triangle;
    }

    public boolean getPreparePickup() {
        return gamepad.share;
    }

    public boolean getInitTransfer() {
        return gamepad.circle;
    }

    public boolean getSpecimenPickupTrigger() {
        return gamepad.cross;
    }

    public boolean getVibrate() {
        return gamepad.square;
    }

    public boolean getHangSpecimen() {
        return gamepad.right_bumper;
    }

    public boolean getClawRotateLeft() {
        return gamepad.dpad_left;
    }

    public boolean getClawRotateRight() {
        return gamepad.dpad_right;
    }

    public boolean getDropIntakeClaw() {
        return gamepad.dpad_down;
    }

    public boolean getCloseIntakeClaw() {
        return gamepad.dpad_up;
    }
}
