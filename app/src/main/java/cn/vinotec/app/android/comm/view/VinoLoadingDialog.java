package cn.vinotec.app.android.comm.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import cn.vinotec.app.android.comm.library.R;

public class VinoLoadingDialog extends Dialog {

	private static VinoLoadingDialog vinoLoadingDialog = null;

	public VinoLoadingDialog(Context context) {
		super(context);
	}

	public VinoLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public static VinoLoadingDialog createDialog(Context context) {
		vinoLoadingDialog = new VinoLoadingDialog(context, R.style.VinoLoadingDialog);
		vinoLoadingDialog.setContentView(R.layout.vino_loading_dialog);
		vinoLoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return vinoLoadingDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (vinoLoadingDialog == null) {
			return;
		}
	}

	public void setMessage(String strMessage) {
		TextView tvMsg = (TextView) vinoLoadingDialog.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}
}
