package thermos.updater;

import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;

import thermos.Thermos;
import thermos.TLog;
import net.minecraft.server.MinecraftServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TVersionRetriever implements Runnable, UncaughtExceptionHandler {
    private static final boolean DEBUG;
    private static final TLog sLogger;
    private static final JSONParser sParser;
    private static MinecraftServer sServer;

    static {
        DEBUG = false;
        sLogger = TLog.get(TVersionRetriever.class.getSimpleName());

        sParser = new JSONParser();
    }

    public static void init(MinecraftServer server) {
        sServer = server;
        if (MinecraftServer.thermosConfig.updatecheckerEnable.getValue()) {
            startServer(DefaultUpdateCallback.INSTANCE, true);
        }
    }

    public static void startServer(IVersionCheckCallback callback, boolean loop) {
        new TVersionRetriever(callback, loop, true, Thermos.getGroup(),
                Thermos.getChannel());
    }

    private final IVersionCheckCallback mCallback;
    private final boolean mLoop;
    private final Thread mThread;
    private final String mGroup;
    private final String mName;
    private final boolean mUpToDateSupport;

    public TVersionRetriever(IVersionCheckCallback callback, boolean loop,
            boolean upToDateSupport, String group, String name) {
        if (DEBUG)
            sLogger.info("Created new version retriever");
        mCallback = callback;
        mLoop = loop;
        mUpToDateSupport = upToDateSupport;
        mGroup = group;
        mName = name;
        mThread = new Thread(Thermos.sThermosThreadGroup, this, "Thermos version retriever");
        mThread.setPriority(Thread.MIN_PRIORITY);
        mThread.setDaemon(true);
        mThread.setUncaughtExceptionHandler(this);
        mThread.start();
    }

    @Override
    public void run() {
        while (!mThread.isInterrupted()) {
            check();
            if (!mLoop)
                break;
            try {
                Thread.sleep(1000 * 60 * 10);// Sleep ten minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void check() {
        try {
            HttpUriRequest request = RequestBuilder
                    .get()
                    .setUri("http://i.yive.me/thermos/version")
                    .addParameter("version", Thermos.getCurrentVersion())
                    .addParameter("hostname", sServer.getHostname())
                    .addParameter("port", "" + sServer.getPort()).build();
            HttpResponse response = HttpClientBuilder.create()
                    .setUserAgent("Thermos Version Retriever")
                    .setRedirectStrategy(new LaxRedirectStrategy()).build()
                    .execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                uncaughtException(mThread, new IllegalStateException(
                        "Status code isn't OK"));
                return;
            }
            JSONObject json = (JSONObject) sParser.parse(new InputStreamReader(
                    response.getEntity().getContent()));
            String version = (String) json.get("version");
            if (!mUpToDateSupport || Thermos.getCurrentVersion() == null
                    || !version.equals(Thermos.getCurrentVersion())) {
                mCallback.newVersion(version);
            } else {
                mCallback.upToDate();
            }
        } catch (Exception e) {
            uncaughtException(null, e);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        sLogger.warning(e, "Error occured during retriving version");
        if (mCallback != null) {
            mCallback.error(e);
        }
    }

    public interface IVersionCheckCallback {
        void upToDate();

        void newVersion(String newVersion);

        void error(Throwable t);
    }
}
