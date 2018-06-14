package cn.vinotec.app.android.comm.annotation;

import java.lang.annotation.*;

/**
 * Created by kulend on 2015/12/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface VinoActivityAnnotation {

    boolean TwoBackClickExit() default false;

    boolean TwoBackClickFinish() default false;

    String LimmersionBarColor() default "";

    boolean CheckVersion() default false;
}
