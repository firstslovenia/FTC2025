package si.vegamind.coyotecore.motion;

import si.vegamind.coyotecore.exceptions.MotorException;
import si.vegamind.coyotecore.motion.motor.Motor;

import java.util.HashMap;
import java.util.HashSet;

public class MotorManager {
    private static HashMap<String, Motor> motorMap = new HashMap<>();
    private static HashSet<Motor> motorSet = new HashSet<>();

    public static Motor getMotor(String name) throws MotorException {
        if (!motorMap.containsKey(name)) {
            throw new MotorException("Motor " + name + " not found");
        }

        return motorMap.get(name);
    }

    public static Motor addMotor(Motor motor) throws MotorException {
        if (motorMap.containsKey(motor.getName())) {
            throw new MotorException("Motor " + motor.getName() + " already exists");
        }

        if (motorSet.contains(motor)) {
            throw new MotorException("Motor with that name" + motor.getName() + " already exists");
        }

        motorSet.add(motor);
        return motorMap.put(motor.getName(), motor);
    }
}
