package cc.colorcat.netbird3.android;

import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.Platform;
import cc.colorcat.netbird3.platform.Logger;
import cc.colorcat.netbird3.platform.Scheduler;


/**
 * Created by cxx on 17-11-20.
 * xx.ch@outlook.com
 */
public class AndroidPlatform extends Platform {
    private final Connection connection = new AndroidHttpConnection();
    private final Scheduler scheduler = new AndroidScheduler();
    private final Logger logger = new AndroidLogger();

    @Override
    public Connection connection() {
        return connection;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
