package frc.robot;

import com.revrobotics.ColorMatch;

import org.ejml.equation.Variable;

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

  // intergers
  int spinNumber = 0;
  int autoStep = 0;
  
  //doubles
  double testMotorSpeed = 0;
  double time = 0;

  // strings
  public static final String kDefaultAuto = "Default";
  public static final String kCustomAuto = "My Auto";

  public static final String kYawDefault = "Z-Axis";
  public static final String kYawXAxis = "X-Axis";
  public static final String kYawYAxis = "Y-Axis";

  public String m_yawSelected;

  
  // color variables
  Color kSelectedTarget = null;
  public Color kPreviousColor = null;


  public static Variables getInstance() {
      return instance;
  }
    
}