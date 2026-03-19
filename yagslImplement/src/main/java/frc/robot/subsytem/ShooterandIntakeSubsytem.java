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
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


 public class ShooterandIntakeSubsytem extends SubsystemBase {
    //Map the motors and encoders for the shooter and indexer from the constants file.
    //It's Important for the controllers to know what kind of motor they are controlling, otherswise they will not function properly or could even damage the mot
    private SparkMax shooterMotor = new SparkMax(Constants.ShooterandIntakeConstants.shooterMotorPort, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(Constants.ShooterandIntakeConstants.indexerMotorPort, MotorType.kBrushless);
    //add intake when ready

    public final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
    public final RelativeEncoder indexerEncoder = indexerMotor.getEncoder();
    //add intake encoder

    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.1, 0.22, 0.012);

    public double shooterSpeed = 0;
    public double indexerSpeed = 0;
    public double intakeSpeed = 0;
    public double targetRPM = 3000; 

    public ShooterandIntakeSubsytem() {
        SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
        SmartDashboard.putNumber("Indexer Speed", indexerSpeed);
        SmartDashboard.putNumber("Shooter RPM", shooterEncoder.getVelocity());
        SmartDashboard.putNumber("Indexer RPM", indexerEncoder.getVelocity());  
        SmartDashboard.putNumber("Shooter Current", shooterMotor.getOutputCurrent());
        SmartDashboard.putNumber("Indexer Current", indexerMotor.getOutputCurrent());   
        SmartDashboard.putNumber("Shooter Voltage", shooterMotor.getAppliedOutput());
        SmartDashboard.putNumber("Indexer Voltage", indexerMotor.getAppliedOutput());
    }

    public void allstop(){
      shooterMotor.setVoltage(0);
      indexerMotor.setVoltage(0);
      //intakeMotor.setVoltage(outputsVolts:0);
    }


    //Converts desired RPM into rps and figures out appropriate voltage for desired ouput
    public double launchMath(){
      return feedforward.calculate(targetRPM/55);
    }

    public void spinUp(){
      shooterMotor.setVoltage(launchMath());
      indexerMotor.set(.2);
    }

    public void SetIndexerSpeed(double iSpeed){
      indexerMotor.set(iSpeed);
    }

    public void fireShooter(){
      if (shooterEncoder.getVelocity() >= targetRPM) {
        indexerMotor.set(-0.8);  
        System.out.println("It's over 9000!!!!");
      }
      else{
        System.out.println("Power level insufficient");
      }
    }

    public void incrementRPM(int rate){
      targetRPM += rate;
      SmartDashboard.putNumber("target RPM",targetRPM);
    }

      public void Shooter(){
        shooterMotor.set(shooterSpeed);
        indexerMotor.set(indexerSpeed);


      }
}