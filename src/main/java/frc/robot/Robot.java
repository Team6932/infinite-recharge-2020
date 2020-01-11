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

  boolean spinning = false;
  boolean searchColor = false;
  boolean inverted = false;

  int spinNumber = 0;
  

  private final Spark m_leftMotor = new Spark(0);
  private final Spark m_rightMotor = new Spark(1);

  private final PWMTalonSRX spinnerMotor = new PWMTalonSRX(2);
  private final PWMTalonSRX spinner2 = new PWMTalonSRX(3);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  private final Joystick controller = new Joystick(1);
  private final Joystick joystick = new Joystick(2);

  private NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
  NetworkTable dsInfo = ntinst.getTable("dsInfo");
  public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();
  
  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

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

    Color detectedColor = m_colorSensor.getColor();      
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

    dsInfo.getEntry("spinning").setValue(spinning);
    dsInfo.getEntry("searchColor").setValue(searchColor);
    dsInfo.getEntry("Inverted").setValue(inverted);
    dsInfo.getEntry("Spins Counted").setValue(spinNumber/8);


    
  }

  @Override
  public void teleopPeriodic() {



    Color detectedColor = m_colorSensor.getColor();      
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    if (controller.getRawButtonPressed(1)) searchColor = !searchColor;
    if (controller.getRawButtonPressed(10)) spinning = !spinning;
    if (joystick.getRawButtonPressed(2)) inverted = !inverted;

    System.out.println(spinNumber);
    System.out.println("previous color = " + kPreviousColor);
    System.out.println("current color = " + match.color);

    if (controller.getRawButton(3)) {

      spinnerMotor.set(.4);
      spinner2.set(-0.4);

    }

    /*
    if (searchColor && !spinning) {
      if(kSelectedTarget != match.color) {
        spinnerMotor.set(0.3);
      }else {
        spinnerMotor.stopMotor();
      }
    } else if(spinning && !searchColor) {
      if (spinNumber/8 < spins.getDouble(3.0)) {

        spinnerMotor.set(0.3);

        if (kPreviousColor != match.color) {
          spinNumber++;
          kPreviousColor = match.color;
        }
      }
      else {
        spinnerMotor.stopMotor();
        spinning = false;
        spinNumber = 0;
      }
    } else if (spinning && searchColor) {
      spinning = false;
      searchColor = false;
      
    } else {
      spinnerMotor.stopMotor();
    }*/


    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.

    if (!inverted) {
      m_robotDrive.arcadeDrive(joystick.getY() * -1 * 0.7, joystick.getZ()* 0.7);

    }else {
      m_robotDrive.arcadeDrive(joystick.getY() * 0.7, joystick.getZ() * 0.7);
    }

    

  }

}

