package cc.colorcat.netbird3.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static cc.colorcat.netbird3.internal.Level.*;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
@IntegerDef({VERBOSE, DEBUG, INFO, WARN, ERROR, NOTHING})
@Retention(RetentionPolicy.SOURCE)
public @interface Level {
    int VERBOSE = 2;
    int DEBUG = 3;
    int INFO = 4;
    int WARN = 5;
    int ERROR = 6;
    int NOTHING = 10;
}
