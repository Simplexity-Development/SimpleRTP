package simplexity.simplertp;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public enum BorderType {
    VANILLA,
    RADIUS;

    private static final Logger logger = SimpleRTP.getInstance().getSLF4JLogger();
    @NotNull
    public static BorderType fromString(String value) {
        if (value == null || value.isEmpty()) {
            logger.warn("Border type not provided, using 'VANILLA' until a valid type is provided");
            return VANILLA;
        }
        try {
           return BorderType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Border type of {} is invalid, using 'VANILLA' until a valid type is provided", value);
            return VANILLA;
        }
    }
}
