package kcauldron;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;

import org.spigotmc.RestartCommand;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class KCauldron {
    private static boolean sManifestParsed = false;

    private static void parseManifest() {
        if (sManifestParsed)
            return;
        sManifestParsed = true;

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
                    String jarFilePath = path.substring(path.indexOf(":") + 1,
                            path.indexOf("!"));
                    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
                    sServerLocation = new File(jarFilePath);

                    sCurrentVersion = version;
                    sGroup = manifest.getProperty("KCauldron-Group");
                    sBranch = manifest.getProperty("KCauldron-Branch");
                    sChannel = manifest.getProperty("KCauldron-Channel");
                    break;
                }
                manifest.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sCurrentVersion;

    public static String getCurrentVersion() {
        parseManifest();
        return sCurrentVersion;
    }

    private static File sServerLocation;

    public static File getServerLocation() {
        parseManifest();
        return sServerLocation;
    }

    private static File sServerHome;

    public static File getServerHome() {
        if (sServerHome == null) {
            String home = System.getenv("KCAULDRON_HOME");
            if (home != null) {
                sServerHome = new File(home);
            } else {
                parseManifest();
                sServerHome = sServerLocation.getParentFile();
            }
        }
        return sServerHome;
    }

    private static String sGroup;

    public static String getGroup() {
        parseManifest();
        return sGroup;
    }

    private static String sBranch;

    public static String getBranch() {
        parseManifest();
        return sBranch;
    }

    private static String sChannel;

    public static String getChannel() {
        parseManifest();
        return sChannel;
    }

    public static File sNewServerLocation;
    public static String sNewServerVersion;
    public static boolean sUpdateInProgress;

    public static void restart() {
        RestartCommand.restart(true);
    }
    
    private static int sForgeRevision = 0;

    public static int lookupForgeRevision() {
        if (sForgeRevision != 0) return sForgeRevision;
        int revision = Integer.parseInt(System.getProperty("kcauldron.forgeRevision", "0"));
        try {
            Properties p = new Properties();
            p.load(KCauldron.class
                    .getResourceAsStream("/fmlversion.properties"));
            revision = Integer.parseInt(String.valueOf(p.getProperty(
                    "fmlbuild.build.number", "0")));
        } catch (Exception e) {
        }
        if (revision == 0) {
            KLog.get().warning("KCauldron: could not parse forge revision, critical error");
            FMLCommonHandler.instance().exitJava(1, false);
        }
        return sForgeRevision = revision;
    }
}
