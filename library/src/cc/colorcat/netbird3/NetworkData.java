package cc.colorcat.netbird3;

/**
 * Created by cxx on 17-2-23.
 * xx.ch@outlook.com
 */
public final class NetworkData<T> {
    public final boolean isSuccess;
    public final int code;
    public final String msg;
    public final T data;

    public static <R> NetworkData<? extends R> newSuccess(R data) {
        return new NetworkData<>(200, "ok", Utils.nonNull(data, "data == null"));
    }

    public static <R> NetworkData<? extends R> newFailure(int code, String msg) {
        return new NetworkData<>(code, Utils.nonNull(msg, "msg == null"), null);
    }

    private NetworkData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        isSuccess = (data != null);
    }
}
