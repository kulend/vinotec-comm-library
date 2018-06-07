package cn.vinotec.app.android.comm.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.vinotec.app.android.comm.library.R;

//应用更新对话框
public class VinoAppUpdateDialog extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnAppUpdateDialogListener{
        void OnCancel();
        void OnOk();
    }

    private OnAppUpdateDialogListener appUpdateDialogListener;
    private String text;

    private Button btn_cancel;
    private Button btn_ok;

    public VinoAppUpdateDialog(Context context, String text, OnAppUpdateDialogListener listener) {
        super(context, R.style.MyDialog);
        this.appUpdateDialogListener = listener;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vino_app_update_dialog);

        setCancelable(true);
        setCanceledOnTouchOutside(false);

        TextView tv = (TextView) findViewById(R.id.tv_update_log);
        tv.setText(text);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appUpdateDialogListener != null)
                {
                    appUpdateDialogListener.OnCancel();
                }
                VinoAppUpdateDialog.this.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VinoAppUpdateDialog.this.dismiss();
                if(appUpdateDialogListener != null)
                {
                    appUpdateDialogListener.OnOk();
                }
            }
        });
    }
}
