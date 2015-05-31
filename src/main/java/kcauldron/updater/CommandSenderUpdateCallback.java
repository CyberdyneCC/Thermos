package kcauldron.updater;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import kcauldron.updater.KVersionRetriever.IVersionCheckCallback;

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
	public void upToDate(String version) {
		CommandSender sender = mSender.get();
		if (sender != null) {
			sender.sendMessage(ChatColor.GREEN
					+ "Running version of KCauldron is up-to-date: " + version);
		}
		DefaultUpdateCallback.INSTANCE.upToDate(version);
	}

	@Override
	public void newVersion(String currentVersion, String newVersion) {
		CommandSender sender = mSender.get();
		if (sender != null) {
			newVersion(sender, currentVersion, newVersion);
		}
		DefaultUpdateCallback.INSTANCE.newVersion(currentVersion, newVersion);
	}

	public static void newVersion(CommandSender sender, String currentVersion, String newVersion) {
		sender.sendMessage(new String[] {
				ChatColor.YELLOW + "Found new version of KCauldron: "
						+ newVersion,
				ChatColor.YELLOW + "Current is " + currentVersion,
				ChatColor.YELLOW + "Type '" + ChatColor.BLUE
						+ "/kc update" + ChatColor.YELLOW
						+ "' to update" });
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
