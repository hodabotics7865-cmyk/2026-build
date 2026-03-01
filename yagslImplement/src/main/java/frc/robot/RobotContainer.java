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
      driverController.x().whileTrue(new InstantCommand((this::doSomething)));
      driverController.y().whileTrue(new InstantCommand(() -> new ShooterandIntakeSubsytem().Shooter()));
      driverController.b().whileTrue(new InstantCommand(() -> new ShooterandIntakeSubsytem().Deposit()));
      driverController.a().whileTrue(new InstantCommand(() -> new ShooterandIntakeSubsytem().Stop()));
  }

  public void doSomething()
  {
     new ShooterandIntakeSubsytem().Intake();
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
