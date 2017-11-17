package cc.colorcat.netbird3;

import cc.colorcat.netbird3.platform.Scheduler;

/**
 * Created by cxx on 17-3-5.
 * xx.ch@outlook.com
 */
final class ScheduleCenter {
    static Scheduler scheduler;

    static void callStartOnTargetThread(final MRequest.Listener<?> listener) {
        if (listener != null) {
            if (scheduler.isTargetThread()) {
                listener.onStart();
            } else {
                scheduler.onTargetThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart();
                    }
                });
            }
        }
    }

    static <T> void deliverDataOnTargetThread(final MRequest.Listener<? super T> listener, final NetworkData<? extends T> data) {
        if (listener != null) {
            if (scheduler.isTargetThread()) {
                deliverData(listener, data);
            } else {
                scheduler.onTargetThread(new Runnable() {
                    @Override
                    public void run() {
                        deliverData(listener, data);
                    }
                });
            }
        }
    }

    private static <T> void deliverData(MRequest.Listener<? super T> listener, NetworkData<? extends T> data) {
        if (data.isSuccess) {
            listener.onSuccess(data.data);
        } else {
            listener.onFailure(data.code, data.msg);
        }
        listener.onFinish();
    }

    static void postProgressOnTargetThread(final ProgressListener listener, final long finished, final long total, final int percent) {
        if (scheduler.isTargetThread()) {
            listener.onChanged(finished, total, percent);
        } else {
            scheduler.onTargetThread(new Runnable() {
                @Override
                public void run() {
                    listener.onChanged(finished, total, percent);
                }
            });
        }
    }

    private ScheduleCenter() {
        throw new AssertionError("no instance");
    }
}
