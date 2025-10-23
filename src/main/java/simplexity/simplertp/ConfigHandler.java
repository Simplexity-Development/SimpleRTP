package simplexity.simplertp;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.slf4j.Logger;

import java.util.*;

public class ConfigHandler {

    private static final Logger logger = SimpleRTP.getInstance().getSLF4JLogger();

    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) {
            instance = new ConfigHandler();
        }
        return instance;
    }

    private ConfigHandler() {
    }

    private final HashMap<UUID, RtpWorld> configuredWorlds = new HashMap<>();
    private final HashMap<String, BorderConfig> configuredBorders = new HashMap<>();
    private final Set<Key> excludedBiomes = new HashSet<>();
    private boolean defaultRtpEnabled;
    private int defaultMaxAttempts, cooldownSeconds;

    private BorderConfig defaultBorderConfig;


    public void reloadConfigValues() {
        FileConfiguration config = SimpleRTP.getInstance().getConfig();
        setupBorderConfigs(config);
        reloadWorldDefaults(config);
        reloadOverrides(config);
        reloadExcludedBiomes(config);

    }

    @SuppressWarnings("PatternValidation")
    private void reloadExcludedBiomes(FileConfiguration config) {
        excludedBiomes.clear();
        List<String> biomeList = config.getStringList("excluded-biomes");
        if (biomeList.isEmpty()) return;
        for (String biome : biomeList) {
            if (!Key.parseable(biome)) {
                logger.warn("{} is not a parsable key! Please make sure you're declaring the biome as 'namespace:biome_name'", biome);
                continue;
            }
            Key key = Key.key(biome);
            excludedBiomes.add(key);
        }
    }


    private void setupBorderConfigs(FileConfiguration config) {
        configuredBorders.clear();
        ConfigurationSection bordersSection = config.getConfigurationSection("borders");
        if (bordersSection == null) {
            logger.warn("There is no config section for 'borders' - please check that SPACEs were used and not TAB");
            return;
        }
        Set<String> borders = bordersSection.getKeys(false);
        for (String border : borders) {
            ConfigurationSection borderSection = bordersSection.getConfigurationSection(border);
            if (borderSection == null) {
                logger.warn("{} is not a proper configuration section, please check your syntax", border);
                continue;
            }
            BorderConfig borderConfig = resolveBorderConfig(borderSection);
            configuredBorders.put(border, borderConfig);
        }
    }

    private BorderConfig resolveBorderConfig(ConfigurationSection config) {
        BorderType borderType = BorderType.fromString(config.getString("type"));
        if (borderType.equals(BorderType.VANILLA)) {
            double margin = config.getDouble("margin", 100);
            return new BorderConfig(BorderType.VANILLA, margin, 0, 0, 0, 0);
        }
        if (borderType.equals(BorderType.RADIUS)) {
            double centerX = config.getDouble("center.x", 0);
            double centerZ = config.getDouble("center.z", 0);
            double radiusX = config.getDouble("radius.x", 2500);
            double radiusZ = config.getDouble("radius.z", 2500);
            return new BorderConfig(BorderType.RADIUS, 0, centerX, centerZ, radiusX, radiusZ);
        }
        return new BorderConfig(BorderType.VANILLA, 0, 0, 0, 0, 0);
    }

    private void reloadWorldDefaults(FileConfiguration config) {
        ConfigurationSection defaultSection = config.getConfigurationSection("world-defaults");
        if (defaultSection == null) {
            logger.warn("No configuration section found for world defaults, please check your config");
            return;
        }
        defaultRtpEnabled = defaultSection.getBoolean("enabled", false);
        defaultMaxAttempts = defaultSection.getInt("max-attempts", 10);
        String borderToUse = defaultSection.getString("border", "world-border");
        defaultBorderConfig = configuredBorders.get(borderToUse);
        if (defaultBorderConfig == null) {
            logger.warn("No border configuration found under the name of '{}' - please make sure that you use the same name as you declared under 'borders'. Falling back to basic world border", borderToUse);
            defaultBorderConfig = new BorderConfig(BorderType.VANILLA, 0, 0, 0, 0, 0);
        }
    }

    private void reloadOverrides(FileConfiguration config) {
        configuredWorlds.clear();
        ConfigurationSection overridesSection = config.getConfigurationSection("world-overrides");
        if (overridesSection == null) {
            logger.warn("No configuration section found for world overrides");
            return;
        }
        Set<String> keys = overridesSection.getKeys(false);
        for (String worldName : keys) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                logger.warn("No world found by the name of '{}' - please use the name of the world as it is in your world folder, not a display name", worldName);
                continue;
            }
            UUID worldUuid = world.getUID();
            RtpWorld rtpWorld = getWorldSettings(overridesSection.getConfigurationSection(worldName), worldUuid);
            configuredWorlds.put(worldUuid, rtpWorld);
        }

    }

    private RtpWorld getWorldSettings(ConfigurationSection section, UUID worldUuid) {
        if (section == null) {
            logger.warn("No configuration section found for world settings, please check your config");
            return new RtpWorld(worldUuid, new BorderConfig(BorderType.VANILLA, 0, 0, 0, 0, 0), false, 10);
        }
        boolean enabled = section.getBoolean("enabled", false);
        int maxAttempts = section.getInt("max-attempts", 10);
        String borderToUse = section.getString("border", "world-border");
        BorderConfig borderConfig = configuredBorders.get(borderToUse);
        if (borderConfig == null) {
            logger.warn("No border configuration found under the name of '{}' - please make sure that you use the same name as you declared under 'borders'. Falling back to basic world border", borderToUse);
            borderConfig = new BorderConfig(BorderType.VANILLA, 0, 0, 0, 0, 0);
        }
        return new RtpWorld(worldUuid, borderConfig, enabled, maxAttempts);
    }

    public boolean isDefaultRtpEnabled() {
        return defaultRtpEnabled;
    }

    public int getDefaultMaxAttempts() {
        return defaultMaxAttempts;
    }


    public BorderConfig getDefaultBorderConfig() {
        return defaultBorderConfig;
    }

    public HashMap<UUID, RtpWorld> getConfiguredWorlds() {
        return configuredWorlds;
    }

    public RtpWorld getRtpWorld(UUID worldUuid) {
        RtpWorld rtpWorld = configuredWorlds.get(worldUuid);
        if (rtpWorld == null)
            rtpWorld = new RtpWorld(worldUuid, defaultBorderConfig, defaultRtpEnabled, defaultMaxAttempts);
        return rtpWorld;
    }

}
