package kcauldron.updater;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import kcauldron.KCauldron;
import net.minecraft.server.MinecraftServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.google.common.base.Splitter;

public class KCauldronUpdater implements Runnable {
	private static final class LatestVersionCallback extends
			CommandSenderUpdateCallback {
		public LatestVersionCallback(CommandSender sender) {
			super(sender);
		}

		@Override
		public void newVersion(String currentVersion, String newVersion) {
			startUpdate(getSender(), newVersion);
		}

		@Override
		public void upToDate(String version) {
			KCauldron.sUpdateInProgress = false;
			CommandSender sender = getSender();
			if (sender != null) {
				sender.sendMessage(ChatColor.DARK_PURPLE + "Current version ("
						+ version + ") is up to date");
			}
		}

		@Override
		public void error(Throwable t) {
			super.error(t);
			KCauldron.sUpdateInProgress = false;
		}
	}

	public static void initUpdate(CommandSender sender, String version) {
		if (KCauldron.sUpdateInProgress) {
			sender.sendMessage(ChatColor.RED
					+ "Update stopped: another update in progress");
			return;
		}
		KCauldron.sUpdateInProgress = true;
		if (version == null) {
			sender.sendMessage(ChatColor.DARK_PURPLE
					+ "Fetching latest version...");
			new KVersionRetriever(new LatestVersionCallback(sender), false);
		} else {
			startUpdate(sender, version);
		}
	}

	private static void startUpdate(CommandSender sender, String version) {
		if (sender != null) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "Starting update to "
					+ version + "...");
		}
		new KCauldronUpdater(sender, version);
	}

	private final CommandSender mSender;
	private final String mVersion;
	private final Thread mThread;

	public KCauldronUpdater(CommandSender sender, String version) {
		mSender = sender;
		mVersion = version;
		mThread = new Thread(this);
		mThread.setName("KCauldron updater");
		mThread.setPriority(Thread.MIN_PRIORITY);
		mThread.start();
	}

	@Override
	public void run() {
		try {
			boolean quite = MinecraftServer.kcauldronConfig.updatecheckerQuite
					.getValue();
			MinecraftServer server = MinecraftServer.getServer();
			final String filename = "KCauldron-" + mVersion + "-server.jar";
			File path = KCauldron.getServerLocation();
			File newPath = new File(path.getParentFile(),
					getInstallAs(filename));
			if (mSender != null) {
				if (!quite) {
					mSender.sendMessage(ChatColor.DARK_PURPLE
							+ "Current version is located in " + ChatColor.GOLD
							+ path.getAbsolutePath());
					mSender.sendMessage(ChatColor.DARK_PURPLE
							+ "Installing new version in " + ChatColor.GOLD
							+ newPath.getAbsolutePath());
				}
				if (newPath.exists()) {
					Bukkit.getConsoleSender().sendMessage(
							"ERROR: Install location already exists: "
									+ newPath.getAbsolutePath());
					mSender.sendMessage(ChatColor.RED
							+ "ERROR: Install location already exists");
					return;
				}
				if (!quite) {
					mSender.sendMessage(ChatColor.DARK_PURPLE
							+ "Downloading new version...");
				}
			}
			HttpUriRequest request = RequestBuilder
					.get()
					.setUri("https://prok.pw/repo/pw/prok/"
							+ KCauldron.getChannel() + "/" + mVersion + "/"
							+ filename)
					.addParameter("hostname", server.getHostname())
					.addParameter("port", "" + server.getPort()).build();
			HttpResponse response = HttpClientBuilder.create()
					.setUserAgent("KCauldron Updater").build().execute(request);
			InputStream is = response.getEntity().getContent();
			Files.copy(is, newPath.toPath());
			if (mSender != null && !quite) {
				mSender.sendMessage(ChatColor.DARK_PURPLE
						+ "Download completed");
			}
			makeSymlinks(newPath);
			if (MinecraftServer.kcauldronConfig.updatecheckerDeleteOld
					.getValue()) {
				if (mSender != null && !quite) {
					mSender.sendMessage(ChatColor.DARK_PURPLE
							+ "Mark old version for deletion");
				}
				path.deleteOnExit();
				if (KCauldron.sNewServerLocation != null) {
					KCauldron.sNewServerLocation.deleteOnExit();
				}
			}
			if (mSender != null) {
				mSender.sendMessage(ChatColor.DARK_PURPLE + "Update completed");
			}
			KCauldron.sNewServerLocation = newPath;
			KCauldron.sNewServerVersion = mVersion;
		} catch (Exception e) {
			e.printStackTrace();
			if (mSender != null) {
				mSender.sendMessage(ChatColor.RED + "Failed update to "
						+ mVersion);
			}
		} finally {
			KCauldron.sUpdateInProgress = false;
		}
	}

	private String getInstallAs(String filename) {
		String path = MinecraftServer.kcauldronConfig.updatecheckerInstallAs
				.getValue().trim();
		if (path.length() > 0) {
			return path.replace("%version%", mVersion);
		}
		return filename;
	}

	private void makeSymlinks(File newPath) {
		try {
			for (String symlink : Splitter.on(File.pathSeparatorChar).split(
					MinecraftServer.kcauldronConfig.updatecheckerSymlinks
							.getValue())) {
				symlink = symlink.trim();
				if (symlink.length() == 0)
					continue;
				File symlinkPath = new File(symlink);
				if (mSender != null
						&& !MinecraftServer.kcauldronConfig.updatecheckerQuite
								.getValue()) {
					mSender.sendMessage(ChatColor.RED + "Create symlink "
							+ ChatColor.GOLD + symlinkPath.getAbsolutePath());
				}
				Files.deleteIfExists(symlinkPath.toPath());
				Files.createSymbolicLink(symlinkPath.toPath(), newPath.toPath());
			}
		} catch (Exception e) {

		}
	}
}
