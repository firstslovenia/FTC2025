package org.firstinspires.ftc.teamcode.vegamind.input;

import com.qualcomm.robotcore.hardware.Gamepad;

public class PrimaryInputMap extends InputMap{
    public PrimaryInputMap(Gamepad gamepad) {
        super(InputMapType.PRIMARY, gamepad);
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
        return -gamepad.left_stick_y;
    }

    public boolean getPrimeBasket() {
        return gamepad.triangle;
    }

    public boolean getBasketClawRelease() {
        return gamepad.x;
    }

    public double getRotation() {
       return gamepad.right_stick_x;
    }

    public boolean getVibrate() {
        return gamepad.square;
    }
}
