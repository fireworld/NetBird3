package cc.colorcat.netbird3;

import cc.colorcat.netbird3.RealCall.AsyncCall;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public final class Dispatcher {
    //    private static final String TAG = "DispatcherTAG";
    private ExecutorService executor;
    private int maxRunning;
    private final Queue<AsyncCall> waitingAsyncCalls = new ConcurrentLinkedQueue<>();
    private final Set<AsyncCall> runningAsyncCalls = new CopyOnWriteArraySet<>();
    private final Set<RealCall> runningSyncCalls = new CopyOnWriteArraySet<>();

    Dispatcher() {
    }

    synchronized void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    synchronized void setMaxRunning(int maxRunning) {
        this.maxRunning = maxRunning;
    }

    boolean executed(RealCall call) {
        return runningSyncCalls.add(call);
    }

    void enqueue(AsyncCall call) {
        if (!waitingAsyncCalls.contains(call) && waitingAsyncCalls.offer(call)) {
            promoteCalls();
        } else {
            onDuplicateRequest(call);
        }
//        logSize(2, "enqueue");
    }

    private synchronized void promoteCalls() {
        if (runningAsyncCalls.size() >= maxRunning) return;
        for (AsyncCall call = waitingAsyncCalls.poll(); call != null; call = waitingAsyncCalls.poll()) {
            if (runningAsyncCalls.add(call)) {
                executor.execute(call);
                if (runningAsyncCalls.size() >= maxRunning) return;
            } else {
                onDuplicateRequest(call);
            }
        }
    }

    private static void onDuplicateRequest(AsyncCall call) {
        Callback callback = call.callback();
        callback.onFailure(call.get(),
                new StateIOException(HttpStatus.MSG_DUPLICATE_REQUEST, HttpStatus.CODE_DUPLICATE_REQUEST));
        callback.onFinish();
    }

    void cancelWaiting(Object tag) {
        Iterator<AsyncCall> iterator = waitingAsyncCalls.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().request().tag().equals(tag)) {
                iterator.remove();
            }
        }
    }

    void cancelAll(Object tag) {
        cancelWaiting(tag);
        for (AsyncCall call : runningAsyncCalls) {
            if (call.request().tag().equals(tag)) {
                call.get().cancel();
            }
        }
        for (RealCall call : runningSyncCalls) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
    }


    void cancelAll() {
        waitingAsyncCalls.clear();
        for (AsyncCall call : runningAsyncCalls) {
            call.get().cancel();
        }
        for (RealCall call : runningSyncCalls) {
            call.cancel();
        }
    }

    void finished(RealCall call) {
        runningSyncCalls.remove(call);
//        logSize(3, "finished RealCall");
    }

    void finished(AsyncCall call) {
        runningAsyncCalls.remove(call);
        promoteCalls();
//        logSize(4, "finished AsyncCall");
    }

//    private void logSize(int level, String mark) {
//        String msg = mark + ": " + "waiting = " + waitingAsyncCalls.size() + ", running = " + runningAsyncCalls.size();
//        switch (level) {
//            case 1:
//                LogUtils.v(TAG, msg);
//                break;
//            case 2:
//                LogUtils.d(TAG, msg);
//                break;
//            case 3:
//                LogUtils.i(TAG, msg);
//                break;
//            case 4:
//                LogUtils.w(TAG, msg);
//                break;
//            case 5:
//                LogUtils.e(TAG, msg);
//                break;
//            default:
//                LogUtils.ii(TAG, msg);
//
//        }
//    }
}
