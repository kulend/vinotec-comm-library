package cn.vinotec.app.android.comm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import cn.vinotec.app.android.comm.tools.AsynImageLoader;

public class BaseService extends Service
{
	private final IBinder binder = new LocalBinder();
	private AsynImageLoader asynImageLoader = null;
	
	public class LocalBinder extends Binder
	{
		public BaseService getService() {
			return BaseService.this;
	   }
	} 

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public BaseService() {
		super();
		asynImageLoader = new AsynImageLoader();
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	/**
	 * 取得异步加载图片实例
	 */
	public AsynImageLoader getAsynImageLoader(){ 
        return asynImageLoader; 
    }
}
