package simplexity.simplertp;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplertp.command.RandomTeleportCommand;
import simplexity.simplertp.command.RtpReloadCommand;
import simplexity.simplertp.config.ConfigHandler;

public final class SimpleRTP extends JavaPlugin {

    private static SimpleRTP instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        ConfigHandler.getInstance().reloadConfigValues();
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS, commands -> {
                    commands.registrar().register(RandomTeleportCommand.createCommand());
                    commands.registrar().register(RtpReloadCommand.createCommand());
                }
        );
        registerPermissions();
        // Plugin startup logic

    }

    public static SimpleRTP getInstance() {
        return instance;
    }

    private void registerPermissions(){
        for (RtpPermission perm : RtpPermission.values()) {
            getServer().getPluginManager().addPermission(perm.getPermission());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
