package cn.vinotec.app.android.comm.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
import cn.vinotec.app.android.comm.VinoApplication;

public class ToastUtil {
    private static boolean debugMode = false;

	protected static Toast toast = null;

    static {
        debugMode = ApplicationUtil.getApplicationMetaDataBoolean(VinoApplication.getContext(), "DEBUG", false);
    }

	public static void showToast(Context mContext, String text, int duration) {
		if (toast != null) {
			toast.setText(text);
		}
		else
			toast = Toast.makeText(mContext, text, duration);
		toast.show();
	}

	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

	public static void showShortToast(Context mContext, String text) {
		showToast(mContext, text, 1500);
	}

	public static void showLongToast(Context mContext, String text) {
		showToast(mContext, text, 3000);
	}

	@SuppressLint("ShowToast")
	public static ToastUtil makeText(Context mContext, String text, int duration) {
		if (toast != null)
			toast.setText(text);
		else
			toast = Toast.makeText(mContext, text, duration);
		return new ToastUtil();
	}

	public void show() {
		toast.show();
	}

	public static void debugToast(Context mContext, String text) {
        if(debugMode)
        {
            showToast(mContext, text, 1500);
        }
	}
}
