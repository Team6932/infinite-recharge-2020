/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorMatchResult;

public class Robot extends TimedRobot {

  // pulls variables from external java files in the package
  RobotHardware robot = RobotHardware.getInstance();
  Variables variables = Variables.getInstance();
  ShuffleboardController dashboard = ShuffleboardController.getInstance();
  Autonomous auto = Autonomous.getInstance();
  double ballLauncherSpeed = (robot.ultrasonicSensor1.getRangeInches() / 12) * 0.1;
  UsbCamera camera1;
  UsbCamera camera2;

  @Override
  public void robotInit() {
    dashboard.init();
    /*
     * try { robot.gyro.calibrateGyro(); } catch (InterruptedException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); }
     */

    // camera setup 2 cameras
    camera1 = CameraServer.getInstance().startAutomaticCapture(1);
    camera2 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setVideoMode(PixelFormat.kMJPEG, 192, 144, 15);
    camera2.setVideoMode(PixelFormat.kMJPEG, 192, 144, 15);
    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

    // adds color options to camera sensor code
    robot.m_colorMatcher.addColorMatch(robot.kBlueTarget);
    robot.m_colorMatcher.addColorMatch(robot.kGreenTarget);
    robot.m_colorMatcher.addColorMatch(robot.kRedTarget);
    robot.m_colorMatcher.addColorMatch(robot.kYellowTarget);

    robot.ultrasonicSensor1.setAutomaticMode(true);
    robot.ultrasonicSensor1.setAutomaticMode(true);
    robot.ultrasonicSensor1.setAutomaticMode(true);

  }

  DigitalInput ballCounter = new DigitalInput(1);

  @Override
  public void robotPeriodic() {
    robotPeriodicColor();
    //robot.gyro.correctGyro();
    dashboard.update();
    controllerVars();

    System.out.println(ballCounter.get());
  }

  @Override
  public void teleopPeriodic() {
    launcherLoader();
    System.out.println(variables.ballSpeed);
    //periodicTesting();
    if(variables.load) {
      auto.ballCollect(true);
    } else if (!variables.load) {
      auto.ballCollect(false);
    }
    //colorWheelControl();
    driveWithModes();

    if (variables.ballLauncher) {
      robot.ballLauncher1.set(variables.ballSpeed);
      robot.ballLauncher2.set(-variables.ballSpeed);
    } else {
      robot.ballLauncher1.stopMotor();
      robot.ballLauncher2.stopMotor();
    }

    // countBalls();

  }
  double time = 0;
  @Override
  public void autonomousInit() {
    //auto.init();
    variables.autonomousTime = System.currentTimeMillis();
    time = System.currentTimeMillis();
  }

  @Override
  public void autonomousPeriodic() {
    /*if (System.currentTimeMillis() <= 2000 + time) {
      robot.drive.arcadeDrive(0.6, 0);
    } else {
      robot.drive.stopMotor();
    }*/
    auto.periodic();
  }

