package frc.robot.subsytem;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Seconds;

// import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


 public class ShooterandIntakeSubsytem extends SubsystemBase {
    
    private SparkMax intakeMotor = new SparkMax(Constants.ShooterandIntakeConstants.intakeMotorPort, MotorType.kBrushless);
    private SparkMax shooterMotor = new SparkMax(Constants.ShooterandIntakeConstants.shooterMotorPort, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(Constants.ShooterandIntakeConstants.indexerMotorPort, MotorType.kBrushless);
    
    public double intakeSpeed = 0;
    public double shooterSpeed = 0;
    public double indexerSpeed = 0;

    public ShooterandIntakeSubsytem() {}

    public void MoterIncrement(double increment){

        shooterSpeed += increment;

      }
      public void  SetIntakeSpeed(double SetItspeed){

        intakeSpeed = SetItspeed;

      }

   public void SetShooterSpeed(double SetMspeed){ 

        shooterSpeed = SetMspeed;
  
      }
   public void SetIndexerSpeed(double SetIdspeed){

        indexerSpeed = SetIdspeed;

      }
   
      public void Shooter(){
        intakeMotor.set(intakeSpeed); 
        shooterMotor.set(shooterSpeed);
        indexerMotor.set(indexerSpeed);
      }
}