package cn.vinotec.app.android.comm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.annotation.VinoViewInject;
import cn.vinotec.app.android.comm.tools.AsynImageLoader;
import cn.vinotec.app.android.comm.utils.StringUtil;
import cn.vinotec.app.android.comm.utils.ToastUtil;
import cn.vinotec.app.android.comm.utils.ViewInjectUtil;

public class VinoFragment extends Fragment {
	
	private VinoApplication app;
	protected Context context;
	protected View view;
	protected LayoutInflater inflater;
	protected VinoViewInject viewInject;

	private AsynImageLoader asynImageLoader;

	public VinoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.context = getActivity();
		this.inflater = getActivity().getLayoutInflater();
		this.app = (VinoApplication) getActivity().getApplication();
		try {
			asynImageLoader = app.getLocalService().getAsynImageLoader();
		} catch (Exception ex) {
		}

		//初始化视图
		Class clazz = this.getClass();
		if(clazz.isAnnotationPresent(VinoViewInject.class))
		{
			viewInject = (VinoViewInject)clazz.getAnnotation(VinoViewInject.class);
			if(viewInject != null && viewInject.id() > 0)
			{
				view = this.inflater.inflate(viewInject.id(), null);
			}
		}
		injectedView(view);
		return view;
	}

    protected void injectedView(View view)
    {
        View v = view;
        if(v == null)
        {
            v = this.view;
        }
        if(v != null)
        {
            ViewInjectUtil.injectedView(this, v);
        }
    }

	public void showImageAsyn(ImageView imageView, String url, int resId) {
		if (StringUtil.isBlank(url) || imageView == null || asynImageLoader == null) {
			return;
		}
		asynImageLoader.showImageAsyn(imageView, url, resId);
	}

	public void showImageAsyn(ImageView imageView, String url) {
		showImageAsyn(imageView, url, 0);
	}

	public void onBackPressed()
	{
		ToastUtil.showShortToast(context, "onBackPressed");
	}
}
