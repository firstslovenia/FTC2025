package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.Servo;

public class ArmClaw {
    private Servo servo;
    Button button;

    public ArmClaw(Servo servo) {
        this.servo = servo;
    }

    public void run(Boolean input) {
        button.update(input);

        if (!button.get_state())
            servo.setPosition(1);
        else
            servo.setPosition(0);
    }
}
