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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class KVersionRetriever implements Runnable, UncaughtExceptionHandler {
	private static final boolean DEBUG;
	private static final KLog sLogger;
	private static final JSONParser sParser;
	private static final String sCurrentVersion;
	private static MinecraftServer sServer;

	static {
		DEBUG = false;
		sLogger = KLog.get(KVersionRetriever.class.getSimpleName());

		sCurrentVersion = KCauldron.getCurrentVersion();
		sParser = new JSONParser();
	}

	public static void init(MinecraftServer server) {
		sServer = server;
		if (MinecraftServer.kcauldronConfig.updatecheckerEnable.getValue()) {
			new KVersionRetriever(DefaultUpdateCallback.INSTANCE, true);
		}
	}

	private final IVersionCheckCallback mCallback;
	private final boolean mLoop;
	private final Thread mThread;

	public KVersionRetriever(IVersionCheckCallback callback, boolean loop) {
		if (DEBUG)
			sLogger.info("Created new version retrivier");
		mCallback = callback;
		mLoop = loop;
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
		if (DEBUG)
			sLogger.info("Requesting for new version...");
		try {
			HttpUriRequest request = RequestBuilder
					.get()
					.setUri("https://prok.pw/version/pw.prok/"
							+ KCauldron.getChannel())
					.addParameter("hostname", sServer.getHostname())
					.addParameter("port", "" + sServer.getPort()).build();
			HttpResponse response = HttpClientBuilder.create()
					.setUserAgent("KCauldron Version Retriever").build()
					.execute(request);
			JSONObject json = (JSONObject) sParser.parse(new InputStreamReader(
					response.getEntity().getContent()));
			String version = (String) json.get("version");
			if (DEBUG) {
				sLogger.info("Got the latest version: %s", version);
				sLogger.info("Current version is %s", sCurrentVersion);
			}
			if (!sCurrentVersion.equals(version)) {
				mCallback.newVersion(sCurrentVersion, version);
			} else {
				mCallback.upToDate(version);
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
		void upToDate(String version);

		void newVersion(String currentVersion, String newVersion);

		void error(Throwable t);
	}
}
