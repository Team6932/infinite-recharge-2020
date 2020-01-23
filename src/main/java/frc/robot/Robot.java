/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//Imports

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



import com.analog.adis16448.frc.ADIS16448_IMU;
import com.analog.adis16448.frc.ADIS16448_IMU.IMUAxis;

import java.util.Map;

import com.revrobotics.ColorMatch;


public class Robot extends TimedRobot {

  public final Spark testSpark = new Spark(8);
  public final PWMTalonSRX testTalon = new PWMTalonSRX(9);

  public final PWMTalonSRX leftMotor = new PWMTalonSRX(0);
  public final PWMTalonSRX rightMotor = new PWMTalonSRX(1);

  public final PWMTalonSRX spinnerMotor = new PWMTalonSRX(2);
  public final Spark spinner2 = new Spark(3);
  public final PWMTalonSRX spinner3 = new PWMTalonSRX(4);

  public final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
  public final Joystick controller = new Joystick(1);
  public final Joystick joystick = new Joystick(2);

    
  public final I2C.Port i2cPort = I2C.Port.kOnboard;

  public final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

  // unknown....
  private ADIS16448_IMU.IMUAxis m_yawActiveAxis;
  private final ADIS16448_IMU m_imu = new ADIS16448_IMU();

  // pulls variables from RobotHardware.java file
  //RobotHardware robot = RobotHardware.getInstance();

  // booleans
  boolean colorSpin = false;
  boolean searchColor = false;
  boolean driveInverted = false;
  boolean ballLauncher = false;
  boolean forward = false;
  boolean testMotor = false;
  boolean testedMotor = false;
  boolean straightDrive = false;

  // booleans for location control
  boolean m_runCal = false;
  boolean m_configCal = false;
  boolean m_reset = false;
  boolean m_setYawAxis = false;

  // intergers
  int spinNumber = 0;
  
  //doubles
  double testMotorSpeed = 0;

  // strings
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private static final String kYawDefault = "Z-Axis";
  private static final String kYawXAxis = "X-Axis";
  private static final String kYawYAxis = "Y-Axis";

  private String m_yawSelected;

  private String m_autoSelected;

  // shuffleboard setup
  private NetworkTableInstance ntinst = NetworkTableInstance.getDefault();

  NetworkTable dsInfo = ntinst.getTable("dsInfo");

  public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();
  private final SendableChooser<String> m_autoChooser = new SendableChooser<>();
  private final SendableChooser<String> m_yawChooser = new SendableChooser<>();

  // color sensor setup
  private final ColorMatch m_colorMatcher = new ColorMatch();

  // setup color options
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  // color variables
  private Color kSelectedTarget = null;
  private Color kPreviousColor = null;

  // shuffleboard variables
  private ShuffleboardTab tab = Shuffleboard.getTab("LiveWindow");
  private NetworkTableEntry spins;

  @Override
  public void robotInit() {

    

    m_imu.reset();
    //m_imu.configCalTime(1);
    m_imu.calibrate();
    //m_imu.reset();

    // camera setup
    CameraServer.getInstance().startAutomaticCapture();

    // setup spin import (from shuffleboard)
    spins = tab.add("Spins", 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 3, "max", 5)) // specify
                                                                                                                    // widget
                                                                                                                    // properties
                                                                                                                    // here
        .getEntry();

    // returns the value "spins" from shuffleboard
    dsInfo.getEntry("spins").setValue(3);

    // adds color chooser options to shuffleboard
    colorChooser.setDefaultOption("Blue", 1);
    colorChooser.addOption("Green", 2);
    colorChooser.addOption("Red", 3);
    colorChooser.addOption("Yellow", 4);

    // sends the color chooser to shuffleboard
    SmartDashboard.putData("colorChooser", colorChooser);

