package cc.colorcat.netbird3.android;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.Platform;
import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Scheduler;


/**
 * Created by cxx on 17-11-20.
 * xx.ch@outlook.com
 */
public class AndroidPlatform extends Platform {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override
    public Connection connection() {
        return new AndroidHttpConnection();
    }

    @Override
    public Scheduler scheduler() {
        return new Scheduler() {
            @Override
            public void onTargetThread(Runnable runnable) {
                HANDLER.post(runnable);
            }

            @Override
            public boolean isTargetThread() {
                return Looper.myLooper() == Looper.getMainLooper();
            }
        };
    }

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
