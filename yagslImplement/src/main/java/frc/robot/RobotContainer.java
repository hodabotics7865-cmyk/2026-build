// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsytem.SwerveSubsytem;
import frc.robot.subsytem.pneumaticSubsystem;
import frc.robot.subsytem.ShooterandIntakeSubsytem;
import swervelib.SwerveInputStream;



import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

    private final pneumaticSubsystem pneumaticSubsystem = new pneumaticSubsystem();
    private final SwerveSubsytem drivebase = new SwerveSubsytem();
    private final ShooterandIntakeSubsytem ShooterandIntake = new ShooterandIntakeSubsytem();
    private final CommandXboxController operatorController = new CommandXboxController(Constants.OperatorConstants.kOperatorControllerPort);
    private final CommandJoystick driverController = 
     new CommandJoystick(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {



    configureBindings();

    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getY() * 1,
                                                                () -> driverController.getX() * 1)
                                                            .withControllerRotationAxis(driverController::getTwist)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverController::getX,
                                                                                             driverController::getY)
                                                           .headingWhile(true);
  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
  
  private void configureBindings() {
      
    // Shooter Controls
      operatorController.rightTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.SetShooterSpeed(-0.6))); // Shoot and Intake with Right trigger button
      operatorController.leftTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.SetShooterSpeed(0.6))); // Deposit with Left trigger button
     
      // Indexer Controls
      operatorController.a().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(0.6))); // Index Intake with left bumper
      operatorController.rightBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(-0.6))); // Index to Shoot or Deposit with right bumper
     
      // Full Stop Controls
      operatorController.b().whileTrue(new InstantCommand(() -> {
        ShooterandIntake.SetShooterSpeed(0.0); // Stop with A button
        ShooterandIntake.SetIndexerSpeed(0.0);
      })); // Set shooter speed to 0.0 and set indexer speed to 0.0 while A button is held
      
      // Hopper Controls
      operatorController.y().whileTrue(new InstantCommand(() -> pneumaticSubsystem.toggleHopper()));
  
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
