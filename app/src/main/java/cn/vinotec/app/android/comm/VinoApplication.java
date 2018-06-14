package cn.vinotec.app.android.comm;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.vinotec.app.android.comm.annotation.VinoApplicationAnnotation;
import cn.vinotec.app.android.comm.library.R;

import cn.vinotec.app.android.comm.tools.VinoImageLoadTool;
import cn.vinotec.app.android.comm.utils.ToastUtil;

public class VinoApplication extends Application
{
	public static boolean IsObtainedStoragePermission = false;

	private List<Activity> activithList = new LinkedList<Activity>();

	private static VinoApplication instance;
	private static Context sContext;
	private BaseService localService;
	private VinoApplicationAnnotation annotation;

	public static VinoApplication getInstance()
	{
		return instance;
	}

	public BaseService getLocalService() {
		return localService;
	}

	public void setLocalService(BaseService localService) {
		this.localService = localService;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		instance = this;
		sContext = getApplicationContext();
		CrashHandler.getInstance().init(this);

		//取得注解
		VinoApplicationAnnotation annotation = getAnnotation();
		int EmptyPhotoResId = R.drawable.comm_empty_photo;
		if(annotation != null && annotation.EmptyImageResId() > 0)
		{
			EmptyPhotoResId = annotation.EmptyImageResId();
		}

        //初始化图片加载工具
		VinoImageLoadTool.init(EmptyPhotoResId);

		ToastUtil.debugToast(this, "当前处于调试模式!");
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}

    public static Context getContext() {
        return sContext;
    }

	public void addActivity(Activity activity)
	{
		activithList.add(activity);
	}

	public void exitApp()
	{
		for (Activity activity : activithList)
		{
			activity.finish();
		}
		System.exit(0);
	}

	/**
	 * 取得注解
     */
	public VinoApplicationAnnotation getAnnotation()
	{
		if(annotation == null)
		{
			Class clazz = this.getClass();
			if(clazz.isAnnotationPresent(VinoApplicationAnnotation.class))
			{
				annotation = (VinoApplicationAnnotation)clazz.getAnnotation(VinoApplicationAnnotation.class);
			}else
			{
				annotation = new VinoApplicationAnnotation(){
					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String LimmersionBarColor() {
						return "";
					}
					@Override
					public String BaseApiUrl() {
						return "http://www.baidu.com/";
					}
					@Override
					public int EmptyImageResId() {
						return 0;
					}
				};
			}
		}
		return annotation;
	}
}
