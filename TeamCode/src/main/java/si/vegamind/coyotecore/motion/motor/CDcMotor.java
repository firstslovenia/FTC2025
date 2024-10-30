package si.vegamind.coyotecore.motion.motor;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CDcMotor extends Motor {
    private final DcMotor hwMotor;

    public CDcMotor(String name) {
        super(name);
        hwMotor = hardwareMap.get(DcMotor.class, getName());
    }

    public void run(double power) {
        hwMotor.setPower(power);
    }
}
