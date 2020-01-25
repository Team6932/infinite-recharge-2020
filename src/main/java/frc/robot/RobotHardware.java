package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.util.Color;

public class RobotHardware {
    public static RobotHardware instance = new RobotHardware();

    
    public final Spark testSpark = new Spark(8);
    public final PWMTalonSRX testTalon = new PWMTalonSRX(9);

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

    
  // color sensor setup
  public final ColorMatch m_colorMatcher = new ColorMatch();
  
  // setup color options
  public final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  public final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  public final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  public final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  
  // gyro setup
  public ADIS16448_IMU.IMUAxis m_yawActiveAxis;
  public final ADIS16448_IMU m_imu = new ADIS16448_IMU();
  
  public String m_autoSelected;
  
    public static RobotHardware getInstance() {
        System.out.println("Instance Created " + instance);
        return instance;
    }

}