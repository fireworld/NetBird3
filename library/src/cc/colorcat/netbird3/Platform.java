package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Scheduler;

import java.util.logging.Logger;

/**
 * Created by cxx on 17-11-19.
 * xx.ch@outlook.com
 */
public abstract class Platform {
    static volatile Platform platform;

    public static Platform get() {
        if (platform == null) {
            synchronized (Platform.class) {
                if (platform == null) {
                    platform = findPlatform();
                }
            }
        }
        return platform;
    }

    static Platform findPlatform() {
        try {
            Class<?> c = Class.forName("cc.colorcat.netbird3.android.AndroidPlatform");
            return (Platform) c.newInstance();
        } catch (Exception ignore) {
            return new GenericPlatform();
        }
    }

    public abstract Scheduler scheduler();

    public abstract void log(@Level int level, String tag, String msg);


    private static class GenericPlatform extends Platform {
        private final Logger LOGGER = Logger.getLogger(NetBird.class.getSimpleName());

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
}
