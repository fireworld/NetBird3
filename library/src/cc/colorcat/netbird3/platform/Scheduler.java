package cc.colorcat.netbird3.platform;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public interface Scheduler {

    void onTargetThread(Runnable runnable);

    boolean isTargetThread();
}
