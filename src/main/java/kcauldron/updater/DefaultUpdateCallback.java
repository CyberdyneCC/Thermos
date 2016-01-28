package kcauldron.updater;

import kcauldron.KCauldron;
import kcauldron.KCauldronCommand;
import kcauldron.updater.KVersionRetriever.IVersionCheckCallback;
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
            if (KCauldron.isLegacy()) {
            }            
            if (!KCauldron.isOfficial()) {
                player.sendMessage(ChatColor.YELLOW + "[KC-Unofficial] " + ChatColor.GOLD + "You're running KC-Unofficial, make sure to check https://github.com/TCPR/KCauldron for updates or to report bugs.");
            }            
            if (mHasUpdate) {
                sendUpdate(player);
            }
        }
    }

    private boolean hasPermission(CommandSender player) {
        return player.hasPermission(KCauldronCommand.UPDATE) || player.isOp();
    }

    private void sendUpdate(CommandSender player) {
        CommandSenderUpdateCallback.newVersion(player, mCurrentVersion,
                mNewVersion);
    }

    private boolean mHasUpdate;
    private String mCurrentVersion;
    private String mNewVersion;

    private DefaultUpdateCallback() {
    }

    @Override
    public void upToDate() {
        mHasUpdate = false;
        mCurrentVersion = KCauldron.getCurrentVersion();
        mNewVersion = null;
    }

    @Override
    public void newVersion(String newVersion) {
        mCurrentVersion = KCauldron.getCurrentVersion();
        mNewVersion = newVersion;
        if (!mHasUpdate) {
            Bukkit.getConsoleSender().sendMessage(
                    "New version of KCauldron avaiable: " + newVersion);
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
