package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;

import static cc.colorcat.netbird3.internal.Level.*;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
final class LogUtils {
    @Level
    private static int level = VERBOSE;

    static void v(String tag, String msg) {
        logWithLevel(VERBOSE, tag, msg);
    }

    static void d(String tag, String msg) {
        logWithLevel(DEBUG, tag, msg);
    }

    static void i(String tag, String msg) {
        logWithLevel(INFO, tag, msg);
    }

    static void w(String tag, String msg) {
        logWithLevel(WARN, tag, msg);
    }

    static void e(String tag, String msg) {
        logWithLevel(ERROR, tag, msg);
    }

    static void e(Throwable e) {
        if (ERROR >= level) {
            e.printStackTrace();
        }
    }

    static void dd(String tag, String msg) {
        realLog(DEBUG, tag, msg);
    }

    static void ii(String tag, String msg) {
        realLog(INFO, tag, msg);
    }

    static void setLevel(@Level int level) {
        LogUtils.level = level;
    }

    private static void logWithLevel(@Level int level, String tag, String msg) {
        if (level >= LogUtils.level) {
            realLog(level, tag, msg);
        }
    }

    private static void realLog(@Level int level, String tag, String msg) {
        Platform.get().logger().log(level, tag, msg);
    }

    private LogUtils() {
        throw new AssertionError("no instance.");
    }
}
