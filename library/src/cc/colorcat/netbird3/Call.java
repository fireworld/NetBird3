package cc.colorcat.netbird3;

import java.io.IOException;

/**
 * Created by cxx on 17-2-22.
 * xx.ch@outlook.com
 */
public interface Call extends Cloneable {
    Request request();

    Response execute() throws IOException;

    /**
     * @param callback 异步请求结果回调
     * @throws NullPointerException 如果 callback 为空会抛出此异常
     */
    void enqueue(Callback callback);

    /**
     * 取消请求
     */
    void cancel();

    boolean isCanceled();

    interface Factory {
        Call newCall(Request request);
    }
}
