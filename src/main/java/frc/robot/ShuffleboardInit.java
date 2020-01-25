package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class ShuffleboardInit {
    
    public static ShuffleboardInit instance = new ShuffleboardInit();

    
  // shuffleboard setup
  public NetworkTableInstance ntinst = NetworkTableInstance.getDefault();

  NetworkTable dsInfo = ntinst.getTable("dsInfo");

  public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();
  public final SendableChooser<String> m_autoChooser = new SendableChooser<>();
  public final SendableChooser<String> m_yawChooser = new SendableChooser<>();

  // shuffleboard variables
  public ShuffleboardTab tab = Shuffleboard.getTab("LiveWindow");
  public NetworkTableEntry spins;
  

    public static ShuffleboardInit getInstance() {
        return instance;
    }

}