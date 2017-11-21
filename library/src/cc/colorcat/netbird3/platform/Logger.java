package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.internal.Level;

/**
 * Created by cxx on 17-11-21.
 * xx.ch@outlook.com
 */
public interface Logger {

    void log(@Level int level, String tag, String msg);
}
