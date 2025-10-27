package simplexity.simplertp.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum LocaleMessage {

    UNABLE_TO_FIND_LOCATION("error.unable-to-find-location", "<red>Unable to find a suitable teleport location!</red>"),
    WORLD_DOES_NOT_EXIST("error.world-does-not-exist", "<red>No worlds found by the name of <world-name>, please check your spelling</red>"),
    WORLD_RTP_DISABLED("error.world-rtp-disabled", "<red>Sorry! Random teleport is not enabled in this world!"),
    ERROR_COMMAND_ON_COOLDOWN("error.command-on-cooldown", "<gray>You must wait <time>s longer before you can use that command again</gray>"),
    SUCCESSFUL_TELEPORT("teleport.success", "<green>Randomly teleported to <yellow><x-loc>x</yellow>, <yellow><y-loc>y</yellow>, <yellow><z-loc>z</yellow>, in <yellow><world-name></yellow>"),
    PLUGIN_RELOADED("plugin.reloaded", "<gold>Simple RTP has been reloaded!</gold>");
    private final String path;
    private String message;

    LocaleMessage(String path, String message) {
        this.path = path;
        this.message = message;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public String getMessage() {
        if (message == null) return "";
        return message;
    }

    public void setMessage(@Nullable String message) {
        if (message == null) message = "";
        this.message = message;
    }
}
