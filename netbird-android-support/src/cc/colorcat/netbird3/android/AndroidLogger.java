package cc.colorcat.netbird3.android;

import android.util.Log;
import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Logger;

/**
 * Created by cxx on 17-11-21.
 * xx.ch@outlook.com
 */
public class AndroidLogger implements Logger {
    private static final int MAX_LENGTH = 1024 * 2;

    @Override
    public void log(@Level int level, String tag, String msg) {
        switch (level) {
            case Level.VERBOSE:
                println(Log.VERBOSE, tag, msg);
                break;
            case Level.DEBUG:
                println(Log.DEBUG, tag, msg);
                break;
            case Level.INFO:
                println(Log.INFO, tag, msg);
                break;
            case Level.WARN:
                println(Log.WARN, tag, msg);
                break;
            case Level.ERROR:
                println(Log.ERROR, tag, msg);
                break;
            default:
                break;
        }
    }

    private static void println(int priority, String tag, String msg) {
        for (int start = 0, end = start + MAX_LENGTH, size = msg.length(); start < size; start = end, end = start + MAX_LENGTH) {
            if (end > size) {
                Log.println(priority, tag, msg.substring(start));
            } else {
                Log.println(priority, tag, msg.substring(start, end));
            }
        }
    }
}
