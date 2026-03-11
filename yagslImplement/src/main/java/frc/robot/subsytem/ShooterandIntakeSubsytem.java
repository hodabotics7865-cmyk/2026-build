package frc.robot.subsytem;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


 public class ShooterandIntakeSubsytem extends SubsystemBase {
    
    private SparkMax shooterMotor = new SparkMax(Constants.ShooterandIntakeConstants.shooterMotorPort, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(Constants.ShooterandIntakeConstants.indexerMotorPort, MotorType.kBrushless);
    public final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
    public final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 0, 0); // Example feedforward values (kS, kV, kA)

  
    public double shooterSpeed = 0;
    public double indexerSpeed = 0;
    public double shooterSpeedIncrement = 3000;
    
    public ShooterandIntakeSubsytem() {}

    public void MoterIncrement(double increment){

        shooterSpeedIncrement += increment;
      }
    
    public void SetMotorSpeed(double SetIdspeed, double ... SetMspeed){ 
        shooterSpeed = SetMspeed[0];
        indexerSpeed = SetIdspeed;
        shooterMotor.set(shooterSpeed);
        indexerMotor.set(indexerSpeed);
  }
    
    /*public void SetShooterSpeed(double SetMspeed){ 

        shooterSpeed = SetMspeed;
  
      }
   public void SetIndexerSpeed(double SetIdspeed){

        indexerSpeed = SetIdspeed;

      } */
   
      public Command Shooter(){
        return this.run(() -> {
            double ff = feedforward.calculate(shooterSpeedIncrement);
            double outputVoltage = ff;
            shooterMotor.setVoltage(outputVoltage);
            double currentVelocity = shooterEncoder.getVelocity();
              // Wait until the shooter speed is within 100 units of the target speed
            
        });
      }
}