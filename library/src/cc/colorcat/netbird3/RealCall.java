package cc.colorcat.netbird3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
final class RealCall implements Call {
    private final NetBird netBird;
    private final Request request;
    private final Connection connection;
    private final AtomicBoolean executed;
    private AtomicBoolean canceled;

    RealCall(NetBird netBird, Request originalRequest) {
        this.netBird = netBird;
        this.request = originalRequest;
        this.connection = netBird.connection();
        this.executed = new AtomicBoolean(false);
        this.canceled = new AtomicBoolean(false);
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response execute() throws IOException {
        if (executed.getAndSet(true)) throw new IllegalStateException("Already Executed");
        if (!netBird.dispatcher().executed(this)) {
            throw new StateIOException(HttpStatus.MSG_DUPLICATE_REQUEST, HttpStatus.CODE_DUPLICATE_REQUEST);
        }
        try {
            return getResponseWithInterceptorChain();
        } finally {
            netBird.dispatcher().finished(this);
        }
    }

    @Override
    public void enqueue(Callback callback) {
        if (executed.getAndSet(true)) throw new IllegalStateException("Already Executed");
        callback.onStart();
        netBird.dispatcher().enqueue(new AsyncCall(callback));
    }

    private Response getResponseWithInterceptorChain() throws IOException {
        List<Interceptor> head = netBird.headInterceptors();
        List<Interceptor> tail = netBird.tailInterceptors();
        int size = head.size() + tail.size() + 3;
        List<Interceptor> interceptors = new ArrayList<>(size);
        interceptors.addAll(head);
        interceptors.add(new BridgeInterceptor(netBird.baseUrl()));
        interceptors.addAll(tail);
        interceptors.add(new GzipInterceptor(netBird.enabledGzip()));
        interceptors.add(new ConnectInterceptor(netBird));
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, 0, request, connection);
        return chain.proceed(request);
    }

    @Override
    public void cancel() {
        canceled.set(true);
        connection.cancel();
    }

    @Override
    public boolean isCanceled() {
        return canceled.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealCall realCall = (RealCall) o;

        return request.equals(realCall.request);
    }

    @Override
    public int hashCode() {
        return 17 * request.hashCode();
    }

    @Override
    public String toString() {
        return "RealCall{" +
                "request=" + request +
                ", executed=" + executed +
                '}';
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    protected RealCall clone() {
        return new RealCall(netBird, request);
    }

    final class AsyncCall implements Runnable {
        private final Callback callback;

        AsyncCall(Callback callback) {
            this.callback = callback;
        }

        Request request() {
            return RealCall.this.request;
        }

        RealCall get() {
            return RealCall.this;
        }

        Callback callback() {
            return callback;
        }

        @Override
        public void run() {
            int code = HttpStatus.CODE_CONNECT_ERROR;
            String msg = null;
            try {
                if (RealCall.this.canceled.get()) {
                    callback.onFailure(RealCall.this, new StateIOException(HttpStatus.MSG_CANCELED, HttpStatus.CODE_CANCELED));
                } else {
                    Response response = getResponseWithInterceptorChain();
                    code = response.code();
                    msg = response.msg();
                    callback.onResponse(RealCall.this, response);
                }
            } catch (IOException e) {
                LogUtils.e(e);
                if (msg == null) {
                    msg = Utils.nullElse(e.getMessage(), HttpStatus.MSG_CONNECT_ERROR);
                } else {
                    msg = "Response msg = " + msg + ", Exception detail = " + e.toString();
                }
                callback.onFailure(RealCall.this, new StateIOException(msg, e, code));
            } finally {
                callback.onFinish();
                netBird.dispatcher().finished(this);
                Utils.close(RealCall.this.connection);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AsyncCall asyncCall = (AsyncCall) o;

            return RealCall.this.request.equals(asyncCall.request());
        }

        @Override
        public int hashCode() {
            return 31 * RealCall.this.request.hashCode();
        }

        @Override
        public String toString() {
            return "AsyncCall{" +
                    "request=" + RealCall.this.request +
                    ", executed=" + RealCall.this.executed +
                    '}';
        }
    }
}
