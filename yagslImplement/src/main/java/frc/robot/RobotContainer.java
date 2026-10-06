// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsytem.SwerveSubsytem;
import frc.robot.subsytem.pneumaticSubsystem;
import frc.robot.subsytem.ShooterandIntakeSubsytem;
import swervelib.SwerveDrive;
import swervelib.SwerveInputStream;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {

    private final pneumaticSubsystem pneumaticSubsystem = new pneumaticSubsystem();
    public final SwerveSubsytem drivebase = new SwerveSubsytem();
    private final ShooterandIntakeSubsytem ShooterandIntake = new ShooterandIntakeSubsytem();
    private final CommandXboxController operatorController = new CommandXboxController(Constants.OperatorConstants.kOperatorControllerPort);
    private final CommandJoystick driverController = new CommandJoystick(Constants.OperatorConstants.kDriverControllerPort);
    private static double  lowGear = 0.1;

    private boolean test = true;

    private Trigger testTrigger = new Trigger(() -> test == true);

  public RobotContainer() {



    configureBindings();
    
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
    // testTrigger
    //   .whileTrue(driveFieldOrientedAngularVelocity) // field centriocd
    //   .whileFalse()// robot centerc
  }
SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getY() * -1,
                                                                () -> driverController.getX() * -1)
                                                            .withControllerRotationAxis(()->Math.pow(driverController.getTwist(), 3))
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(.6 - lowGear)
                                                            .allianceRelativeControl(true);
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverController::getX,
                                                                                             driverController::getY)
                                                           .headingWhile(true);
  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
  
  private void configureBindings() {
    //adjust x-y scalar base Md on if trigger is pressed.

    //Shooter Controls
    driverController.button(1).whileTrue(new InstantCommand(() -> ShooterandIntake.fireShooter()));
    driverController.button(2).whileTrue(Commands.startEnd(//Prepare flywheel for shooting 
        ()-> ShooterandIntake.spinUp() ,
        ()-> ShooterandIntake.allstop(),
             ShooterandIntake )); //just a depenedency declaration 

    driverController.button(9).onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(-500)));
    driverController.button(10).onTrue(new InstantCommand(() -> ShooterandIntake.incrementRPM(500)));

    // Intake and Deposit for driver         
    driverController.button(3).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.Intake(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));
    driverController.button(4).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.Deposit(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));
    // Indexer Controls for driver
    driverController.button(5).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.purge(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));
    driverController.button(6).whileTrue(Commands.startEnd(
      () -> ShooterandIntake.fullPurge(), 
      () -> ShooterandIntake.allstop(), ShooterandIntake));

    driverController.button(7).onTrue(
      new InstantCommand(() -> pneumaticSubsystem.toggleHopper())
    );

    //Debugging and Testing Controls
    //driverController.button(11).onTrue(new InstantCommand( //fix the flywheel inversion issue
    driverController.button(12).onTrue(new InstantCommand(
      () ->{ 
            drivebase.swerveDrive.zeroGyro();
            System.out.println("Gyro Zeroed");  
    }));
    

    // driverController.whileTrue(driveBase.driveRobotOriented); 
 
      
    
    
    
    
    
    // Xbox Conttroller
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
      operatorController.start().onTrue(new InstantCommand(() -> drivebase.swerveDrive.resetOdometry(new Pose2d(0, 0, Rotation2d.kZero))));
  
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
