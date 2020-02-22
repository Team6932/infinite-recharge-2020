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

  // intergers
  int spinNumber = 0;
  int autoStep = 0;
  int musicTime = 0;
  int ballNumber = 0;

  public static final int kLeftMotorPort = 0;
  public static final int kRightMotorPort = 1;

  //doubles
  double testMotorSpeed = 0;
  double time = 0;
  double ballTime = 0;
  double musicSpeed = 0;

  // strings
  public static final String kDefaultAuto = "Default";
  public static final String kCustomAuto = "My Auto";

  public String m_yawSelected;

  
  // color variables
  Color kSelectedTarget = null;
  public Color kPreviousColor = null;


  public static Variables getInstance() {
      return instance;
  }
    
}