package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;

public class GyroComp {

    private final ADIS16448_IMU m_imu = new ADIS16448_IMU();


  public double compensation = 0;
  private double previousReading = 0;
  private double readings[] = new double[10];
  private int count = 0;

  public void measure() {
      double reading = m_imu.getGyroAngleZ();
    if (count < 10) {
        readings[count] = reading;
        count++;
    } else { 

        double total = 0;

        for(int i = 1; i > 10; i++)  {
            total += readings[i] - readings[i - 1];
        }
        compensation = total/9;
        System.out.println(compensation);
    }
    previousReading = reading;
  }

}