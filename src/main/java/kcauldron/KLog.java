package kcauldron;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class KLog {
    private static final KLog DEFAULT_LOGGER = new KLog("KCauldron");

    public static KLog get() {
        return DEFAULT_LOGGER;
    }

    public static KLog get(String tag) {
        return new KLog("KCauldron: " + tag);
    }
    
    private final String mTag;

    public KLog(String tag) {
        mTag = tag;
    }

    public void log(Level level, Throwable throwable, String message,
            Object... args) {
        Throwable t = null;
        if (throwable != null) {
            t = new Throwable();
            t.initCause(throwable);
            t.fillInStackTrace();
        }
        FMLLog.log(mTag, level, t, String.format(message, args));
    }

    public void warning(String message, Object... args) {
        log(Level.WARN, null, message, args);
    }

    public void warning(Throwable throwable, String message,
            Object... args) {
        log(Level.WARN, throwable, message, args);
    }

    public void info(String message, Object... args) {
        log(Level.INFO, null, message, args);
    }

    public void info(Throwable throwable, String message,
            Object... args) {
        log(Level.INFO, throwable, message, args);
    }
}
