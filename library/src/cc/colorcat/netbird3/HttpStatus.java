package cc.colorcat.netbird3;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public final class HttpStatus {
    public static final int CODE_CONNECT_ERROR = -100;
    public static final String MSG_CONNECT_ERROR = "connect error";

    public static final int CODE_DUPLICATE_REQUEST = -101;
    public static final String MSG_DUPLICATE_REQUEST = "duplicate request";

    public static final int CODE_CANCELED = -102;
    public static final String MSG_CANCELED = "request canceled";

    private HttpStatus() {
        throw new AssertionError("no instance");
    }
}
