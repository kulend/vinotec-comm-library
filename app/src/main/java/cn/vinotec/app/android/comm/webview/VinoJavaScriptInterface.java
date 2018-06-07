package cn.vinotec.app.android.comm.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import cn.vinotec.app.android.comm.utils.ToastUtil;

public class VinoJavaScriptInterface {
	protected Activity activity;
	protected WebView mWebView;

	public VinoJavaScriptInterface(Activity activity, WebView webView) {
		this.activity = activity;
		this.mWebView = webView;
	}
	
	

	@JavascriptInterface
	public void Toast(String text) {
		ToastUtil.makeText(activity, text, Toast.LENGTH_LONG).show();
	}

	@JavascriptInterface
	public void CallPhone(String no) {
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
		// 启动
		activity.startActivity(phoneIntent);
	}

	@JavascriptInterface
	public void PlayVideo(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String type = "video/mp4";
		Uri name = Uri.parse(url);
		intent.setDataAndType(name, type);
		// intent.setClassName("com.cooliris.media",
		// "com.cooliris.media.MovieView");
		activity.startActivity(intent);
	}

	@JavascriptInterface
	public void Share(String title, String content, String targetUrl, String imageUrl) {
//		ShareTool tool = new ShareTool(activity);
//		tool.setShareContent(content);
//		tool.setShareImageUrl(imageUrl);
//		tool.setWeixinShare(content, title, targetUrl, imageUrl);
//		tool.setWeiXinCircleShare(content, title, targetUrl, imageUrl);
//		//tool.setQqShare(content, title, targetUrl, imageUrl);
//		//tool.setQZoneShare(content, title, targetUrl, imageUrl);
//		//tool.setCopyUrlShare(targetUrl);
//		tool.setSaveImageShare(getShot(activity));
//		tool.openShare();
	}

	@JavascriptInterface
	public void SaveData(String key, String value) {
		SharedPreferences preferences = activity.getApplication().getSharedPreferences("webviewdata",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	@JavascriptInterface
	public void GetData(final String key, final String callback) {
		SharedPreferences preferences = activity.getApplication().getSharedPreferences("webviewdata",
				Context.MODE_PRIVATE);
		final String value = preferences.getString(key, "");
		mWebView.post(new Runnable() {
			@Override
			public void run() {
				mWebView.loadUrl("javascript:" + callback + "('" + key + "','" + value + "')");
			}
		});
	}

	@JavascriptInterface
	public void GoBack() {
		mWebView.post(new Runnable() {
			@Override
			public void run() {
				mWebView.goBack();
			}
		});
	}

	@JavascriptInterface
	public void CloseWindow() {
		activity.finish();
	}

    public Bitmap getShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }

}
