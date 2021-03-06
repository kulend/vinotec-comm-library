package cn.vinotec.app.android.comm.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.vinotec.app.android.comm.library.R;

//应用更新对话框
public class VinoAppDownloadDialog extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnAppDownloadDialogListener{
        void OnCancel();
        void OnInstall();
    }

    private OnAppDownloadDialogListener appDownloadDialogListener;
    private ProgressBar download_progress;
    private Button btn_cancel;
    private Button btn_install;
    private Button btn_uninstall;

    public VinoAppDownloadDialog(Context context, OnAppDownloadDialogListener listener) {
        super(context, R.style.MyDialog);
        this.appDownloadDialogListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vino_app_download_dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        download_progress = (ProgressBar) findViewById(R.id.download_progress);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appDownloadDialogListener != null)
                {
                    appDownloadDialogListener.OnCancel();
                }
                VinoAppDownloadDialog.this.dismiss();
            }
        });

        btn_install = (Button) findViewById(R.id.btn_install);
        btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appDownloadDialogListener != null)
                {
                    appDownloadDialogListener.OnInstall();
                }
            }
        });

        btn_uninstall = (Button) findViewById(R.id.btn_uninstall);
        btn_uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appDownloadDialogListener != null)
                {
                    appDownloadDialogListener.OnCancel();
                }
                VinoAppDownloadDialog.this.dismiss();
            }
        });
    }

    public void updateProgress(int progress)
    {
        download_progress.setProgress(progress);
        if(progress == 100)
        {
            btn_install.setVisibility(View.VISIBLE);
            btn_uninstall.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
        }
    }
}
