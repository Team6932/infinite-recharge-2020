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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.ColorMatchResult;

import java.util.Map;


public class Robot extends TimedRobot {

  // pulls variables from external java files in the package
  RobotHardware robot = RobotHardware.getInstance();
  Variables variables = Variables.getInstance();
  ShuffleboardInit shuffleboardInit = ShuffleboardInit.getInstance();

  double ballLauncherSpeed = (robot.ultrasonicSensor1.getRangeInches()/12) * 0.1;


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
    robot.ultrasonicSensor3.setAutomaticMode(true);
  
  }

  @Override
  public void robotPeriodic() {

    robotPeriodicColor();

    robotPeriodicDashboard();

  }

  @Override
  public void teleopPeriodic() {

    periodicTesting();

    controllerVars();
    
    //launchBall(0);

    colorWheelControl();

    driveControl();

    //motorMusic();

    ballCollect();
    
    if (variables.ballLauncher) {
      
      robot.spinner2.set(1);
      robot.spinner3.set(-1);
    } else {
      robot.spinner2.stopMotor();
      robot.spinner3.stopMotor();
    }

    //countBalls();

  }

  @Override
  public void autonomousInit() {

    robot.m_autoSelected = shuffleboardInit.m_autoChooser.getSelected();// sets variable autoselected to a variable pulled from dashboard

    variables.time = System.currentTimeMillis();// Sets a variable equal to the current time in milliseconds
    
    //robot.m_imu.reset();// Resets gyro
    //robot.m_imu.calibrate();// Calibrates gyro

  }

  @Override
  public void autonomousPeriodic() {

    autonomousDrive();

  }

  
  public double angleZ() {

  //return robot.m_imu.getGyroAngleZ();
  return 0;
  
}
  public double gyroAxis() {
    double axis = angleZ();

    if (axis > 360) {
      axis= axis - 360;
    }else if (axis < -360) {
      axis = axis + 360;
    }
    return axis * 0.06;
  }

  public void periodicTesting() {

    if (!variables.despam) {
    //System.out.println("X Axis is: " + robot.m_imu.getGyroAngleX());
    //System.out.println("Y Axis is: " + robot.m_imu.getGyroAngleY());
    //System.out.println("Z Axis is: " + robot.m_imu.getGyroAngleZ());

    System.out.println("ultrasonic 1 distance = " + robot.ultrasonicSensor1.getRangeInches());
    System.out.println("ultrasonic 2 distance = " + robot.ultrasonicSensor2.getRangeInches());

    System.out.println("ball launcher = " + variables.ballLauncher);
    }
  }
  public void motorMusic() {

    if (variables.music) {
      robot.rightMotor.set(variables.musicSpeed);
      variables.musicTime++;
      switch (variables.musicTime) {
        case 0: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 20: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 40: {
          variables.musicSpeed =  0.4;
          break;
        }
        case 60: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 80: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 100: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 120: {
          variables.musicSpeed =  0.4;
          break;
        }
        case 140: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 160: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 180: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 200: {
          variables.musicSpeed =  0.4;
          break;
        }
        case 220: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 240: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 260: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 280: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 300: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 320: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 340: {
          variables.musicSpeed = 0.2;
          break;
        }
        case 400: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 410: {
          variables.musicSpeed = 0;
          break;
        }
        case 420: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 430: {
          variables.musicSpeed = 0;
          break;
        }
        case 440: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 450: {
          variables.musicSpeed = 0;
          break;
        }
        case 460: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 470: {
          variables.musicSpeed = 0;
          break;
        }
        case 480: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 490: {
          variables.musicSpeed = 0.0;
          break;
        }
        case 500: {
          variables.musicSpeed = 0.4;
          break;
        }
        case 510: {
          variables.musicSpeed = 0;
          break;
        }
        case 520: {
          variables.musicSpeed = 0.5;
          break;
        }
        case 530: {
          variables.musicSpeed = 0.4;
          break;
        }
        case 540: {
          variables.musicSpeed = 0.5;
          break;
        }
        case 550: {
          variables.musicSpeed = 0.6;
          break;
        }
        case 560: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 570: {
          variables.musicSpeed = 0;
          break;
        }
        case 580: {
          variables.musicSpeed = 0.8;
          break;
        }
        case 590: {
          variables.musicSpeed = 0;
          robot.rightMotor.stopMotor();
          variables.music = false;
          variables.musicTime = -1;
          break;
        }
        default: {
          break;
        }
      }
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
    //SmartDashboard.putNumber("YawAngle", m_imu.getAngle());

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

      switch(variables.autoStep) {
        case 1: {
          
          resetGyro();//sets curent angle to 0 degrees
          variables.autoStep++;
          break;

        }
        case 2: {
          if(angleZ() < 90) {// while the robot is less than 50 degrees
            robot.drive.arcadeDrive(0, 0.5); // Turn    
          } else {
            variables.autoStep++;// increases the step once this step is completed
          }
          break;
        }
        case 3: {// During step 1 the robot turns around 50 degrees
          if (robot.ultrasonicSensor3.getRangeInches() > 12) {
            robot.drive.arcadeDrive(0.5, 0);
          }
          else {
            variables.autoStep++;
          }
          break;
        }      
        case 4: {
          resetGyro();
          variables.autoStep++;
          break;
        }  
        case 5: {
          if (angleZ()<90) {
            robot.drive.arcadeDrive(0, .5);
          } else {
            variables.autoStep++;
          }
            break;
        }
        case 6: {
          if (robot.ultrasonicSensor3.getRangeInches() < 20*12) {
            robot.drive.arcadeDrive(.6, 0);
          } else {
            variables.autoStep++;
          }
          break;
        }
        case 7: {
          resetGyro();
          variables.autoStep++;
          break;
        }
        case 8: {
          if (angleZ()>.0100068884) {
            robot.drive.arcadeDrive(0, .01);
            } else {
              variables.autoStep++;
            }
            break;
          }
          case 9: {
            launchBall(3);
            variables.autoStep++;
            break;
          }
          case 10: {
            resetGyro();
            variables.autoStep++;
            break;
          }
          case 11: {
            if (angleZ() < -.0100068884) {
              robot.drive.arcadeDrive(0, -.01);
            }else {
              variables.autoStep++;
            }
            break;
          }
          case 12: {
            if (robot.ultrasonicSensor3.getRangeInches() < 12*26) {
              ballCollect();
              robot.drive.arcadeDrive(-.5, 0);
            } else {
              variables.autoStep++;
            }
            break;
            }
            case 13: {
              ballCollect();
              if (robot.ultrasonicSensor3.getRangeInches() > 12*20) {
                robot.drive.arcadeDrive(.5, 0);
              } else {
                variables.autoStep++;
              }
              break;
            }
            case 14: {
              resetGyro();
              variables.autoStep++;
            }
            case 15: {
              if (angleZ() < .0100068884) {
                robot.drive.arcadeDrive(0, .1);
              } else {
                variables.autoStep++;
              }
              break;
            }
            case 16: {
              launchBall(3);
              variables.autoStep++;
              break;
            }
          }
        }
      
      }
  public void resetGyro() {
  if (!variables.gyroResetP) {
    //robot.m_imu.reset();
    //robot.m_imu.calibrate();
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
    if (robot.controller.getRawButtonPressed(4))
    variables.despam = !variables.despam;
    if (robot.joystick.getRawButtonPressed(7))
    variables.forward = !variables.forward;
    if (robot.controller.getRawButtonPressed(9)) 
    variables.testMotor = !variables.testMotor;
    if (robot.controller.getRawButtonPressed(8))
    variables.music = !variables.music;
    if (robot.joystick.getRawButtonPressed(11)) 
      variables.load = !variables.load;
    /*if (robot.controller.getRawButtonPressed(12)) {
      robot.m_imu.reset();
      //robot.m_imu.configCalTime(1);
      robot.m_imu.calibrate();
      //robot.m_imu.reset();
    }*/
    

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
    if (!variables.driveInverted && !variables.music) {// inverts drive controls if drive inversion button was pressed
      robot.drive.arcadeDrive(robot.joystick.getY() * -1 * 0.7, robot.joystick.getZ() * 0.7);

    } else if (variables.driveInverted && !variables.music) {// regular drive
      robot.drive.arcadeDrive(robot.joystick.getY() * 0.7, robot.joystick.getZ() * 0.7);
    }

  }
  public void launchBall(int ballNumber) {

    variables.ballTime = System.currentTimeMillis();
    variables.ballLauncher = true;


    // controls the ball launcher
    if (variables.ballLauncher) {// ball launcher control

      // sets the two motors controlling the ball launcher to be inverted to eachother
      // (so it fires properly)
      //robot.spinner2.set(ballLauncherSpeed);
      //robot.spinner3.set(-ballLauncherSpeed);
      robot.spinner2.set(.7);
      robot.spinner3.set(-.7);

    } else if (ballNumber > 0 && !variables.ballLauncher) {
      int i = 0;
      if (i < ballNumber) {
        if (variables.ballTime + (400*i) + 300 == System.currentTimeMillis()) {
          ballInsert();
          i++;
        }
      }
    }
    else {// stops the motors if the option is off (Default)

      robot.spinner2.stopMotor();
      robot.spinner3.stopMotor();

    }
  }
  public void ballInsert() {
    final double insertTime = System.currentTimeMillis();
    robot.insertMotor.set(0.2);
    if (System.currentTimeMillis() == insertTime + 300)
      robot.insertMotor.stopMotor();
  }
  public void ballCollect() {
    if (variables.load) {
      robot.loaderMotor.set(1);
    } else {
      robot.loaderMotor.stopMotor();
    }
  }

  /*public void countBalls() { //TODO add the if statements to count code.
    //when a ball is seen entering
    if (false) {
    variables.ballNumber++;
    }
    //when a ball is seen exiting
    if (false) {
    variables.ballNumber--;
    }
  }

  public double gyroCorrection(double gyro) {
    double gyroTime = System.currentTimeMillis();
    double gyroCorrectionAngle = 0;
    var i = 0;

    if (i < 10) {
      if (System.currentTimeMillis() == gyroTime + (200*i)) {
        i++;
      }
    }

    return 0;
  }*/
}