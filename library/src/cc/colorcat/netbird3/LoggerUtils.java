package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Logger;

import static cc.colorcat.netbird3.internal.Level.*;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
final class LoggerUtils {
    static Logger logger;

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

    static void setLevel(@Level int level) {
        LoggerUtils.level = level;
    }

    @Level
    static int getLevel() {
        return LoggerUtils.level;
    }

    private static void realLog(@Level int level, String tag, String msg) {
        if (level >= LoggerUtils.level && logger != null) {
            logger.log(level, tag, msg);
        }
    }

    private LoggerUtils() {
        throw new AssertionError("no instance.");
    }
}
