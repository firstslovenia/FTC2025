package org.firstinspires.ftc.teamcode.vegamind.input;

import android.renderscript.ScriptGroup;

import com.qualcomm.robotcore.hardware.Gamepad;

import lombok.Getter;
import lombok.Setter;

public abstract class InputMap {

    @Getter
    protected InputMapType type;

    @Setter
    protected Gamepad gamepad;

    InputMap(InputMapType type, Gamepad gamepad) {
        this.type = type;
        this.gamepad = gamepad;
    }


    public abstract boolean getOverride();
    public abstract double getDriveX();
    public abstract double getDriveY();
}
