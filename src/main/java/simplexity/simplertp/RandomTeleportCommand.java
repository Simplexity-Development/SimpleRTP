package simplexity.simplertp;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class RandomTeleportCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("rtp")
                .requires(RandomTeleportCommand::canExecute)
                .executes(RandomTeleportCommand::execute)
                .build();
    }

    private static boolean canExecute(CommandSourceStack css) {
        CommandSender sender = css.getSender();
        if (!(sender instanceof Player player)) return false;
        return player.hasPermission(RtpPermission.RANDOM_TELEPORT.getPermission());

    }


    private static int execute(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
        World world = player.getWorld();
        Bukkit.getScheduler().runTaskAsynchronously(SimpleRTP.getInstance(), () -> {
            Location safeLocation = null;
            int maxAttempts = 10;
            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                Location possibleLocation = getRandomLocation(world);
                int flags = SafetyCheck.checkSafetyFlags(possibleLocation, player);
                if (flags == 0) {
                    safeLocation = possibleLocation;
                    break;
                }
            }
            if (safeLocation == null) {
                player.sendRichMessage("No suitable locations found! Sorry!");
                return;
            }
            Location finalLocation = safeLocation;
            Bukkit.getScheduler().runTask(SimpleRTP.getInstance(), () -> {
                player.teleport(finalLocation);
            });
        });
        return 1;
    }


    private static Location getRandomLocation(World world) {

        double halfWidth = world.getWorldBorder().getSize() / 2.0;
        Location center = world.getWorldBorder().getCenter();
        double centerX = center.x();
        double centerZ = center.z();

        double x = centerX + ThreadLocalRandom.current().nextDouble(-halfWidth, halfWidth);
        double z = centerZ + ThreadLocalRandom.current().nextDouble(-halfWidth, halfWidth);

        int y = world.getHighestBlockYAt((int) x, (int) z);

        return new Location(world, x, y + 1, z);
    }
}
