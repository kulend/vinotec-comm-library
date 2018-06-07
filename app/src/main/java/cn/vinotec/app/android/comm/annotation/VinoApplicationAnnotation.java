package cn.vinotec.app.android.comm.annotation;

import cn.vinotec.app.android.comm.library.R;

import java.lang.annotation.*;

/**
 * Created by kulend on 2015/12/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface VinoApplicationAnnotation {

    boolean LimmersionMode() default true;

    String LimmersionStatusColor() default "#FFFFFF";

    int EmptyImageResId() default 0;
}
