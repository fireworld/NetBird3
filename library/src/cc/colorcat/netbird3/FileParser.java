package cc.colorcat.netbird3;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cxx on 17-2-23.
 * xx.ch@outlook.com
 */
public final class FileParser implements Parser<File> {
    private File savePath;

    /**
     * Constructs a new {@link FileParser} using the specified path.
     *
     * @param savePath the path to be used for save the savePath.
     * @throws NullPointerException if {@code savePath} is {@code null}.
     */
    public static FileParser create(String savePath) {
        File file = new File(savePath);
        return create(file);
    }

    public static FileParser create(File savePath) {
        if (savePath == null) {
            throw new NullPointerException("savePath == null");
        }
        File parent = savePath.getParentFile();
        if (parent.exists() || parent.mkdirs()) {
            return new FileParser(savePath);
        }
        throw new RuntimeException("Can't create directory, " + parent.getAbsolutePath());
    }

    private FileParser(File savePath) {
        this.savePath = savePath;
    }

    @Override
    public NetworkData<? extends File> parse(Response data) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            Utils.justDump(data.body().stream(), fos);
            return NetworkData.newSuccess(savePath);
        } finally {
            Utils.close(fos);
        }
    }
}
