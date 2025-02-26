package org.firstinspires.ftc.teamcode.vegamind.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vegamind.Hardware;
import org.firstinspires.ftc.teamcode.vegamind.input.PrimaryInputMap;
import org.firstinspires.ftc.teamcode.vegamind.input.SecondaryInputMap;

public abstract class Drivetrain extends SampleMecanumDrive {
    protected IMU imu;
    protected final DcMotor leftRearMotor;
    protected final DcMotor rightRearMotor;
    protected final DcMotor leftFrontMotor;
    protected final DcMotor rightFrontMotor;

    public Drivetrain(HardwareMap hardwareMap, IMU imu) {
        super(hardwareMap);
        this.imu = imu;
        imu.resetYaw();
        leftRearMotor = Hardware.getLeftRearMotor();
        rightRearMotor = Hardware.getRightRearMotor();
        leftFrontMotor = Hardware.getLeftFrontMotor();
        rightFrontMotor = Hardware.getRightFrontMotor();
    }

    public abstract void run(PrimaryInputMap primaryInputMap,
                             SecondaryInputMap secondaryInputMap);
}
