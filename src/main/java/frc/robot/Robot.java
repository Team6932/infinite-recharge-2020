/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//Imports

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorMatchResult;

import java.util.Map;


public class Robot extends TimedRobot {

  // pulls variables from external java files in the package
  RobotHardware robot = RobotHardware.getInstance();
  Variables variables = Variables.getInstance();
  ShuffleboardInit shuffleboardInit = ShuffleboardInit.getInstance();



  @Override
  public void robotInit() {

    // camera setup
    CameraServer.getInstance().startAutomaticCapture();

    robotInitDashboard();

    // adds color options to camera sensor code
    robot.m_colorMatcher.addColorMatch(robot.kBlueTarget);
    robot.m_colorMatcher.addColorMatch(robot.kGreenTarget);
    robot.m_colorMatcher.addColorMatch(robot.kRedTarget);
    robot.m_colorMatcher.addColorMatch(robot.kYellowTarget);

    robot.ultrasonicSensor1.setAutomaticMode(true);
    robot.ultrasonicSensor2.setAutomaticMode(true);
  
  }

  @Override
  public void robotPeriodic() {

    robotPeriodicColor();

    robotPeriodicDashboard();

  }

  @Override
  public void teleopPeriodic() {

    System.out.println(robot.ultrasonicSensor1.getRangeInches());
    System.out.println(robot.ultrasonicSensor2.getRangeInches());

    periodicTesting();

    controllerVars();
    
    ballLauncherControl();

    colorWheelControl();

    driveControl();

  }

  @Override
  public void autonomousInit() {

    robot.m_autoSelected = shuffleboardInit.m_autoChooser.getSelected();// sets variable autoselected to a variable pulled from dashboard

    variables.time = System.currentTimeMillis();// Sets a variable equal to the current time in milliseconds
    
    robot.m_imu.reset();// Resets gyro
    robot.m_imu.calibrate();// Calibrates gyro

  }

  @Override
  public void autonomousPeriodic() {

    autonomousDrive();

  }

  
  public double angleZ() {

  return robot.m_imu.getGyroAngleZ();
  
}
  public double gyroAxis() {
    double axis = robot.m_imu.getAngle();

    if (axis > 360) {
      axis= axis - 360;
    }else if (axis < -360) {
      axis = axis + 360;
    }
    return axis * 0.06;
  }

  public void periodicTesting() {

    System.out.println("X Axis is: " + robot.m_imu.getGyroAngleX());
    System.out.println("Y Axis is: " + robot.m_imu.getGyroAngleY());
    System.out.println("Z Axis is: " + robot.m_imu.getGyroAngleZ());

  }
  public void motorMusic() {

    if (variables.music) {

    }

  }
  public void robotInitDashboard() {

    // setup spin import (from shuffleboard)
    shuffleboardInit.spins = shuffleboardInit.tab.add("Spins", 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 3, "max", 5)) // specify
                                                                                                                    // widget
                                                                                                                    // properties
                                                                                                                    // here
        .getEntry();

    // returns the value "spins" from shuffleboard
    shuffleboardInit.dsInfo.getEntry("spins").setValue(3);

    // adds color chooser options to shuffleboard
    shuffleboardInit.colorChooser.setDefaultOption("Blue", 1);
    shuffleboardInit.colorChooser.addOption("Green", 2);
    shuffleboardInit.colorChooser.addOption("Red", 3);
    shuffleboardInit.colorChooser.addOption("Yellow", 4);

    // sends the color chooser to shuffleboard
    SmartDashboard.putData("colorChooser", shuffleboardInit.colorChooser);

    // automated stage option chooser
    shuffleboardInit.m_autoChooser.setDefaultOption("Default Auto", variables.kDefaultAuto);
    shuffleboardInit.m_autoChooser.addOption("My Auto", variables.kCustomAuto);

    // uploads the automation choices to shuffleboard
    SmartDashboard.putData("Auto choices", shuffleboardInit.m_autoChooser);

