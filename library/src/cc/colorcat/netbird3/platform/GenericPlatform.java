package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.HttpConnection;
import cc.colorcat.netbird3.Platform;

/**
 * Created by cxx on 17-11-20.
 * xx.ch@outlook.com
 */
public final class GenericPlatform extends Platform {
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
    public Logger logger() {
        return new GenericLogger();
    }
}
