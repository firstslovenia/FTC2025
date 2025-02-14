package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.Servo;

public class Claw {

    private final Servo servo;

    private boolean is_pressed_last = false;
    private boolean isThatOneServo = false;

    public Claw(Servo servo, boolean isThatOneServo) {
        this.servo = servo;
        this.isThatOneServo = isThatOneServo;
    }

    public void run(boolean is_pressed) {
        if (is_pressed == is_pressed_last) {
            return;
        }

        if (!isThatOneServo) {
            servo.setPosition(
                    servo.getPosition() == 0.5f ? 1.0f : 0.5f
            );
        } else {
            servo.setPosition(
                    servo.getPosition() == 0.5f ? 0.5f : 1.0f
            );
        }


        is_pressed_last = is_pressed;
    }

    public void setOpen(boolean open) {
        if (!isThatOneServo){
            servo.setPosition(!open ? 0 : 0.5);
        } else {
            servo.setPosition(!open ? 0.5 : 0);
        }
    }

    public void setPos(double pos) {
        servo.setPosition(pos);
    }
}
