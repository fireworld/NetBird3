package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.internal.Level;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public final class AnyLogger implements Logger {
    @Override
    public void log(@Level int level, String tag, String msg) {
        String log = tag + " --> " + msg;
        if (level < Level.WARN) {
            System.out.println(log);
        } else {
            System.err.println(log);
        }
    }
}
