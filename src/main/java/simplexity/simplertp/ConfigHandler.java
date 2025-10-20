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
    private double defaultMargin, defaultCenterX, defaultCenterZ,
            defaultRadiusX, defaultRadiusZ;
    private RtpWorld defaultWorld;
    private String defaultTypeString;

    public void reloadConfigValues(){
        FileConfiguration config = SimpleRTP.getInstance().getConfig();

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


    private void setupBorderConfigs(FileConfiguration config){
        configuredBorders.clear();
        ConfigurationSection bordersSection = config.getConfigurationSection("borders");
        if (bordersSection == null) {
            logger.warn("There is no config section for 'borders' - please check that SPACEs were used and not TAB");
            return;
        }
        Set<String> borders = bordersSection.getKeys(false);
        for (String border : borders) {
            ConfigurationSection borderSection = config.getConfigurationSection(border);
            if (borderSection == null) {
                logger.warn("{} is not a proper configuration section, please check your syntax", border);
                continue;
            }
            BorderConfig borderConfig = resolveBorderConfig(borderSection);
            configuredBorders.put(border, borderConfig);
        }
    }

    private BorderConfig resolveBorderConfig(ConfigurationSection config){
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
    private void reloadWorldDefaults(FileConfiguration config){
        defaultRtpEnabled = config.getBoolean("world-settings.default.enabled", false);
        defaultMaxAttempts = config.getInt("world-settings.default.max-attempts", 10);
        defaultTypeString = config.getString("world-settings.default.border.type", "VANILLA");
        defaultCenterX = config.getDouble("border-defaults.radius.center.x", 0);
        defaultCenterZ = config.getDouble("border-defaults.radius.center.z", 0);
        defaultRadiusX = config.getDouble("border-defaults.radius.radius-x", 2500);
        defaultRadiusZ = config.getDouble("border-defaults.radius.radius-z", 2500);
    }

    private void setupWorlds(FileConfiguration config) {
        ConfigurationSection worldOverrides = config.getConfigurationSection("world-overrides");
        if (worldOverrides == null) {
            logger.warn("RTP Settings section is null, please check your config");
            return;
        }
        for (String key : worldOverrides.getKeys(false)) {
            World world = Bukkit.getWorld(key);
            if (world == null) {
                logger.warn("The world '{}' does not appear to exist. Please make sure you are using the name of the world as it is declared in your server folders and not it's display name", key);
                continue;
            }

            ConfigurationSection worldSettings = worldOverrides.getConfigurationSection(key);
            if (worldSettings == null) {
                logger.warn("{} does not appear to have any settings attached to it, please be sure the YML file is valid and is using SPACE and not TAB", key);
                continue;
            }
            boolean enabled = worldSettings.getBoolean("enabled", defaultRtpEnabled);
            int maxAttempts = worldSettings.getInt("max-attempts", defaultMaxAttempts);
            BorderType type = BorderType.fromString(worldSettings.getString("type", defaultTypeString));
            if (type.equals(BorderType.VANILLA)) {}
        }
    }


    public boolean isDefaultRtpEnabled() {
        return defaultRtpEnabled;
    }

    public int getDefaultMaxAttempts() {
        return defaultMaxAttempts;
    }

    public double getDefaultMargin() {
        return defaultMargin;
    }

    public double getDefaultRadiusX() {
        return defaultRadiusX;
    }

    public double getDefaultRadiusZ() {
        return defaultRadiusZ;
    }

    public double getDefaultCenterX() {
        return defaultCenterX;
    }

    public double getDefaultCenterZ() {
        return defaultCenterZ;
    }
}
