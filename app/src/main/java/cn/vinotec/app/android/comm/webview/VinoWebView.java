package cn.vinotec.app.android.comm.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.ApplicationUtil;
import cn.vinotec.app.android.comm.utils.StringUtil;

public class VinoWebView extends WebView {

	public Activity activity;
	private ProgressBar mProgressbar;
	private View mViewError;
	private String mHomeUrl;
	private Handler mOnPageChangeHandler;
	public String ActionSchema = "vino-app://";


    public VinoWebView(Context context) {
		super(context);
	}

	public VinoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public VinoWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	public void init(Activity activity, String homeUrl)
	{
		init(activity, homeUrl, null,null);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	public void init(Activity activity, String homeUrl, String agent, String actionSchema)
	{
		if(!StringUtil.isBlank(actionSchema))
		{
			this.ActionSchema = actionSchema;
		}
		this.activity = activity;
		this.mHomeUrl = homeUrl;

		setHorizontalScrollBarEnabled(false);
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = getSettings();
		settings.setSavePassword(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			settings.setPluginState(WebSettings.PluginState.ON);
		}

		if(StringUtil.isBlank(agent))
		{
			agent = "vinotec_app_android";
		}

		int versioncode = ApplicationUtil.getVersionCode(this.getContext());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			// 设置UserAgent
			String ua = WebSettings.getDefaultUserAgent(this.getContext());
			getSettings().setUserAgentString(ua + " " + agent +  " version(" + versioncode + ")");
		} else {
			String ua = settings.getDefaultUserAgent(this.getContext());
			getSettings().setUserAgentString(ua + " " + agent +  " version(" + versioncode + ")");
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDisplayZoomControls(false);
		settings.setBuiltInZoomControls(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true); // 开启 database storage API 功能
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染优先级

		// 不自动加载图片
		if (Build.VERSION.SDK_INT >= 19) {
			// settings.setLoadsImagesAutomatically(true);
		} else {
			// settings.setLoadsImagesAutomatically(false);
		}
		
		setWebViewClient(new VinoWebViewClient(this));
		setWebChromeClient(new VinoWebChromeClient());
		requestFocus();
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		addJavascriptInterface(new VinoJavaScriptInterface(activity, VinoWebView.this), "app");
		
		// 进度条
		mProgressbar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
		mProgressbar.setMax(100);
		mProgressbar.setProgressDrawable(this.getResources().getDrawable(R.drawable.webview_progress_bar_states));
		mProgressbar.setProgress(5); // 先加载5%，以使用户觉得界面没有卡死
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 6);
		params.addRule(RelativeLayout.ABOVE, VinoWebView.this.getId());
		ViewParent parent = VinoWebView.this.getParent();
		if (parent != null) {
			((ViewGroup) parent).addView(mProgressbar, params);
		}
		
		mViewError = new View(activity);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		mViewError.setBackgroundResource(R.drawable.comm_webview_error);
		if (parent != null) {
			((ViewGroup) parent).addView(mViewError, params2);
		}
		mViewError.setVisibility(View.GONE);
		mViewError.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				VinoWebView.this.loadUrl(mHomeUrl);
				mViewError.setVisibility(View.GONE);
			}
		});
	}
	
	public void setOnPageChangeHandler(Handler handler) {
		mOnPageChangeHandler = handler;
	}

	public void sendPageChangeMessage(String url) {
		if (mOnPageChangeHandler != null) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = url;
			mOnPageChangeHandler.sendMessage(msg);
		}
	}

	public void showProgressbar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(View.VISIBLE);
		}
	}

	public void hideProgressbar() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(View.GONE);
		}
	}

	public void showError() {
		if (mViewError != null) {
			mViewError.setVisibility(View.VISIBLE);
		}
	}

	class VinoWebChromeClient extends WebChromeClient{
        @Override
	    public void onProgressChanged(WebView view, int newProgress) {
			// 动态在标题栏显示进度条
	    	if(mProgressbar != null)
	    	{
				if (newProgress >= 5) {
					mProgressbar.setProgress(newProgress);
		        }
	    	}
	        super.onProgressChanged(view, newProgress);
	    }

	    @Override 
	    public void onReceivedTitle(WebView view, String title) {
	        super.onReceivedTitle(view, title);
	    }
    }
}
