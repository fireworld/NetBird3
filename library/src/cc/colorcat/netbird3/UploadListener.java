package cc.colorcat.netbird3;

/**
 * Created by cxx on 17-2-27.
 * xx.ch@outlook.com
 */
public interface UploadListener extends ProgressListener {

    @Override
    void onChanged(long written, long total, int percent);
}
