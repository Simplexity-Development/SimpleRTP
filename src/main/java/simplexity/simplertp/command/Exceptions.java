package simplexity.simplertp.command;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import simplexity.simplertp.SimpleRTP;
import simplexity.simplertp.config.LocaleMessage;

public class Exceptions {

    private static final MiniMessage miniMessage = SimpleRTP.getMiniMessage();

    public static SimpleCommandExceptionType RTP_DISABLED_IN_WORLD = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(
                    miniMessage.deserialize(LocaleMessage.WORLD_RTP_DISABLED.getMessage())
            )
    );

    public static DynamicCommandExceptionType COMMAND_ON_COOLDOWN = new DynamicCommandExceptionType(time -> {
        return MessageComponentSerializer.message().serialize(
                miniMessage.deserialize(LocaleMessage.ERROR_COMMAND_ON_COOLDOWN.getMessage(),
                        Placeholder.parsed("time", String.valueOf(time)))
        );
    });

}
