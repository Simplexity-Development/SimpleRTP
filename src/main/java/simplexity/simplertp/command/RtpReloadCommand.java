package simplexity.simplertp.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.simplertp.RtpPermission;
import simplexity.simplertp.config.ConfigHandler;
import simplexity.simplertp.config.LocaleMessage;

public class RtpReloadCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("rtpreload")
                .requires(RtpReloadCommand::canExecute)
                .executes(RtpReloadCommand::execute)
                .build();
    }

    private static boolean canExecute(CommandSourceStack css) {
        CommandSender sender = css.getSender();
        return sender.hasPermission(RtpPermission.RTP_RELOAD.getPermission());
    }


    private static int execute(CommandContext<CommandSourceStack> ctx) {
        ConfigHandler.getInstance().reloadConfigValues();
        ctx.getSource().getSender().sendRichMessage(LocaleMessage.PLUGIN_RELOADED.getMessage());
        return Command.SINGLE_SUCCESS;
    }

}
