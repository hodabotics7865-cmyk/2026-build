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



    configureBindings();

    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getLeftY() * 1,
                                                                () -> driverController.getLeftX() * 1)
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
      
    // Shooter Controls
      driverController.rightTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.SetShooterSpeed(-0.6))); // Shoot and Intake with Right trigger button
      driverController.leftTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.SetShooterSpeed(0.6))); // Deposit with Left trigger button
     
      // Indexer Controls
      driverController.a().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(0.6))); // Index Intake with left bumper
      driverController.rightBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(-0.6))); // Index to Shoot or Deposit with right bumper
     
      // Full Stop Controls
      driverController.b().whileTrue(new InstantCommand(() -> {
        ShooterandIntake.SetShooterSpeed(0.0); // Stop with A button
        ShooterandIntake.SetIndexerSpeed(0.0);
      })); // Set shooter speed to 0.0 and set indexer speed to 0.0 while A button is held
      
      // Hopper Controls
      driverController.y().whileTrue(new InstantCommand(() -> pneumaticSubsystem.toggleHopper()));
  
      // Increment and Decrement Shooter Speed Controls
      Command defaultCommand = new InstantCommand(() -> ShooterandIntake.Shooter());
      defaultCommand.addRequirements(ShooterandIntake);
      ShooterandIntake.setDefaultCommand(defaultCommand); // Set the default command to stop the shooter and indexer motors when no buttons are pressed

      driverController.povDown().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(-0.10)));
      driverController.povUp().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(0.10)));
      driverController.povLeft().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(-0.01)));
      driverController.povRight().onTrue(new InstantCommand(() -> ShooterandIntake.MoterIncrement(0.01)));
  }


  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
