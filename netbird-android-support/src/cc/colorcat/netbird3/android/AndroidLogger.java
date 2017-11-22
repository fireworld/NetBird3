package cc.colorcat.netbird3.android;

import android.util.Log;
import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Logger;

/**
 * Created by cxx on 17-11-21.
 * xx.ch@outlook.com
 */
public class AndroidLogger implements Logger {

    @Override
    public void log(@Level int level, String tag, String msg) {
        switch (level) {
            case Level.VERBOSE:
                Log.v(tag, msg);
                break;
            case Level.DEBUG:
                Log.d(tag, msg);
                break;
            case Level.INFO:
                Log.i(tag, msg);
                break;
            case Level.WARN:
                Log.w(tag, msg);
                break;
            case Level.ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }
}
