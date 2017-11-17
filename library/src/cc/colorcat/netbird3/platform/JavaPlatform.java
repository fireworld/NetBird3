package cc.colorcat.netbird3.platform;

import cc.colorcat.netbird3.internal.Level;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
public class JavaPlatform implements Platform {

    @Override
    public Scheduler scheduler() {
        return new Scheduler() {
            @Override
            public void onTargetThread(Runnable runnable) {
                runnable.run();
            }

            @Override
            public boolean isTargetThread() {
                return true;
            }
        };
    }

    @Override
    public Logger logger() {
        return new Logger() {
            @Override
            public void log(@Level int level, String tag, String msg) {
                String log = tag + " --> " + msg;
                if (level < Level.WARN) {
                    System.out.println(log);
                } else {
                    System.out.println(log);
                }
            }
        };
    }
}
