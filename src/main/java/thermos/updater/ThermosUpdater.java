package thermos.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import thermos.Thermos;
import thermos.updater.TVersionRetriever.IVersionCheckCallback;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.google.common.base.Joiner;

public class ThermosUpdater implements Runnable, IVersionCheckCallback {
    private static final class LatestVersionCallback extends
            CommandSenderUpdateCallback {
        public LatestVersionCallback(CommandSender sender) {
            super(sender);
        }

        @Override
        public void newVersion(String newVersion) {
            startUpdate(getSender(), newVersion);
        }

        @Override
        public void upToDate() {
            Thermos.sUpdateInProgress = false;
            CommandSender sender = getSender();
            if (sender != null) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "Current version ("
                        + Thermos.getCurrentVersion() + ") is up to date");
            }
        }

        @Override
        public void error(Throwable t) {
            super.error(t);
            Thermos.sUpdateInProgress = false;
        }
    }

    public static void initUpdate(CommandSender sender, String version) {
        if (Thermos.sUpdateInProgress) {
            sender.sendMessage(ChatColor.RED
                    + "Update stopped: another update in progress");
            return;
        }
        Thermos.sUpdateInProgress = true;
        if (version == null) {
            sender.sendMessage(ChatColor.DARK_PURPLE
                    + "Fetching latest version...");
            TVersionRetriever.startServer(new LatestVersionCallback(sender),
                    false);
        } else {
            startUpdate(sender, version);
        }
    }

    private static void startUpdate(CommandSender sender, String version) {
        if (sender != null) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "Starting update to "
                    + version + "...");
        }
        new ThermosUpdater(sender, version);
    }

    private final CommandSender mSender;
    private final String mVersion;
    private final Thread mThread;

    public ThermosUpdater(CommandSender sender, String version) {
        mSender = sender;
        mVersion = version;
        mThread = new Thread(Thermos.sThermosThreadGroup, this, "Thermos updated");
        mThread.setPriority(Thread.MIN_PRIORITY);
        mThread.start();
    }

    @Override
    public void run() {
        if (!MinecraftServer.thermosConfig.updatecheckerQuiet.getValue()) {
            mSender.sendMessage(ChatColor.DARK_PURPLE
                    + "Retrieving latest KBootstrap version...");
        }
        new TVersionRetriever(this, false, false, "pw.prok", "KBootstrap");
    }

    @Override
    public void upToDate() {

    }

    @Override
    public void newVersion(String kbootstrapVersion) {
        boolean quiet = MinecraftServer.thermosConfig.updatecheckerQuiet
                .getValue();
        try {
            if (!quiet) {
                mSender.sendMessage(ChatColor.DARK_PURPLE
                        + "Downloading KBootstrap " + kbootstrapVersion + "...");
            }
            File kbootstrap = File.createTempFile("kbootstrap",
                    String.valueOf(System.currentTimeMillis()));
            download(
                    "https://api.prok.pw/repo/blob/pw.prok/KBootstrap/latest/app",
                    kbootstrap);
            if (!quiet) {
                mSender.sendMessage(ChatColor.DARK_PURPLE
                        + "Installing Thermos " + mVersion
                        + " via KBootstrap " + kbootstrapVersion + "...");
            }

            String javahome = System.getProperty("java.home");
            String javapath = javahome + "/bin/java";

            List<String> command = new ArrayList<String>();
            command.add(javapath);
            command.add("-jar");
            command.add(kbootstrap.getCanonicalPath());
            command.add("--serverDir");
            command.add(Thermos.getServerHome().getCanonicalPath());
            command.add("--installThermos");
            command.add(String.format("%s:%s:%s", Thermos.getGroup(), Thermos.getChannel(), mVersion));
            final String[] symlinks = MinecraftServer.thermosConfig.updatecheckerSymlinks
                    .getValue().trim().split(File.pathSeparator);
            for (String symlink : symlinks) {
                command.add("--serverSymlinks");
                command.add(symlink);
            }

            Bukkit.getConsoleSender().sendMessage(
                    "Starting command: " + Joiner.on(' ').join(command));

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.environment().put("JAVA_HOME", javahome);
            switch (builder.start().waitFor()) {
            case 0:
                mSender.sendMessage(ChatColor.GREEN + "Thermos " + mVersion
                        + " installed");
                break;
            default:
                mSender.sendMessage(ChatColor.RED
                        + "Failed to install Thermos " + mVersion);
            }
        } catch (Exception e) {
            if (!quiet) {
                e.printStackTrace();
            }
            if (mSender != null) {
                mSender.sendMessage(ChatColor.RED + "Failed update to "
                        + mVersion);
            }
        } finally {
            Thermos.sUpdateInProgress = false;
        }
    }

    @Override
    public void error(Throwable t) {
        Thermos.sUpdateInProgress = false;
        t.printStackTrace();
    }

    private static void download(String url, File destination)
            throws IOException {
        HttpUriRequest request = RequestBuilder
                .get()
                .setUri(url)
                .addParameter("hostname",
                        MinecraftServer.getServer().getHostname())
                .addParameter("port",
                        String.valueOf(MinecraftServer.getServer().getPort()))
                .build();
        CloseableHttpClient client = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setUserAgent("Thermos Updater").build();

        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            client.close();
            throw new IllegalStateException("Could not download " + url);
        }
        InputStream is = response.getEntity().getContent();
        OutputStream os = new FileOutputStream(destination);
        IOUtils.copy(is, os);
        is.close();
        os.close();
        client.close();
    }
}
