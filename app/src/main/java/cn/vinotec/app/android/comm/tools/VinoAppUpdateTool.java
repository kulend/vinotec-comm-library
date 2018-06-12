package cn.vinotec.app.android.comm.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.entity.ApiReply;
import cn.vinotec.app.android.comm.entity.AppVersionEntity;
import cn.vinotec.app.android.comm.https.AppCommDao;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.ApplicationUtil;
import cn.vinotec.app.android.comm.utils.StringUtil;
import cn.vinotec.app.android.comm.utils.ToastUtil;
import cn.vinotec.app.android.comm.view.VinoAppDownloadDialog;
import cn.vinotec.app.android.comm.view.VinoAppUpdateDialog;

public class VinoAppUpdateTool
{
	public static String AppUpdateCheckUrl = "https://datacenter.vinotec.cn:8070/api/version/check?appkey=%s&version=%s";

    //定义回调事件，用于dialog的点击事件
    public interface OnAppUpdateListener{
        void OnReceive(AppVersionEntity VersionInfo);
    }

	private static VinoAppUpdateTool instance;

	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载完成 */
	private static final int DOWNLOAD_FINISH = 2;
    /* 下载停止 */
    private static final int DOWNLOAD_CANCEL = 6;

	private static final int ACTION_CHECK_SILENT = 3;
	private static final int ACTION_CHECK = 4;
    private static final int ACTION_CHECK_INFO = 5;



    private Context context;
	/* 下载保存路径 */
	private String mSavePath;
	
	/* 记录进度条数量 */
	private int progress;
	
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private VinoAppDownloadDialog mDownloadDialog;

	private String VinoAppKey;
	private AppVersionEntity VersionInfo;

	private boolean inCheck = false;

    private OnAppUpdateListener mListener;

    public void setAppUpdateListener(OnAppUpdateListener listener)
    {
        mListener = listener;
    }

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			int versionCode = ApplicationUtil.getVersionCode(VinoApplication.getContext());
			String versionName = ApplicationUtil.getVersionName(VinoApplication.getContext());

