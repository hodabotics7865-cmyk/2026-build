// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsytem.SwerveSubsytem;
import frc.robot.subsytem.ShooterandIntakeSubsytem;
import swervelib.SwerveInputStream;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

  
    private final SwerveSubsytem drivebase = new SwerveSubsytem();
    private final ShooterandIntakeSubsytem ShooterandIntake = new ShooterandIntakeSubsytem();

    private final CommandXboxController driverController = 
     new CommandXboxController(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {


    configureBindings();
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
      driverController.x().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIntakeSpeed(-0.6))); // Intake with X button
      driverController.y().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIntakeSpeed(0.6))); // Deposit with Y button
      driverController.b().whileTrue(new InstantCommand(() -> ShooterandIntake.SetShooterSpeed(0.6))); // Pass with B button
      driverController.a().whileTrue(new InstantCommand(() -> {                                                  // Stop with A button
        ShooterandIntake.SetIntakeSpeed(0.0);
        ShooterandIntake.SetShooterSpeed(0.0);
        ShooterandIntake.SetIndexerSpeed(0.0);
      })); // Set shooter speed to 0.0 and set indexer speed to 0.0 while A button is held
      driverController.leftBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(0.6))); // Index Intake with left bumper
      driverController.rightBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(-0.6))); // Index to shoot with right bumper
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
