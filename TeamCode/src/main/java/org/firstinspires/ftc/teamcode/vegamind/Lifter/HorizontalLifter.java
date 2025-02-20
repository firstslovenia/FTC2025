package org.firstinspires.ftc.teamcode.vegamind.Lifter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.vegamind.BetterTelemetry;
import org.firstinspires.ftc.teamcode.vegamind.Button;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.Lifter.Sequence.Sequence;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.InputMapType;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

import lombok.Getter;

public class HorizontalLifter extends Lifter{

    static final double SWIVEL_TO_TRESHOLD_270 = 150; // TODO test

    @Getter
    private Servo swivelServoRight;

    @Getter
    private Servo swivelServoLeft;

    @Getter
    Servo claw;

    @Getter
    Servo clawSwivel;

    ElapsedTime extendLifterTimer;
    ElapsedTime verticalLiftGrace;

    Button swivelPrimeToggle;

    boolean last_claw_swivel_change = false;

    public HorizontalLifter() {
        super();

        this.liftRight = Hardware.getHorizontalLiftRightMotor();
        this.liftLeft = Hardware.getHorizontalLiftLeftMotor();

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.sensorLeft = Hardware.getHorizontalLiftSensorLeft();
        this.sensorRight = Hardware.getHorizontalLiftSensorRight();

        claw = Hardware.getHorizontalLiftClaw();
        claw.setPosition(1);

        swivelServoLeft = Hardware.getHorizontalSwivelLeft();
        swivelServoLeft.setPosition(degToServoPosSwivel(220));

        swivelServoRight = Hardware.getHorizontalSwivelRight();
        swivelServoRight.setPosition(degToServoPosSwivel(220));

        clawSwivel = Hardware.getHorizontalClawSwivel();
        clawSwivel.scaleRange(0.3333, 1);

        clawSwivel.setPosition(degToServoPosClawSwivel(0));

        swivelPrimeToggle = new Button();
    }

    public static double degToServoPosSwivel(double deg) {
        return (270 - deg) / 600 + 0.66666666667;
    }
    public static double degToServoPosClawSwivel(double deg) {
        return (deg) / 180;
    }

    /*private void run_swivel() {
        swivelPrimeToggle.update(InputMapper.getHorizontalSwivelPrime());

        if (swivelPrimeToggle.isState()){
            swivelServoLeft.setPosition(degToServoPosSwivel(0));
            swivelServoRight.setPosition(degToServoPosSwivel(0));
            claw.setPosition(0.5);
            return;
        }

        claw.setPosition(1);
        swivelServoLeft.setPosition(degToServoPosSwivel(240));
        swivelServoRight.setPosition(degToServoPosSwivel(240));
    }*/

  /*  private void run_claw_swivel() {
        if (InputMapper.getClawSwivel() != 0 && !last_claw_swivel_change) {
            clawSwivel.setPosition(
                    clawSwivel.getPosition() + (InputMapper.getClawSwivel() / 2)
            );
        }

        last_claw_swivel_change = InputMapper.getClawSwivel() != 0;
    }*/

    private void updateIntake(SecondaryInputMap map) {
        if (map.getPreparePickup()) {
            swivelServoLeft.setPosition(degToServoPosSwivel(100));
            swivelServoRight.setPosition(degToServoPosSwivel(100));
            claw.setPosition(0.5);
        }

        if (map.getDropIntakeClaw()) {
            swivelServoLeft.setPosition(degToServoPosSwivel(0));
            swivelServoRight.setPosition(degToServoPosSwivel(0));
        }

        if(map.getCloseIntakeClaw()) {
            claw.setPosition(1);
        }

        if (map.getRetractHorizontalSwivel()) {
            swivelServoLeft.setPosition(degToServoPosSwivel(180));
            swivelServoRight.setPosition(degToServoPosSwivel(180));
        }

        double swivelDir = map.getClawRotateRight() ? 1 : 0 - (map.getClawRotateLeft() ? 1 : 0);

        if (swivelDir != 0 == last_claw_swivel_change) {
            last_claw_swivel_change = swivelDir != 0;
            return;
        }

        clawSwivel.setPosition(clawSwivel.getPosition() + degToServoPosClawSwivel(swivelDir * 30));

        last_claw_swivel_change = swivelDir != 0;


    }

    @Override
    public void run(InputMap map, boolean sequenceActive) {
        if (map.getType() == InputMapType.OVERRIDE) {
            throw new RuntimeException("What the fuck this should not happen");
        }

        if (sequenceActive) {
            homingSequence();
            return;
        }

        SecondaryInputMap secondaryInputMap = (SecondaryInputMap)map;

        run_motors(secondaryInputMap.getHorizontalLift());
        updateIntake(secondaryInputMap);
        //run_swivel();
        //run_claw_swivel();
    }
}
