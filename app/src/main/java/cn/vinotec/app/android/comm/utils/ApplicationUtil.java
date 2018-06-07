package cn.vinotec.app.android.comm.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationUtil {
	
	/**
	 * 获取软件版本号
	 */
	public static int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 获取软件版本名称
	 */
	public static String getVersionName(Context context)
	{
		String versionName = "";
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionName;
	}
	
    @SuppressWarnings("deprecation")
	public static int getAndroidVersion()
    {
    	return Integer.valueOf(android.os.Build.VERSION.SDK);
    }
    
	public static String getApplicationMetaData(Context context, String key)
	{
		ApplicationInfo info;
		String value = null;
		try {
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			value = info.metaData.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static int getApplicationMetaDataInt(Context context, String key)
	{
		ApplicationInfo info;
		int value = 0;
		try {
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			value = info.metaData.getInt(key, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

    public static boolean getApplicationMetaDataBoolean(Context context, String key, boolean defValue)
    {
        ApplicationInfo info;
        boolean value = defValue;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = info.metaData.getBoolean("DEBUG", defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isApkDebugable(Context context) {  
        try {  
            ApplicationInfo info= context.getApplicationInfo();  
               return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;  
        } catch (Exception e) {         
      }  
      return false;  
   }

}
