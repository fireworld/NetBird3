package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.HttpConnection;
import cc.colorcat.netbird3.NetBird;
import cc.colorcat.netbird3.Platform;
import cc.colorcat.netbird3.internal.Level;

import java.util.logging.*;

/**
 * Created by cxx on 17-11-20.
 * xx.ch@outlook.com
 */
public final class GenericPlatform extends Platform {
    private final Logger LOGGER;

    {
        LOGGER = Logger.getLogger(NetBird.class.getSimpleName());
        java.util.logging.Level level = java.util.logging.Level.ALL;
        Formatter formatter = new Formatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
        for (Handler handler : LOGGER.getParent().getHandlers()) {
            handler.setLevel(java.util.logging.Level.OFF);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        handler.setLevel(level);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(level);
    }

    @Override
    public Connection connection() {
        return new HttpConnection();
    }

    @Override
    public Scheduler scheduler() {
        return new Scheduler() {
            @Override
            public void onTargetThread(Runnable runnable) {
                runnable.run();
            }

            @Override
            public boolean isTargetThread() {
                return true;
            }
        };
    }

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