  public double gyroAxis() {
    double axis = robot.gyro.getGyroAngleZ();

    if (axis > 360) {
      axis = axis - 360;
    } else if (axis < -360) {
      axis = axis + 360;
    }
    return axis * 0.06;
  }
  public void periodicTesting() {
    if (!variables.despam) {
      // System.out.println("X Axis is: " + robot.m_imu.getGyroAngleX());
      // System.out.println("Y Axis is: " + robot.m_imu.getGyroAngleY());
      // System.out.println("Z Axis is: " + robot.m_imu.getGyroAngleZ());
      System.out.println("ultrasonic 1 distance = " + robot.ultrasonicSensor1.getRangeInches());
      System.out.println("ultrasonic 2 distance = " + robot.ultrasonicSensor1.getRangeInches());
      System.out.println("ball launcher = " + variables.ballLauncher);
    }
  }
  public void robotPeriodicColor() {

    // gets the selected color and sets it to a variable, then checks for the
    // closest color and makes that a variable.
    Color detectedColor = robot.colorSensor.getColor();
    ColorMatchResult match = robot.m_colorMatcher.matchClosestColor(detectedColor);

    // sets the selected shuffleboard color to an actual color that can be checked.

    switch (dashboard.colorChooser.getSelected()) {
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
  public void resetGyro() {
    if (!variables.gyroResetP) {
      // robot.m_imu.reset();
      // robot.m_imu.calibrate();
      variables.gyroResetP = true;
    }
  }
  public void controllerVars() {

    // detects button inputs and inverts a variable (basically makes the button act
    // like switches instead of buttons)
    if (robot.controller.getRawButtonPressed(5)) {
      
      if (variables.ballLauncher == true) {
        variables.time2 = 0;
        variables.timed = false;
      }

    variables.ballLauncher = !variables.ballLauncher;
    }
    if (robot.controller.getPOV() == 0 && variables.ballSpeed < 0.8) 
      variables.ballSpeed += 0.001;
    if (robot.controller.getPOV() == 180 && variables.ballSpeed > 0.3) {
      variables.ballSpeed -= 0.001;
    }
    if (robot.controller.getRawButtonPressed(4)) 
    variables.ballSpeed = 0.75;
    if (robot.controller.getRawButtonPressed(3))
    variables.ballSpeed = .6;
    if (robot.controller.getRawButtonPressed(2))
    variables.ballSpeed = .3;
    if (robot.controller.getRawButtonPressed(6))
      variables.searchColor = !variables.searchColor;
    if (robot.controller.getRawButtonPressed(10))
      variables.colorSpin = !variables.colorSpin;
    if (robot.joystick.getRawButtonPressed(2))
      variables.driveInverted = !variables.driveInverted;
    if (robot.joystick.getRawButtonPressed(4))
      variables.despam = !variables.despam;
    if (robot.joystick.getRawButtonPressed(7))
      variables.forward = !variables.forward;
    if (robot.controller.getRawButtonPressed(9))
      variables.testMotor = !variables.testMotor;
    if (robot.joystick.getRawButtonPressed(12))
      variables.music = !variables.music;
    if (robot.controller.getRawButtonPressed(7)){
      variables.launcherLoaderB = true;
    }else {
      variables.launcherLoaderB = false;
    }
    if (robot.controller.getRawButtonPressed(8)) {
      variables.launcherLoaderF = true;
    }else {
      variables.launcherLoaderF = false;
    }
    if (robot.controller.getRawButtonPressed(1)) {
      variables.load = !variables.load;
    }
  }
  public void colorWheelControl() {

    // gets the color from the color sensor, and then sets a variable for the
    // detected color.
    Color detectedColor = robot.colorSensor.getColor();
    ColorMatchResult match = robot.m_colorMatcher.matchClosestColor(detectedColor);

    // controls the color wheel motors and color sensor
    if (variables.searchColor && !variables.colorSpin) {// if the color specific wheel command is being run, and not the
                                                        // spin ammount
      // command
      if (variables.kSelectedTarget != match.color) {// if the color seen is not equal to the color chosen by the user
        robot.wheelMotor.set(0.3);// spin the motor
      } else {// if the colors are the same
        robot.wheelMotor.stopMotor();// stop the motor
        variables.colorSpin = false;// disable command
        // TODO spinn motor X amount to put sensor in right spot.
      }
    } else if (variables.colorSpin && !variables.searchColor) {// if the spin ammount is run and not the color searcher
      if (variables.spinNumber / 8 < dashboard.spins.getDouble(3.0)) {// spin the wheel untill the number of
                                                                             // complete spins is equal to the
        // user specified number

        if (variables.spinNumber == 0) {
          variables.kPreviousColor = match.color;
        }

        robot.wheelMotor.set(0.3);

        if (variables.kPreviousColor != match.color) {// adds 1 to the spin number every time it sees a new color (this
                                                      // is divided
          // by 8)
          variables.spinNumber++;
          variables.kPreviousColor = match.color;// sets the current color to the color to the previous color
        }
      } else {// stops the motor if the number of full spins is reached
        robot.wheelMotor.stopMotor();
        variables.colorSpin = false;// stops the command
        variables.spinNumber = 0;// resets the variable for future use
      }
    } else if (variables.colorSpin && variables.searchColor) {// if both are enabled, then both will be automatically
                                                              // disabled to stop
      // conflicts
      variables.colorSpin = false;// disables number specific spin
      variables.searchColor = false;// disables color specific spin

    } else {// stops motors and sets the previous color to null (for the number counter) so
      // it starts fresh
      robot.wheelMotor.stopMotor();// stops motor
      variables.kPreviousColor = null;// resets previous color variable
    }

  }
  public double gyroCorrection() {

    return 0;
  }
  public void motorTest() {

    // Test the motor robot.controller and motors
    if (variables.testMotor && variables.testMotorSpeed <= 1) {
      robot.testSpark.set(variables.testMotorSpeed);// tests the spark in slot 6 based on test speed
      robot.testTalon.set(variables.testMotorSpeed);// tests the talon in slot 6 based on test speed
      variables.testMotorSpeed += 0.01;// increases test speed
      variables.testedMotor = true;

    } else if (variables.testMotor && variables.testMotorSpeed <= 1.2) {// waits for 1 second at maximum speed
      variables.testMotorSpeed += 0.01;// continues counting, but does not effect motor speed

    } else if (variables.testMotorSpeed == 1.2 && variables.testMotor) {// stops the test and resets everything for next
                                                                        // test
      variables.testMotorSpeed = 0;// resets motor speed variable
      variables.testMotor = false;// resets motor test boolean
      variables.testedMotor = false;

      robot.testSpark.stopMotor();// stops motor if its a spark
      robot.testTalon.stopMotor();// stops motor if its a talon

      System.out.println("Test Completed");// sends a message that the test is finished

    } else if (variables.testedMotor && !variables.testMotor) {// if for some reason none of the above are true, which
                                                               // shouldnt happen, stops everything and resets test
      variables.testMotorSpeed = 0;// resets motor speed variable
      variables.testMotor = false;// resets motor test boolean
      variables.testedMotor = false;

      // sends a message that there was an error
      System.out.println("Error when testing motor speed, automatically stopped the motors and reset values.");

      robot.testSpark.stopMotor();// stops motor if it is a spark
      robot.testTalon.stopMotor();// stops motor if it is a talon

    }

  }
  public static boolean Stuffz(Boolean bool) {
    int percentage = 0;
    System.out.println("Starting Operation");
    if (percentage < 100) {
      System.out.println("Inverting " + percentage + "%");
      percentage++;
    } else if (percentage >= 100) {
      System.out.println("Inversion Compelted");
      bool = !bool;
    }
    
    return bool;
  }
  public void driveWithModes() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward.
    // and backward, and the X turns left and right.
    double speedMultiplier = (1- (robot.joystick.getRawAxis(3) + 1)/4); // 0.5 - 1.0 from joystick
    //System.out.println(speedMultiplier);
    if (!variables.driveInverted) { // inverts drive controls if drive inversion button was pressed
      robot.drive.arcadeDrive(robot.joystick.getY() * -speedMultiplier, robot.joystick.getZ() * speedMultiplier);
    } else { // regular drive
      robot.drive.arcadeDrive(robot.joystick.getY() * speedMultiplier, robot.joystick.getZ() * speedMultiplier);
    }
  }
  public void launcherLoader() {
    if (variables.launcherLoaderF && !variables.launcherLoaderB) {
      robot.launcherLoader.set(0.3);
    } else if (variables.launcherLoaderB && !variables.launcherLoaderF) {
      robot.launcherLoader.set(-0.3);
    } else {
      robot.launcherLoader.stopMotor();
    }
  }
}
/*
  public void getGyroCorrection() {
    boolean firstRun = false;
    int i = 0;
    int i2 = 0;
    double sum = 0;
    double now = 0;
    double next = 0;
    double values[] = new double[5];

    if (!firstRun) {
      variables.gyroTime = System.currentTimeMillis();
      firstRun = true;
    }
    if (i < 5) {
      if (System.currentTimeMillis() >= variables.gyroTime + 100 && i2 == 0) {
        now = robot.gyro.getGyroAngleZ();
        i2++;
      } else if (System.currentTimeMillis() >= variables.gyroTime + 200 && i2 == 1) {
        next = robot.gyro.getGyroAngleZ();
        i2++;
      } else if (i2 == 3) {
        values[i] = next - now;
        i++;
        i2 = 0;
      }
    } else if (i == 5) {
      for (int i3 = 0; i3 < 5; i3++) {
        sum += values[i3];
      }
      variables.gyroCorrection = sum/5;
      i++;
    } 
  }
*/