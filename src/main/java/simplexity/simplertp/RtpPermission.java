package simplexity.simplertp;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public enum RtpPermission {

    RANDOM_TELEPORT(new Permission("simplertp.teleport", "Allows the use of /rtp", PermissionDefault.TRUE)),
    DISABLED_WORLD_OVERRIDE(new Permission("simplertp.override.worlds", "Allows the use of /rtp in worlds that are disabled", PermissionDefault.OP));
    private final Permission permission;

    RtpPermission(Permission permission) {
        this.permission = permission;
    }

    @NotNull
    public Permission getPermission() {
        return permission;
    }


}
