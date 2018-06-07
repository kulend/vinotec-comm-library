package cn.vinotec.app.android.comm.tools;

import java.io.InputStream;

import android.content.Context;

public class ResourcesManager {

	private Context context;

	public ResourcesManager(Context context) {
		this.context = context;
	}

	public InputStream getInputStream(int resId) {
		try {
			InputStream inputStream = context.getResources().openRawResource(resId);
			return inputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
