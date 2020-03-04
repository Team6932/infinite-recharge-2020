package frc.robot;

import edu.wpi.first.wpilibj.util.Color;

public class Variables {

    public static Variables instance = new Variables();
    
  // booleans
  boolean colorSpin = false;
  boolean searchColor = false;
  boolean driveInverted = false;
  boolean ballLauncher = false;
  boolean forward = false;
  boolean testMotor = false;
  boolean testedMotor = false;
  boolean straightDrive = false;
  boolean gyroResetP = false;
  boolean turn1 = false;
  boolean music = false;
  boolean despam = true;
  boolean load = false;
  boolean fullSpeed = false;
  boolean twothirdsSpeed = false;
  boolean onethirdSpeed = false;
  boolean wheelSet = false;
  boolean timed = false;
  boolean setLauncherTime = false;
  boolean launcherLoaderF = false;
  boolean launcherLoaderB = false;

  // intergers
  int spinNumber = 0;
  int autoStep = 0;
  int musicTime = 0;
  int ballNumber = 0;
  int ballSpeedSetting = 0;
  int i = 0;

  public static final int kLeftMotorPort = 0;
  public static final int kRightMotorPort = 1;

  //doubles
  double testMotorSpeed = 0;
  double time = 0;
  double ballTime = 0;
  double musicSpeed = 0;
  double gyroCorrection = 0;
  double insertTime = 0;
  double gyroTime = 0;
  double ballSpeed = 0.5;
  double wheelTime = 0;
  double time2 = 0;
  double hopperSpeed = 0;
  double autonomousTime = 0;
  

  // strings
  public final static String kDefaultAuto = "Default";
  public final static String kCustomAuto = "My Auto";

  public String m_yawSelected;

  
  // color variables
  Color kSelectedTarget = null;
  public Color kPreviousColor = null;


  public static Variables getInstance() {
      return instance;
  }
    
}