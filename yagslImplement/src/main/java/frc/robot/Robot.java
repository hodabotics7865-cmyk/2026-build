// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.epilogue.Epilogue;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.util.Units;
import frc.robot.LimelightHelpers;
import frc.robot.subsytem.ShooterandIntakeSubsytem;
import swervelib.SwerveDrive;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

@Logged
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  Thread m_visionThread;  

  public Robot() {
    /*
    private final Field2d m_field = new Field2d();
    // Do this in either robot or subsystem init
    SmartDashboard.putData("Field", m_field);
    // Do this in either robot periodic or subsystem periodic
    */

  // Access via:http://roboRIO-7865-frc.local:1181
    m_visionThread = new Thread(
            () -> {
              // Get the UsbCamera from CameraServer
              UsbCamera camera = CameraServer.startAutomaticCapture();
              // Set the resolution
              camera.setResolution(640, 480);
              camera.setFPS(20);

              // Get a CvSink. This will capture Mats from the camera
              CvSink cvSink = CameraServer.getVideo();
              // Setup a CvSource. This will send images back to the Dashboard
              CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

              }
            );

  // Access via:
  // USB Index 0: http://(robotIP):5801 (UI), http://(robotIP):5800 (stream)
  // USB Index 1: http://(robotIP):5811 (UI), http://(robotIP):5810 (stream)
  LimelightHelpers.setupPortForwardingUSB(1);

    m_robotContainer = new RobotContainer();
    m_visionThread.setDaemon(true);
    m_visionThread.start();
    Epilogue.bind(this);
  }

  @Override
  public void robotPeriodic() {


    CommandScheduler.getInstance().run();
    //SwerveDrive.setRobotPose(m_odometry.getPoseMeters());


  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
