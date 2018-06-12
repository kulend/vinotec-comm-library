package cn.vinotec.app.android.comm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.vinotec.app.android.comm.library.R;

//确认对话框
public class VinoConfirmDialog extends Dialog {

    public static final int BUTTON_THEME_NORMAL = 0;
    public static final int BUTTON_THEME_POSITIVE = 1;
    public static final int BUTTON_THEME_NEGATIVE = 2;

    private final Builder.DialogParams mParams;

    public VinoConfirmDialog(Builder.DialogParams params) {
        super(params.mContext, R.style.MyDialog);
        mParams = params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vino_comfirm_dialog);

        setCancelable(mParams.mCancelable);
        setCanceledOnTouchOutside(mParams.mCanceledOnTouchOutside);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(mParams.mTitle);

        TextView tv_message = (TextView) findViewById(R.id.tv_message);
        tv_message.setText(mParams.mMessage);

        Button btn_negative = (Button) findViewById(R.id.btn_negative);
        btn_negative.setText(mParams.mNegativeButtonText);
        setButtomTheme(btn_negative, mParams.mNegativeButtonTheme);

        Button btn_positive = (Button) findViewById(R.id.btn_positive);
        btn_positive.setText(mParams.mPositiveButtonText);
        setButtomTheme(btn_positive, mParams.mPositiveButtonTheme);

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParams.mNegativeButtonOnClickListener != null)
                {
                    mParams.mNegativeButtonOnClickListener.onClick(VinoConfirmDialog.this, R.id.btn_positive);
                }else
                {
                    VinoConfirmDialog.this.dismiss();
                }
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParams.mPositiveButtonOnClickListener != null)
                {
                    mParams.mPositiveButtonOnClickListener.onClick(VinoConfirmDialog.this, R.id.btn_positive);
                }else
                {
                    VinoConfirmDialog.this.dismiss();
                }
            }
        });
    }

    private void setButtomTheme(Button btn, int theme) {
        if (theme == BUTTON_THEME_POSITIVE) {
            btn.setTextColor(getContext().getResources().getColor(R.color.dialog_button_positive));
        } else if (theme == BUTTON_THEME_NEGATIVE) {
            btn.setTextColor(getContext().getResources().getColor(R.color.dialog_button_negative));
        } else {
            btn.setTextColor(getContext().getResources().getColor(R.color.dialog_button_default));
        }
    }

    public static class Builder {
        private final DialogParams mParams;

        public Builder(Context context) {
            mParams = new DialogParams();
            mParams.mContext = context;
            mParams.mCancelable = true;
            mParams.mCanceledOnTouchOutside = true;
            mParams.mTitle = "提示";
            mParams.mMessage = "确认继续当前操作？";
            mParams.mPositiveButtonTheme = VinoConfirmDialog.BUTTON_THEME_POSITIVE;
            mParams.mPositiveButtonText = "确 定";
            mParams.mNegativeButtonTheme = VinoConfirmDialog.BUTTON_THEME_NORMAL;
            mParams.mNegativeButtonText = "取 消";
        }

        public Builder setCancelable(boolean cancelable) {
            mParams.mCancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mParams.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mParams.mTitle = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mParams.mMessage = message;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            mParams.mPositiveButtonText = text;
            mParams.mPositiveButtonOnClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonTheme(int theme) {
            mParams.mPositiveButtonTheme = theme;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            mParams.mNegativeButtonText = text;
            mParams.mNegativeButtonOnClickListener = listener;
            return this;
        }

        public Builder setNegativeButtonTheme(int theme) {
            mParams.mNegativeButtonTheme = theme;
            return this;
        }

        public VinoConfirmDialog create() {
            return new VinoConfirmDialog(mParams);
        }

        public VinoConfirmDialog show() {
            VinoConfirmDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public class DialogParams {
            public Context mContext;

            public boolean mCancelable;
            public boolean mCanceledOnTouchOutside;

            public CharSequence mTitle;

            public CharSequence mMessage;

            public CharSequence mPositiveButtonText;
            public DialogInterface.OnClickListener mPositiveButtonOnClickListener;
            public int mPositiveButtonTheme;


            public CharSequence mNegativeButtonText;
            public DialogInterface.OnClickListener mNegativeButtonOnClickListener;
            public int mNegativeButtonTheme;

        }
    }
}
