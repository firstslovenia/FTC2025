package si.vegamind.coyotecore.motion.motor;

public class LogMotor extends Motor {
    public LogMotor(String name) {
        super(name);
    }

    public void run(float power) {
        System.out.println("\t\t" + "Motor " + this.getName() + " power: " + power);
    }
}