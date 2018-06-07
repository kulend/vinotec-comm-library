package cn.vinotec.app.android.comm.utils;

import android.view.View;
import cn.vinotec.app.android.comm.annotation.VinoViewInject;

import java.lang.reflect.Field;

public class ViewInjectUtil {
    /**
     * 初始化指定View中的注入属性
     */
    public static void injectedView(Object injectedSource, View sourceView){
        Field[] fields = injectedSource.getClass().getDeclaredFields();
        if(fields!=null && fields.length>0){
            for(Field field : fields){
                VinoViewInject viewInject = field.getAnnotation(VinoViewInject.class);
                if(viewInject!=null){
                    int viewId = viewInject.id();
                    try {
                        field.setAccessible(true);
                        field.set(injectedSource, sourceView.findViewById(viewId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String clickMethod = viewInject.click();
                    if(!StringUtil.isBlank(clickMethod))
                    {
                        setViewClickListener(injectedSource,field,clickMethod);
                    }
                }
            }
        }
    }

    private static void setViewClickListener(Object injectedSource,Field field,String clickMethod){
        try {
            Object obj = field.get(injectedSource);
            if(obj instanceof View){
                ((View)obj).setOnClickListener(new VinoEventListener(injectedSource).click(clickMethod));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
