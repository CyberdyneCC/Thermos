package kcauldron.updater;

import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;

import kcauldron.KCauldron;
import kcauldron.KLog;
import net.minecraft.server.MinecraftServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class KVersionRetriever implements Runnable, UncaughtExceptionHandler {
	private static final boolean DEBUG;
	private static final KLog sLogger;
	private static final JSONParser sParser;
	private static MinecraftServer sServer;

	static {
		DEBUG = false;
		sLogger = KLog.get(KVersionRetriever.class.getSimpleName());

		sParser = new JSONParser();
	}

	public static void init(MinecraftServer server) {
		sServer = server;
		if (MinecraftServer.kcauldronConfig.updatecheckerEnable.getValue()) {
			startServer(DefaultUpdateCallback.INSTANCE, true);
		}
	}

	public static void startServer(IVersionCheckCallback callback, boolean loop) {
		new KVersionRetriever(callback, loop, true, KCauldron.getGroup(),
				KCauldron.getChannel());
	}

	private final IVersionCheckCallback mCallback;
	private final boolean mLoop;
	private final Thread mThread;
	private final String mGroup;
	private final String mName;
	private final boolean mUpToDateSupport;

	public KVersionRetriever(IVersionCheckCallback callback, boolean loop,
			boolean upToDateSupport, String group, String name) {
		if (DEBUG)
			sLogger.info("Created new version retrivier");
		mCallback = callback;
		mLoop = loop;
		mUpToDateSupport = upToDateSupport;
		mGroup = group;
		mName = name;
		mThread = new Thread(this);
		mThread.setName("KCauldron version retrievier");
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
					.setUri("https://api.prok.pw/repo/version/" + mGroup + "/"
							+ mName)
					.addParameter("hostname", sServer.getHostname())
					.addParameter("port", "" + sServer.getPort()).build();
			HttpResponse response = HttpClientBuilder.create()
					.setUserAgent("KCauldron Version Retriever")
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
			if (!mUpToDateSupport || KCauldron.getCurrentVersion() == null
					|| !version.equals(KCauldron.getCurrentVersion())) {
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
