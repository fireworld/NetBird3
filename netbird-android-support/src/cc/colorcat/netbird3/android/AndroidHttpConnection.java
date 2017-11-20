package cc.colorcat.netbird3.android;

import android.net.http.HttpResponseCache;
import cc.colorcat.netbird3.Connection;
import cc.colorcat.netbird3.HttpConnection;

import java.io.File;

/**
 * Created by cxx on 2017/11/20.
 * xx.ch@outlook.com
 */
public final class AndroidHttpConnection extends HttpConnection {

    AndroidHttpConnection() {

    }

    private AndroidHttpConnection(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Connection clone() {
        return new AndroidHttpConnection(cacheEnabled);
    }

    @Override
    protected void enableCache(File path, long cacheSize) {
        if (cacheSize > 0 && path != null) {
            if (!cacheEnabled) {
                try {
                    HttpResponseCache cache = HttpResponseCache.getInstalled();
                    if (cache == null) {
                        File cachePath = new File(path, "NetBird");
                        if (cachePath.exists() || cachePath.mkdirs()) {
                            cache = HttpResponseCache.install(cachePath, cacheSize);
                        }
                    }
                    cacheEnabled = (cache != null);
                } catch (Exception e) {
                    cacheEnabled = false;
                }
            }
        } else if (cacheEnabled) {
            cacheEnabled = false;
            try {
                HttpResponseCache cache = HttpResponseCache.getInstalled();
                if (cache != null) {
                    cache.close();
                }
            } catch (Exception ignore) {
            }
        }
    }
}
