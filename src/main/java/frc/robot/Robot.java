/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;



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

import java.util.Map;

import com.revrobotics.ColorMatch;

/**
 * This is a demo program showing the use of the DifferentialDrive class.
 * Runs the motors with arcade steering.
 */
public class Robot extends TimedRobot {

  RobotHardware robot = RobotHardware.getInstance();

  boolean colorSpin = false;
  boolean searchColor = false;
  boolean driveInverted = false;
  boolean ballLauncher = false;

  int spinNumber = 0;
  



  private NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
  NetworkTable dsInfo = ntinst.getTable("dsInfo");
  public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();

  private final ColorMatch m_colorMatcher = new ColorMatch();

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  private Color kSelectedTarget = null;
  private Color kPreviousColor = null;
  private ShuffleboardTab tab = Shuffleboard.getTab("LiveWindow");
  private NetworkTableEntry spins;


  @Override 
  public void robotInit() {

    CameraServer.getInstance().startAutomaticCapture();

    spins =
    tab.add("Spins", 1)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 3, "max", 5)) // specify widget properties here
    .getEntry();

    dsInfo.getEntry("spins").setValue(3);

    colorChooser.setDefaultOption("Blue", 1);
    colorChooser.addOption("Green", 2);
    colorChooser.addOption("Red", 3);
    colorChooser.addOption("Yellow", 4);

    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);  

    SmartDashboard.putData("colorChooser", colorChooser);

  }

  @Override
  public void robotPeriodic() {

    //System.out.println(colorChooser.getSelected());

    Color detectedColor = robot.colorSensor.getColor();      
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

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

    dsInfo.getEntry("spinning").setValue(colorSpin);
    dsInfo.getEntry("searchColor").setValue(searchColor);
    dsInfo.getEntry("Inverted").setValue(driveInverted);
    dsInfo.getEntry("Spins Counted").setValue(spinNumber/8);


    
  }

  @Override
  public void teleopPeriodic() {



    Color detectedColor = robot.colorSensor.getColor();      
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (robot.controller.getRawButtonPressed(1)) searchColor = !searchColor;
    if (robot.controller.getRawButtonPressed(10)) colorSpin = !colorSpin;
    if (robot.joystick.getRawButtonPressed(2)) driveInverted = !driveInverted;
    if (robot.joystick.getRawButtonPressed(3)) ballLauncher = !ballLauncher;

    System.out.println(spinNumber);
    System.out.println("previous color = " + kPreviousColor);
    System.out.println("current color = " + match.color);

    
    if (ballLauncher) {

      robot.spinner2.set(1);
      robot.spinner3.set(-1);

    }else {

      robot.spinner2.stopMotor();
      robot.spinner3.stopMotor();

    }
    
    if (searchColor && !colorSpin) {
      if(kSelectedTarget != match.color) {
        robot.spinnerMotor.set(0.3);
      }else {
        robot.spinnerMotor.stopMotor();
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

