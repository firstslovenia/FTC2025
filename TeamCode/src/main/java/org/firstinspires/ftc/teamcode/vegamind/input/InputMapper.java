package org.firstinspires.ftc.teamcode.vegamind.input;

import lombok.Getter;

public class InputMapper {
    private static InputMap inputMap = new PrimaryInputMap();

    @Getter
    private static double driveX;
    @Getter
    private static double driveY;
    @Getter
    private static double driveRot;
    @Getter
    private static boolean imuReset;

    @Getter
    private static double lifterY;

    public static void init(InputMap inputMapToSet) {
        inputMap = inputMapToSet;
    }

    public static void update() {
        driveX = inputMap.readDriveX();
        driveY = inputMap.readDriveY();
        driveRot = inputMap.readDriveRot();
        imuReset = inputMap.readImuReset();

        lifterY = inputMap.readLifterY();
    }
}