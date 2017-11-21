package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.GenericPlatform;
import cc.colorcat.netbird3.platform.Logger;
import cc.colorcat.netbird3.platform.Scheduler;

/**
 * Created by cxx on 17-11-19.
 * xx.ch@outlook.com
 */
public abstract class Platform {
    static volatile Platform platform;

    static Platform get() {
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

    public abstract Connection connection();

    public abstract Scheduler scheduler();

    public abstract Logger logger();

//    public abstract void log(@Level int level, String tag, String msg);
}
