// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsytem.SwerveSubsytem;
import frc.robot.subsytem.VisionSubsystem;
import frc.robot.subsytem.pneumaticSubsystem;
import frc.robot.subsytem.ShooterandIntakeSubsytem;
import swervelib.SwerveInputStream;

import java.security.PublicKey;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

    private final pneumaticSubsystem pneumaticSubsystem = new pneumaticSubsystem();
    private final VisionSubsystem visionSubsystem = new VisionSubsystem();
    private final SwerveSubsytem drivebase = new SwerveSubsytem();
    private final ShooterandIntakeSubsytem ShooterandIntake = new ShooterandIntakeSubsytem();

    private final CommandXboxController driverController = 
     new CommandXboxController(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {

    PortForwarder.add(5801, "172.29.0.1", 5801);
    PortForwarder.add(5802, "172.29.0.1", 5802);
    PortForwarder.add(5803, "172.29.0.1", 5803);
    PortForwarder.add(5804, "172.29.0.1", 5804);
    PortForwarder.add(5805, "172.29.0.1", 5805);
    PortForwarder.add(5806, "172.29.0.1", 5806);
    PortForwarder.add(5807, "172.29.0.1", 5807);
    PortForwarder.add(5808, "172.29.0.1", 5808);
    PortForwarder.add(5809, "172.29.0.1", 5809);



    configureBindings();
    pneumaticSubsystem.pneumaticSubsystemInit();
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getLeftY() * -1,
                                                                () -> driverController.getLeftX() * -1)
                                                            .withControllerRotationAxis(driverController::getRightX)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverController::getRightX,
                                                                                             driverController::getRightY)
                                                           .headingWhile(true);
  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
  
  private void configureBindings() {
      
    // Shoot, Intake, Depost Controls
      driverController.rightTrigger().whileTrue(ShooterandIntake.Shooter().until(() -> Math.abs(ShooterandIntake.shooterEncoder.getVelocity() - ShooterandIntake.shooterSpeedIncrement) <= 100).andThen(()-> ShooterandIntake.SetMotorSpeed(-0.4))); // Shoot with Right trigger button
      driverController.leftTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.SetMotorSpeed(0.4, -0.6))); // Intake  with Left trigger button
      driverController.y().whileTrue(new InstantCommand(() -> ShooterandIntake.SetMotorSpeed(-0.4, 0.6))); // Deposit with left bumper
     
      // Full Stop Controls
      driverController.b().whileTrue(new InstantCommand(() -> {
        ShooterandIntake.SetMotorSpeed(0.0, 0.0); // Stop with B button
      })); // Set shooter speed to 0.0 and set indexer speed to 0.0 while B button is held
      
      // Hopper Controls
      driverController.a().whileTrue(new InstantCommand(() -> pneumaticSubsystem.toggleHopper()));
  
      // Increment and Decrement Shooter Speed Controls
      /*Command defaultCommand = new InstantCommand(() -> ShooterandIntake.Shooter());
      defaultCommand.addRequirements(ShooterandIntake);
      ShooterandIntake.setDefaultCommand(defaultCommand); // Set the default command to stop the shooter and indexer motors when no buttons are pressed
      */

      driverController.povDown().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(-1000)));
      driverController.povUp().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(1000)));
      driverController.povLeft().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(-250)));
      driverController.povRight().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(250)));
     // driverController.start().onTrue //Zero Gyro
  }


  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
