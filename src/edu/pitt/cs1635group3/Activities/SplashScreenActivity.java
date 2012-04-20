package edu.pitt.cs1635group3.Activities;

import com.example.push.C2DMReceiver;
import com.google.android.c2dm.C2DMessaging;

import service.SplashScreenTask;
import zebrafish.util.IOUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;
	private int userID;

	protected Context context;
	private static final String TAG = "SplashScreenActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		context = this;
		userID = User.getCurrUser(context);
		Log.i(TAG, "USER ID AT VERY BEGINNING IS " +userID);
		
		if (IOUtil.isOnline(context)) {
			if (userID > 0) {
				new SplashScreenTask().getListsAndInvites(context);
			} else {
				Intent i = new Intent(this, HomeActivity.class);
				startActivity(i);
			}

		} else {
			informConnectionIssue();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	public void informConnectionIssue() {
		final Dialog dialogok = new Dialog(context);

		dialogok.setContentView(R.layout.dialog_ok);
		dialogok.setOwnerActivity(this);
		dialogok.setTitle(getString(R.string.connect_title));
		TextView tv = (TextView) dialogok
				.findViewById(R.id.textViewDialogMessage);
		tv.setText(getString(R.string.connect_issue));
		Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
		OnClickListener l = new OnClickListener() {
			public void onClick(View v) {
				dialogok.dismiss();

				Intent i = new Intent(context, HomeActivity.class);
				startActivity(i);

			}
		};
		ok_btn.setOnClickListener(l);
		dialogok.show();

	}
	

}
