package cc.colorcat.netbird3;


import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
final class Utils {
    static final String UTF8 = "UTF-8";

    static String smartEncode(String value) {
        try {
            String decodedValue = decode(value);
            if (!value.equals(decodedValue)) {
                return value;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return encode(value);
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, Utils.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, Utils.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static String checkedHttp(String url) {
        if (url == null || !url.toLowerCase().matches("^(http)(s)?://(\\S)+")) {
            throw new IllegalArgumentException("Bad url, the scheme must be http or https");
        }
        return url;
    }

    static <T> T nonNull(T value, String msg) {
        if (value == null) {
            throw new NullPointerException(msg);
        }
        return value;
    }

    static <T extends CharSequence> T nonEmpty(T txt, String msg) {
        if (Utils.isEmpty(txt)) {
            throw new IllegalArgumentException(msg);
        }
        return txt;
    }

    private static boolean isEmpty(CharSequence txt) {
        return txt == null || txt.length() == 0;
    }

    static <T> T nullElse(T value, T other) {
        return value != null ? value : other;
    }

    static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    static void checkHeader(String name, String value) {
        if (name == null) throw new NullPointerException("name == null");
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        for (int i = 0, length = name.length(); i < length; i++) {
            char c = name.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                throw new IllegalArgumentException(String.format(Locale.getDefault(),
                        "Unexpected char %#04x at %d in header name: %s", (int) c, i, name));
            }
        }
        if (value == null) throw new NullPointerException("value == null");
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                throw new IllegalArgumentException(String.format(Locale.getDefault(),
                        "Unexpected char %#04x at %d in %s value: %s", (int) c, i, name, value));
            }
        }
    }

    static Charset parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(";");
            final int length = params.length;
            for (int i = 1; i < length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equalsIgnoreCase("charset")) {
                        try {
                            return Charset.forName(pair[1]);
                        } catch (Exception ignore) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    static void close(Closeable cs) {
        if (cs != null) {
            try {
                cs.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
    }

    static String justRead(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(reader);
        char[] buffer = new char[1024];
        for (int length = br.read(buffer); length != -1; length = br.read(buffer)) {
            sb.append(buffer, 0, length);
        }
        return sb.toString();
    }

    static byte[] justRead(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length = bis.read(buffer); length != -1; length = bis.read(buffer)) {
            bos.write(buffer, 0, length);
        }
        bos.flush();
        return bos.toByteArray();
    }

    static void justDump(InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buffer = new byte[2048];
        for (int length = bis.read(buffer); length != -1; length = bis.read(buffer)) {
            bos.write(buffer, 0, length);
        }
        bos.flush();
    }

    private Utils() {
        throw new AssertionError("no instance");
    }
}
