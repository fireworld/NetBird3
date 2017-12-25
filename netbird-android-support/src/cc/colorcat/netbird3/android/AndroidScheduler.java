package cc.colorcat.netbird3.android;

import android.os.Handler;
import android.os.Looper;
import cc.colorcat.netbird3.platform.Scheduler;

/**
 * Created by cxx on 17-12-25.
 * xx.ch@outlook.com
 */
public final class AndroidScheduler implements Scheduler {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    AndroidScheduler() {
    }

    @Override
    public void onTargetThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    @Override
    public boolean isTargetThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
