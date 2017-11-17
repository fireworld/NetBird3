package cc.colorcat.netbird3.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cxx on 17-11-17.
 * xx.ch@outlook.com
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface IntegerDef {
    int[] value() default {};
}
