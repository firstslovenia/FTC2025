package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.Servo;

public class Claw {

    private final Servo servo;

    private boolean is_pressed_last = false;

    public Claw(Servo servo) {
        this.servo = servo;
    }

    public void run(boolean is_pressed) {
        if (is_pressed == is_pressed_last) {
            return;
        }

        servo.setPosition(
                servo.getPosition() == 0.0f ? 1.0f : 0.0f
        );

        is_pressed_last = is_pressed;
    }

    public void setOpen(boolean open) {
        servo.setPosition(open ? 1.0 : 0.0);
    }
}