    // adds boolean output to shuffleboard
    SmartDashboard.putBoolean("RunCal", false);
    SmartDashboard.putBoolean("ConfigCal", false);
    SmartDashboard.putBoolean("Reset", false);
    SmartDashboard.putBoolean("SetYawAxis", false);
  }
  public void robotPeriodicDashboard() {

    // sends different angles to shuffleboard
    SmartDashboard.putNumber("YawAngle", robot.m_imu.getGyroAngleZ());

    // sends misc. inputs to shuffleboard.
    shuffleboardInit.dsInfo.getEntry("spinning").setValue(variables.colorSpin);
    shuffleboardInit.dsInfo.getEntry("searchColor").setValue(variables.searchColor);
    shuffleboardInit.dsInfo.getEntry("Inverted").setValue(variables.driveInverted);
    shuffleboardInit.dsInfo.getEntry("Spins Counted").setValue(variables.spinNumber / 8);
    shuffleboardInit.dsInfo.getEntry("Testing Motor").setValue(variables.testMotor);
    shuffleboardInit.dsInfo.getEntry("drive straight?!?!??!?!?!?!?!?").setValue(variables.straightDrive);

  }
  public void robotPeriodicColor() {
    
    // gets the selected color and sets it to a variable, then checks for the
    // closest color and makes that a variable.
    Color detectedColor = robot.colorSensor.getColor();
    ColorMatchResult match = robot.m_colorMatcher.matchClosestColor(detectedColor);

    // sets the selected shuffleboard color to an actual color that can be checked.

    switch (shuffleboardInit.colorChooser.getSelected()) {
    case 1: {
      variables.kSelectedTarget = robot.kBlueTarget;
      break;
    }
    case 2: {
      variables.kSelectedTarget = robot.kGreenTarget;
      break;
    }
    case 3: {
      variables.kSelectedTarget = robot.kRedTarget;
      break;
    }
    case 4: {
      variables.kSelectedTarget = robot.kYellowTarget;
      break;
    }
    default: {
      variables.kSelectedTarget = null;
      break;
    }

    }

  }
  public void autonomousDrive() {
    
    switch (robot.m_autoSelected) {// checks what option is selected for autonomous

      case Variables.kCustomAuto:
        break;
      case Variables.kDefaultAuto:
      default:

      switch(variables.autoStep) {// DEFAULT: step based system, if a step is "completed" then it moves on to the next one.
        case 0: {// During step 0 the robot goes straight for 5 seconds
          if (System.currentTimeMillis() <= variables.time + 5000){// compares current time to starting time + 5 seconds
            System.out.println("Turn angle = " + gyroAxis());// prints gyro angle for testing purpouses
            robot.drive.arcadeDrive(0.5, -gyroAxis());// moves forward, correcting itself 
          }else {// Once complete increases to the next step
            variables.autoStep++;
          }
          break;
        }
        case 1: {// During step 1 the robot turns around 50 degrees
          resetGyro();//sets curent angle to 0 degrees
          if(angleZ() <= 50) {// while the robot is less than 50 degrees
            robot.drive.arcadeDrive(0, 0.5); // Turn    
          } else {
            variables.autoStep++;// increases the step once this step is completed
          }
          break;
        }        
        case 2: {// During step 2 the robot turns back 50 degrees, leaving it where it started
            if (angleZ() > 0) {// while the degree is greater than 0
              robot.drive.arcadeDrive(0, -0.5);// turns the robot back
            }
            else {
              variables.autoStep++; // increases the step
            }
            break;
        }
      }
        break;
    }
  }
  public void resetGyro() {
  if (!variables.gyroResetP) {
    robot.m_imu.reset();
    robot.m_imu.calibrate();
    variables.gyroResetP = true;
  }
}
  public void controllerVars() {

    // detects button inputs and inverts a variable (basically makes the button act
    // like switches instead of buttons)
    if (robot.controller.getRawButtonPressed(1))
      variables.searchColor = !variables.searchColor;
    if (robot.controller.getRawButtonPressed(10))
    variables.colorSpin = !variables.colorSpin;
    if (robot.joystick.getRawButtonPressed(2))
    variables.driveInverted = !variables.driveInverted;
    if (robot.controller.getRawButtonPressed(3))
    variables.ballLauncher = !variables.ballLauncher;
    if (robot.joystick.getRawButtonPressed(7))
    variables.forward = !variables.forward;
    if (robot.controller.getRawButtonPressed(9)) 
    variables.testMotor = !variables.testMotor;
    if (robot.controller.getRawButtonPressed(8))
    variables.music = !variables.music;
    if (robot.controller.getRawButtonPressed(12)) {
      robot.m_imu.reset();
      //robot.m_imu.configCalTime(1);
      robot.m_imu.calibrate();
      //robot.m_imu.reset();
    }

  }
  public void ballLauncherControl() {
    
    // controls the ball launcher
    if (variables.ballLauncher) {// ball launcher control

      // sets the two motors controlling the ball launcher to be inverted to eachother
      // (so it fires properly)
      robot.spinner2.set(1);
      robot.spinner3.set(-1);

    } else {// stops the motors if the option is off (Default)

      robot.spinner2.stopMotor();
      robot.spinner3.stopMotor();

    }

  }
  public void colorWheelControl() {

    // gets the color from the color sensor, and then sets a variable for the
    // detected color.
    Color detectedColor = robot.colorSensor.getColor();
    ColorMatchResult match = robot.m_colorMatcher.matchClosestColor(detectedColor);

    // controls the color wheel motors and color sensor
    if (variables.searchColor && !variables.colorSpin) {// if the color specific wheel command is being run, and not the spin ammount
      // command
if (variables.kSelectedTarget != match.color) {// if the color seen is not equal to the color chosen by the user
robot.spinnerMotor.set(0.3);// spin the motor
} else {// if the colors are the same
robot.spinnerMotor.stopMotor();// stop the motor
variables.colorSpin = false;// disable command
// TODO spinn motor X amount to put sensor in right spot.
}
} else if (variables.colorSpin && !variables.searchColor) {// if the spin ammount is run and not the color searcher
if (variables.spinNumber / 8 < shuffleboardInit.spins.getDouble(3.0)) {// spin the wheel untill the number of complete spins is equal to the
                    // user specified number

if (variables.spinNumber == 0) {
variables.kPreviousColor = match.color;
}

robot.spinnerMotor.set(0.3);

if (variables.kPreviousColor != match.color) {// adds 1 to the spin number every time it sees a new color (this is divided
                        // by 8)
variables.spinNumber++;
variables.kPreviousColor = match.color;// sets the current color to the color to the previous color
}
} else {// stops the motor if the number of full spins is reached
robot.spinnerMotor.stopMotor();
variables.colorSpin = false;// stops the command
variables.spinNumber = 0;// resets the variable for future use
}
} else if (variables.colorSpin && variables.searchColor) {// if both are enabled, then both will be automatically disabled to stop
            // conflicts
variables.colorSpin = false;// disables number specific spin
variables.searchColor = false;// disables color specific spin

} else {// stops motors and sets the previous color to null (for the number counter) so
// it starts fresh
robot.spinnerMotor.stopMotor();// stops motor
variables.kPreviousColor = null;// resets previous color variable
}


  }
  public void motorTest() {


    // Test the motor robot.controller and motors
    if (variables.testMotor && variables.testMotorSpeed <= 1) {
      robot.testSpark.set(variables.testMotorSpeed);//tests the spark in slot 6 based on test speed
      robot.testTalon.set(variables.testMotorSpeed);//tests the talon in slot 6 based on test speed
      variables.testMotorSpeed += 0.01;// increases test speed
      variables.testedMotor = true;

    } else if (variables.testMotor && variables.testMotorSpeed <= 1.2) {// waits for 1 second at maximum speed
      variables.testMotorSpeed += 0.01;// continues counting, but does not effect motor speed

    } else if (variables.testMotorSpeed == 1.2 && variables.testMotor) {//stops the test and resets everything for next test
      variables.testMotorSpeed = 0;//resets motor speed variable
      variables.testMotor = false;//resets motor test boolean
      variables.testedMotor = false;

      robot.testSpark.stopMotor();//stops motor if its a spark
      robot.testTalon.stopMotor();//stops motor if its a talon

      System.out.println("Test Completed");//sends a message that the test is finished

    } else if (variables.testedMotor && !variables.testMotor) {// if for some reason none of the above are true, which shouldnt happen, stops everything and resets test
      variables.testMotorSpeed = 0;//resets motor speed variable
      variables.testMotor = false;//resets motor test boolean
      variables.testedMotor = false;

      // sends a message that there was an error
      System.out.println("Error when testing motor speed, automatically stopped the motors and reset values.");

      robot.testSpark.stopMotor();//stops motor if it is a spark
      robot.testTalon.stopMotor();//stops motor if it is a talon

    }

  }
  public void driveControl() {
    
    // Drive with arcade drive.
    // That means that the Y axis drives forward.
    // and backward, and the X turns left and right.
    if (!variables.driveInverted) {// inverts drive controls if drive inversion button was pressed
      robot.drive.arcadeDrive(robot.joystick.getY() * -1 * 0.7, robot.joystick.getZ() * 0.7);

    } else if (variables.driveInverted) {// regular drive
      robot.drive.arcadeDrive(robot.joystick.getY() * 0.7, robot.joystick.getZ() * 0.7);
    }

  }

}