			switch (msg.what)
			{
				case DOWNLOAD:
				// 正在下载
				// 设置进度条位置
                    if(mDownloadDialog != null)
                    {
                        mDownloadDialog.updateProgress(progress);
                    }
					break;
				case DOWNLOAD_FINISH:
				// 下载完成
//                    if(mDownloadDialog != null)
//                    {
//                        mDownloadDialog.dismiss();
//                    }
				// 安装文件
					installApk();
					break;
                case DOWNLOAD_CANCEL:
                    // 下载取消
                    if(VersionInfo.isForce())
                    {
                        VinoApplication.getInstance().exitApp();
                    }
                    break;
			case ACTION_CHECK:
				// 检查更新
				if (VersionInfo == null)
				{
					return;
				}
				if (VersionInfo.getUpdate() == 1)
				{
					// 有新版本
					// 显示提示对话框
					showNoticeDialog();
				}
				else
				{
					// 没有新版本
					String message = (String) VinoApplication.getContext().getResources().getText(R.string.comm_update_no);
					message = message.replace("#version#", versionName + "(" + versionCode + ")");
					ToastUtil.makeText(VinoApplication.getContext(), message, Toast.LENGTH_LONG).show();
				}
				break;
			case ACTION_CHECK_SILENT:
				// 检查更新
				if (VersionInfo == null)
				{
					return;
				}

				if (VersionInfo.getUpdate() == 1)
				{
					// 有新版本
					// 显示提示对话框
					showNoticeDialog();
				}
				break;
                case ACTION_CHECK_INFO:
                    if(mListener != null)
                    {
                        mListener.OnReceive(VersionInfo);
                    }
                    break;
				default:
					break;
			}
		};
	};

	public VinoAppUpdateTool()
	{
		VinoAppKey  = ApplicationUtil.getApplicationMetaData(VinoApplication.getContext(), "VinoAppKey");
		if(StringUtil.isBlank(VinoAppKey))
		{
			ToastUtil.debugToast(VinoApplication.getContext(), "未设置VinoAppKey");
		}
	}

    private static VinoAppUpdateTool getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new VinoAppUpdateTool();
        }
        instance.context = context;
        return instance;
    }

    /**
     * APP更新
     */
    public static void update(Context context)
    {
        VinoAppUpdateTool.getInstance(context).CheckSilent();
    }

	/**
	 * APP更新
	 */
	public void CheckSilent()
	{
		if (inCheck)
		{
			return;
		}
		inCheck = true;
		new DoGetVersionTask(ACTION_CHECK_SILENT).execute();
	}

    public static void check(Context context)
    {
        VinoAppUpdateTool.getInstance(context).updateCheck();
    }

    public static void check(Context context, OnAppUpdateListener listener)
    {
        VinoAppUpdateTool.getInstance(context).setAppUpdateListener(listener);
        VinoAppUpdateTool.getInstance(context).checkInfo();
    }

	/**
	 * 检测软件更新
	 */
	private void updateCheck()
	{
		if (inCheck)
		{
			return;
		}
		inCheck = true;
		new DoGetVersionTask(ACTION_CHECK).execute();
	}

    /**
     * 检测软件更新
     */
    private void checkInfo()
    {
        new DoGetVersionTask(ACTION_CHECK_INFO).execute();
    }

	/* 获取最新版本信息 */
    private class DoGetVersionTask extends AsyncTask<String, String, ApiReply<AppVersionEntity>>
	{
		private int action;

		public DoGetVersionTask(int action)
		{
			this.action = action;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		protected ApiReply<AppVersionEntity> doInBackground(String... params)
		{
			ApiReply<AppVersionEntity> reply = new ApiReply<AppVersionEntity>();
			AppCommDao dao = new AppCommDao(VinoApplication.getContext());
			String version = ApplicationUtil.getVersionName(VinoApplication.getContext());
			reply = dao.DoGetAppVersionInfo(VinoAppKey, version + "");
			return reply;
		}

		protected void onPostExecute(ApiReply<AppVersionEntity> reply)
		{
			inCheck = false;
			if (reply == null)
			{
				ToastUtil.makeText(VinoApplication.getContext(), "当前网络状况不良！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (reply.getCode() == 0)
			{
				VersionInfo = reply.getData();
				mHandler.sendEmptyMessage(action);
			}
		}
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
        VinoAppUpdateDialog dialog = new VinoAppUpdateDialog(context, VersionInfo.getUpdate_log(), new VinoAppUpdateDialog.OnAppUpdateDialogListener() {
            @Override
            public void OnCancel() {
            	if(VersionInfo.isForce())
				{
					VinoApplication.getInstance().exitApp();
				}
            }
            @Override
            public void OnOk(final VinoAppUpdateDialog dialog1) {
                AndPermission.with(context)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                dialog1.dismiss();
                                showDownloadDialog();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Uri uri = Uri.parse(VersionInfo.getDownload_url());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent);
                            }
                        }).start();
            }
        });
        dialog.show();
        ToastUtil.debugToast(context,"检测到更新");
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
        // 构造软件下载对话框
        mDownloadDialog = new VinoAppDownloadDialog(context, new VinoAppDownloadDialog.OnAppDownloadDialogListener() {
            @Override
            public void OnCancel() {
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread
	{
		@SuppressLint("HandlerLeak")
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					mSavePath = Environment.getExternalStorageDirectory() + "/Download/";
					URL url = new URL(VersionInfo.getDownload_url());
					// 创建连接
					URLConnection conn = url.openConnection();
					// 获取文件大小
					int length = conn.getContentLength();
					Log.d("VinoAppUpdateTool", "下载文件：" + url + "[" + length + "]");

					// 创建输入流
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
                        file.mkdirs();
					}
                    String filepath = "app_" + VinoAppKey + "_" + VersionInfo.getVersion() + ".apk";
					File apkFile = new File(mSavePath, filepath);
					if(apkFile.exists())
			        {
						apkFile.delete();
			        }
					FileOutputStream fos = new FileOutputStream(mSavePath + filepath);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							Log.d("VinoAppUpdateTool", "下载文件完成!");
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					}
					while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();

					if(cancelUpdate)
                    {
                        // 下载中断
                        Log.d("VinoAppUpdateTool", "下载中断");
                        mHandler.sendEmptyMessage(DOWNLOAD_CANCEL);
                    }
				}else
				{
					Uri uri = Uri.parse(VersionInfo.getDownload_url());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
				}
			} catch (Exception e)
			{
				ToastUtil.showShortToast(context, e.getMessage());
				e.printStackTrace();

                Uri uri = Uri.parse(VersionInfo.getDownload_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
			}
			// 取消下载对话框显示
			//mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
        String filepath = "app_" + VinoAppKey + "_" + VersionInfo.getVersion() + ".apk";
		File apkfile = new File(mSavePath, filepath);
		if (!apkfile.exists())
		{
			return;
		}

		try
        {
            // 通过Intent安装APK文件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, VinoApplication.getContext().getPackageName() + ".fileprovider", apkfile);
                Log.d("VinoAppUpdateTool", "apkUri:" + apkUri.toString());
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            VinoApplication.getContext().startActivity(intent);
        }
        catch (Exception ex)
        {
            ToastUtil.debugToast(context, ex.getMessage());
            ex.printStackTrace();

            ToastUtil.showShortToast(context, "安装出错，正在尝试重新安装！");
            Uri uri = Uri.parse(VersionInfo.getDownload_url());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
	}
}
