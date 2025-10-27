package simplexity.simplertp.logic;

import org.bukkit.Bukkit;
import simplexity.simplertp.SimpleRTP;
import simplexity.simplertp.config.ConfigHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Cooldown {

    public static HashMap<UUID, Long> cooldownUuids = new HashMap<>();

    public static void addToCooldown(UUID uuid) {
        cooldownUuids.put(uuid, System.currentTimeMillis());
        Bukkit.getScheduler().runTaskLaterAsynchronously(SimpleRTP.getInstance(), () -> {
            removeFromCooldown(uuid);
        }, ConfigHandler.getInstance().getCooldownSeconds() * 20L);
    }

    public static Long getTimeLeftSeconds(UUID uuid) {
        Long previousTime = cooldownUuids.get(uuid);
        if (previousTime == null) return null;
        Long currentTime = System.currentTimeMillis();
        long difference = (currentTime - previousTime) / 1000;
        return ConfigHandler.getInstance().getCooldownSeconds() - difference;
    }

    private static void removeFromCooldown(UUID uuid) {
        cooldownUuids.remove(uuid);
    }
}
