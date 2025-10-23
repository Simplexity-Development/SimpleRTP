package simplexity.simplertp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SafetyCheck {

    public static int checkSafetyFlags(Location location) {
        int flags = 0;
        Location blockAbove = location.clone().add(0, 1, 0);
        Location blockBelow = location.clone().add(0, -1, 0);
        // Fall check, is the player in the air? i.e. is the block below them empty/air?
        if (isEmpty(blockBelow) || blockBelow.getBlock().isPassable()) {
            flags |= SafetyFlag.FALLING.bitFlag;
        }
        // Is there lava?
        if (isMaterial(location, Material.LAVA) || isMaterial(blockAbove, Material.LAVA)) {
            flags |= SafetyFlag.LAVA.bitFlag;
        }
        // Is the location encased in blocks?
        if (blockAbove.getBlock().isSolid()) {
            flags |= SafetyFlag.SUFFOCATION.bitFlag;
        }
        // Is the location underwater?
        if (isMaterial(blockAbove, Material.WATER)) {
            flags |= SafetyFlag.UNDERWATER.bitFlag;
        }

        return flags;
    }

    private static boolean isMaterial(Location location, Material material) {
        return location.getBlock().getType() == material;
    }

    private static boolean isEmpty(Location location) {
        return location.getBlock().getType().isAir();
    }
}
