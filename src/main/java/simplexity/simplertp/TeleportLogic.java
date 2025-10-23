package simplexity.simplertp;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class TeleportLogic {


    public static boolean teleportPlayerRandomly(World world, RtpWorld rtpWorld, Player player) {
        BorderConfig borderConfig = rtpWorld.borderConfig();
        Location safeLocation = null;
        int maxAttempts = 10;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Location possibleLocation;
            if (rtpWorld.borderConfig().type().equals(BorderType.RADIUS)) {
                possibleLocation = getRandomRadiusLocation(world, borderConfig);
            } else {
                possibleLocation = getRandomVanillaLocation(world, borderConfig);
            }
            int flags = SafetyCheck.checkSafetyFlags(possibleLocation);
            if (flags == 0) {
                safeLocation = possibleLocation;
                break;
            }
        }
        if (safeLocation == null) {
            player.sendRichMessage(LocaleMessage.UNABLE_TO_FIND_LOCATION.getMessage());
            return false;
        }
        Location finalLocation = safeLocation;
        Bukkit.getScheduler().runTask(SimpleRTP.getInstance(), () -> {
            player.teleport(finalLocation);
            player.sendRichMessage(LocaleMessage.SUCCESSFUL_TELEPORT.getMessage(),
                    Placeholder.parsed("x-loc", String.valueOf(finalLocation.getBlockX())),
                    Placeholder.parsed("y-loc", String.valueOf(finalLocation.getBlockY())),
                    Placeholder.parsed("z-loc", String.valueOf(finalLocation.getBlockZ())),
                    Placeholder.parsed("world-name", finalLocation.getWorld().getName()));
        });
        return true;
    }


    private static Location getRandomRadiusLocation(World world, BorderConfig borderConfig) {
        double centerX = borderConfig.centerX();
        double centerZ = borderConfig.centerZ();
        double radiusX = borderConfig.radiusX();
        double radiusZ = borderConfig.radiusZ();
        double x = centerX + ThreadLocalRandom.current().nextDouble(-radiusX, radiusX);
        double z = centerZ + ThreadLocalRandom.current().nextDouble(-radiusZ, radiusZ);
        int y = world.getHighestBlockYAt((int) x, (int) z);
        // >:U DONT DELETE THE + 1 I KEEP GETTING TELEPORTED HALF IN THE GROUND DANGIT
        return new Location(world, x, y + 1, z);
    }

    private static Location getRandomVanillaLocation(World world, BorderConfig borderConfig) {
        Location centerLocation = world.getWorldBorder().getCenter();
        double margin = borderConfig.margin();
        double centerX = centerLocation.x();
        double centerZ = centerLocation.z();
        double radius = (world.getWorldBorder().getSize() / 2) - margin;
        double x = centerX + ThreadLocalRandom.current().nextDouble(-radius, radius);
        double z = centerZ + ThreadLocalRandom.current().nextDouble(-radius, radius);
        int y = world.getHighestBlockYAt((int) x, (int) z);
        // DONT DELETE IT HERE EITHER
        return new Location(world, x, y + 1, z);
    }
}
