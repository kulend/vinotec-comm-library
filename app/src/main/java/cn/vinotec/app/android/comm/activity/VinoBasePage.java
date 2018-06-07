package cn.vinotec.app.android.comm.activity;

import android.os.AsyncTask;

public interface VinoBasePage {

	void showLoading();

	void showLoading(String loadingText);

	void hideLoading();

	void addAsyncTask(AsyncTask task);
}
