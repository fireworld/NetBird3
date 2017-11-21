package cc.colorcat.netbird3.logging;

import cc.colorcat.netbird3.*;
import cc.colorcat.netbird3.internal.Level;
import cc.colorcat.netbird3.platform.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by cxx on 2017/11/21.
 * xx.ch@outlook.com
 */
public class LoggingTailInterceptor implements Interceptor {
    private static final String TAG = NetBird.class.getSimpleName();
    private static final String LINE = buildLine(80, '-');
    private static final String HALF_LINE = buildLine(38, '-');

    private final Charset defaultCharset;
    private final Logger logger;
    private final Filter filter;

    public LoggingTailInterceptor(Logger logger, Filter filter) {
        this(logger, filter, Charset.forName("UTF-8"));
    }

    public LoggingTailInterceptor(Logger logger, Filter filter, Charset defaultCharset) {
        this.logger = logger;
        this.filter = filter;
        this.defaultCharset = defaultCharset;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        synchronized (TAG) {
            Headers headers;
            // log request
            Method m = request.method();
            log(Level.DEBUG, HALF_LINE + m.name() + HALF_LINE);
            log(Level.DEBUG, "request url --> " + request.url());
            headers = request.headers();
            logPairs(Level.DEBUG, "request header", headers.names(), headers.values());
            if (m.needBody()) {
                Parameters parameters = request.parameters();
                logPairs(Level.DEBUG, "request parameter", parameters.names(), parameters.values());
                logFiles(Level.DEBUG, request.files());
            }

            // log response
            log(Level.INFO, "response --> " + response.code() + "--" + response.msg());
            headers = response.headers();
            logPairs(Level.INFO, "response header", headers.names(), headers.values());
            ResponseBody oldBody = response.body();
            if (oldBody != null) {
                String contentType = oldBody.contentType();
                if (contentType != null && filter.filter(contentType)) {
                    byte[] bytes = oldBody.bytes();
                    Charset charset = oldBody.charset();
                    if (charset == null) {
                        charset = defaultCharset;
                    }
                    String content = new String(bytes, charset);
                    log(Level.INFO, "response content --> " + content);
                    ResponseBody newBody = ResponseBody.create(bytes, contentType);
                    response = response.newBuilder().body(newBody).build();

//                    String content = oldBody.string(defaultCharset);
//                    log(Level.INFO, "response content --> " + content);
//                    ResponseBody newBody = ResponseBody.create(content, contentType, oldBody.charset());
//                    response = response.newBuilder().body(newBody).build();
                }
            }
            log(Level.INFO, LINE);
        }

        return response;
    }

    private void logPairs(@Level int level, String type, List<String> names, List<String> values) {
        for (int i = 0, size = names.size(); i < size; i++) {
            String msg = type + " --> " + names.get(i) + " = " + values.get(i);
            log(level, msg);
        }
    }

    private void logFiles(@Level int level, List<FileBody> bodies) {
        for (int i = 0, size = bodies.size(); i < size; i++) {
            log(level, "request file --> " + bodies.get(i).toString());
        }
    }

    private void log(@Level int level, String msg) {
        logger.log(level, TAG, msg);
    }

    private static String buildLine(int n, char c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(c);
        }
        return sb.toString();
    }
}
