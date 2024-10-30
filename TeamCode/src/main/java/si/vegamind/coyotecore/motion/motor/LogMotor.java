package si.vegamind.coyotecore.motion.motor;

public class LogMotor extends Motor {
    public LogMotor(String name) {
        super(name);
    }

    public void run(double power) {
        System.out.println("\t\t" + "Motor " + this.getName() + " power: " + power);
    }
}