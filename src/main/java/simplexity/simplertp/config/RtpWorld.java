package simplexity.simplertp.config;

import java.util.UUID;

public record RtpWorld(UUID worldUuid, BorderConfig borderConfig, boolean enabled, int maxAttempts) {
}
