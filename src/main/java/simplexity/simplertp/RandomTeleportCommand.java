package simplexity.simplertp;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        RtpWorld rtpWorld = ConfigHandler.getInstance().getRtpWorld(world.getUID());
        //todo exception
        if (!rtpWorld.enabled() && !player.hasPermission(RtpPermission.DISABLED_WORLD_OVERRIDE.getPermission())) {
            player.sendRichMessage(LocaleMessage.WORLD_RTP_DISABLED.getMessage());
            return 0;
        }
        Bukkit.getScheduler().runTaskAsynchronously(SimpleRTP.getInstance(), () -> {
            TeleportLogic.teleportPlayerRandomly(world, rtpWorld, player);
        });
        return 1;
    }


}
