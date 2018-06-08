package cn.vinotec.app.android.comm.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cn.vinotec.app.android.comm.activity.ImagePagerActivity;
import cn.vinotec.app.android.comm.entity.ShowImageEntity;
import cn.vinotec.app.android.comm.tools.ResourcesManager;
import cn.vinotec.app.android.comm.tools.VinoAppUpdateTool;
import cn.vinotec.app.android.comm.utils.StringUtil;
import cn.vinotec.app.android.comm.utils.ToastUtil;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VinoWebViewClient extends WebViewClient {
	protected ResourcesManager rm;
	protected Context context;
	protected VinoWebView mWebView;
    protected Activity mActivity;

	protected static String APP_SCHEME = "vino-app://";

	public VinoWebViewClient(VinoWebView webView) {
		this.mWebView = webView;
		this.APP_SCHEME = webView.ActionSchema;
        this.mActivity = webView.activity;
		this.context = webView.getContext();
		this.rm = new ResourcesManager(context);
	}

	// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// mWebView.getSettings().setLoadsImagesAutomatically(false);
		// view.loadUrl(url);
		// 如果不需要其他对点击链接事件的处理返回true，否则返回false
		mWebView.sendPageChangeMessage(url);
        if (url.startsWith(APP_SCHEME)) {
            String action = parseActionFromUrl(url);
            HashMap<String, Object> pms = parsePmsFromUrl(url);
            ToastUtil.debugToast(context, "webview action:" + action);
            if (pms.containsKey("goback")) {
                mWebView.goBack();
            }
            if("image_preview".equalsIgnoreCase(action))
            {
                String imageUrl = pms.get("url").toString();
                String imageUrls = pms.get("urls").toString();
                if(StringUtil.isBlank(imageUrl) && StringUtil.isBlank(imageUrls))
                {
                    return true;
                }
                List<ShowImageEntity> images = new ArrayList<ShowImageEntity>();
                int index = 0;
                if(StringUtil.isBlank(imageUrls))
                {
                    ShowImageEntity item = new ShowImageEntity();
                    item.setPath(imageUrl);
                    item.setType(ShowImageEntity.Type_URL);
                    images.add(item);
                }else
                {
                    String[] arrUrl = imageUrls.split(",");
                    for (int i = 0; i< arrUrl.length;i++) {
                        String u = arrUrl[i];
                        if(u.equalsIgnoreCase(imageUrl))
                        {
                            index = i;
                        }
                        ShowImageEntity item = new ShowImageEntity();
                        item.setPath(u);
                        item.setType(ShowImageEntity.Type_URL);
                        images.add(item);
                    }
                }
                Intent intent = new Intent(mActivity, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,index);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) images);
                mActivity.startActivity(intent);
                return true;
            }else if("share".equalsIgnoreCase(action)){
//                String text = URLDecoder.decode(pms.get("text").toString());
//                String link = pms.get("url").toString();
//                String image = pms.get("image").toString();
//                String title = URLDecoder.decode(pms.get("title").toString());
//                ShareTool tool = new ShareTool(mActivity);
//                tool.setShareContent(text);
//                tool.setShareImageUrl(image);
//                tool.setWeixinShare(text, title, link, image);
//                tool.setWeiXinCircleShare(text, title, link, image);
//                //tool.setQqShare(content, title, targetUrl, imageUrl);
//                //tool.setQZoneShare(content, title, targetUrl, imageUrl);
//                //tool.setCopyUrlShare(targetUrl);
//                //tool.setSaveImageShare(getShot(mActivity));
//                tool.openShare();
                return true;
            }else if("play_video".equalsIgnoreCase(action)){
                String link = pms.get("url").toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String type = "video/mp4";
                Uri name = Uri.parse(link);
                intent.setDataAndType(name, type);
                mActivity.startActivity(intent);
                return true;
            }else if("call_tel".equalsIgnoreCase(action)){
                String tel = pms.get("tel").toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tel);
                intent.setData(data);
                mActivity.startActivity(intent);
                return true;
            }else if("check_version".equalsIgnoreCase(action)){
                VinoAppUpdateTool.check(context);
                return true;
            }
            else
            {
                return onUrlAction(view, url, action, pms);
            }
        }
		return onUrlLoading(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		mWebView.showProgressbar();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		mWebView.hideProgressbar();
		// 加载图片
		if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
			mWebView.getSettings().setLoadsImagesAutomatically(true);
		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		mWebView.hideProgressbar();
		mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
		mWebView.showError();
	}

	public boolean onUrlLoading(WebView view, String url)
    {
        return false;
    }

    public boolean onUrlAction(WebView view, String url, String action, HashMap<String, Object> pms)
    {
        return false;
    }

    protected String parseActionFromUrl(String url) {
        if (url.indexOf("?") > 0) {
            return url.substring(APP_SCHEME.length(), url.indexOf("?"));
        } else {
            return url.substring(APP_SCHEME.length());
        }
    }

    protected HashMap<String, Object> parsePmsFromUrl(String url) {
        if (url.indexOf("?pms=") < 0) {
            return new HashMap<String, Object>();
        }
        String pmsStr = url.substring(url.indexOf("?pms=") + 5);
        if (StringUtil.isBlank(pmsStr)) {
            return new HashMap<String, Object>();
        }
        HashMap<String, Object> pms = new HashMap<String, Object>();
        ObjectMapper mObjectMapper = new ObjectMapper();
        mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mObjectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        try {
            pms = mObjectMapper.readValue(pmsStr, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            pms = new HashMap<String, Object>();
        }
        return pms;
    }

    private Bitmap getShot(Activity activity) {
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
