package frc.robot.subsytem;

import edu.wpi.first.epilogue.Logged;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
@Logged
public class VisionSubsystem extends SubsystemBase  {

    // Basic targeting data
    double tx = LimelightHelpers.getTX("");  // Horizontal offset from crosshair to target in degrees
    double ty = LimelightHelpers.getTY("");  // Vertical offset from crosshair to target in degrees
    double ta = LimelightHelpers.getTA("");  // Target area (0% to 100% of image)
    boolean hasTarget = LimelightHelpers.getTV(""); // Do you have a valid target?

    double txnc = LimelightHelpers.getTXNC("");  // Horizontal offset from principal pixel/point to target in degrees
    double tync = LimelightHelpers.getTYNC("");  // Vertical offset from principal pixel/point to target in degrees

    



    /*
    public String stream = NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").getString("0");
q@wWWWWWWWWWWWWWWWWWWWWWWW1Azq public double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
q@wWWWWWWWWWWWWWWWWWWWWWWW1Azq public double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    public double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    public boolean hasTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0) == 1;
    */

}

