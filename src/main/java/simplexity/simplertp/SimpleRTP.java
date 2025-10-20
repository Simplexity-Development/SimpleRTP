package simplexity.simplertp;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleRTP extends JavaPlugin {

    private static SimpleRTP instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS, commands -> {
                    commands.registrar().register(RandomTeleportCommand.createCommand());
                }
        );
        // Plugin startup logic

    }

    public static SimpleRTP getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
