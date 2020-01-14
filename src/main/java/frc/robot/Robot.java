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

import java.util.Map;

import com.revrobotics.ColorMatch;


public class Robot extends TimedRobot {

  




//unknown....
  private ADIS16448_IMU.IMUAxis m_yawActiveAxis;
  private final ADIS16448_IMU m_imu = new ADIS16448_IMU();

  //pulls variables from RobotHardware.java file
  RobotHardware robot = RobotHardware.getInstance();

  //booleans
  boolean colorSpin = false;
  boolean searchColor = false;
  boolean driveInverted = false;
  boolean ballLauncher = false;

  //booleans for location control
  boolean m_runCal = false;
  boolean m_configCal = false;
  boolean m_reset = false;
  boolean m_setYawAxis = false;

  // intergers
  int spinNumber = 0;
  
  //strings
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private static final String kYawDefault = "Z-Axis";
  private static final String kYawXAxis = "X-Axis";
  private static final String kYawYAxis = "Y-Axis";
  
  private String m_yawSelected;
  
  //shuffleboard setup
  private NetworkTableInstance ntinst = NetworkTableInstance.getDefault();

  NetworkTable dsInfo = ntinst.getTable("dsInfo");

  public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();
  private final SendableChooser<String> m_autoChooser = new SendableChooser<>();
  private final SendableChooser<String> m_yawChooser = new SendableChooser<>();

  //color sensor setup
  private final ColorMatch m_colorMatcher = new ColorMatch();

  //setup color options
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  //color variables
  private Color kSelectedTarget = null;
  private Color kPreviousColor = null;

  //shuffleboard variables
  private ShuffleboardTab tab = Shuffleboard.getTab("LiveWindow");
  private NetworkTableEntry spins;


