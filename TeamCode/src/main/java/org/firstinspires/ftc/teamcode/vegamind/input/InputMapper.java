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
    @Getter
    private static boolean specimenClaw;
    @Getter
    private static double horizontalLifterX;
    @Getter
    private static boolean horizontalSwivelPrime;
    @Getter
    private static boolean horizontalLifterClaw;
    @Getter
    private static boolean transferSequenceInit;
    @Getter
    private static double clawSwivel;

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
        specimenClaw = inputMap.readSpecimenClaw();

        horizontalLifterX = inputMap.readHorizontalLifterX();
        horizontalSwivelPrime = inputMap.readHorizontalSwivelPrime();
        horizontalLifterClaw = inputMap.readHorizontalLifterClaw();
        transferSequenceInit = inputMap.readTransferSequenceInit();

        clawSwivel = inputMap.readClawSwivel();
    }

    public static boolean getVerticalLifterClaw() { //LOMBOK NE DELA *shrug*
        return verticalLifterClaw;
    }

    public static double getHorizontalLifterX() {
        return horizontalLifterX;
    }

    public static boolean getHorizontalSwivelPrime() {
        return horizontalSwivelPrime;
    }

    public static boolean getHorizontalLifterClaw() {
        return horizontalLifterClaw;
    }

    public static boolean getTransferSequenceInit() {
        return transferSequenceInit;
    }

    public static boolean getSpecimenClaw() {
        return specimenClaw;
    }
}