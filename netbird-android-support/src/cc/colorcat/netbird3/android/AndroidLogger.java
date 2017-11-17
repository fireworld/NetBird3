package cc.colorcat.netbird3.android;

import android.util.Log;
import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Logger;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public final class AndroidLogger implements Logger {
    @Override
    public void log(@Level int level, String tag, String msg) {
        Log.println(level, tag, msg);
    }
}
