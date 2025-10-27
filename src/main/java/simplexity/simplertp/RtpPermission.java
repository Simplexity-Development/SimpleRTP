package simplexity.simplertp;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public enum RtpPermission {

    RANDOM_TELEPORT(new Permission("simplertp.teleport", "Allows the use of /rtp", PermissionDefault.TRUE)),
    RTP_RELOAD(new Permission("simplertp.reload", "Allows a player to reload the rtp plugin", PermissionDefault.OP)),
    DISABLED_WORLD_BYPASS(new Permission("simplertp.bypass.worlds", "Allows the use of /rtp in worlds that are disabled", PermissionDefault.FALSE)),
    COOLDOWN_BYPASS(new Permission("simplertp.bypass.cooldown", "Allows a player to rtp without waiting for the cooldown", PermissionDefault.FALSE));
    private final Permission permission;

    RtpPermission(Permission permission) {
        this.permission = permission;
    }

    @NotNull
    public Permission getPermission() {
        return permission;
    }


}
