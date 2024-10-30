package org.firstinspires.ftc.teamcode.vegamind;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class TeleopMecanumDrive extends SampleMecanumDrive {
    public TeleopMecanumDrive(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    public void drive(Pose2d pose2d) {
        this.setDrivePower(pose2d);
    }
}
