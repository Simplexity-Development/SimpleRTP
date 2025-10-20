package simplexity.simplertp;

import java.util.UUID;

public record RtpWorld(UUID worldUuid, BorderType type, BorderConfig borderConfig, boolean enabled, int maxAttempts) {
}
