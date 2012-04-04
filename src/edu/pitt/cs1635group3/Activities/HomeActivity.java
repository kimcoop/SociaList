package edu.pitt.cs1635group3.Activities;

import com.example.push.*;
import com.google.android.c2dm.*;

import edu.pitt.cs1635group3.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	private static Activity curActivity = null;
	private EditText messageText = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HOME ACTIVITY", "Got here");
        setContentView(R.layout.dashboard);
		curActivity = this;
        
        
        new AlertDialog.Builder(this)
		.setTitle("Push Notifications")
		.setMessage(getString(R.string.push_explain))
		.setNegativeButton("No thanks",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
				// do nothing
			}
		})
		.setPositiveButton("Sure!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						showPushNotificationDialog();
					}
				}).show();
        
        
        
    }
	
    public void myLists(View v) {

		Intent intent = new Intent(HomeActivity.this, SociaListActivity.class);
		startActivityForResult(intent, 0);

	}
    
    /**
	 * Show push notification dialog.
	 */
	public void showPushNotificationDialog() {
		Log.i("CLICKED BTN", "Show push notification dialog");

		final SharedPreferences prefs = getSharedPreferences(
				C2DMessaging.PREFERENCE, Context.MODE_PRIVATE);
		if (C2DMessaging.shouldRegisterForPush(getApplicationContext())) {
			if ((Build.VERSION.SDK_INT >= 8)
					&& (C2DMessaging
							.shouldRegisterForPush(getApplicationContext()))) {
				C2DMReceiver.refreshAppC2DMRegistrationState(
						getApplicationContext(), true);
			}
		}
		
		if (prefs.contains("dm_registration")) {
			return;
		}
		

		Log.i("PUSH", "Here 1");

		if (Build.VERSION.SDK_INT >= 8) {

			Editor e = prefs.edit();
			e.putString("dm_registration", "");
			e.commit();
			C2DMessaging.setRegisterForPush(getApplicationContext(), true);
			C2DMReceiver.refreshAppC2DMRegistrationState(
					getApplicationContext(), true);

		} else {
			final Dialog dialogok = new Dialog(this);

			dialogok.setContentView(R.layout.dialog_ok);
			dialogok.setOwnerActivity(this);
			dialogok.setTitle(getString(R.string.app_name));
			TextView tv = (TextView) dialogok
					.findViewById(R.id.textViewDialogMessage);
			tv.setText("This Phone does not support push notification");
			Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
			OnClickListener l = new OnClickListener() {
				public void onClick(View v) {
					dialogok.dismiss();
					Editor e = prefs.edit();
					e.putString("dm_registration", "");
					e.commit();
					C2DMessaging.setRegisterForPush(getApplicationContext(),
							false);
				}
			};
			ok_btn.setOnClickListener(l);
			dialogok.show();
		}
	} // end showPushNotificationDialog()
	

	public static Activity getCurrentActivity() {
		// TODO Auto-generated method stub
		return curActivity;
	}
}
