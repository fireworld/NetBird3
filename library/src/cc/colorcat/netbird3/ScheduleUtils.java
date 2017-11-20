package cc.colorcat.netbird3;

/**
 * Created by cxx on 17-3-5.
 * xx.ch@outlook.com
 */
final class ScheduleUtils {

    static void callStartOnTargetThread(final MRequest.Listener<?> listener) {
        if (listener != null) {
            if (isTargetThread()) {
                listener.onStart();
            } else {
                onTargetThread(new Runnable() {
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
            if (isTargetThread()) {
                deliverData(listener, data);
            } else {
                onTargetThread(new Runnable() {
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
        if (isTargetThread()) {
            listener.onChanged(finished, total, percent);
        } else {
            onTargetThread(new Runnable() {
                @Override
                public void run() {
                    listener.onChanged(finished, total, percent);
                }
            });
        }
    }

    private static boolean isTargetThread() {
        return Platform.get().scheduler().isTargetThread();
    }

    private static void onTargetThread(Runnable runnable) {
        Platform.get().scheduler().onTargetThread(runnable);
    }

    private ScheduleUtils() {
        throw new AssertionError("no instance");
    }
}
