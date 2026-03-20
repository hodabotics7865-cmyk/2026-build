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
    private final CommandJoystick driverController = new CommandJoystick(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {



    configureBindings();

    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getY() * 1,
                                                                () -> driverController.getX() * 1)
                                                            .withControllerRotationAxis(driverController:: getTwist)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverController::getX,
                                                                                             driverController::getY)
                                                           .headingWhile(true);
  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
  
  private void configureBindings() {
    //driverController.button(0).whileTrue(new InstantCommand()() ->)



    // Shooter Controls
      operatorController.rightTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.fireShooter()));// Draw balls from indexer into the flywheel
      operatorController.leftTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.spinUp())); // Prepare flywheel for shooting
     
      // Indexer Controls
      operatorController.a().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(0.6))); // Index Intake with left bumper
      operatorController.rightBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.SetIndexerSpeed(-0.6))); // Index to Shoot or Deposit with right bumper
      operatorController.x().whileTrue(new InstantCommand(() -> ShooterandIntake.purge())); //Reload
      // Full Stop Controls
      operatorController.b().whileTrue(new InstantCommand(() -> ShooterandIntake.allstop())); 
      
      // Hopper Toggle Controls Actuated with Y button
      operatorController.y().whileTrue(new InstantCommand(() -> pneumaticSubsystem.toggleHopper()));
  
      // Increment and Decrement Shooter Speed Target Controls
      operatorController.povUp().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(500)));
      operatorController.povDown().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(-500)));
      
      operatorController.povLeft().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(-100)));
      operatorController.povRight().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(100)));
  }


  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
