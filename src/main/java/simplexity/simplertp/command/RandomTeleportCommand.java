package simplexity.simplertp.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplertp.RtpPermission;
import simplexity.simplertp.SimpleRTP;
import simplexity.simplertp.config.ConfigHandler;
import simplexity.simplertp.config.RtpWorld;
import simplexity.simplertp.logic.Cooldown;
import simplexity.simplertp.logic.TeleportLogic;

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


    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
        Long secondsOfCooldown = Cooldown.getTimeLeftSeconds(player.getUniqueId());
        if (secondsOfCooldown != null && !player.hasPermission(RtpPermission.COOLDOWN_BYPASS.getPermission()))
            throw Exceptions.COMMAND_ON_COOLDOWN.create(secondsOfCooldown);
        World world = player.getWorld();
        RtpWorld rtpWorld = ConfigHandler.getInstance().getRtpWorld(world.getUID());
        if (!rtpWorld.enabled() && !player.hasPermission(RtpPermission.DISABLED_WORLD_BYPASS.getPermission()))
            throw Exceptions.RTP_DISABLED_IN_WORLD.create();

        Bukkit.getScheduler().runTaskAsynchronously(SimpleRTP.getInstance(), () -> {
            TeleportLogic.teleportPlayerRandomly(world, rtpWorld, player);
        });
        return 1;
    }


}
