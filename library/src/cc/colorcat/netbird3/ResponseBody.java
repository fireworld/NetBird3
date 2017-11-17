package cc.colorcat.netbird3;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */

public abstract class ResponseBody implements Closeable {

    public abstract long contentLength();

    public abstract String contentType();

    public abstract Charset charset();

    public abstract InputStream stream();

    public final Reader reader() {
        Charset charset = charset();
        return charset != null ? new InputStreamReader(stream(), charset) : new InputStreamReader(stream());
    }

    public final Reader reader(Charset defCharset) {
        Charset charset = Utils.nullElse(charset(), defCharset);
        return new InputStreamReader(stream(), charset);
    }

    public final String string() throws IOException {
        return Utils.justRead(reader());
    }

    public final String string(Charset defCharset) throws IOException {
        return Utils.justRead(reader(defCharset));
    }

    public final byte[] bytes() throws IOException {
        return Utils.justRead(stream());
    }

    @Override
    public final void close() {
        Utils.close(stream());
    }

    public static ResponseBody create(String content) {
        byte[] bytes = content.getBytes();
        return create(bytes);
    }

    public static ResponseBody create(String content, String contentType) {
        Charset charset = Utils.parseCharset(contentType);
        byte[] bytes = charset != null ? content.getBytes(charset) : content.getBytes();
        return create(new ByteArrayInputStream(bytes), bytes.length, contentType, charset);
    }

    public static ResponseBody create(String content, String contentType, Charset charset) {
        Charset c = Utils.nullElse(charset, Utils.parseCharset(contentType));
        byte[] bytes = c != null ? content.getBytes(c) : content.getBytes();
        return create(new ByteArrayInputStream(bytes), bytes.length, contentType, c);
    }

    public static ResponseBody create(byte[] content) {
        return create(content, null, null);
    }

    public static ResponseBody create(byte[] content, String contentType) {
        if (content == null) throw new NullPointerException("content == null");
        return create(new ByteArrayInputStream(content), content.length, contentType);
    }

    public static ResponseBody create(byte[] content, String contentType, Charset charset) {
        if (content == null) throw new NullPointerException("content == null");
        return create(new ByteArrayInputStream(content), content.length, contentType, charset);
    }

    public static ResponseBody create(final InputStream is) {
        return create(is, -1L, null, null);
    }

    public static ResponseBody create(final InputStream is, final String contentType) {
        return create(is, -1L, contentType);
    }

    public static ResponseBody create(final InputStream is, final long contentLength, final String contentType) {
        return create(is, contentLength, contentType, Utils.parseCharset(contentType));
    }

    public static ResponseBody create(
            final InputStream is, final long contentLength, final String contentType, final Charset charset) {
        if (is == null) throw new NullPointerException("is == null");
        return new ResponseBody() {
            @Override
            public long contentLength() {
                return contentLength;
            }

            @Override
            public String contentType() {
                return contentType;
            }

            @Override
            public Charset charset() {
                return charset;
            }

            @Override
            public InputStream stream() {
                return is;
            }
        };
    }
}
