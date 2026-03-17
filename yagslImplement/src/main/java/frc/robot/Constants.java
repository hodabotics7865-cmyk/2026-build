package frc.robot;

import edu.wpi.first.math.util.Units;

public final class Constants {

    public static class OperatorConstants {
        public static final int kDriverControllerPort = 0;
        public static final double DEADBAND = 0.05;
    }

    public static class ShooterandIntakeConstants {
        public static final int shooterMotorPort = 10;
        public static final int indexerMotorPort = 11;
    }

    public static class ControlHubConstants{
        public static final int powerHubID = 31;
        public static int pneumaticsHubID = 1;
    }
    
    public static final double maxSpeed = Units.feetToMeters( 20);
}
