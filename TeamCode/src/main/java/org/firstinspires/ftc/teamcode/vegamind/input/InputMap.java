package org.firstinspires.ftc.teamcode.vegamind.input;

import lombok.Getter;

public interface InputMap {
    double readDriveX();
    double readDriveY();
    double readDriveRot();
    boolean readImuReset();

    double readLifterY();
}
