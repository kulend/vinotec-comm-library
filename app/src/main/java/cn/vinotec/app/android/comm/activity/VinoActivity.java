package cn.vinotec.app.android.comm.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.annotation.VinoActivityAnnotation;
import cn.vinotec.app.android.comm.annotation.VinoViewInject;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.tools.VinoAppUpdateTool;
import cn.vinotec.app.android.comm.utils.StringUtil;
import cn.vinotec.app.android.comm.utils.ViewInjectUtil;
import cn.vinotec.app.android.comm.view.VinoLoadingDialog;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VinoActivity extends Activity implements VinoBasePage {

	private static final String TAG = "VinoActivity";
    protected VinoActivityAnnotation annotation;
	protected VinoViewInject viewInject;

	public LayoutInflater inflater;
	protected Context context;
	protected VinoApplication app;

	protected View view;

	protected VinoLoadingDialog mloadingDialog;
	protected List<AsyncTask> mAsyncTasks = new ArrayList<AsyncTask>();

	protected boolean TwoBackClickFinish = false;
	protected boolean TwoBackClickExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Class clazz = this.getClass();
        if(clazz.isAnnotationPresent(VinoActivityAnnotation.class))
        {
            annotation = (VinoActivityAnnotation)clazz.getAnnotation(VinoActivityAnnotation.class);
			TwoBackClickFinish = annotation.TwoBackClickFinish();
			TwoBackClickExit = annotation.TwoBackClickExit();
        }

        context = this;
		inflater = getLayoutInflater();
		app = (VinoApplication) getApplication();
		app.addActivity(this);

        initWindow();

        //初始化视图
        if(clazz.isAnnotationPresent(VinoViewInject.class))
        {
			viewInject = (VinoViewInject)clazz.getAnnotation(VinoViewInject.class);
            if(viewInject != null && viewInject.id() > 0)
            {
                view = inflater.inflate(viewInject.id(), null);
                setContentView(view);
            }
        }
        injectedView(view);

		//检测更新
		if(annotation != null && annotation.CheckVersion() == true)
		{
			VinoAppUpdateTool.update(context);
		}
	}

    protected void injectedView(View v)
    {
        if(v == null)
        {
            v = getWindow().getDecorView();
        }
        ViewInjectUtil.injectedView(this, v);
    }

    @TargetApi(19)
    private void initWindow() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		ImmersionBar bar = ImmersionBar.with(this);
		String barColor = VinoApplication.getInstance().getAnnotation().LimmersionBarColor();
        if(annotation != null && !StringUtil.isBlank(annotation.LimmersionBarColor()))
        {
            barColor = annotation.LimmersionBarColor();
        }
        if(!StringUtil.isBlank(barColor))
        {
            bar.barColor(barColor);
        }
		bar.init();
    }

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

        for (AsyncTask task : mAsyncTasks) {
            if (task != null && task.isCancelled() == false) {
                task.cancel(false);
            }
            task = null;
        }

        mAsyncTasks.clear();

		if (mloadingDialog != null) {
			mloadingDialog.dismiss();
			mloadingDialog = null;
		}

		ImmersionBar.with(this).destroy();
	}

	public void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        startActivity(intent);
	}

	public void showLoading() {
		showLoading("加载中，请稍后...");
	}

	@Override
	public void showLoading(String loadingText) {
		if (mloadingDialog == null) {
			mloadingDialog = VinoLoadingDialog.createDialog(context);
		}
		mloadingDialog.setMessage(loadingText);
		mloadingDialog.show();
	}

	public void hideLoading() {
		if (mloadingDialog == null) {
			return;
		}
		mloadingDialog.dismiss();
	}

    @Override
    public void addAsyncTask(AsyncTask task) {
        mAsyncTasks.add(task);
    }

	@SuppressLint("NewApi")
	@Override
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * 连续按两次返回键就退出
	 */
	private int keyBackClickCount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(TwoBackClickFinish || TwoBackClickExit)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				switch (keyBackClickCount++)
				{
					case 0:
						if(TwoBackClickExit)
						{
							Toast.makeText(this, getResources().getString(R.string.toast_press_again_exit), Toast.LENGTH_SHORT).show();
						}else
						{
							Toast.makeText(this, getResources().getString(R.string.toast_press_again_finish), Toast.LENGTH_SHORT).show();
						}
						Timer timer = new Timer();
						timer.schedule(new TimerTask()
						{
							@Override
							public void run()
							{
								keyBackClickCount = 0;
							}
						}, 3000);
						break;
					case 1:
						if(TwoBackClickExit)
						{
							VinoApplication.getInstance().exitApp();
						}else
						{
							finish();
						}
						break;
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
