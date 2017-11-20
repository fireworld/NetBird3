package cc.colorcat.netbird3;


import java.io.*;


/**
 * 包装 {@link InputStream} 以显示流的读取进度。
 * <p>
 * Created by cxx on 2016/12/12.
 * xx.ch@outlook.com
 */
public final class ProgressInputStream extends FilterInputStream {
    private ProgressListener listener;
    private final long contentLength;
    private long finished = 0;
    private int currentPercent;
    private int lastPercent = currentPercent;

    /**
     * @param is            数据读取的来源
     * @param contentLength is 所包含的数据总长度
     * @param listener      读取数据进度监听器
     * @return ProgressInputStream
     */
    public static InputStream wrap(InputStream is, long contentLength, ProgressListener listener) {
        if (is != null && contentLength > 0 && listener != null) {
            return new ProgressInputStream(is, contentLength, listener);
        }
        return is;
    }

    /**
     * @param file     数据来源于此文件
     * @param listener 读取数据进度监听器
     * @return ProgressInputStream
     */
    public static InputStream wrap(File file, ProgressListener listener) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        if (listener != null) {
            is = new ProgressInputStream(is, file.length(), listener);
        }
        return is;
    }

    private ProgressInputStream(InputStream is, long contentLength, ProgressListener listener) {
        super(is);
        this.contentLength = contentLength;
        this.listener = listener;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = in.read(b, off, len);
        if (count != -1) {
            updateProgress(count);
        }
        return count;
    }

    @Override
    public int read() throws IOException {
        int nextByte = in.read();
        if (nextByte != -1) {
            updateProgress(1);
        }
        return nextByte;
    }

    private void updateProgress(final int readCount) {
        finished += readCount;
        currentPercent = (int) (finished * 100 / contentLength);
        if (currentPercent > lastPercent) {
            ScheduleUtils.postProgressOnTargetThread(listener, finished, contentLength, currentPercent);
            lastPercent = currentPercent;
        }
    }
}
