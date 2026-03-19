package frc.robot.subsytem;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.RelativeEncoder;
// import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


 public class ShooterandIntakeSubsytem extends SubsystemBase {
    
    private SparkMax shooterMotor = new SparkMax(Constants.ShooterandIntakeConstants.shooterMotorPort, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(Constants.ShooterandIntakeConstants.indexerMotorPort, MotorType.kBrushless);

    public final RelativeEncoder shooterEncoder =  shooterMotor.getAlternateEncoder();
    public final RelativeEncoder indexerEncoder = indexerMotor.getAlternateEncoder(); 

    public double shooterSpeed = 0;
    public double indexerSpeed = 0;

    public ShooterandIntakeSubsytem() {}

    public void MoterIncrement(double increment){

        shooterSpeed += increment;
        indexerSpeed += increment;



      }
    
   public void SetShooterSpeed(double SetMspeed){ 

        shooterSpeed = SetMspeed;
  
      }
   public void SetIndexerSpeed(double SetIdspeed){

        indexerSpeed = SetIdspeed;

      }
   
      public void Shooter(){
        shooterMotor.set(shooterSpeed);
        indexerMotor.set(indexerSpeed);

        SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
        SmartDashboard.putNumber("Indexer Speed", indexerSpeed);
        SmartDashboard.putNumber("Shooter RPM", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("Indexer RPM", indexerEncoder.getVelocity());  
        SmartDashboard.putNumber("Shooter Current", shooterMotor.getOutputCurrent());
        SmartDashboard.putNumber("Indexer Current", indexerMotor.getOutputCurrent());   
        SmartDashboard.putNumber("Shooter Voltage", shooterMotor.getAppliedOutput());
        SmartDashboard.putNumber("Indexer Voltage", indexerMotor.getAppliedOutput());
      }
}