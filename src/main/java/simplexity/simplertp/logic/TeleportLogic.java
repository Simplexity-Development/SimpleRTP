package simplexity.simplertp.logic;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import simplexity.simplertp.SimpleRTP;
import simplexity.simplertp.config.BorderConfig;
import simplexity.simplertp.config.BorderType;
import simplexity.simplertp.config.LocaleMessage;
import simplexity.simplertp.config.RtpWorld;

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
                    Placeholder.parsed("world-name", finalLocation.getWorld().getName().replace("_", " ")));
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
        Integer y = highestYBlock(world, (int) x, (int) z);
        if (y == null) return null;
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
        Integer y = highestYBlock(world, (int) x, (int) z);
        if (y == null) return null;
        // DONT DELETE IT HERE EITHER
        return new Location(world, x, y + 1, z);
    }

    private static Integer highestYBlock(World world, int x, int z){
        if (!world.hasCeiling()) return world.getHighestBlockYAt(x, z);
        int ceilingHeightProbably = world.getHighestBlockYAt(z, x);
        boolean haveHitAir = false;
        for (int i = ceilingHeightProbably; i >= 20; i--) {
            Block block = world.getBlockAt(x, i, z);
            if (block.getType().isAir()) {
                haveHitAir = true;
                continue;
            }
            if (haveHitAir && block.isSolid()) {
                return i;
            }
        }
        return null;
    }
}
