package cn.vinotec.app.android.comm.action;

import android.os.Handler;
import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.activity.VinoBasePage;
import cn.vinotec.app.android.comm.annotation.VinoTaskAnnotation;
import cn.vinotec.app.android.comm.utils.StringUtil;
import cn.vinotec.app.android.comm.utils.ToastUtil;

public class VinoActionAsyncTask<Params, Progress, Result> extends VinoAsyncTask<Params, Progress, Result> {

	private VinoBasePage mPage;
	private String loadingText = "处理中，请稍后...";
    private int mTimeout = 0;

	private boolean InProcess = false;

	public VinoActionAsyncTask(VinoBasePage page) {
		this.mPage = page;
		if(page != null)
		{
			page.addAsyncTask(this);
		}
        Class clazz = this.getClass();
        if(clazz.isAnnotationPresent(VinoTaskAnnotation.class))
        {
            VinoTaskAnnotation anno = (VinoTaskAnnotation)clazz.getAnnotation(VinoTaskAnnotation.class);
            if(anno != null)
            {
                if(!StringUtil.isBlank(anno.LoadingText()))
                {
                    loadingText = anno.LoadingText();
                }
                mTimeout = anno.Timeout();
            }
        }
	}

	protected void setLoadingText(String text) {
		this.loadingText = text;
	}

	@Override
	protected void onPreExecute() {
		if (mPage != null) {
			mPage.showLoading(this.loadingText);
		}
        if(mTimeout > 0)
        {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    if (mPage != null) {
                        mPage.hideLoading();
                    }
                    if(!VinoActionAsyncTask.this.isCancelled() && InProcess)
                    {
                        VinoActionAsyncTask.this.cancel(false);
						ToastUtil.showShortToast(VinoApplication.getContext(), "操作已超时");
                    }
                }
            }, mTimeout);
        }
        InProcess = true;
		super.onPreExecute();
	}

	@Override
	protected Result doInBackground(Params... params) {
		return null;
	}

	@Override
	protected void onPostExecute(Result result) {
        InProcess = false;
		if (mPage != null) {
			mPage.hideLoading();
		}
		super.onPostExecute(result);
	}

}
