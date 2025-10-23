package simplexity.simplertp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum LocaleMessage {

    UNABLE_TO_FIND_LOCATION("error.unable-to-find-location", "<red>Unable to find a suitable teleport location!</red>"),
    WORLD_RTP_DISABLED("error.world-rtp-disabled", "<red>Sorry! Random teleport is not enabled in this world!"),
    SUCCESSFUL_TELEPORT("teleport.success", "<green>Randomly teleported to <yellow><x-loc>x</yellow>, <yellow><y-loc>y</yellow>, <yellow><z-loc>z</yellow>, in <yellow><world-name></yellow>");
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
