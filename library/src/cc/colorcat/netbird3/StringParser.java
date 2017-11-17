package cc.colorcat.netbird3;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by cxx on 17-2-23.
 * xx.ch@outlook.com
 */
public final class StringParser implements Parser<String> {
    private static volatile StringParser utf8;
    private static volatile StringParser instance;

    public static StringParser create(String charset) {
        return new StringParser(Charset.forName(charset));
    }

    public static StringParser get() {
        if (instance == null) {
            synchronized (StringParser.class) {
                if (instance == null) {
                    instance = new StringParser(null);
                }
            }
        }
        return instance;
    }

    public static StringParser getUtf8() {
        if (utf8 == null) {
            synchronized (StringParser.class) {
                if (utf8 == null) {
                    utf8 = new StringParser(Charset.forName(Utils.UTF8));
                }
            }
        }
        return utf8;
    }

    private final Charset charset;

    private StringParser(Charset charset) {
        this.charset = charset;
    }

    @Override
    public NetworkData<? extends String> parse(Response data) throws IOException {
        NetworkData<? extends String> networkData;
        if (charset != null) {
            networkData = NetworkData.newSuccess(data.body().string(charset));
        } else {
            networkData = NetworkData.newSuccess(data.body().string());
        }
        return networkData;
    }
}