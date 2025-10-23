package simplexity.simplertp.logic;

public enum SafetyFlag {
    DAMAGE_RISK(1),
    FALLING(2),
    FIRE(4),
    LAVA(8),
    NOT_SOLID(16),
    SUFFOCATION(32),
    UNDERWATER(64),
    UNSTABLE(128),
    NULL_LOCATION(256);

    final int bitFlag;

    SafetyFlag(int bitFlag) {
        this.bitFlag = bitFlag;
    }

    public boolean matches(int bitFlags) {
        int result = bitFlag & bitFlags;
        return result == bitFlag;
    }
}
