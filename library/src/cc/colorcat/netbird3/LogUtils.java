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
        realLog(VERBOSE, tag, msg);
    }

    static void d(String tag, String msg) {
        realLog(DEBUG, tag, msg);
    }

    static void i(String tag, String msg) {
        realLog(INFO, tag, msg);
    }

    static void w(String tag, String msg) {
        realLog(WARN, tag, msg);
    }

    static void e(String tag, String msg) {
        realLog(ERROR, tag, msg);
    }

    static void e(Throwable e) {
        if (ERROR >= level) {
            e.printStackTrace();
        }
    }

    static void dd(String tag, String msg) {
        Platform.get().log(DEBUG, tag, msg);
    }

    static void ii(String tag, String msg) {
        Platform.get().log(INFO, tag, msg);
    }

    static void setLevel(@Level int level) {
        LogUtils.level = level;
    }

    private static void realLog(@Level int level, String tag, String msg) {
        if (level >= LogUtils.level) {
            Platform.get().log(level, tag, msg);
        }
    }

    private LogUtils() {
        throw new AssertionError("no instance.");
    }
}
