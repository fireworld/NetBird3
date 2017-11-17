package cc.colorcat.netbird3;

/**
 * Created by cxx on 2017/3/5.
 * xx.ch@outlook.com
 */
final class MLoadListener implements LoadListener {

    static LoadListener wrap(LoadListener listener) {
        if (listener != null) {
            return new MLoadListener(listener);
        }
        return null;
    }

    private LoadListener listener;

    private MLoadListener(LoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onChanged(long read, long total, int percent) {
        ScheduleCenter.postProgressOnTargetThread(listener, read, total, percent);
    }
}
