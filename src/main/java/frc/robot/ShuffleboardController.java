package frc.robot;//HOI

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleboardController {
    NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
    Variables variables = Variables.getInstance();
    NetworkTable dsInfo = ntinst.getTable("dsInfo");
    RobotHardware robot = RobotHardware.getInstance();
    static ShuffleboardController instance = new ShuffleboardController();

    public SendableChooser<Integer> colorChooser = new SendableChooser<Integer>();
    public SendableChooser<String> m_autoChooser = new SendableChooser<>();
    public SendableChooser<String> m_yawChooser = new SendableChooser<>();

    public ShuffleboardTab tab = Shuffleboard.getTab("LiveWindow");
    public NetworkTableEntry spins;

    // Send non-changing entries to dashboard
    public void init() {
        dsInfo.getEntry("spins").setValue(3);

        colorChooser.setDefaultOption("Blue", 1);
        colorChooser.addOption("Green", 2);
        colorChooser.addOption("Red", 3);
        colorChooser.addOption("Yellow", 4);
        SmartDashboard.putData("colorChooser", colorChooser);

        m_autoChooser.setDefaultOption("Default Auto", variables.kDefaultAuto);
        m_autoChooser.addOption("My Auto", variables.kCustomAuto);
    }

    // Send values to be updated
    public void update() {
    SmartDashboard.putNumber("YawValue", robot.gyro.getGyroAngleZ());

    dsInfo.getEntry("spinning").setValue(variables.colorSpin);
    dsInfo.getEntry("searchColor").setValue(variables.searchColor);
    dsInfo.getEntry("Inverted").setValue(variables.driveInverted);
    dsInfo.getEntry("Spins Counted").setValue(variables.spinNumber / 8);
    dsInfo.getEntry("Testing Motor").setValue(variables.testMotor);
    dsInfo.getEntry("drive straight?!?!??!?!?!?!?!?").setValue(variables.straightDrive);
    dsInfo.getEntry("UltraSonic distance in feet").setValue(robot.ultrasonicSensor1.getRangeInches() / 12);
    dsInfo.getEntry("Ball Launcher Power").setValue(variables.ballSpeed);
    dsInfo.getEntry("Ball Launcher Enabled").setValue(variables.ballLauncher);
    }

    public static ShuffleboardController getInstance() {
        return instance;
    }
}