package org.firstinspires.ftc.teamcode.vegamind;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BetterTelemetry {
    private static Telemetry telemetry;
    public static void init(Telemetry telemetry) {
        BetterTelemetry.telemetry = telemetry;
    }

    static void print(String name, double data) {
        telemetry.addData(name, data);
    }
}
