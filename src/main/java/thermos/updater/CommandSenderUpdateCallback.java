package thermos.updater;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import thermos.Thermos;
import thermos.updater.TVersionRetriever.IVersionCheckCallback;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandSenderUpdateCallback implements IVersionCheckCallback {
    private Reference<CommandSender> mSender;

    public CommandSenderUpdateCallback(CommandSender sender) {
        mSender = new WeakReference<CommandSender>(sender);
    }

    protected CommandSender getSender() {
        return mSender.get();
    }

    @Override
    public void upToDate() {
        CommandSender sender = mSender.get();
        if (sender != null) {
            sender.sendMessage(ChatColor.GREEN
                    + "Running version of Thermos is up-to-date: "
                    + Thermos.getCurrentVersion());
        }
        DefaultUpdateCallback.INSTANCE.upToDate();
    }

    @Override
    public void newVersion(String newVersion) {
        CommandSender sender = mSender.get();
        if (sender != null) {
            newVersion(sender, Thermos.getCurrentVersion(), newVersion);
        }
        DefaultUpdateCallback.INSTANCE.newVersion(newVersion);
    }

    public static void newVersion(CommandSender sender, String currentVersion,
            String newVersion) {
        sender.sendMessage(new String[] {
                ChatColor.YELLOW + "Found new version of Thermos: "
                        + newVersion,
                ChatColor.YELLOW + "Current is " + currentVersion,
                ChatColor.YELLOW + "Type '" + ChatColor.BLUE + "/kc update"
                        + ChatColor.YELLOW + "' to update" });
    }

    @Override
    public void error(Throwable t) {
        CommandSender sender = mSender.get();
        if (sender != null) {
            sender.sendMessage(ChatColor.RED
                    + "Error ocurred durring version check, see details in server log");
        }
    }
}
