package frc.robot.subsytem;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
// import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.math.controller.BangBangController;
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
    private SparkFlex intakeMotor = new SparkFlex(Constants.ShooterandIntakeConstants.intakeMotorPort, MotorType.kBrushless);


    public final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
    public final RelativeEncoder indexerEncoder = indexerMotor.getEncoder();
    public final RelativeEncoder intakeEncoder = intakeMotor.getEncoder();

    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.1, 0.22, 0.0125);
    private BangBangController shooterClearBang = new BangBangController();

    public double shooterSpeed = 0;
    public double indexerSpeed = 0;
    public double intakeSpeed = 0;
    public double targetRPM = 3000; 

    public ShooterandIntakeSubsytem() {

    }

    public void allstop(){
      shooterMotor.setVoltage(0);
      indexerMotor.setVoltage(0);
      intakeMotor.setVoltage(0);
      System.out.println("All Stop");
    }


    //Converts desired RPM into rps and figures out appropriate voltage for desired ouput
    public double launchMath(){

      return feedforward.calculate(targetRPM/40);
    }

    public void spinUp(){
      shooterMotor.setVoltage(launchMath());
      indexerMotor.set(.2);

    }

    public void purge(){
      indexerMotor.set(1);
      //we should find motor stall levels in testing and find a way to reactively apply voltage. 
      shooterMotor.set(-0.3);
      intakeMotor.set(-.5);

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

    public void Intake(){
      indexerMotor.set(.5);
      intakeMotor.set(.5);
    }

    public void Deposit(){
      indexerMotor.set(-.5);
      intakeMotor.set(-.5);
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