package frc.robot;

import org.ejml.equation.VariableScalar;

public class Autonomous {
    private Variables variables = Variables.getInstance();
    private RobotHardware robot = RobotHardware.getInstance();
    private ShuffleboardController dashboard = ShuffleboardController.getInstance();
    private static Autonomous instance = new Autonomous();

    private long autoStartTime = 0;

    public void init() {

        robot.m_autoSelected = dashboard.m_autoChooser.getSelected();
        autoStartTime = System.currentTimeMillis(); // get current time

        robot.gyro.reset();// Resets gyro
    }

    public void periodic() {
        //if (robot.m_autoSelected == Variables.kDefaultAuto) {
            if(System.currentTimeMillis() < variables.autonomousTime + 2000) {
                robot.drive.arcadeDrive(-.6, 0);
            } else if(System.currentTimeMillis() < variables.autonomousTime + 5000) {
                robot.drive.arcadeDrive(0.6, 0.9);
            } else if (System.currentTimeMillis() < variables.autonomousTime + 6500) {
                robot.drive.stopMotor();
                robot.ballLauncher1.set(0.5);
                robot.ballLauncher2.set(-0.5);
            } else if (System.currentTimeMillis() < variables.autonomousTime + 7500) {
                robot.launcherLoader.set(0.3);
            }else {
                robot.drive.stopMotor();
                robot.launcherLoader.stopMotor();
                robot.ballLauncher1.stopMotor();
                robot.ballLauncher2.stopMotor();
                
            }

        //}
            
    }

    public void launchBall(int ballNumber) {
        if (!variables.setLauncherTime) {
        variables.ballTime = System.currentTimeMillis();
        variables.setLauncherTime = true;
        } else if (ballNumber > 0 && variables.setLauncherTime) {
            
            if (variables.i < ballNumber) {
                if (variables.ballTime + (400 * variables.i) + 300 == System.currentTimeMillis()) {
                    ballInsert();
                    variables.i++;
                }
            }
        } else {
            robot.ballLauncher1.stopMotor();
            robot.ballLauncher2.stopMotor();
            variables.setLauncherTime = false;
        }
    }

    public void ballInsert() {
        boolean first = false;
        if (!first) {
        variables.insertTime = System.currentTimeMillis();
        first = true;
        }
        robot.launcherLoader.set(0.2);
        if (System.currentTimeMillis() == variables.insertTime + 300)
            robot.launcherLoader.stopMotor();
    }

    public void ballCollect(boolean enabled) {
        if (enabled) {
        robot.hopperLoader.set(1);
        } else if (!enabled) {
            robot.hopperLoader.stopMotor();
        }
    }

    public static Autonomous getInstance() {
        return instance;
    }
}