  @Override 
  public void robotInit() {

    //camera setup
    CameraServer.getInstance().startAutomaticCapture();

    //setup spin import (from shuffleboard)
    spins =
    tab.add("Spins", 1)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 3, "max", 5)) // specify widget properties here
    .getEntry();

    // returns the value "spins" from shuffleboard
    dsInfo.getEntry("spins").setValue(3);

    // adds color chooser options to shuffleboard
    colorChooser.setDefaultOption("Blue", 1);
    colorChooser.addOption("Green", 2);
    colorChooser.addOption("Red", 3);
    colorChooser.addOption("Yellow", 4);

    //sends the color chooser to shuffleboard
    SmartDashboard.putData("colorChooser", colorChooser);

    // adds color options to camera sensor code
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);  

    // automated stage option chooser
    m_autoChooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_autoChooser.addOption("My Auto", kCustomAuto);

    //uploads the automation choices to shuffleboard
    SmartDashboard.putData("Auto choices", m_autoChooser);

    // adds axis options for shuffleboard
    m_yawChooser.setDefaultOption("Z-Axis", kYawDefault);
    m_yawChooser.addOption("X-Axis", kYawXAxis);
    m_yawChooser.addOption("Y-Axis", kYawYAxis);

    //sends the axis options to shuffleboard
    SmartDashboard.putData("IMUYawAxis", m_yawChooser);

    //adds boolean output to shuffleboard
    SmartDashboard.putBoolean("RunCal", false);
    SmartDashboard.putBoolean("ConfigCal", false);
    SmartDashboard.putBoolean("Reset", false);
    SmartDashboard.putBoolean("SetYawAxis", false);

  }

  @Override
  public void robotPeriodic() {
    
    // sends different angles to shuffleboard
    SmartDashboard.putNumber("YawAngle", m_imu.getAngle());
    SmartDashboard.putNumber("XCompAngle", m_imu.getXComplementaryAngle());
    SmartDashboard.putNumber("YCompAngle", m_imu.getYComplementaryAngle());

    // takes booleans from shuffleboard and sets them to variables.
    m_runCal = SmartDashboard.getBoolean("RunCal", false);
    m_configCal = SmartDashboard.getBoolean("ConfigCal", false);
    m_reset = SmartDashboard.getBoolean("Reset", false);
    m_setYawAxis = SmartDashboard.getBoolean("SetYawAxis", false);

    // sets variable to whatever the user chose in shuffleboard
    m_yawSelected = m_yawChooser.getSelected();



    // Set IMU settings
    if (m_configCal) {

      m_imu.configCalTime(8);

      //gets the boolean result "config calibration" from shuffleboard
      m_configCal = SmartDashboard.putBoolean("ConfigCal", false);

    }    if (m_reset) {

      m_imu.reset();

      //gets the boolean result "reset" from shuffleboard
      m_reset = SmartDashboard.putBoolean("Reset", false);

    }    if (m_runCal) {

      m_imu.calibrate();

      //gets the boolean result "run calibration" from shuffleboard
      m_runCal = SmartDashboard.putBoolean("RunCal", false);

    }

    

    // Read the desired yaw axis from the dashboard
    // I'm not going to try to explain this...
    if (m_yawSelected == "X-Axis") {

      m_yawActiveAxis = ADIS16448_IMU.IMUAxis.kX;

    }   else if (m_yawSelected == "Y-Axis") {

      m_yawActiveAxis = ADIS16448_IMU.IMUAxis.kY;

    }   else {

      m_yawActiveAxis = ADIS16448_IMU.IMUAxis.kZ;

    }

    // Set the desired yaw axis from the dashboard

    if (m_setYawAxis) {

      m_imu.setYawAxis(m_yawActiveAxis);

      // gets the result of "set Yaw Axis" from shuffleboard
      m_setYawAxis = SmartDashboard.putBoolean("SetYawAxis", false);

    }

    // gets the selected color and sets it to a variable, then checks for the closest color and makes that a variable.
    Color detectedColor = robot.colorSensor.getColor();      
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    // sets the selected shuffleboard color to an actual color that can be checked.
    switch(colorChooser.getSelected()) {
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
    dsInfo.getEntry("Spins Counted").setValue(spinNumber/8);


    
  }

  //TODO Finish adding code

  @Override
  public void teleopPeriodic() {

    //gets the color from the color sensor, and then sets a variable for the detected color.
    Color detectedColor = robot.colorSensor.getColor();      
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    //detects button inputs and inverts a variable (basically makes the button act like switches instead of buttons)
    if (robot.controller.getRawButtonPressed(1)) searchColor = !searchColor;
    if (robot.controller.getRawButtonPressed(10)) colorSpin = !colorSpin;
    if (robot.joystick.getRawButtonPressed(2)) driveInverted = !driveInverted;
    if (robot.joystick.getRawButtonPressed(3)) ballLauncher = !ballLauncher;

    //debugging (prints out special variables of my choosing)
    System.out.println(spinNumber);
    System.out.println("previous color = " + kPreviousColor);
    System.out.println("current color = " + match.color);

    //controls the ball launcher
    if (ballLauncher) {// ball launcher control

      //sets the two motors controlling the ball launcher to be inverted to eachother (so it fires properly)
      robot.spinner2.set(1);
      robot.spinner3.set(-1);

    }else {// stops the motors if the option is off (Default)

      robot.spinner2.stopMotor();
      robot.spinner3.stopMotor();

    }
    
    //controls the color wheel motors and color sensor
    if (searchColor && !colorSpin) {// if the color specific 
      if(kSelectedTarget != match.color) {
        robot.spinnerMotor.set(0.3);
      }else {
        robot.spinnerMotor.stopMotor();
        colorSpin = false;
        //TODO spinn motor X amount to put sensor in right spot.
      }
    } else if(colorSpin && !searchColor) {
      if (spinNumber/8 < spins.getDouble(3.0)) {

        robot.spinnerMotor.set(0.3);

        if (kPreviousColor != match.color) {
          spinNumber++;
          kPreviousColor = match.color;
        }
      }
      else {
        robot.spinnerMotor.stopMotor();
        colorSpin = false;
        spinNumber = 0;
      }
    } else if (colorSpin && searchColor) {
      colorSpin = false;
      searchColor = false;
      
    } else {
      robot.spinnerMotor.stopMotor();
      kPreviousColor = null;
    }


    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.

    if (!driveInverted) {
      robot.drive.arcadeDrive(robot.joystick.getY() * -1 * 0.7, robot.joystick.getZ()* 0.7);

    }else {
      robot.drive.arcadeDrive(robot.joystick.getY() * 0.7, robot.joystick.getZ() * 0.7);
    }

    

  }
  

}

