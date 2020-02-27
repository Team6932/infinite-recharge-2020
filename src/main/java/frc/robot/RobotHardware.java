package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.util.Color;



public class RobotHardware {

    public static RobotHardware instance = new RobotHardware();
    
    public final Spark testSpark = new Spark(8);
    public final PWMTalonSRX testTalon = new PWMTalonSRX(9);

    public final Spark leftMotor = new Spark(5);
    public final Spark rightMotor = new Spark(0);
  
    public final PWMTalonSRX hopperLoader = new PWMTalonSRX(3);
    public final PWMTalonSRX launcherLoader = new PWMTalonSRX(6);
    public final PWMTalonSRX wheelMotor = new PWMTalonSRX(11);
    public final PWMTalonSRX ballLauncher1 = new PWMTalonSRX(1);
    public final PWMTalonSRX ballLauncher2 = new PWMTalonSRX(4);
  
    public final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);

    public final Joystick controller = new Joystick(1);
    public final Joystick joystick = new Joystick(2);

      
    public final I2C.Port i2cPort = I2C.Port.kOnboard;

    public final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

    public Ultrasonic ultrasonicSensor1 = new Ultrasonic(8, 9);
    
  // color sensor setup 
  public final ColorMatch m_colorMatcher = new ColorMatch();
  
  // setup color options
  public final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  public final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  public final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  public final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  
  // gyro setup
  //public GyroCompensation gyro = new GyroCompensation();
  public ADIS16448_IMU gyro = new ADIS16448_IMU(); 
  
  public String m_autoSelected;
  
    public static RobotHardware getInstance() {
        return instance;
    }

}
	
