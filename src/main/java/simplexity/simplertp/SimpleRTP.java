package simplexity.simplertp;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplertp.command.RandomTeleportCommand;
import simplexity.simplertp.config.ConfigHandler;

public final class SimpleRTP extends JavaPlugin {

    private static SimpleRTP instance;

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
