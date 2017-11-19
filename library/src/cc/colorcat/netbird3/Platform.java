package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Scheduler;

import java.util.logging.Logger;

/**
 * Created by cxx on ${DATA}.
 * xx.ch@outlook.com
 */
public abstract class Platform {
    static Platform platform;

    public static Platform findPlatForm() {
        if (platform == null) {
            synchronized (Platform.class) {
                if (platform == null) {
                    platform = new GenericPlatform();
                }
            }
        }
        return platform;
    }

    public abstract Scheduler scheduler();

    public abstract void log(@Level int level, String tag, String msg);


    private static class GenericPlatform extends Platform {
        private static final Logger LOGGER = Logger.getLogger(NetBird.class.getSimpleName());

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
        public void log(int level, String tag, String msg) {
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