    // adds color options to camera sensor code
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);

    // automated stage option chooser
    m_autoChooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_autoChooser.addOption("My Auto", kCustomAuto);

    // uploads the automation choices to shuffleboard
    SmartDashboard.putData("Auto choices", m_autoChooser);

    // adds boolean output to shuffleboard
    SmartDashboard.putBoolean("RunCal", false);
    SmartDashboard.putBoolean("ConfigCal", false);
    SmartDashboard.putBoolean("Reset", false);
    SmartDashboard.putBoolean("SetYawAxis", false);
  
  }

  @Override
  public void robotPeriodic() {
    System.out.println(testMotor);
    System.out.println(testMotorSpeed);

    System.out.println("Gyro Angle = " + m_imu.getAngle());

    // sends different angles to shuffleboard
    SmartDashboard.putNumber("YawAngle", m_imu.getGyroAngleZ());

    // gets the selected color and sets it to a variable, then checks for the
    // closest color and makes that a variable.
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    // sets the selected shuffleboard color to an actual color that can be checked.

    switch (colorChooser.getSelected()) {
    case 1: {
      kSelectedTarget = kBlueTarget;
      break;
    }
    case 2: {
      kSelectedTarget = kGreenTarget;
      break;
    }
    case 3: {
      kSelectedTarget = kRedTarget;
      break;
    }
    case 4: {
      kSelectedTarget = kYellowTarget;
      break;
    }
    default: {
      kSelectedTarget = null;
      break;
    }

    }

    // sends misc. inputs to shuffleboard.
    dsInfo.getEntry("spinning").setValue(colorSpin);
    dsInfo.getEntry("searchColor").setValue(searchColor);
    dsInfo.getEntry("Inverted").setValue(driveInverted);
    dsInfo.getEntry("Spins Counted").setValue(spinNumber / 8);
    dsInfo.getEntry("Testing Motor").setValue(testMotor);
    dsInfo.getEntry("drive straight?!?!??!?!?!?!?!?").setValue(straightDrive);

  }

  public double gyroAxis() {
    double axis = m_imu.getAngle();
    return axis * 0.1;
  }

  // TODO Finish adding code

  @Override
  public void teleopPeriodic() {

    // gets the color from the color sensor, and then sets a variable for the
    // detected color.
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    // detects button inputs and inverts a variable (basically makes the button act
    // like switches instead of buttons)
    if (controller.getRawButtonPressed(1))
      searchColor = !searchColor;
    if (controller.getRawButtonPressed(10))
      colorSpin = !colorSpin;
    if (joystick.getRawButtonPressed(2))
      driveInverted = !driveInverted;
    if (controller.getRawButtonPressed(3))
      ballLauncher = !ballLauncher;
    if (joystick.getRawButtonPressed(7))
      forward = !forward;
    if (controller.getRawButtonPressed(9)) 
      testMotor = !testMotor;
    if (controller.getRawButtonPressed(5))
      straightDrive = !straightDrive;

    // debugging (prints out special variables of my choosing)
    /*System.out.println("Spin number = " + spinNumber);
    System.out.println("previous color = " + kPreviousColor);
    System.out.println("current color = " + match.color);*/

    // controls the ball launcher
    if (ballLauncher) {// ball launcher control

      // sets the two motors controlling the ball launcher to be inverted to eachother
      // (so it fires properly)
      spinner2.set(1);
      spinner3.set(-1);

    } else {// stops the motors if the option is off (Default)

      spinner2.stopMotor();
      spinner3.stopMotor();

    }

    // controls the color wheel motors and color sensor
    if (searchColor && !colorSpin) {// if the color specific wheel command is being run, and not the spin ammount
                                    // command
      if (kSelectedTarget != match.color) {// if the color seen is not equal to the color chosen by the user
        spinnerMotor.set(0.3);// spin the motor
      } else {// if the colors are the same
        spinnerMotor.stopMotor();// stop the motor
        colorSpin = false;// disable command
        // TODO spinn motor X amount to put sensor in right spot.
      }
    } else if (colorSpin && !searchColor) {// if the spin ammount is run and not the color searcher
      if (spinNumber / 8 < spins.getDouble(3.0)) {// spin the wheel untill the number of complete spins is equal to the
                                                  // user specified number

        if (spinNumber == 0) {
          kPreviousColor = match.color;
        }

        spinnerMotor.set(0.3);

        if (kPreviousColor != match.color) {// adds 1 to the spin number every time it sees a new color (this is divided
                                            // by 8)
          spinNumber++;
          kPreviousColor = match.color;// sets the current color to the color to the previous color
        }
      } else {// stops the motor if the number of full spins is reached
        spinnerMotor.stopMotor();
        colorSpin = false;// stops the command
        spinNumber = 0;// resets the variable for future use
      }
    } else if (colorSpin && searchColor) {// if both are enabled, then both will be automatically disabled to stop
                                          // conflicts
      colorSpin = false;// disables number specific spin
      searchColor = false;// disables color specific spin

    } else {// stops motors and sets the previous color to null (for the number counter) so
            // it starts fresh
      spinnerMotor.stopMotor();// stops motor
      kPreviousColor = null;// resets previous color variable
    }

    // Test the motor controller and motors
    if (testMotor && testMotorSpeed <= 1) {
      testSpark.set(testMotorSpeed);//tests the spark in slot 6 based on test speed
      testTalon.set(testMotorSpeed);//tests the talon in slot 6 based on test speed
      testMotorSpeed += 0.01;// increases test speed
      testedMotor = true;

    } else if (testMotor && testMotorSpeed <= 1.2) {// waits for 1 second at maximum speed
      testMotorSpeed += 0.01;// continues counting, but does not effect motor speed

    } else if (testMotorSpeed == 1.2 && testMotor) {//stops the test and resets everything for next test
      testMotorSpeed = 0;//resets motor speed variable
      testMotor = false;//resets motor test boolean
      testedMotor = false;

      testSpark.stopMotor();//stops motor if its a spark
      testTalon.stopMotor();//stops motor if its a talon

      System.out.println("Test Completed");//sends a message that the test is finished

    } else if (testedMotor && !testMotor) {// if for some reason none of the above are true, which shouldnt happen, stops everything and resets test
      testMotorSpeed = 0;//resets motor speed variable
      testMotor = false;//resets motor test boolean
      testedMotor = false;

      // sends a message that there was an error
      System.out.println("Error when testing motor speed, automatically stopped the motors and reset values.");

      testSpark.stopMotor();//stops motor if it is a spark
      testTalon.stopMotor();//stops motor if it is a talon

    }

    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    if (straightDrive) {
      System.out.println("Turn angle = " + gyroAxis());
      drive.arcadeDrive(0, -gyroAxis());
    } else {
      System.out.println("Not being called");
    }

    /*if (!driveInverted) {// inverts drive controls if drive inversion button was pressed
      drive.arcadeDrive(joystick.getY() * -1 * 0.7, joystick.getZ() * 0.7);

    } else {// regular drive
      drive.arcadeDrive(joystick.getY() * 0.7, joystick.getZ() * 0.7);
    }*/

  }

  @Override

  public void autonomousInit() {

    m_autoSelected = m_autoChooser.getSelected();// sets variable autoselected to a variable pulled from dashboard
                                                 // earlier

    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);

    System.out.println("Auto selected: " + m_autoSelected);// prints the variable

  }

public double angleZ() {

  return m_imu.getGyroAngleZ();
}

  @Override

  public void autonomousPeriodic() {

    switch (m_autoSelected) {

      case kCustomAuto:

        // Put custom auto code here

        break;

      case kDefaultAuto:

      default:

        //drive.arcadeDrive(1, angleZ() * 0.001);

        break;

    }

  }
  

}

