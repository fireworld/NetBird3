package cc.colorcat.test;

import cc.colorcat.netbird3.*;
import cc.colorcat.netbird3.logging.LoggingFilter;
import cc.colorcat.netbird3.logging.LoggingTailInterceptor;

import java.io.File;
import java.nio.charset.Charset;

public class Main {
    private static final NetBird BIRD;

    static {
        LoggingFilter filter = new LoggingFilter() {
            @Override
            public boolean filter(String contentType) {
                String s = contentType.toLowerCase();
                return s.contains("json") || s.contains("utf") || s.contains("text");
            }
        };
        BIRD = new NetBird.Builder("https://www.qq.com/")
                .enableExceptionLog(true)
                .addTailInterceptor(new LoggingTailInterceptor(filter, Charset.forName("GBK")))
                .readTimeOut(10000)
                .connectTimeOut(10000)
                .build();
    }

    public static void main(String[] args) {
        String url = "http://www.pconline.com.cn/";
        MRequest<String> request = new MRequest.Builder<>(StringParser.create("GBK"))
                .url(url)
                .listener(new MRequest.Listener<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("onSuccess, result = " + result);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        System.out.println("onFailure, code = " + code + ", msg = " + msg);
                    }

                    @Override
                    public void onFinish() {

                    }
                })
                .build();
        BIRD.send(request);
    }

    private static void testDownload() {
        String url = "https://dldir1.qq.com/invc/tt/QQBrowser_Setup_9.6.12501.400.exe";
        File file = new File("/Users/cxx/Downloads/qq.exe");
        MRequest<File> request = new MRequest.Builder<>(FileParser.create(file))
                .url(url)
                .loadListener(new LoadListener() {
                    @Override
                    public void onChanged(long read, long total, int percent) {
                        System.out.printf("read = %d, total = %d, percent = %d\n", read, total, percent);
                    }
                })
                .listener(new MRequest.Listener<File>() {
                    @Override
                    public void onStart() {
                        System.out.println("onStart");
                    }

                    @Override
                    public void onSuccess(File result) {
                        System.out.println("onSuccess, result = " + result.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        System.out.printf("onFailure, code = %d, msg = %s\n", code, msg);
                    }

                    @Override
                    public void onFinish() {
                        System.out.println("onFinish");
                    }
                })
                .build();
        BIRD.send(request);
    }
}
