package kcauldron;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;

public class KCauldron {
	private static String sCurrentVersion;

	public static String getCurrentVersion() {
		if (sCurrentVersion != null)
			return sCurrentVersion;
		try {
			Enumeration<URL> resources = KCauldron.class.getClassLoader()
					.getResources("META-INF/MANIFEST.MF");
			Properties manifest = new Properties();
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				manifest.load(url.openStream());
				String version = manifest.getProperty("KCauldron-Version");
				if (version != null) {
					String path = url.getPath();
				    String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
				    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
				    sServerLocation = new File(jarFilePath);
					return sCurrentVersion = version;
				}
				manifest.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sCurrentVersion = "UNKNOWN";
	}

	private static File sServerLocation;

	public static File getServerLocation() {
		getCurrentVersion();
		return sServerLocation;
	}
	
	public static File sNewServerLocation;
	public static String sNewServerVersion;
	public static boolean sUpdateInProgress;
}
