package edu.pitt.cs1635group3.Activities;

import service.InviteTask;
import service.SplashScreenTask;
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
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.push.C2DMReceiver;
import com.google.android.c2dm.C2DMessaging;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class HomeActivity extends Activity {

	private EditText messageText = null;
	private static Context context;
	private static final String TAG = "HomeActivity";
	
	private static int userID;
	private static SharedPreferences prefs;
	protected static Button pending;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		context = this;
		pending = (Button) findViewById(R.id.home_btn_pending);

		userID = User.getCurrUser(context);
		Log.i(TAG, "user id is " + userID);

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		int counter = prefs.getInt("counter", 0);

		if (counter == 0 || userID <= 0) { // then user isn't registered yet, so register
			showRegisterDialog();
		}

		Editor e = prefs.edit();
		e.putInt("counter", ++counter); // inc the counter
		e.commit();

	}
	
	public static void updateNumInvites(int n) {
		pending.setText("Invites (" + n + ")");
	}

	public void showRegisterDialog() {

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_register);
		dialog.setOwnerActivity(this);
		dialog.setTitle("Single Sign-In");
		TextView tv = (TextView) dialog
				.findViewById(R.id.textViewDialogMessage);
		tv.setText("Sign up so friends can invite you to lists!");
		Button ok_btn = (Button) dialog.findViewById(R.id.buttonOK);
		Button cancel_btn = (Button) dialog.findViewById(R.id.buttonCancel);

		OnClickListener l_ok = new OnClickListener() {
			public void onClick(View v) {

				EditText txtvEmail = (EditText) dialog
						.findViewById(R.id.txtvEmail);
				EditText txtvName = (EditText) dialog
						.findViewById(R.id.txtvName);

				CheckBox chbox = (CheckBox) dialog.findViewById(R.id.chboxPush);

				String name = txtvName.getText().toString().trim();
				String email = txtvEmail.getText().toString().trim();
				boolean allowPush = chbox.isChecked();
				String pn = User.getPhoneNumber(context);

				if (!name.equals("") && !email.equals("")) { // allow store

					storeUser(name, email, pn, allowPush);
					dialog.dismiss();

				} else {
					TextView descrip = (TextView) dialog
							.findViewById(R.id.textViewDialogMessage);
					descrip.setText("Fields must not be blank");
					descrip.setTextColor(Color.parseColor("red"));
				}
			}
		};

		OnClickListener l_cancel = new OnClickListener() {
			public void onClick(View v) {
				storeUser(null, null, User.getPhoneNumber(context), false); // store without registration
				dialog.dismiss();
			}
		};

		ok_btn.setOnClickListener(l_ok);
		cancel_btn.setOnClickListener(l_cancel);
		dialog.show();

	} // end showRegisterDialog

	
	public void registerPushNotification() {

		Log.d(TAG, "in registerPushNotification()");

		final SharedPreferences c2dmPrefs = getSharedPreferences(
				C2DMessaging.PREFERENCE, Context.MODE_PRIVATE);
		if (C2DMessaging.shouldRegisterForPush(getApplicationContext())) {
			if ((Build.VERSION.SDK_INT >= 8)
					&& (C2DMessaging
							.shouldRegisterForPush(getApplicationContext()))) {
				C2DMReceiver.refreshAppC2DMRegistrationState(
						getApplicationContext(), true);
			}
		}

		if (c2dmPrefs.contains("dm_registration")) {
			Log.i(TAG, "Device already registered.");
		} else { // don't reshow the push notifications prompt if they've
			// already registered
			Log.i(TAG, "Registering device");
			if (Build.VERSION.SDK_INT >= 8) {

				Editor e = c2dmPrefs.edit();
				e.putString("dm_registration", "");
				e.commit();
				C2DMessaging.setRegisterForPush(context, true);
				C2DMReceiver.refreshAppC2DMRegistrationState(context, true);

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
						Editor e = c2dmPrefs.edit();
						e.putString("dm_registration", "");
						e.commit();
						C2DMessaging.setRegisterForPush(context, false);
					}
				};
				ok_btn.setOnClickListener(l);
				dialogok.show();
			}
		}
	} // end showPushNotificationDialog()

	protected void storeUser(String name, String email, String pn,
			boolean allowPush) {
		// pass the ID back to shared prefs to recall later
		Log.i(TAG, "user name " + name + ", email " + email + " pn " + pn);

		userID = User.storeUser(context, name, email, pn);
		Editor e = prefs.edit();
		e.putInt("userID", userID);
		e.putString("name", name);
		e.putString("email", email);
		e.commit();

		if (allowPush) {
			registerPushNotification();
		}

		Log.i(TAG, "User name (" + name + ") registered");
	}

	public void myLists(View v) {
		Intent intent = new Intent(context, SociaListActivity.class);
		startActivityForResult(intent, 0);
	}

	public void myItems(View v) {
		Intent intent = new Intent(context, ItemsActivity.class);
		startActivity(intent);
	}

	public void myPendingInvites(View v) {
		Intent intent = new Intent(context, PendingInvitesActivity.class);
		startActivity(intent);
	}

	public void browseForListByID(View v) {
		// Intent intent = new Intent(context, BrowseForListActivity.class);
		// startActivity(intent);
	}

	public void mySettings(View v) {
		Intent intent = new Intent(context, SettingsActivity.class);
		startActivity(intent);
	}

}
