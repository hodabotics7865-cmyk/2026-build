package frc.robot.subsytem;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Logged
public class pneumaticSubsystem extends SubsystemBase {
    private final DoubleSolenoid rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 1, 2);
    private final DoubleSolenoid leftSolenoid  = new DoubleSolenoid(PneumaticsModuleType.REVPH, 3, 4);

    public pneumaticSubsystem() {
        rightSolenoid.set(DoubleSolenoid.Value.kReverse);
        leftSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void toggleHopper() {
        rightSolenoid.toggle();
        leftSolenoid.toggle();
    }

    public void jiggyWitIt(){
        //auto pulse hopper to assist intake
    }

    @Override
    public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    // Publish the solenoid state to telemetry.
    builder.addBooleanProperty("Hopper extended", () -> rightSolenoid.get() == Value.kForward , null);
    }
}
