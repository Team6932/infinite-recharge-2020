package frc.robot;

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
        if (robot.m_autoSelected == Variables.kDefaultAuto) {
            switch (variables.autoStep) {
            case 1: {
                robot.gyro.reset(); // sets curent angle to 0 degrees
                variables.autoStep++;
                break;
            }

            case 2: { // Turn 50 degrees
                if (robot.gyro.getGyroAngleZ() < 90) {
                    robot.drive.arcadeDrive(0, 0.5);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 3: { // Drive until 12in
                if (robot.ultrasonicSensor1.getRangeInches() > 12) {
                    robot.drive.arcadeDrive(0.5, 0);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 4: { // Set gyro to 0
                robot.gyro.reset();
                variables.autoStep++;
                break;
            }

            case 5: { // Rotate +90
                if (robot.gyro.getGyroAngleZ() < 90) {
                    robot.drive.arcadeDrive(0, .5);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 6: { // Drive until closer than 20 ft (in * 12)
                if (robot.ultrasonicSensor1.getRangeInches() < 20 * 12) {
                    robot.drive.arcadeDrive(.6, 0);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 7: { // Reset gyro to 0
                robot.gyro.reset();
                variables.autoStep++;
                break;
            }

            case 8: {
                if (robot.gyro.getGyroAngleZ() > .0100068884) {
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
                robot.gyro.reset();
                variables.autoStep++;
                break;
            }

            case 11: {
                if (robot.gyro.getGyroAngleZ() < -.0100068884) {
                    robot.drive.arcadeDrive(0, -.01);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 12: {
                if (robot.ultrasonicSensor1.getRangeInches() < 12 * 26) {
                    ballCollect(true);
                    robot.drive.arcadeDrive(-.5, 0);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 13: {
                ballCollect(false);
                if (robot.ultrasonicSensor1.getRangeInches() > 12 * 20) {
                    robot.drive.arcadeDrive(.5, 0);
                } else {
                    variables.autoStep++;
                }
                break;
            }

            case 14: {
                robot.gyro.reset();
                variables.autoStep++;
            }

            case 15: {
                if (robot.gyro.getGyroAngleZ() < .0100068884) {
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

    public void launchBall(int ballNumber) {

        variables.ballTime = System.currentTimeMillis();

         if (ballNumber > 0) {
            int i = 0;
            if (i < ballNumber) {
                if (variables.ballTime + (400 * i) + 300 == System.currentTimeMillis()) {
                    ballInsert();
                    i++;
                }
            }
        } else {
            robot.ballLauncher1.stopMotor();
            robot.ballLauncher2.stopMotor();
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

    public void launchBall() {
        // controls the ball launcher
        if (
            variables.twothirdsSpeed && variables.onethirdSpeed 
            || variables.twothirdsSpeed && variables.fullSpeed 
            || variables.onethirdSpeed && variables.fullSpeed
            ) {
                variables.fullSpeed = false;
                variables.onethirdSpeed = false;
                variables.twothirdsSpeed = false;
        } else if (variables.fullSpeed) {
            // sets the two motors controlling the ball launcher to be inverted to eachother
            // (so it fires properly)
            // robot.spinner2.set(ballLauncherSpeed);
            // robot.spinner3.set(-ballLauncherSpeed);
            robot.ballLauncher1.set(1);
            robot.ballLauncher2.set(-1);
        }else if (variables.twothirdsSpeed) {
            robot.ballLauncher1.set(.6);
            robot.ballLauncher2.set(-.6);
        }else if (variables.onethirdSpeed) {
            robot.ballLauncher1.set(.3);
            robot.ballLauncher2.set(-.3);
        } 
    }

    public static Autonomous getInstance() {
        return instance;
    }
}
