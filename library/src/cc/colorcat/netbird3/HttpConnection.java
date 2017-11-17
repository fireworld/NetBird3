package cc.colorcat.netbird3;

//import android.net.http.HttpResponseCache;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public final class HttpConnection implements Connection {
    private boolean cacheEnabled = false;
    private HttpURLConnection conn;
    private InputStream is;
    private LoadListener listener;

    public HttpConnection() {

    }

    private HttpConnection(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    @Override
    public void connect(NetBird netBird, Request request) throws IOException {
        listener = request.loadListener();
        enableCache(netBird.cachePath(), netBird.cacheSize());
        URL url = new URL(request.url());
        Proxy proxy = netBird.proxy();
        conn = (HttpURLConnection) (proxy == null ? url.openConnection() : url.openConnection(proxy));
        conn.setConnectTimeout(netBird.connectTimeOut());
        conn.setReadTimeout(netBird.readTimeOut());
        conn.setDoInput(true);
        Method method = request.method();
        conn.setRequestMethod(method.name());
        conn.setDoOutput(method.needBody());
        conn.setUseCaches(cacheEnabled);
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection connection = (HttpsURLConnection) conn;
            SSLSocketFactory factory = netBird.sslSocketFactory();
            if (factory != null) {
                connection.setSSLSocketFactory(factory);
            }
            HostnameVerifier verifier = netBird.hostnameVerifier();
            if (verifier != null) {
                connection.setHostnameVerifier(verifier);
            }
        }
    }

    @Override
    public void writeHeaders(Headers headers) throws IOException {
        for (int i = 0, size = headers.size(); i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            conn.addRequestProperty(name, value);
        }
    }

    @Override
    public void writeBody(RequestBody body) throws IOException {
        long contentLength = body.contentLength();
        if (contentLength > 0) {
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
                body.writeTo(os);
                os.flush();
            } finally {
                Utils.close(os);
            }
        }
    }

    @Override
    public int responseCode() throws IOException {
        return conn.getResponseCode();
    }

    @Override
    public String responseMsg() throws IOException {
        return conn.getResponseMessage();
    }

    @Override
    public Headers responseHeaders() throws IOException {
        Map<String, List<String>> map = conn.getHeaderFields();
        return map != null ? Headers.create(map) : Headers.emptyHeaders();
    }

    @Override
    public ResponseBody responseBody(Headers headers) throws IOException {
        if (is == null) {
            is = conn.getInputStream();
        }
        return RealResponseBody.create(is, headers, listener);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Connection clone() {
        return new HttpConnection(cacheEnabled);
    }

    @Override
    public void close() throws IOException {
        Utils.close(is);
        if (conn != null) {
            conn.disconnect();
        }
    }

    @Override
    public void cancel() {
        if (conn != null) {
            conn.disconnect();
        }
    }

    private void enableCache(File path, long cacheSize) {
//        if (cacheSize > 0 && path != null) {
//            if (!cacheEnabled) {
//                try {
//                    HttpResponseCache cache = HttpResponseCache.getInstalled();
//                    if (cache == null) {
//                        File cachePath = new File(path, "NetBird");
//                        if (cachePath.exists() || cachePath.mkdirs()) {
//                            cache = HttpResponseCache.install(cachePath, cacheSize);
//                        }
//                    }
//                    cacheEnabled = (cache != null);
//                } catch (Exception e) {
//                    cacheEnabled = false;
//                    LoggerUtils.e(e);
//                }
//            }
//        } else if (cacheEnabled) {
//            cacheEnabled = false;
//            try {
//                HttpResponseCache cache = HttpResponseCache.getInstalled();
//                if (cache != null) {
//                    cache.close();
//                }
//            } catch (Exception e) {
//                LoggerUtils.e(e);
//            }
//        }
    }
}
