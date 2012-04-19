package zebrafish.util;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.pitt.cs1635group3.R;

public final class UIUtil {

	public static final String TAG = "UIUTIL";

	public static void showMessage(Context context, Throwable exception) {
		if (exception instanceof IOException) {
			showMessage(context, R.string.network_access_failure);
		} else {
			showMessage(context, exception.getMessage());
		}
	}

	public static void showMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showMessage(Context context, int resId) {
		showMessage(context, context.getResources().getString(resId));
	}

	public static int getMessageId(Throwable exception) {
		if (exception instanceof IOException) {
			return R.string.network_access_failure;
		} else {
			return R.string.unknown_failure;
		}
	}

	public static void showCustomDialog(Context context, Activity act,
			String titleStr, String contentStr) {
		Log.i(TAG, "ShowCustomDialog");
		final Dialog dialogok = new Dialog(context);

		dialogok.setContentView(R.layout.dialog_ok);
		dialogok.setOwnerActivity(act);
		dialogok.setTitle(titleStr);
		TextView tv = (TextView) dialogok
				.findViewById(R.id.textViewDialogMessage);
		tv.setText(contentStr);
		Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
		OnClickListener l = new OnClickListener() {
			public void onClick(View v) {
				dialogok.dismiss();
			}
		};
		ok_btn.setOnClickListener(l);
		dialogok.show();

	}

	private UIUtil() {
	}
}
