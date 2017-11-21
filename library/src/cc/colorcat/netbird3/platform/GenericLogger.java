package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.NetBird;
import cc.colorcat.netbird3.internal.Level;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by cxx on 17-11-21.
 * xx.ch@outlook.com
 */
public class GenericLogger implements Logger {
    private final java.util.logging.Logger logger;

    {
        logger = java.util.logging.Logger.getLogger(NetBird.class.getSimpleName());
        java.util.logging.Level level = java.util.logging.Level.ALL;
        Formatter formatter = new Formatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
        for (Handler handler : logger.getParent().getHandlers()) {
            handler.setLevel(java.util.logging.Level.OFF);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        handler.setLevel(level);
        logger.addHandler(handler);
        logger.setLevel(level);
    }

    @Override
    public void log(@Level int level, String tag, String msg) {
        String log = tag + " --> " + msg;
        switch (level) {
            case Level.VERBOSE:
                logger.log(java.util.logging.Level.FINE, log);
                break;
            case Level.DEBUG:
                logger.log(java.util.logging.Level.CONFIG, log);
                break;
            case Level.INFO:
                logger.log(java.util.logging.Level.INFO, log);
                break;
            case Level.WARN:
                logger.log(java.util.logging.Level.WARNING, log);
                break;
            case Level.ERROR:
                logger.log(java.util.logging.Level.SEVERE, log);
                break;
            default:
                break;
        }
    }
}
