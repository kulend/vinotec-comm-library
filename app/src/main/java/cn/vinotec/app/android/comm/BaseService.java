package cn.vinotec.app.android.comm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BaseService extends Service
{
	private final IBinder binder = new LocalBinder();

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
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	}

}
