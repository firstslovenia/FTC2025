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
    private static double verticalLifterY;
    @Getter
    private static boolean verticalLifterClaw;

    public static void init(InputMap inputMapToSet) {
        inputMap = inputMapToSet;
    }

    public static void update() {
        driveX = inputMap.readDriveX();
        driveY = inputMap.readDriveY();
        driveRot = inputMap.readDriveRot();
        imuReset = inputMap.readImuReset();

        verticalLifterY = inputMap.readVerticalLifterY();
        verticalLifterClaw = inputMap.readVerticalLifterClaw();
    }

    public static boolean getVerticalLifterClaw() { //LOMBOK NE DELA *shrug*
        return verticalLifterClaw;
    }
}