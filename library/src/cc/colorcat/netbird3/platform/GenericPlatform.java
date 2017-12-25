package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.HttpConnection;
import cc.colorcat.netbird3.Platform;

/**
 * Created by cxx on 17-11-20.
 * xx.ch@outlook.com
 */
public final class GenericPlatform extends Platform {
    private final Connection connection = new HttpConnection();
    private final Scheduler scheduler = new GenericScheduler();
    private final Logger logger = new GenericLogger();

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
