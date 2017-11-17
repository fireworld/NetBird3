package cc.colorcat.netbird3.platform;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public class AnyScheduler implements Scheduler {
    @Override
    public void onTargetThread(Runnable runnable) {
        runnable.run();
    }

    @Override
    public boolean isTargetThread() {
        return true;
    }
}
