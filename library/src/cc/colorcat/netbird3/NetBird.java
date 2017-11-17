package cc.colorcat.netbird3;

import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.GenericLogger;
import cc.colorcat.netbird3.platform.GenericScheduler;
import cc.colorcat.netbird3.platform.Logger;
import cc.colorcat.netbird3.platform.Scheduler;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public final class NetBird implements Call.Factory {
    private final Scheduler scheduler;
    private final Logger logger;
    private final List<Interceptor> headInterceptors;
    private final List<Interceptor> tailInterceptors;
    private final ExecutorService executor;
    private final Dispatcher dispatcher;
    private final Connection connection;
    private final Proxy proxy;
    private final SSLSocketFactory sslSocketFactory;
    private final HostnameVerifier hostnameVerifier;
    private final String baseUrl;
    private final long cacheSize;
    private final File cachePath;
    private final int maxRunning;
    private final int readTimeOut;
    private final int connectTimeOut;
    private final boolean enabledExceptionLog;
    private final boolean enabledGzip;

    private NetBird(Builder builder) {
        this.scheduler = builder.scheduler;
        this.logger = builder.logger;
        this.headInterceptors = Utils.immutableList(builder.headInterceptors);
        this.tailInterceptors = Utils.immutableList(builder.tailInterceptors);
        this.executor = builder.executor;
        this.connection = builder.connection;
        this.proxy = builder.proxy;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.baseUrl = builder.baseUrl;
        this.cacheSize = builder.cacheSize;
        this.cachePath = builder.cachePath;
        this.maxRunning = builder.maxRunning;
        this.readTimeOut = builder.readTimeOut;
        this.connectTimeOut = builder.connectTimeOut;
        this.dispatcher = builder.dispatcher;
        this.dispatcher.setExecutor(executor);
        this.dispatcher.setMaxRunning(maxRunning);
        this.enabledExceptionLog = builder.enabledExceptionLog;
        this.enabledGzip = builder.enabledGzip;
        ScheduleCenter.scheduler = this.scheduler;
        LoggerUtils.logger = this.logger;
        LoggerUtils.setLevel(enabledExceptionLog ? Level.VERBOSE : Level.NOTHING);
    }

    /**
     * @return unmodifiable list
     * @see Builder#addHeadInterceptor(Interceptor)
     */
    public List<Interceptor> headInterceptors() {
        return headInterceptors;
    }

    /**
     * @return unmodifiable list
     * @see Builder#addTailInterceptor(Interceptor)
     */
    public List<Interceptor> tailInterceptors() {
        return tailInterceptors;
    }

    ExecutorService executor() {
        return executor;
    }

    Dispatcher dispatcher() {
        return dispatcher;
    }

    public Connection connection() {
        return connection;
    }

    public Proxy proxy() {
        return proxy;
    }

    public SSLSocketFactory sslSocketFactory() {
        return sslSocketFactory;
    }

    public HostnameVerifier hostnameVerifier() {
        return hostnameVerifier;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public long cacheSize() {
        return cacheSize;
    }

    public File cachePath() {
        return cachePath;
    }

    public int maxRunning() {
        return maxRunning;
    }

    public int readTimeOut() {
        return readTimeOut;
    }

    public int connectTimeOut() {
        return connectTimeOut;
    }

    public boolean enabledGzip() {
        return enabledGzip;
    }

    /**
     * @throws NullPointerException 如果 request 为空会抛出此异常
     */
    @Override
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    /**
     * @return 返回 request 的 tag, 可用此 tag 取消请求
     * @throws NullPointerException 如果 request 为空会抛出此异常
     * @see NetBird#cancelWaiting(Object)
     * @see NetBird#cancelAll(Object)
     */
    public <T> Object send(MRequest<T> request) {
        newCall(request).enqueue(new MCallback<>(request.parser(), request.listener()));
        return request.tag();
    }

    public <T> T execute(MRequest<T> request) throws IOException {
        Response response = newCall(request).execute();
        if (response.code() == 200 && response.body() != null) {
            return request.parser().parse(response).data;
        }
        return null;
    }

    public void cancelWaiting(Object tag) {
        if (tag != null) {
            dispatcher.cancelWaiting(tag);
        }
    }

    public void cancelAll(Object tag) {
        if (tag != null) {
            dispatcher.cancelAll(tag);
        }
    }

    public void cancelAll() {
        dispatcher.cancelAll();
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Scheduler scheduler;
        private Logger logger;
        private List<Interceptor> headInterceptors;
        private List<Interceptor> tailInterceptors;
        private ExecutorService executor;
        private Dispatcher dispatcher;
        private Connection connection;
        private Proxy proxy;
        private SSLSocketFactory sslSocketFactory;
        private HostnameVerifier hostnameVerifier;
        private String baseUrl;
        private long cacheSize;
        private File cachePath;
        private int maxRunning = 6;
        private int readTimeOut = 10000;
        private int connectTimeOut = 10000;
        private boolean enabledExceptionLog;
        private boolean enabledGzip;

        /**
         * @param baseUrl 默认的 url, 如果 {@link Request} 中的 url 为空将使用此代替之。
         * @throws NullPointerException     如果 baseUrl 为 {@code null} 将抛出此异常
         * @throws IllegalArgumentException 如果 baseUrl 不是以 "http" 开始将抛出此异常
         */
        public Builder(String baseUrl) {
            this.scheduler = new GenericScheduler();
            this.logger = new GenericLogger();
            this.baseUrl = Utils.checkedHttp(baseUrl);
            this.cacheSize = -1L;
            this.headInterceptors = new ArrayList<>(2);
            this.tailInterceptors = new ArrayList<>(2);
            this.dispatcher = new Dispatcher();
            this.enabledExceptionLog = true;
            this.enabledGzip = false;
        }

        private Builder(NetBird netBird) {
            this.scheduler = netBird.scheduler;
            this.logger = netBird.logger;
            this.baseUrl = netBird.baseUrl;
            this.headInterceptors = new ArrayList<>(netBird.headInterceptors);
            this.tailInterceptors = new ArrayList<>(netBird.tailInterceptors);
            this.executor = netBird.executor;
            this.dispatcher = netBird.dispatcher;
            this.connection = netBird.connection;
            this.proxy = netBird.proxy;
            this.sslSocketFactory = netBird.sslSocketFactory;
            this.hostnameVerifier = netBird.hostnameVerifier;
            this.cacheSize = netBird.cacheSize;
            this.cachePath = netBird.cachePath;
            this.maxRunning = netBird.maxRunning;
            this.readTimeOut = netBird.readTimeOut;
            this.connectTimeOut = netBird.connectTimeOut;
            this.enabledExceptionLog = netBird.enabledExceptionLog;
            this.enabledGzip = netBird.enabledGzip;
        }

        /**
         * @param scheduler 线程调度器，如 android 结果监听应在主线程，需手动配置。
         */
        public Builder scheduler(Scheduler scheduler) {
            this.scheduler = Utils.nonNull(scheduler, "scheduler == null");
            return this;
        }

        /**
         * @param logger 自定义日志打印
         */
        public Builder logger(Logger logger) {
            this.logger = Utils.nonNull(logger, "logger == null");
            return this;
        }

        /**
         * 配置 {@link ExecutorService}，一般可忽略，此时将自动构建。
         *
         * @param executor 异步请求均由 executor 发送
         * @throws NullPointerException 如果 executor 为 {@code null}，将抛出此异常
         */
        public Builder executor(ExecutorService executor) {
            this.executor = Utils.nonNull(executor, "executor == null");
            return this;
        }

        /**
         * 配置 {@link Connection}，一般可忽略，此时将使用 {@link java.net.HttpURLConnection} 实现的 {@link Connection}
         *
         * @throws NullPointerException 如果 connection 为 {@code null}，将抛出此异常
         */
        public Builder connection(Connection connection) {
            this.connection = Utils.nonNull(connection, "connection == null");
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * 在此添加的 {@link Interceptor} 可对 {@link Request} 重建修改
         * 此时 {@link Request} 未冻结
         *
         * @see Request#isFreeze()
         */
        public Builder addHeadInterceptor(Interceptor interceptor) {
            headInterceptors.add(Utils.nonNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 在此添加的 {@link Interceptor} 不可对 {@link Request} 重建修改
         * 此时 {@link Request} 已冻结，可在此添加日志打印模块
         *
         * @see Request#isFreeze()
         */
        public Builder addTailInterceptor(Interceptor interceptor) {
            tailInterceptors.add(Utils.nonNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 启用缓存
         *
         * @param cachePath 缓存路径
         * @param cacheSize 缓存大小，单位: bytes
         * @throws IllegalArgumentException 如果 cachePath 为空/不存在，或 cacheSize 小于/等于 0，均将抛出此异常
         */
        public Builder cache(File cachePath, long cacheSize) {
            if (cachePath == null || !cachePath.exists()) {
                throw new IllegalArgumentException("cachePath non existent");
            }
            if (cacheSize <= 0L) {
                throw new IllegalArgumentException("cacheSize <= 0");
            }
            this.cachePath = cachePath;
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder disableCache() {
            this.cachePath = null;
            this.cacheSize = -1L;
            return this;
        }

        /**
         * 配置最大请求数，即正在执行的 {@link Request} 数量的限制
         *
         * @param maxRunning 正在执行的 {@link Request} 的数量不会超过此限制
         */
        public Builder maxRunning(int maxRunning) {
            if (maxRunning < 1) {
                throw new IllegalArgumentException("maxRunning < 1");
            }
            this.maxRunning = maxRunning;
            return this;
        }

        /**
         * @param milliseconds 网络读取超时限制
         */
        public Builder readTimeOut(int milliseconds) {
            if (milliseconds <= 0) {
                throw new IllegalArgumentException("readTimeOut <= 0");
            }
            this.readTimeOut = milliseconds;
            return this;
        }

        /**
         * @param milliseconds 网络连接超时限制
         */
        public Builder connectTimeOut(int milliseconds) {
            if (milliseconds <= 0) {
                throw new IllegalArgumentException("connectTimeOut <= 0");
            }
            this.connectTimeOut = milliseconds;
            return this;
        }

        /**
         * @param enabled 是否启用异常日志
         */
        public Builder enableExceptionLog(boolean enabled) {
            this.enabledExceptionLog = enabled;
            return this;
        }

        /**
         * @param enabled 是否启用 gzip 压缩
         */
        public Builder enableGzip(boolean enabled) {
            this.enabledGzip = enabled;
            return this;
        }

        public NetBird build() {
            if (executor == null) executor = defaultService(maxRunning);
            if (connection == null) connection = new HttpConnection();
            return new NetBird(this);
        }

        private static ExecutorService defaultService(int corePoolSize) {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, 10, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
            executor.allowCoreThreadTimeOut(true);
            return executor;
        }
    }
}
