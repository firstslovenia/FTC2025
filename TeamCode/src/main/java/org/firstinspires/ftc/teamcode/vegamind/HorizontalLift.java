package org.firstinspires.ftc.teamcode.vegamind;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class HorizontalLift {
    //Humbly recycled from the Lifter Code :)
    private final DcMotor horiLeft;
    private final DcMotor horiRight;

    private final TouchSensor sensorLeft;
    private final TouchSensor sensorRight;

    private final double MAX_HEIGHT = 3500;

    private boolean last_reset_left = false;
    private boolean last_reset_right = false;

    public HorizontalLift() {
        horiLeft = Hardware.getHorizontalLiftLeftMotor();
        horiRight = Hardware.getHorizontalLiftRightMotor();
        horiLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horiRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horiLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horiRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //TODO: Uncomment when sensors are added to the robot
        //sensorLeft = Hardware.getLifterSensorLeft();
        //sensorRight = Hardware.getLifterSensorRight();
        sensorLeft = null;
        sensorRight = null;
    }

    private double calculatePower(double motor1, double motor2, double inputY) {
        double dist = (motor1 / MAX_HEIGHT - motor2 / MAX_HEIGHT);
        double power = (0.9 + Vtils.clamp(-0.1 * dist * 100, -0.1, 0.1)) * inputY;
        if ((power > 0 && motor1 >= MAX_HEIGHT) || (power < 0 && motor1 < 0)) return 0;
        return power;
    }

    public void run(double inputY) {
        double posLeft = horiLeft.getCurrentPosition();
        double posRight = horiRight.getCurrentPosition();

        BetterTelemetry.print("Pos Left (H)", posLeft);
        BetterTelemetry.print("Pos Right (H)", posRight);

        BetterTelemetry.print("Pow Left (H)", calculatePower(posLeft, posRight, inputY));
        BetterTelemetry.print("Pow Right (H)", calculatePower(posRight, posLeft, inputY));

        BetterTelemetry.print("sl (H)", sensorLeft.isPressed());
        BetterTelemetry.print("sr (H)", sensorRight.isPressed());

        //TODO: This code will not work until sensors are added.
        /*if(sensorLeft.isPressed() && !last_reset_left) {
            horiLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            horiLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (sensorRight.isPressed() && !last_reset_right) {
            horiRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            horiRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }*/

        horiLeft.setPower(calculatePower(posLeft, posRight, inputY));
        horiRight.setPower(calculatePower(posRight, posLeft, inputY));

        //last_reset_left = sensorLeft.isPressed();
        //last_reset_right = sensorRight.isPressed();
    }
}
