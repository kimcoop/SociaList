package zebrafish.util;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.HomeActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public final class IOUtil {

	public static boolean isOnline(Context context) {
		Log.i("IOUtil", "checking if app isOnline");
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	

	public static void informConnectionIssue(Context context) {
		final Dialog dialogok = new Dialog(context);
		
		String title, content;
		title = "Connectivity Problem";
		content = "Internet signal too weak. Changes will not persist.";

		dialogok.setContentView(R.layout.dialog_ok);
		dialogok.setOwnerActivity((Activity) context);
		dialogok.setTitle(title);
		TextView tv = (TextView) dialogok
				.findViewById(R.id.textViewDialogMessage);
		tv.setText(content);
		Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
		OnClickListener l = new OnClickListener() {
			public void onClick(View v) {
				dialogok.dismiss();
			}
		};
		ok_btn.setOnClickListener(l);
		dialogok.show();

	}
	
	

	private IOUtil() {
	}
}