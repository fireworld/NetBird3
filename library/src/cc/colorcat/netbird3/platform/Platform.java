package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.internal.Level;

/**
 * Created by cxx on ${DATA}.
 * xx.ch@outlook.com
 */
public abstract class Platform {

    public static Platform findPlatForm() {
        return new Platform() {
            @Override
            public Scheduler scheduler() {
                return null;
            }

            @Override
            public void log(int level, String tag, String msg) {

            }
        };
    }

    public abstract Scheduler scheduler();

    public abstract void log(@Level int level, String tag, String msg);
}
