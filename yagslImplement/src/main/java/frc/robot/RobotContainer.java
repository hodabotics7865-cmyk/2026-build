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
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

    private final pneumaticSubsystem pneumaticSubsystem = new pneumaticSubsystem();
    public final SwerveSubsytem drivebase = new SwerveSubsytem();
    private final ShooterandIntakeSubsytem ShooterandIntake = new ShooterandIntakeSubsytem();
    private final CommandXboxController operatorController = new CommandXboxController(Constants.OperatorConstants.kOperatorControllerPort);
    private final CommandJoystick driverController = new CommandJoystick(Constants.OperatorConstants.kDriverControllerPort);
    private static double  lowGear = 0.1;
  public RobotContainer() {



    configureBindings();

    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getY() * 1,
                                                                () -> driverController.getX() * 1)
                                                            .withControllerRotationAxis(driverController::  getTwist)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(.8 - lowGear)
                                                            .allianceRelativeControl(true);
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverController::getX,
                                                                                             driverController::getY)
                                                           .headingWhile(true);
  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
  
  private void configureBindings() {
    //adjust x-y scalar based on if trigger is pressed.
    driverController.button(2).whileTrue(Commands.startEnd(
      () -> lowGear = 0.1,
      () -> lowGear = 0,
      drivebase
      ));
    //Intake for driver
    driverController.button(1).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.Intake(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));

driverController.button(3).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.Deposit(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));
      
      // Shooter Controls
      operatorController.rightTrigger().whileTrue(new InstantCommand(() -> ShooterandIntake.fireShooter()));// Draw balls from indexer into the flywheel
      operatorController.leftTrigger().whileTrue(Commands.startEnd(//Prepare flywheel for shooting 
        ()-> ShooterandIntake.spinUp() ,
        ()-> ShooterandIntake.allstop(),
             ShooterandIntake )); //just a depenedency declaration 
      
      // Intake Controls
      operatorController.rightBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.Intake())); // Intake
      operatorController.leftBumper().whileTrue(new InstantCommand(() -> ShooterandIntake.Deposit())); // Deposit


      // Indexer Controls
      operatorController.a().whileTrue(new InstantCommand(() -> ShooterandIntake.purge())); //
      operatorController.x().whileTrue(new InstantCommand(() -> ShooterandIntake.fullPurge())); //Reload

      // Full Stop of Subsystem
      operatorController.b().whileTrue(new InstantCommand(() -> ShooterandIntake.allstop())); 
      
      // Hopper Toggle Controls Actuated with Y button
      operatorController.back().whileTrue(new InstantCommand(() -> pneumaticSubsystem.toggleHopper()));
  
      // Increment and Decrement Shooter Speed Target Controls
      operatorController.povUp().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(500)));
      operatorController.povDown().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(-500)));
      //fine tuning
      operatorController.povLeft().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(-100)));
      operatorController.povRight().onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(100)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

}
