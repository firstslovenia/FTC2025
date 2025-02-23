package org.firstinspires.ftc.teamcode.vegamind;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BetterTelemetry {
    private static Telemetry telemetry;
    public static void init(Telemetry telemetry) {
        BetterTelemetry.telemetry = telemetry;
        telemetry.setMsTransmissionInterval(250);
    }

    public static <T> void print(String name, T data) {
        telemetry.addData(name, data);
    }

    public static void update() {
        telemetry.update();
    }
}
