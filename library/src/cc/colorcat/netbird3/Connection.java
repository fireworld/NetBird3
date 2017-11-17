package cc.colorcat.netbird3;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public interface Connection extends Closeable, Cloneable {

    void connect(NetBird netBird, Request request) throws IOException;

    void writeHeaders(Headers headers) throws IOException;

    void writeBody(RequestBody body) throws IOException;

    int responseCode() throws IOException;

    String responseMsg() throws IOException;

    Headers responseHeaders() throws IOException;

    ResponseBody responseBody(Headers headers) throws IOException;

    Connection clone();

    void cancel();
}
