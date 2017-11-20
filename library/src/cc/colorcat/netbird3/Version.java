package cc.colorcat.netbird3;

/**
 * Created by cxx on 2017/2/24.
 * xx.ch@outlook.com
 */
final class Version {
    static String userAgent() {
        return "NetBird/3.0.0";
    }

    private Version() {
        throw new AssertionError("no instance");
    }
}
