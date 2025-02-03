package org.firstinspires.ftc.teamcode.vegamind.input;

import lombok.Getter;

public class InputMapper {
    private static InputMap inputMap;

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
    @Getter
    private static boolean claw;

    public static void init(InputMap inputMapToSet) {
        inputMap = inputMapToSet;
    }

    public static void update() {
        driveX = inputMap.readDriveX();
        driveY = inputMap.readDriveY();
        driveRot = inputMap.readDriveRot();
        imuReset = inputMap.readImuReset();

        lifterY = inputMap.readLifterY();
        claw = inputMap.readClaw();
    }

    public static boolean getClaw() { // VID PROSIM POGLEJ ZAKAJ TA GOD FORSAKEN LOMBOK NE DELA
        return claw;
    }
}