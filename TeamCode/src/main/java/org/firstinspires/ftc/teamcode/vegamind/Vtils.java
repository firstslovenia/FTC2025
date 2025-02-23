package org.firstinspires.ftc.teamcode.vegamind;

// Idi bidi utils class z bits and bobs of cool thingies
public class Vtils {
    public static double clamp(double input, double low, double high) {
        return Math.max(low, Math.min(input, high));
    }
}
