package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.NetBird;
import cc.colorcat.netbird3.internal.Level;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public final class GenericLogger implements Logger {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(NetBird.class.getSimpleName());

    @Override
    public void log(@Level int level, String tag, String msg) {
        String log = tag + " --> " + msg;
        switch (level) {
            case Level.VERBOSE:
                LOGGER.log(java.util.logging.Level.FINE, log);
                break;
            case Level.DEBUG:
                LOGGER.log(java.util.logging.Level.CONFIG, log);
                break;
            case Level.INFO:
                LOGGER.log(java.util.logging.Level.INFO, log);
                break;
            case Level.WARN:
                LOGGER.log(java.util.logging.Level.WARNING, log);
                break;
            case Level.ERROR:
                LOGGER.log(java.util.logging.Level.SEVERE, log);
                break;
            default:
                break;
        }
    }
}
