package thermos.updater;

import thermos.Thermos;
import thermos.ThermosCommand;
import thermos.updater.TVersionRetriever.IVersionCheckCallback;
import net.minecraft.server.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultUpdateCallback implements IVersionCheckCallback {
    public static DefaultUpdateCallback INSTANCE;

    static {
        INSTANCE = new DefaultUpdateCallback();
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hasPermission(player)) {
            if (mHasUpdate) {
                sendUpdate(player);
            }
        }
    }

    private boolean hasPermission(CommandSender player) {
        return player.hasPermission(ThermosCommand.UPDATE) || player.isOp();
    }

    private void sendUpdate(CommandSender player) {
        CommandSenderUpdateCallback.newVersion(player, mCurrentVersion, mNewVersion);
    }

    private boolean mHasUpdate;
    private String mCurrentVersion;
    private String mNewVersion;

    private DefaultUpdateCallback() {
    }

    @Override
    public void upToDate() {
        mHasUpdate = false;
        mCurrentVersion = Thermos.getCurrentVersion();
        mNewVersion = null;
    }

    @Override
    public void newVersion(String newVersion) {
        mCurrentVersion = Thermos.getCurrentVersion();
        mNewVersion = newVersion;
        if (!mHasUpdate) {
            Bukkit.getConsoleSender().sendMessage("New version of Thermos available: " + newVersion);
            Bukkit.getConsoleSender().sendMessage("Download at: https://tcpr.ca/downloads/thermos");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (hasPermission(player)) {
                    sendUpdate(player);
                }
            }
        }
        mHasUpdate = true;
    }

    @Override
    public void error(Throwable t) {

    }
}
