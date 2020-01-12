package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class RobotHardware {
    private static RobotHardware instance = new RobotHardware();

    public final Spark leftMotor = new Spark(0);
    public final Spark rightMotor = new Spark(1);
  
    public final PWMTalonSRX spinnerMotor = new PWMTalonSRX(2);
    public final Spark spinner2 = new Spark(3);
    public final PWMTalonSRX spinner3 = new PWMTalonSRX(4);
  
    public final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
    public final Joystick controller = new Joystick(1);
    public final Joystick joystick = new Joystick(2);

      
    public final I2C.Port i2cPort = I2C.Port.kOnboard;

    public final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

    public static RobotHardware getInstance() {
        return instance;
    }
}