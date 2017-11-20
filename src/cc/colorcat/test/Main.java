package cc.colorcat.test;

import cc.colorcat.netbird3.FileParser;
import cc.colorcat.netbird3.LoadListener;
import cc.colorcat.netbird3.MRequest;
import cc.colorcat.netbird3.NetBird;

import java.io.File;

public class Main {
    private static final NetBird BIRD;

    static {
        BIRD = new NetBird.Builder("https://www.qq.com/")
                .enableExceptionLog(true)
                .readTimeOut(10000)
                .connectTimeOut(10000)
                .build();
    }

    public static void main(String[] args) {
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
